package com.br.estimativadeprojetodesoftware.presenter.perfil;

import java.util.List;

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
    private List<String> funcionalidades;

    public ManterPerfilPresenter(ManterPerfilView view, PerfilRepositoryMock repository) {
        this.view = view;
        this.view.setVisible(true);

        setFuncionalidades();
        
    }

    @Override
    public void update() {
        
    }

    private void setFuncionalidades() {
        funcionalidades.add("Pequeno");
        funcionalidades.add("Médio");
        funcionalidades.add("Grande");
        funcionalidades.add("MVP");
        funcionalidades.add("Básico");
        funcionalidades.add("Profissional");
        funcionalidades.add("Cadastro por Email e Senha");
        funcionalidades.add("Cadastro Pelo Facebook");
    }
}
