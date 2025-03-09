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
        usuarioPresenter.getView().getTxtNome().setEnabled(true);
        usuarioPresenter.getView().getTxtSenhaAtual().setEnabled(true);
        usuarioPresenter.getBarraService().getBotao("Salvar usuário").setEnabled(true);
        usuarioPresenter.getBarraService().getBotao("Excluir usuário").setEnabled(false);
        usuarioPresenter.getBarraService().getBotao("Editar usuário").setEnabled(false);
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
