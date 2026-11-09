package com.br.estimativadeprojetodesoftware.command.projeto;

import com.br.estimativadeprojetodesoftware.command.Command;
import com.br.estimativadeprojetodesoftware.presenter.projeto.ManterProjetoPresenter;
import com.br.estimativadeprojetodesoftware.state.projeto.EdicaoProjetoState;

/** Volta ao projeto salvo, sem gravar nada, e continua em edição. */
public class DescartarAlteracoesCommand implements Command {

    private final ManterProjetoPresenter presenter;

    public DescartarAlteracoesCommand(ManterProjetoPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void execute() {
        presenter.recarregar(() -> presenter.setEstado(new EdicaoProjetoState(presenter)));
    }
}
