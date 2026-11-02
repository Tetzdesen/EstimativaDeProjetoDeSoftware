package com.br.estimativadeprojetodesoftware.repository.jdbc;

import static com.br.estimativadeprojetodesoftware.repository.jdbc.JdbcSuporte.dataHora;
import static com.br.estimativadeprojetodesoftware.repository.jdbc.JdbcSuporte.executar;

import com.br.estimativadeprojetodesoftware.model.Usuario;
import com.br.estimativadeprojetodesoftware.repository.IUsuarioRepository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/** Repository de usuários em JDBC (SQLite e H2). */
public class JdbcUsuarioRepository implements IUsuarioRepository {

    private static final String COLUNAS = "id, nome, email, senha, criado_em";
    private final Connection conexao;

    public JdbcUsuarioRepository(Connection conexao) {
        this.conexao = conexao;
    }

    @Override
    public void salvar(Usuario usuario) {
        executar("Erro ao salvar o usuário", () -> {
            try (PreparedStatement st = conexao.prepareStatement(
                    "INSERT INTO usuario (" + COLUNAS + ") VALUES (?, ?, ?, ?, ?)")) {
                st.setString(1, usuario.getId().toString());
                st.setString(2, usuario.getNome());
                st.setString(3, usuario.getEmail());
                st.setString(4, usuario.getSenha());
                st.setString(5, dataHora(usuario.getCreated_at()));
                return st.executeUpdate();
            }
        });
    }

    @Override
    public void atualizar(Usuario usuario) {
        executar("Erro ao atualizar o usuário", () -> {
            try (PreparedStatement st = conexao.prepareStatement(
                    "UPDATE usuario SET nome = ?, email = ?, senha = ? WHERE id = ?")) {
                st.setString(1, usuario.getNome());
                st.setString(2, usuario.getEmail());
                st.setString(3, usuario.getSenha());
                st.setString(4, usuario.getId().toString());
                return st.executeUpdate();
            }
        });
    }

    @Override
    public boolean removerPorId(UUID id) {
        return executar("Erro ao remover o usuário", () -> {
            try (PreparedStatement st = conexao.prepareStatement("DELETE FROM usuario WHERE id = ?")) {
                st.setString(1, id.toString());
                return st.executeUpdate() > 0;
            }
        });
    }

    @Override
    public Optional<Usuario> buscarPorId(UUID id) {
        return buscarUm("SELECT " + COLUNAS + " FROM usuario WHERE id = ?", id.toString());
    }

    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        if (email == null) {
            return Optional.empty();
        }
        return buscarUm("SELECT " + COLUNAS + " FROM usuario WHERE LOWER(email) = LOWER(?)", email.trim());
    }

    private Optional<Usuario> buscarUm(String sql, String parametro) {
        return executar("Erro ao buscar o usuário", () -> {
            try (PreparedStatement st = conexao.prepareStatement(sql)) {
                st.setString(1, parametro);
                try (ResultSet rs = st.executeQuery()) {
                    return rs.next() ? Optional.of(mapear(rs)) : Optional.<Usuario>empty();
                }
            }
        });
    }

    @Override
    public List<Usuario> buscarTodos() {
        return executar("Erro ao listar os usuários", () -> {
            List<Usuario> lista = new ArrayList<>();
            try (PreparedStatement st = conexao.prepareStatement("SELECT " + COLUNAS + " FROM usuario ORDER BY nome");
                    ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
            return lista;
        });
    }

    @Override
    public Map<String, String> buscarEmailESenhaDeUsuarios() {
        return executar("Erro ao buscar as credenciais", () -> {
            Map<String, String> mapa = new HashMap<>();
            try (PreparedStatement st = conexao.prepareStatement("SELECT email, senha FROM usuario");
                    ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    mapa.put(rs.getString("email"), rs.getString("senha"));
                }
            }
            return mapa;
        });
    }

    private Usuario mapear(ResultSet rs) throws SQLException {
        return new Usuario(UUID.fromString(rs.getString("id")), rs.getString("nome"), rs.getString("email"),
                rs.getString("senha"), dataHora(rs.getString("criado_em")));
    }
}
