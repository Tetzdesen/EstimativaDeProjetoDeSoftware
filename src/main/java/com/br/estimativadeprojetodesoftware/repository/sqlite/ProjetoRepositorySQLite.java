package com.br.estimativadeprojetodesoftware.repository.sqlite;

import com.br.estimativadeprojetodesoftware.model.Projeto;
import com.br.estimativadeprojetodesoftware.repository.IProjetoRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ProjetoRepositorySQLite implements IProjetoRepository {
    private final Connection connection;

    public ProjetoRepositorySQLite(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void salvar(Projeto projeto) {
        String sql = "INSERT INTO projeto (idProjeto, nomeProjeto, tipoProjeto, created_atProjeto, status, estimativa_idEstimativa) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, projeto.getId().toString());
            stmt.setString(2, projeto.getNome());
            stmt.setString(3, projeto.getTipo());
            stmt.setTimestamp(4, Timestamp.valueOf(projeto.getCreated_at()));
            stmt.setString(6, projeto.getStatus());
            stmt.setString(7, projeto.getEstimativa().getId().toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar projeto", e);
        }
    }

    @Override
    public void atualizar(Projeto projeto) {
        String sql = "UPDATE projeto SET nomeProjeto = ?, tipoProjeto = ?, updated_atProjeto = NOW(), status = ?, estimativa_idEstimativa = ? WHERE idProjeto = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, projeto.getNome());
            stmt.setString(2, projeto.getTipo());
            stmt.setString(4, projeto.getStatus());
            stmt.setString(5, projeto.getEstimativa().getId().toString());
            stmt.setString(6, projeto.getId().toString());
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
                return Optional.of(new Projeto(
                        UUID.fromString(rs.getString("idProjeto")),
                        rs.getString("nomeProjeto"),
                        rs.getString("tipoProjeto"),
                        rs.getTimestamp("created_atProjeto").toLocalDateTime(),
                        rs.getString("status")
                ));
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
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                projetos.add(new Projeto(
                        UUID.fromString(rs.getString("idProjeto")),
                        rs.getString("nomeProjeto"),
                        rs.getString("tipoProjeto"),
                        rs.getTimestamp("created_atProjeto").toLocalDateTime(),
                        rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar todos os projetos", e);
        }
        return projetos;
    }
}
