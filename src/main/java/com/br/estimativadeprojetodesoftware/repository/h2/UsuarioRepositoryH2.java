package com.br.estimativadeprojetodesoftware.repository.h2;

import com.br.estimativadeprojetodesoftware.model.Campo;
import com.br.estimativadeprojetodesoftware.model.PerfilProjeto;
import com.br.estimativadeprojetodesoftware.model.Projeto;
import com.br.estimativadeprojetodesoftware.model.Usuario;
import com.br.estimativadeprojetodesoftware.repository.IUsuarioRepository;
import com.br.estimativadeprojetodesoftware.service.CampoRepositoryService;
import com.br.estimativadeprojetodesoftware.service.PerfilRepositoryService;
import com.br.estimativadeprojetodesoftware.service.ProjetoRepositoryService;
import com.br.estimativadeprojetodesoftware.service.UsuarioRepositoryService;
import com.br.estimativadeprojetodesoftware.singleton.ConexaoSingleton;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class UsuarioRepositoryH2 implements IUsuarioRepository {

    private final Connection connection;

    public UsuarioRepositoryH2() {
        this.connection = ConexaoSingleton.getInstancia().getConexao();
    }

    @Override
    public void salvar(Usuario usuario) {
        String sql = "INSERT INTO usuario (idUsuario, nomeUsuario, email, senha, created_atUsuario, log) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, usuario.getId().toString());
            statement.setString(2, usuario.getNome());
            statement.setString(3, usuario.getEmail());
            statement.setString(4, usuario.getSenha());
            statement.setTimestamp(5, Timestamp.valueOf(usuario.getCreated_at()));
            statement.setString(6, usuario.getLog());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void atualizar(Usuario usuario) {
        String sql = "UPDATE usuario SET nomeUsuario = ?, email = ?, senha = ?, updated_atUsuario = CURRENT_TIMESTAMP, log = ? WHERE idUsuario = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, usuario.getNome());
            statement.setString(2, usuario.getEmail());
            statement.setString(3, usuario.getSenha());
            statement.setString(4, usuario.getLog());
            statement.setString(5, usuario.getId().toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean removerPorId(UUID id) {
        String sql = "DELETE FROM usuario WHERE idUsuario = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public Optional<Usuario> buscarPorId(UUID id) {
        String sql = "SELECT * FROM usuario WHERE idUsuario = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id.toString());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(mapToUsuario(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        String sql = "SELECT * FROM usuario WHERE email = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(mapToUsuario(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Usuario> buscarTodos() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuario";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                usuarios.add(mapToUsuario(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usuarios;
    }

    private Usuario mapToUsuario(ResultSet resultSet) throws SQLException {
        UUID idUsuario = UUID.fromString(resultSet.getString("idUsuario"));

        List<PerfilProjeto> perfis = new ArrayList<>();
        perfis.addAll(new PerfilRepositoryService().buscarTodosPerfisPorIdUsuario(idUsuario));

        List<PerfilProjeto> perfisNovos = new ArrayList<>();

        for (PerfilProjeto perfil : perfis) {

            // buscar nome do campo pelo id do Perfil
            List<Campo> camposTamanhoApp = new CampoRepositoryService().buscarPorIdPerfilTipo(perfil.getId(), "tamanho app");

            for (Campo campo : camposTamanhoApp) {
                Double dias = new CampoRepositoryService().buscarDiasPorPerfilCampo(perfil.getId(), campo.getId());
                perfil.adicionarTamanhoApp(campo.getNome(), dias.intValue());

            }

            List<Campo> camposNivelUI = new CampoRepositoryService().buscarPorIdPerfilTipo(perfil.getId(), "nivel ui");

            for (Campo campo : camposNivelUI) {
                Double dias = new CampoRepositoryService().buscarDiasPorPerfilCampo(perfil.getId(), campo.getId());
                perfil.adicionarNivelUI(campo.getNome(), dias.intValue());
            }

            List<Campo> camposFuncionalidades = new CampoRepositoryService().buscarPorIdPerfilTipo(perfil.getId(), "funcionalidades");

            for (Campo campo : camposFuncionalidades) {
                Double dias = new CampoRepositoryService().buscarDiasPorPerfilCampo(perfil.getId(), campo.getId());
                perfil.adicionarFuncionalidade(campo.getNome(), dias.intValue());
            }

            List<Campo> taxasDiarias = new CampoRepositoryService().buscarPorIdPerfilTipo(perfil.getId(), "taxa diaria");

            for (Campo campo : taxasDiarias) {
                Double dias = new CampoRepositoryService().buscarDiasPorPerfilCampo(perfil.getId(), campo.getId());
                perfil.adicionarTaxaDiaria(campo.getNome(), dias.intValue());
            }

            perfisNovos.add(perfil);
        }

        List<Projeto> projetos = new ArrayList<>();
        List<String> idProjetos = new ProjetoRepositoryService().buscarProjetosPorUsuario(idUsuario);

        for (String id : idProjetos) {

            Projeto projeto = new ProjetoRepositoryService().buscarPorId(UUID.fromString(id)).get();

            Campo campo = new CampoRepositoryService().buscarPorIdProjeto(projeto.getId());

            Integer dias = new CampoRepositoryService().buscarDiasPorProjeto(projeto.getId());

            campo.setDias(dias.doubleValue());;

            projeto.adicionarCampo(campo);

            projetos.add(projeto);
        }

        return new Usuario(
                idUsuario,
                resultSet.getString("nomeUsuario"),
                resultSet.getString("email"),
                resultSet.getString("senha"),
                resultSet.getTimestamp("created_atUsuario").toLocalDateTime(),
                resultSet.getString("log"),
                projetos,
                perfis
        );
    }

    @Override
    public List<Usuario> buscarUsuariosPorProjeto(UUID idProjeto) {
        List<Usuario> usuarios = new ArrayList<>();
        List<String> usuarioIds = new ProjetoRepositoryService().buscarProjetosPorUsuario(idProjeto);

        for (String userId : usuarioIds) {
            new UsuarioRepositoryService().buscarPorId(UUID.fromString(userId)).ifPresent(usuarios::add);
        }
        return usuarios;
    }

    @Override
    public Map<String, String> buscarEmailESenhaDeUsuarios() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
