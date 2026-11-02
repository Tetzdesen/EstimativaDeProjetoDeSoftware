package com.br.estimativadeprojetodesoftware.command.projeto;

import com.br.estimativadeprojetodesoftware.command.Command;
import com.br.estimativadeprojetodesoftware.model.ResultadoEstimativa;
import com.br.estimativadeprojetodesoftware.presenter.projeto.ManterProjetoPresenter;
import com.br.estimativadeprojetodesoftware.state.projeto.VisualizacaoProjetoState;

/** Calcula a estimativa, marca o projeto como calculado e mostra o resultado. */
public class CalcularProjetoCommand implements Command {

    private final ManterProjetoPresenter presenter;

    public CalcularProjetoCommand(ManterProjetoPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void execute() {
        if (!presenter.aplicarCamposNoProjeto()) {
            return;
        }
        ResultadoEstimativa resultado = presenter.getProjetoService().calcular(presenter.getProjeto(), presenter.getUsuario());
        presenter.setEstado(new VisualizacaoProjetoState(presenter));
        presenter.mostrarResultado(resultado);
        presenter.getView().mostrarAbaResultado();
    }
}
