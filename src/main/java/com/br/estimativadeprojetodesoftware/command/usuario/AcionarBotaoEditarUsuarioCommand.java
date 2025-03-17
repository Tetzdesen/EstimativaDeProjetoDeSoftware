package com.br.estimativadeprojetodesoftware.command.usuario;

import com.br.estimativadeprojetodesoftware.presenter.usuario.ManterUsuarioPresenter;
import com.br.estimativadeprojetodesoftware.command.Command;

/**
 *
 * @author tetzner
 */
public class AcionarBotaoEditarUsuarioCommand implements Command {

    private final ManterUsuarioPresenter usuarioPresenter;

    public AcionarBotaoEditarUsuarioCommand(ManterUsuarioPresenter usuarioPresenter) {
        this.usuarioPresenter = usuarioPresenter;
    }

    @Override
    public void execute() {
        usuarioPresenter.editar();
    }
    
}
