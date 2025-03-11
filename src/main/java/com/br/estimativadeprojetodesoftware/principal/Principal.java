package com.br.estimativadeprojetodesoftware.principal;

import com.br.estimativadeprojetodesoftware.command.usuario.AbrirTelaPrincipalCommand;
import com.br.estimativadeprojetodesoftware.singleton.ConexaoSingleton;

/**
 *
 * @author tetzner
 */
public class Principal {

    public static void main(String[] args) {
        ConexaoSingleton.getInstancia();
        new AbrirTelaPrincipalCommand().execute();
 
    }
}