package com.br.estimativadeprojetodesoftware.strategy;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Strategy com o que muda entre os bancos: preparo do armazenamento, ajustes
 * da conexão e criação das tabelas.
 */
public interface DatabaseInitializer {

    /**
     * Classe do driver JDBC. É carregada explicitamente porque, no jar com
     * dependências, só um arquivo META-INF/services/java.sql.Driver sobrevive.
     */
    String getDriver();

    /** Prepara o armazenamento antes de abrir a conexão (ex.: criar a pasta do arquivo). */
    void prepararArmazenamento(String url);

    /** Ajustes de sessão logo após abrir a conexão. */
    default void configurarConexao(Connection conexao) throws SQLException {
    }

    /** Cria as tabelas que ainda não existem. Pode rodar a cada início. */
    default void criarTabelas(Connection conexao) throws SQLException {
        try (Statement st = conexao.createStatement()) {
            for (String ddl : EsquemaBanco.TABELAS) {
                st.execute(ddl);
            }
        }
    }
}
