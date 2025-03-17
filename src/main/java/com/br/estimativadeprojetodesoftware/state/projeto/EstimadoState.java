package com.br.estimativadeprojetodesoftware.state.projeto;

import com.br.estimativadeprojetodesoftware.command.projeto.AbrirCompartilhamentoProjetoCommand;
import com.br.estimativadeprojetodesoftware.command.projeto.AbrirExportarProjetoCommand;
import com.br.estimativadeprojetodesoftware.model.Projeto;
import com.br.estimativadeprojetodesoftware.presenter.projeto.DetalheProjetoPresenter;

/**
 *
 * @author tetzner
 */
public class EstimadoState extends DetalheProjetoPresenterState {

    public EstimadoState(DetalheProjetoPresenter detalheProjetoPresenter) {
        super(detalheProjetoPresenter);
        detalheProjetoPresenter.getView().getBtnEstimar().setEnabled(false);
        detalheProjetoPresenter.getView().getBtnCancelar().setEnabled(true);
        detalheProjetoPresenter.getView().getBtnCompartilhar().setEnabled(true);
        detalheProjetoPresenter.getView().getBtnExportar().setEnabled(true);
    }

    @Override
    public void cancelarEstimativa() {
        Projeto projeto = detalheProjetoPresenter.getProjeto();
        projeto.setStatus("Não estimado");
        projeto.setCompartilhado(false);
        detalheProjetoPresenter.getProjetoService().atualizar(projeto);
        detalheProjetoPresenter.setEstado(new NaoEstimadoState(detalheProjetoPresenter));
    }

    @Override
    public void compartilharProjeto() {
        new AbrirCompartilhamentoProjetoCommand(detalheProjetoPresenter.getProjetoService(), detalheProjetoPresenter.getProjeto().getNome()).execute();
    }

    @Override
    public void exportarProjeto() {
        new AbrirExportarProjetoCommand(detalheProjetoPresenter.getProjeto().getNome()).execute();
    }

    @Override
    public String toString() {
        return "Estimado";
    }

}
