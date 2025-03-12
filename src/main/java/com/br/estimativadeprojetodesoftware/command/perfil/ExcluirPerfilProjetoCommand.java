package com.br.estimativadeprojetodesoftware.command.perfil;

import javax.swing.JOptionPane;

import com.br.estimativadeprojetodesoftware.command.ProjetoCommand;
import com.br.estimativadeprojetodesoftware.presenter.perfil.ManterPerfilPresenter;

public class ExcluirPerfilProjetoCommand implements ProjetoCommand {
    private final ManterPerfilPresenter presenter;

    public ExcluirPerfilProjetoCommand(ManterPerfilPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void execute() {
        int confirmacao = JOptionPane.showConfirmDialog(
            presenter.getView(), 
            "Tem certeza de que deseja remover o produto selecionado?", 
            "Confirmação de Remoção", 
            JOptionPane.YES_NO_OPTION
        );

        if (confirmacao == JOptionPane.YES_OPTION) {
            presenter.getRepository().removerPorId(presenter.getPerfil().getId());
            presenter.getRepository().notifyObservers();
            JOptionPane.showMessageDialog(presenter.getView(), "Perfil removido com sucesso!", "Remoção", JOptionPane.INFORMATION_MESSAGE);
            presenter.setAllBtnVisibleFalse();
            presenter.getView().dispose();
            
        }
    }
}
