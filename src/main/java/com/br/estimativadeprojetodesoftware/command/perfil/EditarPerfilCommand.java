package com.br.estimativadeprojetodesoftware.command.perfil;

import com.br.estimativadeprojetodesoftware.command.Command;
import com.br.estimativadeprojetodesoftware.presenter.perfil.ManterPerfilPresenter;
import com.br.estimativadeprojetodesoftware.state.perfil.EdicaoPerfilState;

public class EditarPerfilCommand implements Command {

    private final ManterPerfilPresenter presenter;

    public EditarPerfilCommand(ManterPerfilPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void execute() {
        presenter.setEstado(new EdicaoPerfilState(presenter));
    }
}
