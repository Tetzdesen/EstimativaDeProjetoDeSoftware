package com.br.estimativadeprojetodesoftware.command;

import com.br.estimativadeprojetodesoftware.presenter.DashBoardProjetoPresenter;
import com.br.estimativadeprojetodesoftware.presenter.helpers.WindowManager;
import com.br.estimativadeprojetodesoftware.repository.ProjetoRepositoryMock;
import com.br.estimativadeprojetodesoftware.view.DashBoardProjetoView;

import javax.swing.*;

public class AbrirDashboardProjetoCommand implements ProjetoCommand {
    private final JDesktopPane desktop;
    private final ProjetoRepositoryMock repository;

    public AbrirDashboardProjetoCommand(JDesktopPane desktop, ProjetoRepositoryMock repository) {
        this.desktop = desktop;
        this.repository = repository;
    }

    @Override
    public void execute() {
        String tituloJanela = "Dashboard de Projetos";
        WindowManager windowManager = WindowManager.getInstance();

        if (windowManager.isFrameAberto(tituloJanela)) {
            windowManager.bringToFront(tituloJanela);
        } else {
            DashBoardProjetoView dashboardView = new DashBoardProjetoView();
            new DashBoardProjetoPresenter(dashboardView, repository);
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
