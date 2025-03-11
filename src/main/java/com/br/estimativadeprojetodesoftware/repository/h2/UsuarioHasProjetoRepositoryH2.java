package com.br.estimativadeprojetodesoftware.repository.h2;

import com.br.estimativadeprojetodesoftware.repository.IUsuarioHasProjetoRepository;
import com.br.estimativadeprojetodesoftware.singleton.ConexaoSingleton;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author tetzner
 */
public class UsuarioHasProjetoRepositoryH2 implements IUsuarioHasProjetoRepository {

    private final Connection connection;

    public UsuarioHasProjetoRepositoryH2() {
        this.connection = ConexaoSingleton.getInstancia().getConexao();
    }

    @Override
    public void salvar(UUID idUsuario, UUID idProjeto, boolean isCompartilhado) {
        String sql = "INSERT INTO usuario_has_projeto (usuario_idUsuario, projeto_idProjeto, isCompartilhado) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, idUsuario.toString());
            statement.setString(2, idProjeto.toString());
            statement.setBoolean(3, isCompartilhado);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removerPorIds(UUID idUsuario, UUID idProjeto) {
        String sql = "DELETE FROM usuario_has_projeto WHERE usuario_idUsuario = ? AND projeto_idProjeto = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, idUsuario.toString());
            statement.setString(2, idProjeto.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
        String sql = "SELECT projeto_idProjeto FROM usuario_has_projeto WHERE usuario_idUsuario = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
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
