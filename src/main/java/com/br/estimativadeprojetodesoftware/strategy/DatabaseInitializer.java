package com.br.estimativadeprojetodesoftware.strategy;

/**
 *
 * @author tetzner
 */
public interface DatabaseInitializer {
    void inicializarBanco(String url, String user, String senha);
}
