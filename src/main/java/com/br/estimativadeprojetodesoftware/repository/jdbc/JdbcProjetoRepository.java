package com.br.estimativadeprojetodesoftware.repository.jdbc;

import static com.br.estimativadeprojetodesoftware.repository.jdbc.JdbcSuporte.dataHora;
import static com.br.estimativadeprojetodesoftware.repository.jdbc.JdbcSuporte.decimal;
import static com.br.estimativadeprojetodesoftware.repository.jdbc.JdbcSuporte.emTransacao;
import static com.br.estimativadeprojetodesoftware.repository.jdbc.JdbcSuporte.executar;

import com.br.estimativadeprojetodesoftware.model.CustoAdicional;
import com.br.estimativadeprojetodesoftware.model.Funcionalidade;
import com.br.estimativadeprojetodesoftware.model.ItemProjeto;
import com.br.estimativadeprojetodesoftware.model.Plataforma;
import com.br.estimativadeprojetodesoftware.model.Projeto;
import com.br.estimativadeprojetodesoftware.repository.IProjetoRepository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/** Repository de projetos em JDBC (SQLite e H2). */
public class JdbcProjetoRepository implements IProjetoRepository {

    private static final String COLUNAS = "p.id, p.nome, p.dono_id, u.nome AS dono_nome, p.web_backend, p.ios, p.android, "
            + "p.pct_impostos, p.pct_lucro, p.calculado, p.criado_em, p.atualizado_em";

    private final Connection conexao;
    private final ItensJdbc itens;

    public JdbcProjetoRepository(Connection conexao) {
        this.conexao = conexao;
        this.itens = new ItensJdbc(conexao, "projeto");
    }

