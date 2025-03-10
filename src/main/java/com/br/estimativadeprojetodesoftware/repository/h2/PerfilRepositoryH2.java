package com.br.estimativadeprojetodesoftware.repository.h2;

import com.br.estimativadeprojetodesoftware.model.Perfil;
import com.br.estimativadeprojetodesoftware.repository.IPerfilRepository;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class PerfilRepositoryH2 implements IPerfilRepository {

    private Connection connection;

    public PerfilRepositoryH2(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void salvar(Perfil perfil) {
        String sql = "INSERT INTO perfil (idPerfil, nomePerfil, perfilBackend, created_atPerfil, usuario_idUsuario) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, perfil.getId().toString());
            statement.setString(2, perfil.getNome());
            statement.setBoolean(3, perfil.isPerfilBackEnd());
            statement.setTimestamp(4, Timestamp.valueOf(perfil.getCreated_at()));
            statement.setString(6, perfil.getUsuario().getId().toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void atualizar(Perfil perfil) {
        String sql = "UPDATE perfil SET nomePerfil = ?, perfilBackend = ?, updated_atPerfil = NOW(), usuario_idUsuario = ? WHERE idPerfil = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, perfil.getNome());
            statement.setBoolean(2, perfil.isPerfilBackEnd());
            statement.setString(4, perfil.getUsuario().getId().toString());
            statement.setString(5, perfil.getId().toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removerPorId(UUID id) {
        String sql = "DELETE FROM perfil WHERE idPerfil = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<Perfil> buscarPorId(UUID id) {
        String sql = "SELECT * FROM perfil WHERE idPerfil = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id.toString());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(mapToPerfil(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Perfil> buscarTodos() {
        List<Perfil> perfis = new ArrayList<>();
        String sql = "SELECT * FROM perfil";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                perfis.add(mapToPerfil(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return perfis;
    }

    private Perfil mapToPerfil(ResultSet resultSet) throws SQLException {
        return new Perfil(
            UUID.fromString(resultSet.getString("idPerfil")),
            resultSet.getString("nomePerfil"),
            resultSet.getBoolean("perfilBackend"),
            resultSet.getTimestamp("created_atPerfil").toLocalDateTime(),
            null
            //UUID.fromString(resultSet.getString("usuario_idUsuario"))
        );
    }

 
}
