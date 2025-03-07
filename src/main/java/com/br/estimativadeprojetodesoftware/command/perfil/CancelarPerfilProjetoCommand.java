package com.br.estimativadeprojetodesoftware.command.perfil;

import com.br.estimativadeprojetodesoftware.command.ProjetoCommand;
import com.br.estimativadeprojetodesoftware.presenter.perfil.ManterPerfilPresenter;
import com.br.estimativadeprojetodesoftware.state.perfil.VisualizacaoPerfilState;

public class CancelarPerfilProjetoCommand implements ProjetoCommand {
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
