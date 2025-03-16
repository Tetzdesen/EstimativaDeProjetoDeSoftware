package com.br.estimativadeprojetodesoftware.repository.sqlite;

import com.br.estimativadeprojetodesoftware.model.Campo;
import com.br.estimativadeprojetodesoftware.model.PerfilProjeto;
import com.br.estimativadeprojetodesoftware.model.Projeto;
import com.br.estimativadeprojetodesoftware.model.Usuario;
import com.br.estimativadeprojetodesoftware.repository.IProjetoRepository;
import com.br.estimativadeprojetodesoftware.service.CampoRepositoryService;
import com.br.estimativadeprojetodesoftware.service.PerfilRepositoryService;
import com.br.estimativadeprojetodesoftware.service.UsuarioRepositoryService;
import com.br.estimativadeprojetodesoftware.singleton.ConexaoSingleton;
import com.br.estimativadeprojetodesoftware.singleton.UsuarioLogadoSingleton;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ProjetoRepositorySQLite implements IProjetoRepository {

    private final Connection connection;

    public ProjetoRepositorySQLite() {
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
            stmt.executeUpdate();

            for (PerfilProjeto perfil : projeto.getPerfis()) {
                if (!existeAssociacaoProjetoPerfil(projeto.getId(), perfil.getId())) {  // Verifica se já existe a associação
                    salvarPerfilProjeto(projeto, perfil);
                }
            }

            salvarCampos(projeto);
            salvarUsuarioProjeto(projeto);

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar projeto", e);
        }
    }

    private void salvarCampos(Projeto projeto) {
        CampoRepositoryService campoService = new CampoRepositoryService();

        for (Campo campo : projeto.getCampos()) {
            campoService.salvarProjetoCampo(projeto, campo);
        }
    }

    private void salvarUsuarioProjeto(Projeto projeto) {
        String sql = "INSERT INTO usuario_has_projeto (usuario_idUsuario, projeto_idProjeto, isCompartilhado) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, projeto.getUsuarios().get(0).getId().toString());
            stmt.setString(2, projeto.getId().toString());
            stmt.setBoolean(3, projeto.isCompartilhado());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar usuário-projeto", e);
        }
    }

    private void salvarPerfilProjeto(Projeto projeto, PerfilProjeto perfil) {
        String sql = "INSERT INTO projeto_has_perfil (projeto_idProjeto, perfil_idPerfil) VALUES (?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, projeto.getId().toString());
            stmt.setString(2, perfil.getId().toString());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar perfil-projeto", e);
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

            System.out.println("ID do projeto: " + projeto.getId());
            System.out.println("Nome do projeto: " + projeto.getNome());
            System.out.println("Tipo do projeto: " + projeto.getTipo());
            System.out.println("Status do projeto: " + projeto.getStatus());
            stmt.executeUpdate();

            removerPerfisProjeto(projeto.getId());

            for (PerfilProjeto perfil : projeto.getPerfis()) {
                salvarPerfilProjeto(projeto, perfil);
            }

            String sqlRemoverCampos = "DELETE FROM projeto_has_campo WHERE projeto_idProjeto = ?";
            try (PreparedStatement stmtRemover = connection.prepareStatement(sqlRemoverCampos)) {
                stmtRemover.setString(1, projeto.getId().toString());
                stmtRemover.executeUpdate();
            }

            salvarCampos(projeto);

            for (Campo campo : projeto.getCampos()) {
                new CampoRepositoryService().atualizar(campo);
                if (!existeAssociacaoProjetoCampo(projeto.getId(), campo.getId())) {
                    new CampoRepositoryService().salvarProjetoCampo(projeto, campo);
                }
            }

        } catch (SQLException e) {
            System.out.println(e);
            throw new RuntimeException("Erro ao atualizar projeto", e);
        }
    }

    private void removerPerfisProjeto(UUID idProjeto) {
        String sql = "DELETE FROM projeto_has_perfil WHERE projeto_idProjeto = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, idProjeto.toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao remover perfis do projeto", e);
        }
    }

    private boolean existeAssociacaoProjetoPerfil(UUID idProjeto, UUID idPerfil) {
        String sql = "SELECT COUNT(*) FROM projeto_has_perfil WHERE projeto_idProjeto = ? AND perfil_idPerfil = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, idProjeto.toString());
            stmt.setString(2, idPerfil.toString());
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao verificar associação de perfil", e);
        }
    }

    private boolean existeAssociacaoProjetoCampo(UUID idProjeto, UUID idCampo) {
        String sql = "SELECT COUNT(*) FROM projeto_has_campo WHERE projeto_idProjeto = ? AND campo_idCampo = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, idProjeto.toString());
            stmt.setString(2, idCampo.toString());
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao verificar associação", e);
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
    public Optional<Projeto> buscarProjetoPorNome(String nomeProjeto) {
        String sql = "SELECT * FROM projeto WHERE nomeProjeto = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nomeProjeto);
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
    public List<String> buscarNomesDeProjetosPorUsuario(UUID idUsuario) {
        List<String> projetos = new ArrayList<>();
        String query = "SELECT projeto_idProjeto FROM usuario_has_projeto WHERE usuario_idUsuario = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, idUsuario.toString());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Projeto projeto = buscarPorId(UUID.fromString(resultSet.getString("projeto_idProjeto"))).get();
                projetos.add(projeto.getNome());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return projetos;
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

        // Tratamento para evitar o erro:
        UUID idUsuario = usuarios.isEmpty() ? null : usuarios.get(0).getId();
        return new Projeto(
                idProjeto,
                rs.getString("nomeProjeto"),
                UsuarioLogadoSingleton.getInstancia().getUsuario().getNome(),
                rs.getString("tipoProjeto"),
                rs.getTimestamp("created_atProjeto").toLocalDateTime(),
                rs.getString("status"),
                buscarIsCompartilhadoPorId(idUsuario, idProjeto),
                null,
                perfis,
                usuarios,
                campos
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

}
