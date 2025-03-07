package com.br.estimativadeprojetodesoftware.principal;

import com.br.estimativadeprojetodesoftware.command.usuario.AbrirTelaPrincipalCommand;

/**
 *
 * @author tetzner
 */
public class Principal {

    public static void main(String[] args) {
        new AbrirTelaPrincipalCommand().execute();
    }
}