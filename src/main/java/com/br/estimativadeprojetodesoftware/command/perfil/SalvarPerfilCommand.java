package com.br.estimativadeprojetodesoftware.command.perfil;

import com.br.estimativadeprojetodesoftware.command.Command;
import com.br.estimativadeprojetodesoftware.presenter.perfil.ManterPerfilPresenter;
import com.br.estimativadeprojetodesoftware.state.perfil.InclusaoPerfilState;
import com.br.estimativadeprojetodesoftware.state.perfil.VisualizacaoPerfilState;

/** Inclui (perfil novo) ou atualiza o perfil. */
public class SalvarPerfilCommand implements Command {

    private final ManterPerfilPresenter presenter;

    public SalvarPerfilCommand(ManterPerfilPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void execute() {
        if (!presenter.aplicarCampos()) {
            return;
        }
        if (presenter.getEstado() instanceof InclusaoPerfilState) {
            presenter.getService().incluir(presenter.getPerfil(), presenter.getUsuario());
        } else {
            presenter.getService().salvar(presenter.getPerfil(), presenter.getUsuario());
        }
        presenter.setEstado(new VisualizacaoPerfilState(presenter));
        presenter.sucesso("Perfil \"" + presenter.getPerfil().getNome() + "\" salvo.");
    }
}
