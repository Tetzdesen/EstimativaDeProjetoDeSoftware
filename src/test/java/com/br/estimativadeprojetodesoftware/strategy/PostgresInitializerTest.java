package com.br.estimativadeprojetodesoftware.strategy;

import static org.junit.jupiter.api.Assertions.*;

import com.br.estimativadeprojetodesoftware.singleton.ConexaoSingleton;
import org.junit.jupiter.api.Test;

class PostgresInitializerTest {

    private final PostgresInitializer postgres = new PostgresInitializer();

    @Test
    void recusaUrlSemSslVerificado() {
        for (String url : new String[]{
            "jdbc:postgresql://servidor:5432/postgres",
            "jdbc:postgresql://servidor:5432/postgres?sslmode=require&sslrootcert=db/certs/ca.crt",
            "jdbc:postgresql://servidor:5432/postgres?sslmode=verify-full"}) {
            IllegalStateException e = assertThrows(IllegalStateException.class, () -> postgres.prepararArmazenamento(url), url);
            assertTrue(e.getMessage().contains("sslmode=verify-full"), e.getMessage());
        }
    }

    @Test
    void recusaUrlDeOutroBanco() {
        assertThrows(IllegalStateException.class, () -> postgres.prepararArmazenamento("jdbc:h2:mem:x"));
    }

    @Test
    void aceitaUrlComSslVerificado() {
        assertDoesNotThrow(() -> postgres.prepararArmazenamento(
                "jdbc:postgresql://servidor:5432/postgres?currentSchema=estimativa&sslmode=verify-full&sslrootcert=db/certs/ca.crt"));
        assertEquals("org.postgresql.Driver", postgres.getDriver());
    }

    @Test
    void tiposDeBanco() {
        assertInstanceOf(PostgresInitializer.class, ConexaoSingleton.inicializadorPara("Postgres"));
        assertInstanceOf(PostgresInitializer.class, ConexaoSingleton.inicializadorPara(" postgres "));
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> ConexaoSingleton.inicializadorPara("MySQL"));
        assertTrue(e.getMessage().contains("SQLite, H2 ou Postgres"), e.getMessage());
    }
}
