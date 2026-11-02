package com.br.estimativadeprojetodesoftware.command.projeto;

import com.br.estimativadeprojetodesoftware.command.Command;
import com.br.estimativadeprojetodesoftware.presenter.projeto.ManterProjetoPresenter;

public class FecharProjetoCommand implements Command {

    private final ManterProjetoPresenter presenter;

    public FecharProjetoCommand(ManterProjetoPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void execute() {
        presenter.fechar();
    }
}
