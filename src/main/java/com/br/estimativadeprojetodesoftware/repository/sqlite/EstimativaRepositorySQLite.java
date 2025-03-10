package com.br.estimativadeprojetodesoftware.repository.sqlite;

import com.br.estimativadeprojetodesoftware.model.Estimativa;
import com.br.estimativadeprojetodesoftware.repository.IEstimativaRepository;
import com.br.estimativadeprojetodesoftware.repository.IEstimativaRepository;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class EstimativaRepositorySQLite implements IEstimativaRepository {

    private Connection connection;

    public EstimativaRepositorySQLite(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void salvar(Estimativa estimativa) {
        String sql = "INSERT INTO estimativa (idEstimativa, created_atEstimativa) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, estimativa.getId().toString());
            statement.setTimestamp(2, Timestamp.valueOf(estimativa.getCreated_at()));
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void atualizar(Estimativa estimativa) {
        String sql = "UPDATE estimativa SET updated_atEstimativa = NOW() WHERE idEstimativa = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(2, estimativa.getId().toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removerPorId(UUID id) {
        String sql = "DELETE FROM estimativa WHERE idEstimativa = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<Estimativa> buscarPorId(UUID id) {
        String sql = "SELECT * FROM estimativa WHERE idEstimativa = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id.toString());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(mapToEstimativa(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Estimativa> buscarTodos() {
        List<Estimativa> estimativas = new ArrayList<>();
        String sql = "SELECT * FROM estimativa";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                estimativas.add(mapToEstimativa(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return estimativas;
    }

    private Estimativa mapToEstimativa(ResultSet resultSet) throws SQLException {
        return new Estimativa(
            UUID.fromString(resultSet.getString("idEstimativa")),
            resultSet.getTimestamp("created_atEstimativa").toLocalDateTime()
        );
    }
}
