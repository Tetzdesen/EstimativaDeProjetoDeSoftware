package com.br.estimativadeprojetodesoftware.repository.sqlite;

import com.br.estimativadeprojetodesoftware.model.Perfil;
import com.br.estimativadeprojetodesoftware.model.Projeto;
import com.br.estimativadeprojetodesoftware.model.Usuario;
import com.br.estimativadeprojetodesoftware.repository.IProjetoRepository;
import com.br.estimativadeprojetodesoftware.service.PerfilRepositoryService;
import com.br.estimativadeprojetodesoftware.service.UsuarioHasProjetoRepositoryService;
import com.br.estimativadeprojetodesoftware.service.UsuarioRepositoryService;
import com.br.estimativadeprojetodesoftware.singleton.ConexaoSingleton;

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
         //   stmt.setString(6, projeto.getEstimativa().getId().toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar projeto", e);
        }
    }

    @Override
    public void atualizar(Projeto projeto) {
        String sql = "UPDATE projeto SET nomeProjeto = ?, tipoProjeto = ?, updated_atProjeto = NOW(), status = ? WHERE idProjeto = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, projeto.getNome());
            stmt.setString(2, projeto.getTipo());
            stmt.setString(3, projeto.getStatus());
        //    stmt.setString(4, projeto.getEstimativa().getId().toString());
            stmt.setString(4, projeto.getId().toString());
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
        UUID projetoId = UUID.fromString(rs.getString("idProjeto"));
        List<Perfil> perfis = buscarPerfisPorProjeto(projetoId);
        List<Usuario> usuarios = buscarUsuariosPorProjeto(projetoId);
       // Estimativa estimativa = buscarEstimativaPorId(UUID.fromString(rs.getString("estimativa_idEstimativa")));
        
        return new Projeto(
                projetoId,
                rs.getString("nomeProjeto"),
                rs.getString("tipoProjeto"),
                usuarios.get(0).getNome(),
                rs.getTimestamp("created_atProjeto").toLocalDateTime(),
                rs.getString("status"),
                UsuarioHasProjetoRepositoryService.getInstancia().buscarIsCompartilhadoPorId(usuarios.get(0).getId(), projetoId),
                usuarios.get(0).getNome(),
                perfis,
                usuarios,
                null
        );
    }
    
    private List<Usuario> buscarUsuariosPorProjeto(UUID projetoId) {
        List<Usuario> usuarios = new ArrayList<>();
        List<String> usuarioIds = UsuarioHasProjetoRepositoryService.getInstancia().buscarProjetosPorUsuario(projetoId);

        for (String userId : usuarioIds) {
            UsuarioRepositoryService.getInstancia().buscarPorId(UUID.fromString(userId)).ifPresent(usuarios::add);
        }
        return usuarios;
    }

    private List<Perfil> buscarPerfisPorProjeto(UUID projetoId) {
        List<Perfil> perfis = new ArrayList<>();
        String sql = "SELECT perfil_idPerfil FROM projeto_has_perfil WHERE projeto_idProjeto = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, projetoId.toString());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                perfis.add(PerfilRepositoryService.getInstancia().buscarPorId(UUID.fromString(rs.getString("perfil_idPerfil"))).get());
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar perfis do projeto", e);
        }
        return perfis;
    }

    
    /*
    private Estimativa buscarEstimativaPorId(UUID estimativaId) {
        String sql = "SELECT * FROM estimativa WHERE idEstimativa = ?";
        Map<String, Integer> campos = new HashMap<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, estimativaId.toString());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String estimativaIdString = rs.getString("idEstimativa");
                String createdAt = rs.getString("created_atEstimativa");

                // Buscando campos associados à estimativa
                String camposQuery = "SELECT * FROM estimativa_has_campo WHERE estimativa_idEstimativa = ?";
                try (PreparedStatement camposStmt = connection.prepareStatement(camposQuery)) {
                    camposStmt.setString(1, estimativaIdString);
                    ResultSet camposRs = camposStmt.executeQuery();
                    while (camposRs.next()) {
                        String campoNome = camposRs.getString("campo_idCampo");
                        int diasEstimativaCampo = camposRs.getInt("diasEstimativaCampo");
                        campos.put(campoNome, diasEstimativaCampo);
                    }
                } catch (SQLException e) {
                    throw new RuntimeException("Erro ao buscar campos da estimativa", e);
                }

                return new Estimativa(
                        UUID.fromString(estimativaIdString),
                        LocalDateTime.parse(createdAt),
                        campos
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar estimativa do projeto", e);
        }
        return null;
    }*/
}

