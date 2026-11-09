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
        boolean inclusao = presenter.getEstado() instanceof InclusaoPerfilState;
        presenter.noBanco("Salvando…", () -> {
            if (inclusao) {
                presenter.getService().incluir(presenter.getPerfil(), presenter.getUsuario());
            } else {
                presenter.getService().salvar(presenter.getPerfil(), presenter.getUsuario());
            }
            return null;
        }, ok -> {
            presenter.setEstado(new VisualizacaoPerfilState(presenter));
            presenter.sucesso("Perfil \"" + presenter.getPerfil().getNome() + "\" salvo.");
        });
    }
}
