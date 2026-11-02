package com.br.estimativadeprojetodesoftware.service;

import io.github.cdimascio.dotenv.Dotenv;

/**
 * Lê o arquivo .env da pasta de execução. Se ele não existir, usa as variáveis
 * de ambiente do sistema.
 */
public final class DotenvService {

    private static final Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

    private DotenvService() {
    }

    public static String getEnv(String key) {
        return dotenv.get(key);
    }

    /** Valor sem aspas e sem espaços nas pontas, ou null se a chave não existir. */
    public static String getValor(String key) {
        String valor = dotenv.get(key);
        if (valor == null) {
            return null;
        }
        valor = valor.trim();
        if (valor.length() >= 2 && valor.startsWith("\"") && valor.endsWith("\"")) {
            valor = valor.substring(1, valor.length() - 1).trim();
        }
        return valor.isEmpty() ? null : valor;
    }
}
