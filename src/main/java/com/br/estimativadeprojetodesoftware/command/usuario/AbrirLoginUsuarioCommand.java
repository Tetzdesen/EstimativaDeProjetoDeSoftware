package com.br.estimativadeprojetodesoftware.command.usuario;

import com.br.estimativadeprojetodesoftware.presenter.usuario.LoginPresenter;
import com.br.estimativadeprojetodesoftware.presenter.usuario.PrincipalUsuarioPresenter;
import com.br.estimativadeprojetodesoftware.view.usuario.LoginView;
import com.br.estimativadeprojetodesoftware.command.Command;

/**
 *
 * @author tetzner
 */
public class AbrirLoginUsuarioCommand implements Command {
    
    private final PrincipalUsuarioPresenter principalUsuarioPresenter;

    public AbrirLoginUsuarioCommand(PrincipalUsuarioPresenter principalUsuarioPresenter) {
        this.principalUsuarioPresenter = principalUsuarioPresenter;   
    }
    
    @Override
    public void execute() {        
        LoginView view = new LoginView();
        view.setSize(546, 450);
        view.setLocationRelativeTo(null);
        new LoginPresenter(view, principalUsuarioPresenter);
    }

}
