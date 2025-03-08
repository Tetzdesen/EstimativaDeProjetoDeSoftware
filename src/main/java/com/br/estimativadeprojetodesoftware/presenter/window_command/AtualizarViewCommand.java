package com.br.estimativadeprojetodesoftware.presenter.window_command;

import com.br.estimativadeprojetodesoftware.presenter.projeto.PrincipalPresenter;
import com.br.estimativadeprojetodesoftware.service.BarraService;

/**
 *
 * @author tetzner
 */
public class AtualizarViewCommand implements WindowCommand {

    private final PrincipalPresenter presenter;

    public AtualizarViewCommand(PrincipalPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void execute() {
        BarraService barraService = presenter.getCriarBarraService(); 
        barraService.atualizarLabelUsuarioLogado();
    }
}
