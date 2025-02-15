package com.br.estimativadeprojetodesoftware.command.perfil;

import com.br.estimativadeprojetodesoftware.command.ProjetoCommand;
import com.br.estimativadeprojetodesoftware.presenter.ManterPerfilPresenter;
import com.br.estimativadeprojetodesoftware.presenter.PerfilProjetoPresenter;
import com.br.estimativadeprojetodesoftware.presenter.helpers.WindowManager;
import com.br.estimativadeprojetodesoftware.repository.PerfilRepositoryMock;
import com.br.estimativadeprojetodesoftware.view.ManterPerfilView;
import com.br.estimativadeprojetodesoftware.view.PerfilProjetoView;

import javax.swing.*;

public class VisualizarPerfisProjetoCommand implements ProjetoCommand {
    private final JDesktopPane desktop;
    private final PerfilRepositoryMock repository;

    public VisualizarPerfisProjetoCommand(JDesktopPane desktop, PerfilRepositoryMock repository) {
        this.desktop = desktop;
        this.repository = repository;
    }

    @Override
    public void execute() {
        String tituloJanela = "Ver Perfis de Projeto";
        WindowManager windowManager = WindowManager.getInstance();

        if (windowManager.isFrameAberto(tituloJanela)) {
            windowManager.bringToFront(tituloJanela);
        } else {
            PerfilProjetoView perfilProjetoView = new PerfilProjetoView();
            new PerfilProjetoPresenter(perfilProjetoView, repository);
            perfilProjetoView.setTitle(tituloJanela);
            desktop.add(perfilProjetoView);
            perfilProjetoView.setVisible(true);
            try {
                perfilProjetoView.setMaximum(true);
            } catch (Exception ignored) {
            }
        }
    }
}
