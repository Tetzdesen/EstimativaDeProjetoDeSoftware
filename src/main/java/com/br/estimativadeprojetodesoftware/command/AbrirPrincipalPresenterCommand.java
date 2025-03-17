package com.br.estimativadeprojetodesoftware.command;

import com.br.estimativadeprojetodesoftware.presenter.projeto.PrincipalProjetoPresenter;
import com.br.estimativadeprojetodesoftware.presenter.helpers.WindowManager;

/**
 *
 * @author tetzner
 */
public class AbrirPrincipalPresenterCommand implements Command {

    @Override
    public void execute() {
        PrincipalProjetoPresenter presenter = new PrincipalProjetoPresenter();
        WindowManager.getInstance().initialize(presenter);
    }

}
