package com.br.estimativadeprojetodesoftware.state.usuario;

import com.br.estimativadeprojetodesoftware.command.usuario.ExcluirUsuarioCommand;
import com.br.estimativadeprojetodesoftware.presenter.usuario.ManterUsuarioPresenter;
import com.br.estimativadeprojetodesoftware.command.ConfirmarCommand;

/**
 *
 * @author tetzner
 */
public class VisualizacaoUsuarioState extends ManterUsuarioPresenterState {

    public VisualizacaoUsuarioState(ManterUsuarioPresenter usuarioPresenter) {
        super(usuarioPresenter);
        usuarioPresenter.getView().getTxtNome().setEditable(false);
        usuarioPresenter.getView().getTxtSenhaAtual().setEditable(false);
        usuarioPresenter.getView().getBtnSalvar().setEnabled(false);
        usuarioPresenter.getView().getBtnExcluir().setEnabled(true);
        usuarioPresenter.getView().getBtnEditar().setEnabled(true);
    }

    @Override
    public void editar() {
        usuarioPresenter.setState(new EdicaoUsuarioState(usuarioPresenter));
    }

    @Override
    public void excluir() {
        if (ConfirmarCommand.perguntar("Excluir conta",
                "Excluir a sua conta \"" + usuarioPresenter.getUsuario().getNome() + "\"?\nEsta ação não pode ser desfeita.",
                "Excluir")) {
            new ExcluirUsuarioCommand(usuarioPresenter).execute();
        }
    }

    @Override
    public String toString() {
        return "visualização";
    }

}
