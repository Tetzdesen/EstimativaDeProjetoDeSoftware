package com.br.estimativadeprojetodesoftware.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SenhaHashServiceTest {

    private static final String SENHA = "A@#bb";

    @Test
    @DisplayName("O hash gerado não contém a senha em texto puro")
    void hashNaoGuardaSenhaEmTextoPuro() {
        String hash = SenhaHashService.gerarHash(SENHA);

        assertFalse(hash.contains(SENHA));
        assertTrue(SenhaHashService.estaHasheada(hash));
    }

    @Test
    @DisplayName("Cada hash usa um salt diferente")
    void hashUsaSaltAleatorio() {
        assertNotEquals(SenhaHashService.gerarHash(SENHA), SenhaHashService.gerarHash(SENHA));
    }

    @Test
    @DisplayName("A senha correta confere com o hash guardado")
    void senhaCorretaConfere() {
        String hash = SenhaHashService.gerarHash(SENHA);

        assertTrue(SenhaHashService.verificar(SENHA, hash));
    }

    @Test
    @DisplayName("A senha errada não confere com o hash guardado")
    void senhaErradaNaoConfere() {
        String hash = SenhaHashService.gerarHash(SENHA);

        assertFalse(SenhaHashService.verificar("outraSenha", hash));
        assertFalse(SenhaHashService.verificar("", hash));
        assertFalse(SenhaHashService.verificar(null, hash));
    }

    @Test
    @DisplayName("Recalcular com o salt guardado reproduz o mesmo hash")
    void recalcularReproduzOMesmoHash() {
        String hash = SenhaHashService.gerarHash(SENHA);

        assertEquals(hash, SenhaHashService.recalcularHash(SENHA, hash));
        assertNotEquals(hash, SenhaHashService.recalcularHash("outraSenha", hash));
    }

    @Test
    @DisplayName("Valores fora do formato não são tratados como hash")
    void valorEmTextoPuroNaoEhHash() {
        assertFalse(SenhaHashService.estaHasheada(SENHA));
        assertFalse(SenhaHashService.estaHasheada(null));
        assertFalse(SenhaHashService.estaHasheada(""));
        assertFalse(SenhaHashService.verificar(SENHA, SENHA));
    }

    @Test
    @DisplayName("Senha vazia ou nula não gera hash")
    void senhaVaziaNaoGeraHash() {
        assertThrows(IllegalArgumentException.class, () -> SenhaHashService.gerarHash(null));
        assertThrows(IllegalArgumentException.class, () -> SenhaHashService.gerarHash(""));
    }

    @Test
    @DisplayName("Recalcular exige um hash em formato válido")
    void recalcularExigeHashValido() {
        assertThrows(IllegalArgumentException.class, () -> SenhaHashService.recalcularHash(SENHA, SENHA));
        assertThrows(IllegalArgumentException.class, () -> SenhaHashService.recalcularHash(SENHA, null));
    }
}
