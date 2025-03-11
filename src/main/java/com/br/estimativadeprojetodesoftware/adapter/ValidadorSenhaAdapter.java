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
        var validacoes = validador.validar(senha);

        if (!validacoes.isEmpty()) {
            // Concatena as mensagens de validação, cada uma em uma nova linha
            String mensagemErro = String.join("\n", validacoes);
            throw new Exception(mensagemErro);
        }

        return true;
    }

}
