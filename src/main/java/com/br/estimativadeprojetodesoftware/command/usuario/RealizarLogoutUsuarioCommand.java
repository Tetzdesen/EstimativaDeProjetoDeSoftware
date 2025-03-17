package com.br.estimativadeprojetodesoftware.command.usuario;

import com.br.estimativadeprojetodesoftware.presenter.usuario.PrincipalUsuarioPresenter;
import com.br.estimativadeprojetodesoftware.singleton.UsuarioLogadoSingleton;
import com.br.estimativadeprojetodesoftware.command.Command;

/**
 *
 * @author tetzner
 */
public class RealizarLogoutUsuarioCommand implements Command {

    @Override
    public void execute() {
        UsuarioLogadoSingleton.getInstancia().logout();   
        new PrincipalUsuarioPresenter();
    }
}
