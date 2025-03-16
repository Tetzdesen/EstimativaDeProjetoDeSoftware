package com.br.estimativadeprojetodesoftware.repository.h2;

import com.br.estimativadeprojetodesoftware.model.Usuario;
import com.br.estimativadeprojetodesoftware.repository.IUsuarioRepository;
import com.br.estimativadeprojetodesoftware.service.UsuarioRepositoryService;
import com.br.estimativadeprojetodesoftware.singleton.ConexaoSingleton;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class UsuarioRepositoryH2 implements IUsuarioRepository {

    private final Connection connection;

    public UsuarioRepositoryH2() {
        this.connection = ConexaoSingleton.getInstancia().getConexao();
    }

    @Override
    public void salvar(Usuario usuario) {
        String sql = "INSERT INTO usuario (idUsuario, nomeUsuario, email, senha, created_atUsuario, log) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, usuario.getId().toString());
            statement.setString(2, usuario.getNome());
            statement.setString(3, usuario.getEmail());
            statement.setString(4, usuario.getSenha());
            statement.setTimestamp(5, Timestamp.valueOf(usuario.getCreated_at()));
            statement.setString(6, "JSON");
            statement.executeUpdate();
 
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void atualizar(Usuario usuario) {
        String sql = "UPDATE usuario SET nomeUsuario = ?, email = ?, senha = ?, updated_atUsuario = CURRENT_TIMESTAMP, log = ? WHERE idUsuario = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, usuario.getNome());
            statement.setString(2, usuario.getEmail());
            statement.setString(3, usuario.getSenha());
            statement.setString(4, usuario.getLog());
            statement.setString(5, usuario.getId().toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean removerPorId(UUID id) {
        String sql = "DELETE FROM usuario WHERE idUsuario = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public Optional<Usuario> buscarPorId(UUID id) {
        String sql = "SELECT * FROM usuario WHERE idUsuario = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id.toString());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(mapToUsuario(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        String sql = "SELECT * FROM usuario WHERE email = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(mapToUsuario(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Usuario> buscarTodos() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuario";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                usuarios.add(mapToUsuario(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usuarios;
    }

    @Override
    public Map<String, String> buscarEmailESenhaDeUsuarios() {
        Map<String, String> usuarios = new HashMap<>();
        String sql = "SELECT email, senha FROM usuario";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String email = resultSet.getString("email");
                String senha = resultSet.getString("senha");
                usuarios.put(email, senha);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usuarios;
    }

    private Usuario mapToUsuario(ResultSet resultSet) throws SQLException {
        UUID idUsuario = UUID.fromString(resultSet.getString("idUsuario"));
        return new Usuario(
                idUsuario,
                resultSet.getString("nomeUsuario"),
                resultSet.getString("email"),
                resultSet.getString("senha"),
                resultSet.getTimestamp("created_atUsuario").toLocalDateTime(),
                resultSet.getString("log")
        );
    }

    @Override
    public List<Usuario> buscarUsuariosPorProjeto(UUID idProjeto) {
        List<String> usuarioIds = new ArrayList<>();
        String query = "SELECT usuario_idUsuario FROM usuario_has_projeto WHERE projeto_idProjeto = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, idProjeto.toString());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                usuarioIds.add(resultSet.getString("usuario_idUsuario"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        List<Usuario> usuarios = new ArrayList<>();
  
        for (String userId : usuarioIds) {
           usuarios.add(new UsuarioRepositoryService().buscarPorId(UUID.fromString(userId)).get());
        }
        
        return usuarios;
    }
}
