package com.br.estimativadeprojetodesoftware.repository.h2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tetzner
 */
public class UsuarioHasProjetoRepositoryH2 {

    private Connection connection;

    public UsuarioHasProjetoRepositoryH2(Connection connection) {
        this.connection = connection;
    }

    public void salvar(String usuarioId, String projetoId, boolean isCompartilhado) {
        String sql = "INSERT INTO usuario_has_projeto (usuario_idUsuario, projeto_idProjeto, isCompartilhado) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, usuarioId);
            statement.setString(2, projetoId);
            statement.setBoolean(3, isCompartilhado);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removerPorIds(String usuarioId, String projetoId) {
        String sql = "DELETE FROM usuario_has_projeto WHERE usuario_idUsuario = ? AND projeto_idProjeto = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, usuarioId);
            statement.setString(2, projetoId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> buscarProjetosPorUsuario(String usuarioId) {
        List<String> projetos = new ArrayList<>();
        String sql = "SELECT projeto_idProjeto FROM usuario_has_projeto WHERE usuario_idUsuario = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, usuarioId);
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
