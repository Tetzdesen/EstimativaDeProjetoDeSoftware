package com.br.estimativadeprojetodesoftware.command.usuario;

import com.br.estimativadeprojetodesoftware.command.ProjetoCommand;
import com.br.estimativadeprojetodesoftware.presenter.usuario.PrincipalUsuarioPresenter;
import com.br.estimativadeprojetodesoftware.singleton.UsuarioLogadoSingleton;

/**
 *
 * @author tetzner
 */
public class RealizarLogoutUsuarioCommand implements ProjetoCommand {

    @Override
    public void execute() {
        UsuarioLogadoSingleton.getInstancia().logout();   
        new PrincipalUsuarioPresenter();
    }
}
