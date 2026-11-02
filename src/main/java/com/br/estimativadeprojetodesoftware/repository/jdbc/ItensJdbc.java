package com.br.estimativadeprojetodesoftware.repository.jdbc;

import static com.br.estimativadeprojetodesoftware.repository.jdbc.JdbcSuporte.decimal;

import com.br.estimativadeprojetodesoftware.model.Funcionalidade;
import com.br.estimativadeprojetodesoftware.model.Papel;
import com.br.estimativadeprojetodesoftware.model.Plataforma;
import com.br.estimativadeprojetodesoftware.model.TaxaDiaria;
import com.br.estimativadeprojetodesoftware.model.TipoSelecao;
import com.br.estimativadeprojetodesoftware.model.TipoValor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Leitura e gravação de funcionalidades, valores por plataforma e taxas, comum
 * a perfis (prefixo "perfil") e projetos (prefixo "projeto"). Valor nulo em
 * *_valor significa "não aplicável": a linha não é gravada.
 */
final class ItensJdbc {

    private final Connection conexao;
    private final String prefixo;

    ItensJdbc(Connection conexao, String prefixo) {
        this.conexao = conexao;
        this.prefixo = prefixo;
    }

    void apagar(UUID donoId) throws SQLException {
        try (PreparedStatement val = conexao.prepareStatement("DELETE FROM " + prefixo + "_valor WHERE funcionalidade_id IN "
                + "(SELECT id FROM " + prefixo + "_funcionalidade WHERE " + prefixo + "_id = ?)");
                PreparedStatement fun = conexao.prepareStatement("DELETE FROM " + prefixo + "_funcionalidade WHERE " + prefixo + "_id = ?");
                PreparedStatement tax = conexao.prepareStatement("DELETE FROM " + prefixo + "_taxa WHERE " + prefixo + "_id = ?")) {
            for (PreparedStatement st : new PreparedStatement[]{val, fun, tax}) {
                st.setString(1, donoId.toString());
                st.executeUpdate();
            }
        }
    }

    /** Insere a funcionalidade; {@code selecionado} só é usado em projetos. */
    void inserirFuncionalidade(UUID donoId, Funcionalidade f, Boolean selecionado) throws SQLException {
        boolean projeto = selecionado != null;
        String sql = "INSERT INTO " + prefixo + "_funcionalidade (id, " + prefixo + "_id, categoria, nome, tipo_selecao, tipo_valor, ordem"
                + (projeto ? ", selecionado) VALUES (?, ?, ?, ?, ?, ?, ?, ?)" : ") VALUES (?, ?, ?, ?, ?, ?, ?)");
        try (PreparedStatement st = conexao.prepareStatement(sql)) {
            st.setString(1, f.getId().toString());
            st.setString(2, donoId.toString());
            st.setString(3, f.getCategoria());
            st.setString(4, f.getNome());
            st.setString(5, f.getTipoSelecao().name());
            st.setString(6, f.getTipoValor().name());
            st.setInt(7, f.getOrdem());
            if (projeto) {
                st.setBoolean(8, selecionado);
            }
            st.executeUpdate();
        }
        try (PreparedStatement st = conexao.prepareStatement(
                "INSERT INTO " + prefixo + "_valor (funcionalidade_id, plataforma, valor) VALUES (?, ?, ?)")) {
            for (Plataforma p : Plataforma.values()) {
                if (f.isAplicavel(p)) {
                    st.setString(1, f.getId().toString());
                    st.setString(2, p.name());
                    st.setString(3, decimal(f.getValor(p).orElseThrow()));
                    st.addBatch();
                }
            }
            st.executeBatch();
        }
    }

    void inserirTaxas(UUID donoId, TaxaDiaria taxas) throws SQLException {
        try (PreparedStatement st = conexao.prepareStatement(
                "INSERT INTO " + prefixo + "_taxa (" + prefixo + "_id, plataforma, papel, valor) VALUES (?, ?, ?, ?)")) {
            for (Plataforma p : Plataforma.values()) {
                for (Papel papel : Papel.values()) {
                    st.setString(1, donoId.toString());
                    st.setString(2, p.name());
                    st.setString(3, papel.name());
                    st.setString(4, decimal(taxas.get(p, papel)));
                    st.addBatch();
                }
            }
            st.executeBatch();
        }
    }

    TaxaDiaria lerTaxas(UUID donoId) throws SQLException {
        TaxaDiaria taxas = new TaxaDiaria();
        try (PreparedStatement st = conexao.prepareStatement(
                "SELECT plataforma, papel, valor FROM " + prefixo + "_taxa WHERE " + prefixo + "_id = ?")) {
            st.setString(1, donoId.toString());
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    taxas.set(Plataforma.valueOf(rs.getString(1)), Papel.valueOf(rs.getString(2)), decimal(rs.getString(3)));
                }
            }
        }
        return taxas;
    }

    /** Funcionalidades em ordem, com a seleção (falso para perfis). */
    Map<Funcionalidade, Boolean> lerFuncionalidades(UUID donoId, boolean projeto) throws SQLException {
        Map<Funcionalidade, Boolean> resultado = new LinkedHashMap<>();
        Map<String, Funcionalidade> porId = new LinkedHashMap<>();
        String sql = "SELECT id, categoria, nome, tipo_selecao, tipo_valor, ordem" + (projeto ? ", selecionado" : "")
                + " FROM " + prefixo + "_funcionalidade WHERE " + prefixo + "_id = ? ORDER BY ordem";
        try (PreparedStatement st = conexao.prepareStatement(sql)) {
            st.setString(1, donoId.toString());
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    Funcionalidade f = new Funcionalidade(UUID.fromString(rs.getString("id")), rs.getString("categoria"),
                            rs.getString("nome"), TipoSelecao.valueOf(rs.getString("tipo_selecao")),
                            TipoValor.valueOf(rs.getString("tipo_valor")), rs.getInt("ordem"));
                    resultado.put(f, projeto && rs.getBoolean("selecionado"));
                    porId.put(f.getId().toString(), f);
                }
            }
        }
        try (PreparedStatement st = conexao.prepareStatement("SELECT v.funcionalidade_id, v.plataforma, v.valor FROM "
                + prefixo + "_valor v JOIN " + prefixo + "_funcionalidade f ON f.id = v.funcionalidade_id WHERE f."
                + prefixo + "_id = ?")) {
            st.setString(1, donoId.toString());
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    Funcionalidade f = porId.get(rs.getString(1));
                    if (f != null && rs.getString(3) != null) {
                        f.setValor(Plataforma.valueOf(rs.getString(2)), decimal(rs.getString(3)));
                    }
                }
            }
        }
        return resultado;
    }
}
