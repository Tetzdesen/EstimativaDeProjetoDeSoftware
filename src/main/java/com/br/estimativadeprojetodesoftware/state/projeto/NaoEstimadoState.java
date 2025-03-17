package com.br.estimativadeprojetodesoftware.state.projeto;

import com.br.estimativadeprojetodesoftware.command.projeto.RealizarEstimativaProjetoCommand;
import com.br.estimativadeprojetodesoftware.presenter.projeto.DetalheProjetoPresenter;

/**
 *
 * @author tetzner
 */
public class NaoEstimadoState extends DetalheProjetoPresenterState {

    public NaoEstimadoState(DetalheProjetoPresenter detalheProjetoPresenter) {
        super(detalheProjetoPresenter);
        detalheProjetoPresenter.getView().getBtnEstimar().setEnabled(true);
        detalheProjetoPresenter.getView().getBtnCancelar().setEnabled(false);
        detalheProjetoPresenter.getView().getBtnCompartilhar().setEnabled(false);
        detalheProjetoPresenter.getView().getBtnExportar().setEnabled(false);
    }

    @Override
    public void estimar() {
        new RealizarEstimativaProjetoCommand(detalheProjetoPresenter).execute();
        detalheProjetoPresenter.setEstado(new EstimadoState(detalheProjetoPresenter));
    }

    @Override
    public String toString() {
        return "Não estimado";
    }
}
