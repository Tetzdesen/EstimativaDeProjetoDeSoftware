package com.br.estimativadeprojetodesoftware.command.usuario;

import com.br.estimativadeprojetodesoftware.command.Command;
import com.br.estimativadeprojetodesoftware.model.Usuario;
import com.br.estimativadeprojetodesoftware.service.UsuarioService;

/** Cria a conta pelo UsuarioService (que valida e grava a senha como hash). */
public class RealizarCadastroUsuarioCommand implements Command {

    private final UsuarioService usuarioService;
    private final String nome;
    private final String email;
    private final String senha;
    private final String senhaConfirmada;
    private Usuario criado;

    public RealizarCadastroUsuarioCommand(String nome, String email, String senha, String senhaConfirmada) {
        this(new UsuarioService(), nome, email, senha, senhaConfirmada);
    }

    public RealizarCadastroUsuarioCommand(UsuarioService usuarioService, String nome, String email, String senha, String senhaConfirmada) {
        this.usuarioService = usuarioService;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.senhaConfirmada = senhaConfirmada;
    }

    @Override
    public void execute() {
        criado = usuarioService.cadastrar(nome, email, senha, senhaConfirmada);
    }

    public Usuario getCriado() {
        return criado;
    }
}
