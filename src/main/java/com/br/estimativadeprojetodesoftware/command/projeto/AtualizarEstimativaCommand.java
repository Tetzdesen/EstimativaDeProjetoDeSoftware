package com.br.estimativadeprojetodesoftware.command.projeto;

import com.br.estimativadeprojetodesoftware.command.Command;
import com.br.estimativadeprojetodesoftware.model.ResultadoEstimativa;
import com.br.estimativadeprojetodesoftware.presenter.projeto.ManterProjetoPresenter;

/**
 * Recalcula a estimativa com o que está na tela e mostra o resultado, só em
 * memória: não grava no banco, não registra no log e não notifica outras janelas.
 */
public class AtualizarEstimativaCommand implements Command {

    private final ManterProjetoPresenter presenter;

    public AtualizarEstimativaCommand(ManterProjetoPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void execute() {
        boolean percentuaisValidos = presenter.aplicarCamposDeEstimativa();
        try {
            ResultadoEstimativa resultado = presenter.getProjetoService().resultado(presenter.getProjeto());
            if (percentuaisValidos) {
                presenter.mostrarResultado(resultado);
            } else {
                presenter.marcarEstimativaDesatualizada();
            }
        } catch (IllegalArgumentException e) {
            presenter.exibirEstimativaIndisponivel(e.getMessage());
        }
    }
}
