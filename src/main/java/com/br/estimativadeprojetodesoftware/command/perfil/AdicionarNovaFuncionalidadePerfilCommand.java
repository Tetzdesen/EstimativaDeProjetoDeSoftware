package com.br.estimativadeprojetodesoftware.command.perfil;

import com.br.estimativadeprojetodesoftware.command.ProjetoCommand;
import com.br.estimativadeprojetodesoftware.presenter.perfil.ManterPerfilPresenter;

public class AdicionarNovaFuncionalidadePerfilCommand implements ProjetoCommand {
    private final ManterPerfilPresenter presenter;

    public AdicionarNovaFuncionalidadePerfilCommand(ManterPerfilPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void execute() {
        presenter.getView().getModeloTabela().addRow(new Object[]{"", "", ""});
        int novaLinha = presenter.getView().getModeloTabela().getRowCount() - 1;
        presenter.getView().getTabelaDetalhes().setRowSelectionInterval(novaLinha, novaLinha);
        presenter.getView().getTabelaDetalhes().setColumnSelectionInterval(0, 0);
        presenter.getView().getTabelaDetalhes().editCellAt(novaLinha, 0);
        presenter.getView().getTabelaDetalhes().requestFocusInWindow();
        presenter.getView().getTabelaDetalhes().getCellRect(novaLinha, 0, true);
        presenter.getView().getTabelaDetalhes().scrollRectToVisible(presenter.getView().getTabelaDetalhes().getCellRect(novaLinha, 0, true));
    }
}
