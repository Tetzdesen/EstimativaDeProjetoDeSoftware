package com.br.estimativadeprojetodesoftware.command.projeto;

import com.br.estimativadeprojetodesoftware.command.Command;
import com.br.estimativadeprojetodesoftware.presenter.projeto.ManterProjetoPresenter;
import com.br.estimativadeprojetodesoftware.state.projeto.VisualizacaoProjetoState;

/** Descarta as alterações não salvas, recarregando o projeto do banco. */
public class CancelarEdicaoProjetoCommand implements Command {

    private final ManterProjetoPresenter presenter;

    public CancelarEdicaoProjetoCommand(ManterProjetoPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void execute() {
        presenter.recarregar();
        presenter.setEstado(new VisualizacaoProjetoState(presenter));
    }
}
