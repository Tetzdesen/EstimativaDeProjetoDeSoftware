package com.br.estimativadeprojetodesoftware.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UsuarioTest {

    private Usuario novoUsuario() {
        return new Usuario("Maria", "maria@email.com", "pbkdf2_sha256$1$c2FsdA==$aGFzaA==");
    }

    @Test
    @DisplayName("O toString não expõe a senha")
    void toStringNaoExpoeSenha() {
        Usuario usuario = novoUsuario();

        assertFalse(usuario.toString().contains(usuario.getSenha()));
        assertTrue(usuario.toString().contains("senha=****"));
    }

    @Test
    @DisplayName("Usuários são iguais pelo id")
    void igualdadePorId() {
        Usuario a = novoUsuario();
        Usuario b = new Usuario(a.getId(), "Outro", "x@y.com", "s", LocalDateTime.now());
        assertEquals(a, b);
    }
}
