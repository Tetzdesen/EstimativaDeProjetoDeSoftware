package com.br.estimativadeprojetodesoftware.adapter.senha;

import java.util.List;

/** Interface alvo do Adapter do validador de senhas. */
public interface IValidadorSenha {

    /** Mensagens de regras não atendidas; lista vazia quando a senha é aceita. */
    List<String> validar(String senha);
}
