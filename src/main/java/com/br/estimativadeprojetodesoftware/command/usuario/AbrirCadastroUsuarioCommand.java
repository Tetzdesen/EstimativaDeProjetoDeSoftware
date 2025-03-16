package com.br.estimativadeprojetodesoftware.command.usuario;

import com.br.estimativadeprojetodesoftware.command.ProjetoCommand;
import com.br.estimativadeprojetodesoftware.presenter.usuario.CadastroUsuarioPresenter;
import com.br.estimativadeprojetodesoftware.presenter.usuario.PrincipalUsuarioPresenter;
import com.br.estimativadeprojetodesoftware.view.usuario.CadastroUsuarioView;

/**
 *
 * @author tetzner
 */
public class AbrirCadastroUsuarioCommand implements ProjetoCommand {

    
    private final PrincipalUsuarioPresenter principalUsuarioPresenter;
    
    public AbrirCadastroUsuarioCommand(PrincipalUsuarioPresenter principalUsuarioPresenter) {
        this.principalUsuarioPresenter = principalUsuarioPresenter;
    }
    
    @Override
    public void execute() { 
        CadastroUsuarioView view = new CadastroUsuarioView();
        new CadastroUsuarioPresenter(view, principalUsuarioPresenter);      
    }
    
}
