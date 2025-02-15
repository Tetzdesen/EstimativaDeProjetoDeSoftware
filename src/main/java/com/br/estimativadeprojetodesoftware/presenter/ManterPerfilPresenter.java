package com.br.estimativadeprojetodesoftware.presenter;

import javax.swing.JDesktopPane;

import com.br.estimativadeprojetodesoftware.repository.PerfilRepositoryMock;
import com.br.estimativadeprojetodesoftware.view.ManterPerfilView;

public class ManterPerfilPresenter {
    private ManterPerfilView view;

    public ManterPerfilPresenter(ManterPerfilView view, PerfilRepositoryMock repository) {
        this.view = new ManterPerfilView();
        this.view.setVisible(true);
    }
}
