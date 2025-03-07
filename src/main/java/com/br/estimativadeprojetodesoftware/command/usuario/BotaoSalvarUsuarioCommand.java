package com.br.estimativadeprojetodesoftware.command.usuario;

import com.br.estimativadeprojetodesoftware.command.ProjetoCommand;
import com.br.estimativadeprojetodesoftware.presenter.usuario.ManterUsuarioPresenter;

/**
 *
 * @author tetzner
 */
public class BotaoSalvarUsuarioCommand implements ProjetoCommand {
    private final ManterUsuarioPresenter usuarioPresenter;

    public BotaoSalvarUsuarioCommand(ManterUsuarioPresenter usuarioPresenter) {
        this.usuarioPresenter = usuarioPresenter;
    }

    @Override
    public void execute() {
        usuarioPresenter.salvar();
    }
    
}
