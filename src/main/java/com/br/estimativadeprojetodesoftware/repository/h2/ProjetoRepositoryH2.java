package com.br.estimativadeprojetodesoftware.repository.h2;

import com.br.estimativadeprojetodesoftware.model.Campo;
import com.br.estimativadeprojetodesoftware.model.PerfilProjeto;
import com.br.estimativadeprojetodesoftware.model.Projeto;
import com.br.estimativadeprojetodesoftware.model.Usuario;
import com.br.estimativadeprojetodesoftware.repository.IProjetoRepository;
import com.br.estimativadeprojetodesoftware.service.CampoRepositoryService;
import com.br.estimativadeprojetodesoftware.service.PerfilRepositoryService;
import com.br.estimativadeprojetodesoftware.service.UsuarioRepositoryService;
import com.br.estimativadeprojetodesoftware.singleton.ConexaoSingleton;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ProjetoRepositoryH2 implements IProjetoRepository {

    private final Connection connection;

    public ProjetoRepositoryH2() {
        this.connection = ConexaoSingleton.getInstancia().getConexao();
    }

@Override
    public void salvar(Projeto projeto) {
        String sql = "INSERT INTO projeto (idProjeto, nomeProjeto, tipoProjeto, created_atProjeto, status) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, projeto.getId().toString());
            stmt.setString(2, projeto.getNome());
            stmt.setString(3, projeto.getTipo());
            stmt.setTimestamp(4, Timestamp.valueOf(projeto.getCreated_at()));
            stmt.setString(5, projeto.getStatus());

            for (Campo campo : projeto.getCampos()) {
                new CampoRepositoryService().salvarProjetoCampo(projeto, campo);
            }

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar projeto", e);
        }
    }

    @Override
    public void atualizar(Projeto projeto) {
        String sql = "UPDATE projeto SET nomeProjeto = ?, tipoProjeto = ?, updated_atProjeto = CURRENT_TIMESTAMP, status = ? WHERE idProjeto = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, projeto.getNome());
            stmt.setString(2, projeto.getTipo());
            stmt.setString(3, projeto.getStatus());
            stmt.setString(4, projeto.getId().toString());

            for (Campo campo : projeto.getCampos()) {
                new CampoRepositoryService().atualizar(campo);
                new CampoRepositoryService().atualizarDiasProjetoCampo(projeto, campo);
            }

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar projeto", e);
        }
    }

    @Override
    public void removerPorId(UUID id) {
        String sql = "DELETE FROM projeto WHERE idProjeto = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id.toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao remover projeto", e);
        }
    }

    @Override
    public Optional<Projeto> buscarPorId(UUID id) {
        String sql = "SELECT * FROM projeto WHERE idProjeto = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id.toString());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapProjetoFromResultSet(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar projeto", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Projeto> buscarTodos() {
        List<Projeto> projetos = new ArrayList<>();
        String sql = "SELECT * FROM projeto";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                projetos.add(mapProjetoFromResultSet(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar todos os projetos", e);
        }
        return projetos;
    }

    private Projeto mapProjetoFromResultSet(ResultSet rs) throws SQLException {
        UUID idProjeto = UUID.fromString(rs.getString("idProjeto"));
        List<PerfilProjeto> perfis = new PerfilRepositoryService().buscarPerfisPorProjeto(idProjeto);
        List<Usuario> usuarios = new UsuarioRepositoryService().buscarUsuariosPorProjeto(idProjeto);
        List<Campo> campos = new CampoRepositoryService().listarTodosPorIdProjeto(idProjeto);
        List<Campo> camposNovos = new ArrayList<>();

        for (Campo campo : campos) {
            Integer dias = new CampoRepositoryService().buscarDiasPorProjetoCampo(idProjeto, campo.getId());
            campo.setDias(dias.doubleValue());;
            camposNovos.add(campo);
        }

        return new Projeto(
                idProjeto,
                rs.getString("nomeProjeto"),
                rs.getString("tipoProjeto"),
                usuarios.get(0).getNome(),
                rs.getTimestamp("created_atProjeto").toLocalDateTime(),
                rs.getString("status"),
                buscarIsCompartilhadoPorId(usuarios.get(0).getId(), idProjeto),
                usuarios.get(0).getNome(),
                perfis,
                usuarios,
                camposNovos
        );
    }

    @Override
    public boolean buscarIsCompartilhadoPorId(UUID idUsuario, UUID idProjeto) {
        boolean isCompartilhado = false;
        String query = "SELECT isCompartilhado FROM usuario_has_projeto WHERE usuario_idUsuario = ? AND projeto_idProjeto = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, idUsuario.toString());
            statement.setString(2, idProjeto.toString());
            ResultSet resultSet = statement.executeQuery();
            isCompartilhado = resultSet.getBoolean("isCompartilhado");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isCompartilhado;
    }

    @Override
    public List<String> buscarProjetosPorUsuario(UUID idUsuario) {
        List<String> projetos = new ArrayList<>();
        String query = "SELECT projeto_idProjeto FROM usuario_has_projeto WHERE usuario_idUsuario = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, idUsuario.toString());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                projetos.add(resultSet.getString("projeto_idProjeto"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return projetos;
    }

    @Override
    public List<String> buscarNomesDeProjetosPorUsuario(UUID idUsuario) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Optional<Projeto> buscarProjetoPorNome(String nomeProjeto) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
