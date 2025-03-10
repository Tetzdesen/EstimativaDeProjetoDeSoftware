package com.br.estimativadeprojetodesoftware.repository.h2;

import com.br.estimativadeprojetodesoftware.singleton.ConexaoSingleton;
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
public class EstimativaHasCampoRepositoryH2 {

    private Connection connection;

    public EstimativaHasCampoRepositoryH2() {
        this.connection = ConexaoSingleton.getInstancia().getConexao();
    }

    public void salvar(String estimativaId, int campoId, double valorEstimativaCampo) {
        String sql = "INSERT INTO estimativa_has_campo (estimativa_idEstimativa, campo_idCampo, valorEstimativaCampo) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, estimativaId);
            statement.setInt(2, campoId);
            statement.setDouble(3, valorEstimativaCampo);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removerPorIds(String estimativaId, int campoId) {
        String sql = "DELETE FROM estimativa_has_campo WHERE estimativa_idEstimativa = ? AND campo_idCampo = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, estimativaId);
            statement.setInt(2, campoId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Double> buscarValoresPorEstimativa(String estimativaId) {
        List<Double> valores = new ArrayList<>();
        String sql = "SELECT valorEstimativaCampo FROM estimativa_has_campo WHERE estimativa_idEstimativa = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, estimativaId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                valores.add(resultSet.getDouble("valorEstimativaCampo"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return valores;
    }
}