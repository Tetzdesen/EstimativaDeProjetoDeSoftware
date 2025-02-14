package com.br.estimativadeprojetodesoftware.command;

import com.br.estimativadeprojetodesoftware.presenter.UsuarioPresenter;

/**
 *
 * @author tetzner
 */
public class SalvarUsuarioCommand implements ProjetoCommand {

    private final UsuarioPresenter usuarioPresenter;
    
    public SalvarUsuarioCommand(UsuarioPresenter usuarioPresenter) {
        this.usuarioPresenter = usuarioPresenter;
    }
    
    @Override
    public void execute() {
        usuarioPresenter.salvar();
    }

}
