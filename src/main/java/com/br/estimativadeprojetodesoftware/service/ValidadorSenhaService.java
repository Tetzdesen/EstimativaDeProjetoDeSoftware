package com.br.estimativadeprojetodesoftware.service;

import com.pss.senha.validacao.ValidadorSenha;

/**
 *
 * @author tetzner
 */
public class ValidadorSenhaService {

    private final ValidadorSenha validador;

    public ValidadorSenhaService() {
        this.validador = new ValidadorSenha();
    }

    public boolean validarSenha(String senha) throws Exception {
        var validacoes = validador.validar(senha);

        if (!validacoes.isEmpty()) {
            String mensagemErro = String.join("\n", validacoes);
            throw new Exception(mensagemErro);
        }

        return true;
    }
}
