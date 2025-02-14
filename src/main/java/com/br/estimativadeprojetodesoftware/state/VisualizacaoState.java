package com.br.estimativadeprojetodesoftware.state;

import com.br.estimativadeprojetodesoftware.command.MostrarMensagemProjetoCommand;
import com.br.estimativadeprojetodesoftware.command.ProjetoCommand;
import com.br.estimativadeprojetodesoftware.presenter.UsuarioPresenter;
import com.br.estimativadeprojetodesoftware.singleton.UsuarioLogadoSingleton;
import javax.swing.JOptionPane;

/**
 *
 * @author tetzner
 */
public class VisualizacaoState extends UsuarioPresenterState {

    public VisualizacaoState(UsuarioPresenter usuarioPresenter) {
        super(usuarioPresenter);
        usuarioPresenter.getView().getTxtNome().setEnabled(false);
        usuarioPresenter.getView().getTxtEmail().setEnabled(false);
    }

    @Override
    public void editar() {
        usuarioPresenter.getView().getTxtNome().setEnabled(true);
        usuarioPresenter.getView().getTxtEmail().setEnabled(true);
        usuarioPresenter.setState(new EdicaoState(usuarioPresenter));
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
            boolean removido = usuarioPresenter.getRepository().removerUsuarioPorEmail(usuarioPresenter.getUsuario().getEmail());

            if (removido) {
                usuarioPresenter.setUsuario(null);
                UsuarioLogadoSingleton.getInstancia().setUsuario(usuarioPresenter.getUsuario());
                new MostrarMensagemProjetoCommand("Usuário \"" + usuarioPresenter.getUsuario().getNome() + "\" removido com sucesso!").execute();
            } else {
                new MostrarMensagemProjetoCommand("Erro ao remover o usuário \"" + usuarioPresenter.getUsuario().getNome() + "\".").execute();
            }
        }

    }

    @Override
    public String toString() {
        return "Visualização";
    }

}
