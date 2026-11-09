package com.br.estimativadeprojetodesoftware.command.projeto;

import com.br.estimativadeprojetodesoftware.command.Command;
import com.br.estimativadeprojetodesoftware.presenter.projeto.ManterProjetoPresenter;
import com.br.estimativadeprojetodesoftware.state.projeto.VisualizacaoProjetoState;

/** Persiste a alteração e define o estado calculado (registrado no log pelo service). */
public class SalvarProjetoCommand implements Command {

    private final ManterProjetoPresenter presenter;

    public SalvarProjetoCommand(ManterProjetoPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void execute() {
        if (!presenter.aplicarCamposNoProjeto()) {
            return;
        }
        presenter.cancelarRecalculoPendente();
        presenter.getProjetoService().salvarComEstimativa(presenter.getProjeto(), presenter.getUsuario());
        presenter.marcarSemAlteracoes();
        presenter.setEstado(new VisualizacaoProjetoState(presenter));
        presenter.mostrarResultadoAtual();
        presenter.sucesso("Projeto salvo.");
    }
}
