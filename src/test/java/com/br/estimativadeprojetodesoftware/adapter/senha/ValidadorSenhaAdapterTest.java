package com.br.estimativadeprojetodesoftware.adapter.senha;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ValidadorSenhaAdapterTest {

    @Test
    void mensagensDoValidadorChegamComAcentoESemPontoEVirgula() {
        assertEquals("Senha não deve possuir uma sequência de letras.",
                ValidadorSenhaAdapter.adaptar("Senha nao deve possuir uma sequencia de letras;"));
        assertEquals("A senha deve conter letras maiúsculas.", ValidadorSenhaAdapter.adaptar("A senha deve conter letras maiusculas;"));
        assertEquals("A senha deve conter no mínimo 4 caracteres distintos.",
                ValidadorSenhaAdapter.adaptar("A senha deve conter no minimo 4 caracteres distintos;"));
        assertEquals("Não use datas.", ValidadorSenhaAdapter.adaptar("Nao use datas"));
    }

    @Test
    void senhaFracaGeraMensagensJaAdaptadas() {
        var mensagens = new ValidadorSenhaAdapter().validar("abc");
        assertFalse(mensagens.isEmpty());
        for (String m : mensagens) {
            assertFalse(m.endsWith(";"), m);
            assertFalse(m.matches(".*\\b(nao|minimo|maiusculas|sequencia)\\b.*"), m);
        }
    }
}
