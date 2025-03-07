package com.br.estimativadeprojetodesoftware.state.usuario;

import com.br.estimativadeprojetodesoftware.command.usuario.ExcluirUsuarioCommand;
import com.br.estimativadeprojetodesoftware.presenter.usuario.ManterUsuarioPresenter;
import javax.swing.JOptionPane;
/**
 *
 * @author tetzner
 */
public class VisualizacaoUsuarioState extends ManterUsuarioPresenterState {

    public VisualizacaoUsuarioState(ManterUsuarioPresenter usuarioPresenter) {
        super(usuarioPresenter);
        usuarioPresenter.getView().getTxtNome().setEnabled(false);
        usuarioPresenter.getView().getTxtSenhaAtual().setEnabled(false);
    }

    @Override
    public void editar() {
        usuarioPresenter.setState(new EdicaoUsuarioState(usuarioPresenter));
    }

    @Override
    public void excluir() {
        int confirmacao = JOptionPane.showConfirmDialog(
                null,
                "Deseja realmente excluir o seu usuário \"" + usuarioPresenter.getUsuario().getNome() + "\"?",
                "Confirmar Exclusão",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacao == JOptionPane.YES_OPTION) {
            new ExcluirUsuarioCommand(usuarioPresenter).execute();
        }
    }

    @Override
    public String toString() {
        return "visualização";
    }

}
