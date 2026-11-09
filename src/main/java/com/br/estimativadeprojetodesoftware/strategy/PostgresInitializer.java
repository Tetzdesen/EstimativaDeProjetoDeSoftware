package com.br.estimativadeprojetodesoftware.strategy;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;

/**
 * PostgreSQL remoto (ex.: Supabase). Não há arquivo a preparar, mas a URL
 * precisa de SSL com verificação do certificado do servidor; sem isso a
 * conexão é recusada antes de qualquer acesso.
 */
public class PostgresInitializer implements DatabaseInitializer {

    @Override
    public String getDriver() {
        return "org.postgresql.Driver";
    }

    @Override
    public void prepararArmazenamento(String url) {
        String minuscula = url.toLowerCase(Locale.ROOT);
        if (!minuscula.startsWith("jdbc:postgresql:")) {
            throw new IllegalStateException("A URL do PostgreSQL precisa começar com jdbc:postgresql:");
        }
        if (!parametro(minuscula, "sslmode").equals("verify-full") || parametro(minuscula, "sslrootcert").isEmpty()) {
            throw new IllegalStateException("A conexão com o PostgreSQL precisa de SSL com verificação do certificado: "
                    + "acrescente sslmode=verify-full e sslrootcert=<caminho do certificado da autoridade> à DB_URL "
                    + "(veja o .env.example).");
        }
    }

    /** Garante que as tabelas sem esquema na consulta são as do esquema indicado em currentSchema. */
    @Override
    public void configurarConexao(Connection conexao) throws SQLException {
        String esquema = conexao.getSchema();
        if (esquema != null && esquema.matches("[a-z_][a-z0-9_]*")) {
            try (Statement st = conexao.createStatement()) {
                st.execute("SET search_path TO " + esquema);
            }
        }
    }

    /**
     * Cria as tabelas que faltam e ativa o RLS em todas elas. Sem políticas,
     * o RLS nega tudo aos papéis da API do Supabase (anon, authenticated); o
     * papel do sistema, dono das tabelas, não é afetado.
     */
    @Override
    public void criarTabelas(Connection conexao) throws SQLException {
        DatabaseInitializer.super.criarTabelas(conexao);
        try (Statement st = conexao.createStatement()) {
            for (String ddl : EsquemaBanco.TABELAS) {
                java.util.regex.Matcher m = java.util.regex.Pattern.compile("CREATE TABLE IF NOT EXISTS (\\w+)").matcher(ddl);
                if (m.find()) {
                    st.execute("ALTER TABLE " + m.group(1) + " ENABLE ROW LEVEL SECURITY");
                }
            }
        }
    }

    /** Valor de um parâmetro da URL (já em minúsculas), ou vazio se não existir. */
    private static String parametro(String url, String nome) {
        int inicio = url.indexOf('?');
        if (inicio < 0) {
            return "";
        }
        for (String par : url.substring(inicio + 1).split("&")) {
            String[] partes = par.split("=", 2);
            if (partes[0].trim().equals(nome)) {
                return partes.length > 1 ? partes[1].trim() : "";
            }
        }
        return "";
    }
}
