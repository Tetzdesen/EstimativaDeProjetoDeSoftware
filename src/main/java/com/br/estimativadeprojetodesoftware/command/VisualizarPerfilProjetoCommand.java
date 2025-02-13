package com.br.estimativadeprojetodesoftware.command;

import com.br.estimativadeprojetodesoftware.presenter.PerfilProjetoPresenter;
import com.br.estimativadeprojetodesoftware.presenter.helpers.WindowManager;
import com.br.estimativadeprojetodesoftware.repository.PerfilRepositoryMock;
import com.br.estimativadeprojetodesoftware.view.PerfilProjetoView;

import javax.swing.*;

public class VisualizarPerfilProjetoCommand implements ProjetoCommand {
    private final JDesktopPane desktop;
    private final PerfilRepositoryMock repository;

    public VisualizarPerfilProjetoCommand(JDesktopPane desktop, PerfilRepositoryMock repository) {
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
            PerfilProjetoView dashboardView = new PerfilProjetoView();
            new PerfilProjetoPresenter(dashboardView, repository);
            dashboardView.setTitle(tituloJanela);
            desktop.add(dashboardView);
            dashboardView.setVisible(true);
            try {
                dashboardView.setMaximum(true);
            } catch (Exception ignored) {
            }
        }
    }
}
