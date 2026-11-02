package com.br.estimativadeprojetodesoftware.repository.jdbc;

import com.br.estimativadeprojetodesoftware.repository.RepositorioException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * Utilitários comuns dos repositórios JDBC. Valores decimais são gravados como
 * texto para preservar a escala exata do BigDecimal nos dois bancos (o SQLite
 * converteria DECIMAL em ponto flutuante).
 */
final class JdbcSuporte {

    private JdbcSuporte() {
    }

    @FunctionalInterface
    interface AcaoSql<T> {
        T executar() throws SQLException;
    }

    static String decimal(BigDecimal valor) {
        return valor == null ? null : valor.toPlainString();
    }

    static BigDecimal decimal(String texto) {
        return texto == null ? null : new BigDecimal(texto);
    }

    static String dataHora(LocalDateTime valor) {
        return valor == null ? null : valor.toString();
    }

    static LocalDateTime dataHora(String texto) {
        return texto == null ? null : LocalDateTime.parse(texto);
    }

    /** Executa a ação em transação; desfaz tudo em caso de erro. */
    static <T> T emTransacao(Connection conexao, String mensagemErro, AcaoSql<T> acao) {
        try {
            boolean autoCommitAnterior = conexao.getAutoCommit();
            conexao.setAutoCommit(false);
            try {
                T resultado = acao.executar();
                conexao.commit();
                return resultado;
            } catch (SQLException | RuntimeException e) {
                conexao.rollback();
                throw e;
            } finally {
                conexao.setAutoCommit(autoCommitAnterior);
            }
        } catch (SQLException e) {
            throw new RepositorioException(mensagemErro + ": " + e.getMessage(), e);
        }
    }

    static <T> T executar(String mensagemErro, AcaoSql<T> acao) {
        try {
            return acao.executar();
        } catch (SQLException e) {
            throw new RepositorioException(mensagemErro + ": " + e.getMessage(), e);
        }
    }
}
