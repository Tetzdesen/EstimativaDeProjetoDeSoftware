package com.br.estimativadeprojetodesoftware.repository.h2;

import com.br.estimativadeprojetodesoftware.model.Projeto;
import com.br.estimativadeprojetodesoftware.repository.IProjetoRepository;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ProjetoRepositoryH2 implements IProjetoRepository {

    private Connection connection;

    public ProjetoRepositoryH2(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void salvar(Projeto projeto) {
        String sql = "INSERT INTO projeto (idProjeto, nomeProjeto, tipoProjeto, created_atProjeto, status, estimativa_idEstimativa) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, projeto.getId().toString());
            statement.setString(2, projeto.getNome());
            statement.setString(3, projeto.getTipo());
            statement.setTimestamp(4, Timestamp.valueOf(projeto.getCreated_at()));
            statement.setString(6, projeto.getStatus());
            statement.setString(7, projeto.getEstimativa().toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void atualizar(Projeto projeto) {
        String sql = "UPDATE projeto SET nomeProjeto = ?, tipoProjeto = ?, updated_atProjeto = NOW(), status = ?, estimativa_idEstimativa = ? WHERE idProjeto = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, projeto.getNome());
            statement.setString(2, projeto.getTipo());
            statement.setString(4, projeto.getStatus());
            statement.setString(5, projeto.getEstimativa().getId().toString());
            statement.setString(6, projeto.getId().toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removerPorId(UUID id) {
        String sql = "DELETE FROM projeto WHERE idProjeto = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<Projeto> buscarPorId(UUID id) {
        String sql = "SELECT * FROM projeto WHERE idProjeto = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id.toString());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(mapToProjeto(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Projeto> buscarTodos() {
        List<Projeto> projetos = new ArrayList<>();
        String sql = "SELECT * FROM projeto";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                projetos.add(mapToProjeto(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return projetos;
    }

    private Projeto mapToProjeto(ResultSet resultSet) throws SQLException {
        return new Projeto(
            UUID.fromString(resultSet.getString("idProjeto")),
            resultSet.getString("nomeProjeto"),
            resultSet.getString("tipoProjeto"),
            resultSet.getTimestamp("created_atProjeto").toLocalDateTime(),
            resultSet.getString("status")
        );
    }
}
