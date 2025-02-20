package com.br.estimativadeprojetodesoftware.command.usuario;

import com.br.estimativadeprojetodesoftware.command.ProjetoCommand;
import com.br.estimativadeprojetodesoftware.presenter.usuario.UsuarioPresenter;

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
