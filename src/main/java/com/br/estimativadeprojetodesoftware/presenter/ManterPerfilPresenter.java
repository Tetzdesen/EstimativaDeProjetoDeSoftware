package com.br.estimativadeprojetodesoftware.presenter;

import javax.swing.JDesktopPane;
import javax.swing.SwingUtilities;

import com.br.estimativadeprojetodesoftware.presenter.window_command.ConfigurarViewCommand;
import com.br.estimativadeprojetodesoftware.presenter.window_command.FecharJanelasRelacionadasCommand;
import com.br.estimativadeprojetodesoftware.presenter.window_command.WindowCommand;
import com.br.estimativadeprojetodesoftware.repository.PerfilRepositoryMock;
import com.br.estimativadeprojetodesoftware.view.ManterPerfilView;

public class ManterPerfilPresenter implements Observer {
    private ManterPerfilView view;

    public ManterPerfilPresenter(ManterPerfilView view, PerfilRepositoryMock repository) {
        this.view = new ManterPerfilView();
        this.view.setVisible(true);
    }

    @Override
    public void update() {
        
    }
}
