package com.br.estimativadeprojetodesoftware.presenter.perfil;

import javax.swing.JDesktopPane;
import javax.swing.SwingUtilities;

import com.br.estimativadeprojetodesoftware.presenter.Observer;
import com.br.estimativadeprojetodesoftware.presenter.window_command.ConfigurarViewCommand;
import com.br.estimativadeprojetodesoftware.presenter.window_command.FecharJanelasRelacionadasCommand;
import com.br.estimativadeprojetodesoftware.presenter.window_command.WindowCommand;
import com.br.estimativadeprojetodesoftware.repository.PerfilRepositoryMock;
import com.br.estimativadeprojetodesoftware.view.perfil.ManterPerfilView;

public class ManterPerfilPresenter implements Observer {
    private ManterPerfilView view;

    public ManterPerfilPresenter(ManterPerfilView view, PerfilRepositoryMock repository) {
        this.view = view;
        this.view.setVisible(true);
    }

    @Override
    public void update() {
        
    }
}
