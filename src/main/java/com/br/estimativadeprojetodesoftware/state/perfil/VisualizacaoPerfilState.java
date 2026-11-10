package com.br.estimativadeprojetodesoftware.state.perfil;

import com.br.estimativadeprojetodesoftware.command.perfil.EditarPerfilCommand;
import com.br.estimativadeprojetodesoftware.command.perfil.ExcluirPerfilCommand;
import com.br.estimativadeprojetodesoftware.presenter.perfil.ManterPerfilPresenter;
import com.br.estimativadeprojetodesoftware.view.perfil.ManterPerfilView;

public class VisualizacaoPerfilState extends ManterPerfilPresenterState {

    public VisualizacaoPerfilState(ManterPerfilPresenter presenter) {
        super(presenter);
    }

    @Override
    public void entrar() {
        ManterPerfilView v = presenter.getView();
        if (presenter.podeAlterar() && presenter.getPerfil().isPadrao()) {
            habilitar(v.getBtnEditar()); // o padrão pode ser editado, mas não excluído
        } else if (presenter.podeAlterar()) {
            habilitar(v.getBtnEditar(), v.getBtnExcluir());
        } else {
            habilitar();
        }
        presenter.setEditavel(false);
        v.getLblSituacao().setText(presenter.getPerfil().isPadrao() ? "Perfil padrão: pode ser editado, mas não excluído" : "Perfil personalizado");
    }

    @Override
    public String getNome() {
        return "visualização";
    }

    @Override
    public void editar() {
        new EditarPerfilCommand(presenter).execute();
    }

    @Override
    public void excluir() {
        new ExcluirPerfilCommand(presenter).execute();
    }
}
