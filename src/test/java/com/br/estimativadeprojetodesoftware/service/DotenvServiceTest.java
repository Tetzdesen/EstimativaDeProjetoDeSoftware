package com.br.estimativadeprojetodesoftware.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/** Os testes rodam numa pasta sem .env: sem os padrões ligados, nada aponta para um banco. */
class DotenvServiceTest {

    @AfterEach
    void desligar() {
        DotenvService.desligarPadroes();
    }

    @Test
    void semEnvENemPadroesNaoHaBancoConfigurado() {
        assertNull(DotenvService.getValor("TIPO_BANCO"));
        assertNull(DotenvService.getValor("DB_URL"));
        assertFalse(DotenvService.usandoPadroes());
    }

    @Test
    void aberturaDoAppSemEnvUsaSqliteNaPastaDoUsuario() {
        DotenvService.usarPadroesSemEnv();

        assertTrue(DotenvService.usandoPadroes());
        assertEquals("SQLite", DotenvService.getValor("TIPO_BANCO"));
        assertEquals("jdbc:sqlite:" + DotenvService.PASTA_PADRAO.resolve("database.db"), DotenvService.getValor("DB_URL"));
        assertTrue(DotenvService.getValor("SQLite").endsWith("FabricaRepositorySQLite"));
        assertTrue(DotenvService.getValor("LOG_ARQUIVO").startsWith(DotenvService.PASTA_PADRAO.toString()));
        assertNull(DotenvService.getValor("DB_PASSWORD"));
    }
}