    @Override
    public void salvar(Projeto projeto) {
        emTransacao(conexao, "Erro ao salvar o projeto \"" + projeto.getNome() + "\"", () -> {
            boolean existe;
            try (PreparedStatement st = conexao.prepareStatement("SELECT 1 FROM projeto WHERE id = ?")) {
                st.setString(1, projeto.getId().toString());
                try (ResultSet rs = st.executeQuery()) {
                    existe = rs.next();
                }
            }
            String sql = existe
                    ? "UPDATE projeto SET nome = ?, dono_id = ?, web_backend = ?, ios = ?, android = ?, pct_impostos = ?, "
                    + "pct_lucro = ?, calculado = ?, criado_em = ?, atualizado_em = ? WHERE id = ?"
                    : "INSERT INTO projeto (nome, dono_id, web_backend, ios, android, pct_impostos, pct_lucro, calculado, "
                    + "criado_em, atualizado_em, id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement st = conexao.prepareStatement(sql)) {
                st.setString(1, projeto.getNome());
                st.setString(2, projeto.getDonoId().toString());
                st.setBoolean(3, projeto.isPlataformaMarcada(Plataforma.WEB_BACKEND));
                st.setBoolean(4, projeto.isPlataformaMarcada(Plataforma.IOS));
                st.setBoolean(5, projeto.isPlataformaMarcada(Plataforma.ANDROID));
                st.setString(6, decimal(projeto.getPercentualImpostos()));
                st.setString(7, decimal(projeto.getPercentualLucro()));
                st.setBoolean(8, projeto.isCalculado());
                st.setString(9, dataHora(projeto.getCriadoEm()));
                st.setString(10, dataHora(projeto.getAtualizadoEm()));
                st.setString(11, projeto.getId().toString());
                st.executeUpdate();
            }
            itens.apagar(projeto.getId());
            apagarCustos(projeto.getId());
            for (ItemProjeto item : projeto.getItens()) {
                itens.inserirFuncionalidade(projeto.getId(), item.getFuncionalidade(), item.isSelecionado());
            }
            itens.inserirTaxas(projeto.getId(), projeto.getTaxas());
            try (PreparedStatement st = conexao.prepareStatement(
                    "INSERT INTO projeto_custo_adicional (id, projeto_id, descricao, valor, ordem) VALUES (?, ?, ?, ?, ?)")) {
                int ordem = 0;
                for (CustoAdicional c : projeto.getCustosAdicionais()) {
                    st.setString(1, c.getId().toString());
                    st.setString(2, projeto.getId().toString());
                    st.setString(3, c.getDescricao());
                    st.setString(4, decimal(c.getValor()));
                    st.setInt(5, ++ordem);
                    st.addBatch();
                }
                st.executeBatch();
            }
            return null;
        });
    }

    private void apagarCustos(UUID projetoId) throws SQLException {
        try (PreparedStatement st = conexao.prepareStatement("DELETE FROM projeto_custo_adicional WHERE projeto_id = ?")) {
            st.setString(1, projetoId.toString());
            st.executeUpdate();
        }
    }

    @Override
    public boolean remover(UUID id) {
        return emTransacao(conexao, "Erro ao remover o projeto", () -> {
            itens.apagar(id);
            apagarCustos(id);
            try (PreparedStatement comp = conexao.prepareStatement("DELETE FROM compartilhamento WHERE projeto_id = ?");
                    PreparedStatement st = conexao.prepareStatement("DELETE FROM projeto WHERE id = ?")) {
                comp.setString(1, id.toString());
                comp.executeUpdate();
                st.setString(1, id.toString());
                return st.executeUpdate() > 0;
            }
        });
    }

    @Override
    public Optional<Projeto> buscarPorId(UUID id) {
        List<Projeto> lista = buscar("WHERE p.id = ?", id.toString());
        return lista.isEmpty() ? Optional.empty() : Optional.of(lista.get(0));
    }

    @Override
    public List<Projeto> buscarPorDono(UUID donoId) {
        return buscar("WHERE p.dono_id = ? ORDER BY p.nome", donoId.toString());
    }

    @Override
    public boolean existeNomeParaDono(String nome, UUID donoId, UUID ignorarProjetoId) {
        return executar("Erro ao consultar projetos", () -> {
            try (PreparedStatement st = conexao.prepareStatement(
                    "SELECT COUNT(*) FROM projeto WHERE LOWER(nome) = LOWER(?) AND dono_id = ? AND id <> ?")) {
                st.setString(1, nome.trim());
                st.setString(2, donoId.toString());
                st.setString(3, ignorarProjetoId == null ? "" : ignorarProjetoId.toString());
                try (ResultSet rs = st.executeQuery()) {
                    return rs.next() && rs.getInt(1) > 0;
                }
            }
        });
    }

    private List<Projeto> buscar(String filtro, String parametro) {
        return executar("Erro ao buscar projetos", () -> {
            List<Projeto> projetos = new ArrayList<>();
            try (PreparedStatement st = conexao.prepareStatement(
                    "SELECT " + COLUNAS + " FROM projeto p JOIN usuario u ON u.id = p.dono_id " + filtro)) {
                st.setString(1, parametro);
                try (ResultSet rs = st.executeQuery()) {
                    while (rs.next()) {
                        projetos.add(mapearCabecalho(rs));
                    }
                }
            }
            for (Projeto p : projetos) {
                completar(p);
            }
            return projetos;
        });
    }

    private Projeto mapearCabecalho(ResultSet rs) throws SQLException {
        Projeto p = new Projeto(UUID.fromString(rs.getString("id")), rs.getString("nome"),
                UUID.fromString(rs.getString("dono_id")), null, decimal(rs.getString("pct_impostos")),
                decimal(rs.getString("pct_lucro")), rs.getBoolean("calculado"), dataHora(rs.getString("criado_em")),
                dataHora(rs.getString("atualizado_em")));
        p.setDonoNome(rs.getString("dono_nome"));
        p.setPlataformaMarcada(Plataforma.WEB_BACKEND, rs.getBoolean("web_backend"));
        p.setPlataformaMarcada(Plataforma.IOS, rs.getBoolean("ios"));
        p.setPlataformaMarcada(Plataforma.ANDROID, rs.getBoolean("android"));
        return p;
    }

    private void completar(Projeto p) throws SQLException {
        p.setTaxas(itens.lerTaxas(p.getId()));
        for (Map.Entry<Funcionalidade, Boolean> e : itens.lerFuncionalidades(p.getId(), true).entrySet()) {
            p.adicionarItem(new ItemProjeto(e.getKey(), e.getValue()));
        }
        try (PreparedStatement st = conexao.prepareStatement(
                "SELECT id, descricao, valor FROM projeto_custo_adicional WHERE projeto_id = ? ORDER BY ordem")) {
            st.setString(1, p.getId().toString());
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    p.adicionarCustoAdicional(new CustoAdicional(UUID.fromString(rs.getString(1)), rs.getString(2),
                            decimal(rs.getString(3))));
                }
            }
        }
    }
}
