package com.br.estimativadeprojetodesoftware.state.usuario;

import com.br.estimativadeprojetodesoftware.command.usuario.SalvarUsuarioCommand;
import com.br.estimativadeprojetodesoftware.presenter.usuario.ManterUsuarioPresenter;

/**
 *
 * @author tetzner
 */
public class EdicaoUsuarioState extends ManterUsuarioPresenterState {

    public EdicaoUsuarioState(ManterUsuarioPresenter usuarioPresenter) {
        super(usuarioPresenter);
    }

    @Override
    public void salvar() throws Exception {
        new SalvarUsuarioCommand(usuarioPresenter).execute();   
        usuarioPresenter.setState(new VisualizacaoUsuarioState(usuarioPresenter));
    }

    @Override
    public String toString() {
        return "edição";
    }
}
