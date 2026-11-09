package com.br.estimativadeprojetodesoftware.suporte;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

/**
 * Envolve uma conexão e conta as idas ao banco: cada execute*, executeBatch,
 * commit e rollback. Opcionalmente faz um executeBatch falhar, para testar a
 * transação.
 */
public final class ContadorDeIdas {

    private final AtomicInteger idas = new AtomicInteger();
    private final Connection conexao;
    private Predicate<String> falharLoteSe = sql -> false;

    public ContadorDeIdas(Connection real) {
        this.conexao = envolver(real, Connection.class, null);
    }

    public Connection getConexao() {
        return conexao;
    }

    public int getIdas() {
        return idas.get();
    }

    public void zerar() {
        idas.set(0);
    }

    /** Faz falhar o executeBatch dos comandos cujo SQL satisfaz a condição. */
    public void falharLoteSe(Predicate<String> condicao) {
        this.falharLoteSe = condicao;
    }

    @SuppressWarnings("unchecked")
    private <T> T envolver(T alvo, Class<T> tipo, String sql) {
        return (T) Proxy.newProxyInstance(tipo.getClassLoader(), new Class<?>[]{tipo}, (p, m, a) -> {
            String nome = m.getName();
            if (nome.startsWith("execute") || nome.equals("commit") || nome.equals("rollback")) {
                idas.incrementAndGet();
                if (nome.equals("executeBatch") && sql != null && falharLoteSe.test(sql)) {
                    throw new SQLException("falha simulada no lote: " + sql);
                }
            }
            Object r;
            try {
                r = m.invoke(alvo, a);
            } catch (InvocationTargetException e) {
                throw e.getCause();
            }
            if (r instanceof PreparedStatement ps) {
                return envolver(ps, PreparedStatement.class, (String) a[0]);
            }
            if (r instanceof Statement st) {
                return envolver(st, Statement.class, null);
            }
            return r;
        });
    }
}
