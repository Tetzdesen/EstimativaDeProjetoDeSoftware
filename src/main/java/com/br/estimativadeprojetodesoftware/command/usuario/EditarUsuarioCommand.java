package com.br.estimativadeprojetodesoftware.command.usuario;

import com.br.estimativadeprojetodesoftware.command.ProjetoCommand;
import com.br.estimativadeprojetodesoftware.presenter.usuario.UsuarioPresenter;

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
