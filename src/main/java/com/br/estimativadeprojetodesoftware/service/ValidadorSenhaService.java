package com.br.estimativadeprojetodesoftware.service;

import com.br.estimativadeprojetodesoftware.adapter.ValidadorSenhaAdapter;
/**
 *
 * @author tetzner
 */
public class ValidadorSenhaService {
    private final ValidadorSenhaAdapter adapter;

    public ValidadorSenhaService() {
        this.adapter = new ValidadorSenhaAdapter();
    }
    
    public boolean validarSenha(String senha) throws Exception{
        return adapter.validarSenha(senha);
    }
}
