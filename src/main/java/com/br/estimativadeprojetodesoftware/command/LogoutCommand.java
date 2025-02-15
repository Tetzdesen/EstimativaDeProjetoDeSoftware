package com.br.estimativadeprojetodesoftware.command;

import com.br.estimativadeprojetodesoftware.presenter.PrincipalPresenter;
import com.br.estimativadeprojetodesoftware.singleton.UsuarioLogadoSingleton;

/**
 *
 * @author tetzner
 */
public class LogoutCommand implements ProjetoCommand {

    private final PrincipalPresenter principalPresenter;

    public LogoutCommand(PrincipalPresenter principalPresenter) {
        this.principalPresenter = principalPresenter;
    }

    @Override
    public void execute() {
        UsuarioLogadoSingleton.getInstancia().logout();
        // Fecha todas as janelas abertas
        principalPresenter.getView().dispose();
       // new FecharTodasJanelasCommand(principalPresenter).execute();
        new LoginCommand().execute();
    }
}
