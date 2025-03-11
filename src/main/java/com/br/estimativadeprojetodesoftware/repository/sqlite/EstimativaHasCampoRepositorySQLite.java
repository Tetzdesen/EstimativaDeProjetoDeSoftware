package com.br.estimativadeprojetodesoftware.repository.sqlite;

import com.br.estimativadeprojetodesoftware.repository.IEstimativaHasCampoRepository;
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
public class EstimativaHasCampoRepositorySQLite implements IEstimativaHasCampoRepository {

    private final Connection connection;

    public EstimativaHasCampoRepositorySQLite() {
        this.connection = ConexaoSingleton.getInstancia().getConexao();
    }

    @Override
    public void salvar(String estimativaId, int campoId, double valorEstimativaCampo) {
        String query = "INSERT INTO estimativa_has_campo (estimativa_idEstimativa, campo_idCampo, valorEstimativaCampo) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, estimativaId);
            statement.setInt(2, campoId);
            statement.setDouble(3, valorEstimativaCampo);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removerPorIds(String estimativaId, int campoId) {
        String query = "DELETE FROM estimativa_has_campo WHERE estimativa_idEstimativa = ? AND campo_idCampo = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, estimativaId);
            statement.setInt(2, campoId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<String> buscarNomesPorEstimativa(String estimativaId) {
        List<String> nomesCampos = new ArrayList<>();
        String query = "SELECT c.nomeCampo FROM estimativa_has_campo e " +
                       "JOIN campo c ON e.campo_idCampo = c.idCampo " +
                       "WHERE e.estimativa_idEstimativa = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, estimativaId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    nomesCampos.add(resultSet.getString("nomeCampo"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nomesCampos;
    }

    @Override
    public List<Double> buscarValoresPorEstimativa(String estimativaId) {
        List<Double> valores = new ArrayList<>();
        String query = "SELECT valorEstimativaCampo FROM estimativa_has_campo WHERE estimativa_idEstimativa = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, estimativaId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    valores.add(resultSet.getDouble("valorEstimativaCampo"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return valores;
    }

}
