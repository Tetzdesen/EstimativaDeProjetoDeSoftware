package com.br.estimativadeprojetodesoftware.command.perfil;

import com.br.estimativadeprojetodesoftware.presenter.perfil.ManterPerfilPresenter;
import com.br.estimativadeprojetodesoftware.state.perfil.VisualizacaoPerfilState;
import com.br.estimativadeprojetodesoftware.command.Command;

public class CancelarPerfilProjetoCommand implements Command {
    private final ManterPerfilPresenter presenter;

    public CancelarPerfilProjetoCommand(ManterPerfilPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void execute() {
        presenter.setAllBtnVisibleFalse();
        presenter.setEstado(new VisualizacaoPerfilState(presenter));
    }
}
