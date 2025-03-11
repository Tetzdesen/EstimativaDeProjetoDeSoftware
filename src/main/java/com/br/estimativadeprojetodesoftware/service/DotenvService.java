package com.br.estimativadeprojetodesoftware.service;

import io.github.cdimascio.dotenv.Dotenv;

/**
 *
 * @author tetzner
 */

public final class DotenvService {
    private static final Dotenv dotenv = Dotenv.load();

    private DotenvService() {
        // Construtor privado para evitar instância
    }

    public static String getEnv(String key) {
        return dotenv.get(key);
    }
}
