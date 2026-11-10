package com.br.estimativadeprojetodesoftware.service;

import io.github.cdimascio.dotenv.Dotenv;

/**
 * Lê o arquivo .env da pasta de execução. Se ele não existir, usa as variáveis
 * de ambiente do sistema.
 */
public final class DotenvService {

    private static final Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

    /** Pasta usada quando o sistema é aberto sem .env (ex.: o jar baixado do release). */
    public static final java.nio.file.Path PASTA_PADRAO =
            java.nio.file.Path.of(System.getProperty("user.home"), ".estimativa-de-projeto");

    private static volatile boolean usarPadroes;

    /**
     * Sem .env (ou sem TIPO_BANCO nele), passa a usar SQLite e log em
     * {@link #PASTA_PADRAO}, para o jar funcionar com dois cliques. Só a
     * inicialização do aplicativo liga isso; os testes continuam sem banco
     * configurado.
     */
    public static void usarPadroesSemEnv() {
        usarPadroes = true;
    }

    /** Para testes: volta ao comportamento sem padrões. */
    static void desligarPadroes() {
        usarPadroes = false;
    }

    /** true se o sistema está rodando com os padrões, sem .env. */
    public static boolean usandoPadroes() {
        return usarPadroes && dotenv.get("TIPO_BANCO") == null;
    }

    private static String padrao(String key) {
        if (!usandoPadroes()) {
            return null;
        }
        return switch (key) {
            case "TIPO_BANCO" -> "SQLite";
            case "DB_URL" -> "jdbc:sqlite:" + PASTA_PADRAO.resolve("database.db");
            case "SQLite" -> "com.br.estimativadeprojetodesoftware.abstractfactory.FabricaRepositorySQLite";
            case "LOG_ARQUIVO" -> PASTA_PADRAO.resolve("logs").resolve("estimativa").toString();
            default -> null;
        };
    }

    private DotenvService() {
    }

    public static String getEnv(String key) {
        return dotenv.get(key);
    }

    /** Valor sem aspas e sem espaços nas pontas, ou null se a chave não existir. */
    public static String getValor(String key) {
        String valor = dotenv.get(key);
        if (valor == null) {
            return padrao(key);
        }
        valor = valor.trim();
        if (valor.length() >= 2 && valor.startsWith("\"") && valor.endsWith("\"")) {
            valor = valor.substring(1, valor.length() - 1).trim();
        }
        return valor.isEmpty() ? null : valor;
    }
}
