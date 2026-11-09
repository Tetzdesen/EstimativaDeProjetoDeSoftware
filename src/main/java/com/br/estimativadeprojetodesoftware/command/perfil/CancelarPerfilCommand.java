package com.br.estimativadeprojetodesoftware.command.perfil;

import com.br.estimativadeprojetodesoftware.command.Command;
import com.br.estimativadeprojetodesoftware.presenter.perfil.ManterPerfilPresenter;
import com.br.estimativadeprojetodesoftware.state.perfil.InclusaoPerfilState;
import com.br.estimativadeprojetodesoftware.state.perfil.VisualizacaoPerfilState;

/** Inclusão: fecha sem salvar. Edição: descarta alterações recarregando o perfil. */
public class CancelarPerfilCommand implements Command {

    private final ManterPerfilPresenter presenter;

    public CancelarPerfilCommand(ManterPerfilPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void execute() {
        if (presenter.getEstado() instanceof InclusaoPerfilState) {
            presenter.fechar();
            return;
        }
        presenter.recarregar(() -> presenter.setEstado(new VisualizacaoPerfilState(presenter)));
    }
}
