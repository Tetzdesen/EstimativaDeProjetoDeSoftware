package com.br.estimativadeprojetodesoftware.principal;

import com.br.estimativadeprojetodesoftware.command.usuario.AbrirTelaPrincipalCommand;
/**
 *
 * @author tetzner
 */
public class Principal {

    public static void main(String[] args) {
        // sem .env ao lado, usa SQLite na pasta do usuário: o jar do release abre direto
        com.br.estimativadeprojetodesoftware.service.DotenvService.usarPadroesSemEnv();
        new AbrirTelaPrincipalCommand().execute();
    }
    
}