package com.br.estimativadeprojetodesoftware.command.usuario;

import com.br.estimativadeprojetodesoftware.command.ProjetoCommand;
import com.br.estimativadeprojetodesoftware.presenter.usuario.CadastroUsuarioPresenter;
import com.br.estimativadeprojetodesoftware.view.usuario.CadastroUsuarioView;

/**
 *
 * @author tetzner
 */
public class AbrirCadastroUsuarioCommand implements ProjetoCommand {

    @Override
    public void execute() {
        
        CadastroUsuarioView view = new CadastroUsuarioView();
        new CadastroUsuarioPresenter(view);
        
    }
    
}
