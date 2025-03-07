package com.br.estimativadeprojetodesoftware.state.perfil;

import java.awt.event.ActionListener;

import com.br.estimativadeprojetodesoftware.presenter.perfil.ManterPerfilPresenter;

public abstract class ManterPerfilPresenterState {
    protected ManterPerfilPresenter presenter;

    public ManterPerfilPresenterState(ManterPerfilPresenter presenter) {
        this.presenter = presenter;
        removeListeners();
    }

    public void salvar() {
        throw new RuntimeException("Não é possível utilizar a função salvar neste estado: " + toString());
    }

    public void excluir() {
        throw new RuntimeException("Não é possível utilizar a função excluir neste estado: " + toString());
    }

    public void editar() {
        throw new RuntimeException("Não é possível utilizar a função editar neste estado: " + toString());
    }

    public void cancelar() {
        throw new RuntimeException("Não é possível utilizar a função cancelar neste estado: " + toString());
    }

    private void removeListeners() {
        ActionListener listeners[] =  presenter.getView().getBtnSalvar().getActionListeners();
        for(ActionListener listener: listeners) {
            presenter.getView().getBtnSalvar().removeActionListener(listener);            
        }

        listeners = presenter.getView().getBtnAdicionarCampo().getActionListeners();
        for(ActionListener listener: listeners) {
            presenter.getView().getBtnAdicionarCampo().removeActionListener(listener);            
        }

        listeners = presenter.getView().getBtnRemoverCampo().getActionListeners();
        for(ActionListener listener: listeners) {
            presenter.getView().getBtnRemoverCampo().removeActionListener(listener);            
        }

        listeners = presenter.getView().getBtnExcluir().getActionListeners();
        for(ActionListener listener: listeners) {
            presenter.getView().getBtnExcluir().removeActionListener(listener);            
        }

        listeners = presenter.getView().getBtnEditar().getActionListeners();
        for(ActionListener listener: listeners) {
            presenter.getView().getBtnEditar().removeActionListener(listener);            
        }

        listeners = presenter.getView().getBtnCancelar().getActionListeners();
        for(ActionListener listener: listeners) {
            presenter.getView().getBtnCancelar().removeActionListener(listener);            
        }

    }
}
