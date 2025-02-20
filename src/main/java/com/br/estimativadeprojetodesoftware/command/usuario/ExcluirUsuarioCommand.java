package com.br.estimativadeprojetodesoftware.command.usuario;

import com.br.estimativadeprojetodesoftware.command.ProjetoCommand;
import com.br.estimativadeprojetodesoftware.presenter.usuario.UsuarioPresenter;
import javax.swing.JOptionPane;

/**
 *
 * @author tetzner
 */
public class ExcluirUsuarioCommand implements ProjetoCommand {

    private final UsuarioPresenter usuarioPresenter;

    public ExcluirUsuarioCommand(UsuarioPresenter usuarioPresenter) {
        this.usuarioPresenter = usuarioPresenter;
    }

    @Override
    public void execute() {
       usuarioPresenter.excluir();
    }

}
