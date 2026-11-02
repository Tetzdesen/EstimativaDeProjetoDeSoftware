package com.br.estimativadeprojetodesoftware.command.usuario;

import com.br.authifyjava.ResultadoAutenticacao;
import com.br.estimativadeprojetodesoftware.model.Usuario;
import com.br.estimativadeprojetodesoftware.service.AutenticacaoService;
import com.br.estimativadeprojetodesoftware.service.UsuarioService;
import com.br.estimativadeprojetodesoftware.singleton.UsuarioLogadoSingleton;
import com.br.estimativadeprojetodesoftware.command.Command;

/**
 *
 * @author tetzner
 */
public final class RealizarAutenticacaoUsuarioCommand implements Command {

    private final UsuarioService usuarioService;
    private final AutenticacaoService autenticacaoService;
    private final String email;
    private final String senha;

    public RealizarAutenticacaoUsuarioCommand(String email, String senha) {
        this.email = email;
        this.senha = senha;
        usuarioService = new UsuarioService();
        autenticacaoService = new AutenticacaoService();
    }

    @Override
    public void execute() {
        if (camposInvalidos(email, senha)) {
            throw new IllegalArgumentException("Informe e-mail e senha.");
        }
        ResultadoAutenticacao resultadoAutenticacao = autenticacaoService.autenticar(email, senha);
        if (resultadoAutenticacao.isAutenticado()) {
            Usuario usuario = usuarioService.buscarPorEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("E-mail ou senha inválidos."));
            UsuarioLogadoSingleton.getInstancia().setUsuario(usuario);
        } else {
            throw new IllegalArgumentException("E-mail ou senha inválidos.");
        }
    }

    private boolean camposInvalidos(String email, String senha) {
        return email == null || email.trim().isEmpty() || senha == null || senha.trim().isEmpty();
    }

}
