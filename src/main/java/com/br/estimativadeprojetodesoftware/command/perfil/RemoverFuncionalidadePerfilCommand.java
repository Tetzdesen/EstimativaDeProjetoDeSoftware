package com.br.estimativadeprojetodesoftware.command.perfil;

import javax.swing.JOptionPane;

import com.br.estimativadeprojetodesoftware.command.ProjetoCommand;
import com.br.estimativadeprojetodesoftware.presenter.perfil.ManterPerfilPresenter;

public class RemoverFuncionalidadePerfilCommand implements ProjetoCommand {
    private final ManterPerfilPresenter presenter;

    public RemoverFuncionalidadePerfilCommand(ManterPerfilPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void execute() {
        int linhaSelecionada = presenter.getView().getTabelaDetalhes().getSelectedRow();

        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(presenter.getView(), "Selecione uma linha para remover.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int resposta = JOptionPane.showConfirmDialog(
            presenter.getView(), 
            "Tem certeza que deseja remover este campo?", 
            "Confirmação", 
            JOptionPane.YES_NO_OPTION
        );

        if (resposta == JOptionPane.YES_OPTION) {
            presenter.getView().getModeloTabela().removeRow(linhaSelecionada);
        }
    }
}
