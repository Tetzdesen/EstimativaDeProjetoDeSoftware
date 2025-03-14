package com.br.estimativadeprojetodesoftware.command.usuario;

import com.br.authifyjava.ResultadoAutenticacao;
import com.br.estimativadeprojetodesoftware.command.ProjetoCommand;
import com.br.estimativadeprojetodesoftware.model.Usuario;
import com.br.estimativadeprojetodesoftware.service.AutenticacaoService;
import com.br.estimativadeprojetodesoftware.service.UsuarioRepositoryService;
import com.br.estimativadeprojetodesoftware.singleton.UsuarioLogadoSingleton;

/**
 *
 * @author tetzner
 */
public final class RealizarAutenticacaoUsuarioCommand implements ProjetoCommand {

    private final UsuarioRepositoryService usuarioService;
    private final AutenticacaoService autenticacaoService;
    private final String email;
    private final String senha;

    public RealizarAutenticacaoUsuarioCommand(String email, String senha) {
        this.email = email;
        this.senha = senha;
        usuarioService = new UsuarioRepositoryService();
        autenticacaoService = new AutenticacaoService();
    }

    @Override
    public void execute() {
        if (camposInvalidos(email, senha)) {
            throw new IllegalArgumentException("Os campos de nome e senha não podem estar vazios");
        }
        ResultadoAutenticacao resultadoAutenticacao = autenticacaoService.autenticar(email, senha);
        if (resultadoAutenticacao.isAutenticado()) {
            Usuario usuario = usuarioService.buscarPorEmail(email).get();
            UsuarioLogadoSingleton.getInstancia().setUsuario(usuario);
        } else {
            throw new IllegalArgumentException("Falha ao realizar o login: " + resultadoAutenticacao.getMensagem());
        }
    }

    private boolean camposInvalidos(String email, String senha) {
        return email == null || email.trim().isEmpty() || senha == null || senha.trim().isEmpty();
    }

}
