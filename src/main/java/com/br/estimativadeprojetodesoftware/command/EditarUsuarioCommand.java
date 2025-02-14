package com.br.estimativadeprojetodesoftware.command;

import com.br.estimativadeprojetodesoftware.presenter.UsuarioPresenter;

/**
 *
 * @author tetzner
 */
public class EditarUsuarioCommand implements ProjetoCommand {

    private final UsuarioPresenter usuarioPresenter;

    public EditarUsuarioCommand(UsuarioPresenter usuarioPresenter) {
        this.usuarioPresenter = usuarioPresenter;
    }

    @Override
    public void execute() {
        usuarioPresenter.editar();
    }

}
