package com.br.estimativadeprojetodesoftware.presenter.window_command;

import com.br.estimativadeprojetodesoftware.command.Command;
import com.br.estimativadeprojetodesoftware.presenter.projeto.PrincipalProjetoPresenter;
import com.br.estimativadeprojetodesoftware.service.BarraService;

/**
 *
 * @author tetzner
 */
public class AtualizarPrincipalProjetoPresenterCommand implements Command {

    private final PrincipalProjetoPresenter presenter;

    public AtualizarPrincipalProjetoPresenterCommand(PrincipalProjetoPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void execute() {
        BarraService barraService = presenter.getCriarBarraService(); 
        barraService.atualizarLabelUsuarioLogado();
    }
}