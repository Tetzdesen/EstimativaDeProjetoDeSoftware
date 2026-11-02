package com.br.estimativadeprojetodesoftware.command.projeto;

import com.br.estimativadeprojetodesoftware.command.Command;
import com.br.estimativadeprojetodesoftware.presenter.projeto.ManterProjetoPresenter;
import com.br.estimativadeprojetodesoftware.state.projeto.EdicaoProjetoState;

public class EditarProjetoCommand implements Command {

    private final ManterProjetoPresenter presenter;

    public EditarProjetoCommand(ManterProjetoPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void execute() {
        presenter.setEstado(new EdicaoProjetoState(presenter));
    }
}
