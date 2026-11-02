package com.br.estimativadeprojetodesoftware.command.projeto;

import com.br.estimativadeprojetodesoftware.command.Command;
import com.br.estimativadeprojetodesoftware.presenter.helpers.Navegador;
import com.br.estimativadeprojetodesoftware.presenter.projeto.ManterProjetoPresenter;

public class AbrirCompartilhamentoProjetoCommand implements Command {

    private final ManterProjetoPresenter presenter;

    public AbrirCompartilhamentoProjetoCommand(ManterProjetoPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void execute() {
        Navegador.abrirCompartilhamento(presenter.getProjeto());
    }
}
