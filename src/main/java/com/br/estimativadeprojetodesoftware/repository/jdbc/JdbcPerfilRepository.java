package com.br.estimativadeprojetodesoftware.repository.jdbc;

import static com.br.estimativadeprojetodesoftware.repository.jdbc.JdbcSuporte.dataHora;
import static com.br.estimativadeprojetodesoftware.repository.jdbc.JdbcSuporte.emTransacao;
import static com.br.estimativadeprojetodesoftware.repository.jdbc.JdbcSuporte.executar;

import com.br.estimativadeprojetodesoftware.model.Funcionalidade;
import com.br.estimativadeprojetodesoftware.model.Perfil;
import com.br.estimativadeprojetodesoftware.repository.IPerfilRepository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/** Repository de perfis em JDBC (SQLite e H2). */
public class JdbcPerfilRepository implements IPerfilRepository {

    private final Connection conexao;
    private final ItensJdbc itens;

    public JdbcPerfilRepository(Connection conexao) {
        this.conexao = conexao;
        this.itens = new ItensJdbc(conexao, "perfil");
    }

    @Override
    public void salvar(Perfil perfil) {
        emTransacao(conexao, "Erro ao salvar o perfil \"" + perfil.getNome() + "\"", () -> {
            itens.apagar(perfil.getId());
            try (PreparedStatement del = conexao.prepareStatement("DELETE FROM perfil WHERE id = ?")) {
                del.setString(1, perfil.getId().toString());
                del.executeUpdate();
            }
            try (PreparedStatement st = conexao.prepareStatement(
                    "INSERT INTO perfil (id, nome, padrao, dono_id, criado_em) VALUES (?, ?, ?, ?, ?)")) {
                st.setString(1, perfil.getId().toString());
                st.setString(2, perfil.getNome());
                st.setBoolean(3, perfil.isPadrao());
                st.setString(4, perfil.getDonoId() == null ? null : perfil.getDonoId().toString());
                st.setString(5, dataHora(perfil.getCriadoEm()));
                st.executeUpdate();
            }
            itens.inserirFuncionalidades(perfil.getId(), perfil.getFuncionalidades(), null);
            itens.inserirTaxas(perfil.getId(), perfil.getTaxas());
            return null;
        });
    }

    @Override
    public boolean remover(UUID id) {
        return emTransacao(conexao, "Erro ao remover o perfil", () -> {
            itens.apagar(id);
            try (PreparedStatement st = conexao.prepareStatement("DELETE FROM perfil WHERE id = ?")) {
                st.setString(1, id.toString());
                return st.executeUpdate() > 0;
            }
        });
    }

    @Override
    public Optional<Perfil> buscarPorId(UUID id) {
        List<Perfil> lista = buscar("WHERE id = ?", id.toString());
        return lista.isEmpty() ? Optional.empty() : Optional.of(lista.get(0));
    }

    @Override
    public List<Perfil> buscarVisiveisPara(UUID usuarioId) {
        return buscar("WHERE padrao = ? OR dono_id = ? ORDER BY padrao DESC, nome", Boolean.TRUE, usuarioId.toString());
    }

    @Override
    public boolean existePerfilPadrao() {
        return executar("Erro ao consultar o perfil padrão", () -> {
            try (PreparedStatement st = conexao.prepareStatement("SELECT COUNT(*) FROM perfil WHERE padrao = ?")) {
                st.setBoolean(1, true);
                try (ResultSet rs = st.executeQuery()) {
                    return rs.next() && rs.getInt(1) > 0;
                }
            }
        });
    }

    private List<Perfil> buscar(String filtro, Object... parametros) {
        return executar("Erro ao buscar perfis", () -> {
            List<Perfil> perfis = new ArrayList<>();
            try (PreparedStatement st = conexao.prepareStatement(
                    "SELECT id, nome, padrao, dono_id, criado_em FROM perfil " + filtro)) {
                for (int i = 0; i < parametros.length; i++) {
                    if (parametros[i] instanceof Boolean b) {
                        st.setBoolean(i + 1, b);
                    } else {
                        st.setString(i + 1, (String) parametros[i]);
                    }
                }
                try (ResultSet rs = st.executeQuery()) {
                    while (rs.next()) {
                        perfis.add(mapearCabecalho(rs));
                    }
                }
            }
            List<Perfil> completos = new ArrayList<>();
            for (Perfil p : perfis) {
                completos.add(completar(p));
            }
            return completos;
        });
    }

    private Perfil mapearCabecalho(ResultSet rs) throws SQLException {
        String dono = rs.getString("dono_id");
        return new Perfil(UUID.fromString(rs.getString("id")), rs.getString("nome"), rs.getBoolean("padrao"),
                dono == null ? null : UUID.fromString(dono), null, dataHora(rs.getString("criado_em")));
    }

    private Perfil completar(Perfil cabecalho) throws SQLException {
        Perfil perfil = new Perfil(cabecalho.getId(), cabecalho.getNome(), cabecalho.isPadrao(), cabecalho.getDonoId(),
                itens.lerTaxas(cabecalho.getId()), cabecalho.getCriadoEm());
        for (Funcionalidade f : itens.lerFuncionalidades(cabecalho.getId(), false).keySet()) {
            perfil.adicionarFuncionalidade(f);
        }
        return perfil;
    }
}
