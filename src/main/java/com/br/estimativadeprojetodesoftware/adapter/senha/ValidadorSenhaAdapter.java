package com.br.estimativadeprojetodesoftware.adapter.senha;

import com.pss.senha.validacao.ValidadorSenha;
import java.util.ArrayList;
import java.util.List;

/**
 * Adapter que envolve o validador de senhas do JitPack
 * (com.github.claytonfraga:validadorsenha). Só esta classe conhece a API externa.
 */
public class ValidadorSenhaAdapter implements IValidadorSenha {

    private final ValidadorSenha validador = new ValidadorSenha();

    @Override
    public List<String> validar(String senha) {
        List<String> mensagens = validador.validar(senha == null ? "" : senha);
        return mensagens == null ? List.of() : new ArrayList<>(mensagens);
    }
}
