package com.br.estimativadeprojetodesoftware.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    @DisplayName("Usuário novo começa sem projetos e sem perfis")
    void usuarioNovoComecaVazio() {
        Usuario usuario = novoUsuario();

        assertTrue(usuario.getProjetos().isEmpty());
        assertTrue(usuario.getPerfis().isEmpty());
    }

    @Test
    @DisplayName("As listas devolvidas são imutáveis")
    void listasSaoImutaveis() {
        Usuario usuario = novoUsuario();

        assertThrows(UnsupportedOperationException.class, () -> usuario.getProjetos().clear());
        assertThrows(UnsupportedOperationException.class, () -> usuario.getPerfis().clear());
    }

    @Test
    @DisplayName("Não é possível adicionar projeto ou perfil nulo")
    void naoAceitaProjetoOuPerfilNulo() {
        Usuario usuario = novoUsuario();

        assertThrows(IllegalArgumentException.class, () -> usuario.adicionarProjeto(null));
        assertThrows(IllegalArgumentException.class, () -> usuario.adicionarPerfil(null));
        assertThrows(IllegalArgumentException.class, () -> usuario.removerProjeto(null));
        assertThrows(IllegalArgumentException.class, () -> usuario.removerPerfil(null));
    }

    @Test
    @DisplayName("O log não pode ser vazio")
    void logNaoPodeSerVazio() {
        Usuario usuario = novoUsuario();

        assertThrows(IllegalArgumentException.class, () -> usuario.setLog(""));

        usuario.setLog("JSON");
        assertEquals("JSON", usuario.getLog());
    }
}
