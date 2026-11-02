package com.br.estimativadeprojetodesoftware.repository.jdbc;

import static com.br.estimativadeprojetodesoftware.repository.jdbc.JdbcSuporte.executar;

import com.br.estimativadeprojetodesoftware.repository.ICompartilhamentoRepository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/** Repository da tabela compartilhamento. */
public class JdbcCompartilhamentoRepository implements ICompartilhamentoRepository {

    private final Connection conexao;

    public JdbcCompartilhamentoRepository(Connection conexao) {
        this.conexao = conexao;
    }

    @Override
    public void compartilhar(UUID projetoId, UUID usuarioId) {
        executar("Erro ao compartilhar o projeto", () -> {
            try (PreparedStatement st = conexao.prepareStatement(
                    "INSERT INTO compartilhamento (projeto_id, usuario_id, compartilhado_em) VALUES (?, ?, ?)")) {
                st.setString(1, projetoId.toString());
                st.setString(2, usuarioId.toString());
                st.setString(3, LocalDateTime.now().toString());
                return st.executeUpdate();
            }
        });
    }

    @Override
    public boolean estaCompartilhado(UUID projetoId, UUID usuarioId) {
        return executar("Erro ao consultar o compartilhamento", () -> {
            try (PreparedStatement st = conexao.prepareStatement(
                    "SELECT 1 FROM compartilhamento WHERE projeto_id = ? AND usuario_id = ?")) {
                st.setString(1, projetoId.toString());
                st.setString(2, usuarioId.toString());
                try (ResultSet rs = st.executeQuery()) {
                    return rs.next();
                }
            }
        });
    }

    @Override
    public List<UUID> buscarProjetosCompartilhadosCom(UUID usuarioId) {
        return listar("SELECT projeto_id FROM compartilhamento WHERE usuario_id = ? ORDER BY compartilhado_em", usuarioId);
    }

    @Override
    public List<UUID> buscarDestinatarios(UUID projetoId) {
        return listar("SELECT usuario_id FROM compartilhamento WHERE projeto_id = ? ORDER BY compartilhado_em", projetoId);
    }

    private List<UUID> listar(String sql, UUID parametro) {
        return executar("Erro ao consultar os compartilhamentos", () -> {
            List<UUID> ids = new ArrayList<>();
            try (PreparedStatement st = conexao.prepareStatement(sql)) {
                st.setString(1, parametro.toString());
                try (ResultSet rs = st.executeQuery()) {
                    while (rs.next()) {
                        ids.add(UUID.fromString(rs.getString(1)));
                    }
                }
            }
            return ids;
        });
    }
}
