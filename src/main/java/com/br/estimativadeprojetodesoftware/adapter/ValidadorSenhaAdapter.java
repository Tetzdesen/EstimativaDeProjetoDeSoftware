package com.br.estimativadeprojetodesoftware.adapter;

import com.pss.senha.validacao.ValidadorSenha;
import java.util.List;

/**
 *
 * @author tetzner
 */
public class ValidadorSenhaAdapter {

    private final ValidadorSenha validador;

    public ValidadorSenhaAdapter() {
        this.validador = new ValidadorSenha();
    }

    public boolean validarSenha(String senha) throws Exception {
        List<String> validacoesOcorridas = validador.validar(senha);

        if (validacoesOcorridas.isEmpty()) {
            return true;
        }
        throw new Exception(String.join("\n", validacoesOcorridas));
    }

}
