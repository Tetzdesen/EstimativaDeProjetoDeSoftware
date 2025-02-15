package com.br.estimativadeprojetodesoftware.principal;

import com.br.estimativadeprojetodesoftware.command.LoginCommand;

/**
 *
 * @author tetzner
 */
public class Principal {

    public static void main(String[] args) {
        new LoginCommand().execute();
    }
    
}
