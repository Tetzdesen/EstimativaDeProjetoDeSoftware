package com.br.estimativadeprojetodesoftware.repository.sqlite;

import com.br.estimativadeprojetodesoftware.model.Perfil;
import com.br.estimativadeprojetodesoftware.model.Projeto;
import com.br.estimativadeprojetodesoftware.model.Usuario;
import com.br.estimativadeprojetodesoftware.repository.IUsuarioRepository;
import com.br.estimativadeprojetodesoftware.service.PerfilRepositoryService;
import com.br.estimativadeprojetodesoftware.service.ProjetoRepositoryService;
import com.br.estimativadeprojetodesoftware.service.UsuarioHasProjetoRepositoryService;
import com.br.estimativadeprojetodesoftware.singleton.ConexaoSingleton;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UsuarioRepositorySQLite implements IUsuarioRepository {

    private final Connection connection;

    public UsuarioRepositorySQLite() {
        this.connection = ConexaoSingleton.getInstancia().getConexao();
    }

    @Override
    public void salvar(Usuario usuario) {
        String sql = "INSERT INTO usuario (idUsuario, nomeUsuario, email, senha, created_atUsuario, log) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, usuario.getId().toString());
            statement.setString(3, usuario.getNome());
            statement.setString(4, usuario.getEmail());
            statement.setString(5, usuario.getSenha());
            statement.setTimestamp(6, Timestamp.valueOf(usuario.getCreated_at()));
            statement.setString(8, usuario.getLog());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void atualizar(Usuario usuario) {
        String sql = "UPDATE usuario SET nomeUsuario = ?, email = ?, senha = ?, updated_atUsuario = NOW(), log = ? WHERE idUsuario = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, usuario.getNome());
            statement.setString(2, usuario.getEmail());
            statement.setString(3, usuario.getSenha());
            statement.setString(5, usuario.getLog());
            statement.setString(6, usuario.getId().toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removerPorId(UUID id) {
        String sql = "DELETE FROM usuario WHERE idUsuario = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
        List<Perfil> perfis = PerfilRepositoryService.getInstancia().buscarTodosPerfisPorIdUsuario(idUsuario);
        List<Projeto> projetos = new ArrayList<>();
        
        List<String> idProjetos = UsuarioHasProjetoRepositoryService.getInstancia().buscarProjetosPorUsuario(idUsuario);
        
        for(String id : idProjetos){
            projetos.add(ProjetoRepositoryService.getInstancia().buscarPorId(UUID.fromString(id)).get());
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
}
