package com.br.estimativadeprojetodesoftware.command.usuario;

import com.br.estimativadeprojetodesoftware.command.ProjetoCommand;
import com.br.estimativadeprojetodesoftware.presenter.usuario.ManterUsuarioPresenter;

/**
 *
 * @author tetzner
 */
public class AcionarBotaoExcluirUsuarioCommand implements ProjetoCommand {

    private final ManterUsuarioPresenter usuarioPresenter;

    public AcionarBotaoExcluirUsuarioCommand(ManterUsuarioPresenter usuarioPresenter) {
        this.usuarioPresenter = usuarioPresenter;
    }

    @Override
    public void execute() {
        usuarioPresenter.excluir();
    }
    
}
