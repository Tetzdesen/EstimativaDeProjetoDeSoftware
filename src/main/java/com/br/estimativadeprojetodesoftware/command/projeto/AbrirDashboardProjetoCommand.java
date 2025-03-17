package com.br.estimativadeprojetodesoftware.command.projeto;

import com.br.estimativadeprojetodesoftware.presenter.projeto.DashBoardProjetoPresenter;
import com.br.estimativadeprojetodesoftware.presenter.helpers.WindowManager;
import com.br.estimativadeprojetodesoftware.service.ProjetoService;
import com.br.estimativadeprojetodesoftware.view.projeto.DashBoardProjetoView;

import javax.swing.*;
import com.br.estimativadeprojetodesoftware.command.Command;

public class AbrirDashboardProjetoCommand implements Command {
    private final JDesktopPane desktop;
    private final ProjetoService projetoService;

    public AbrirDashboardProjetoCommand(JDesktopPane desktop, ProjetoService projetoService) {
        this.desktop = desktop;
        this.projetoService = projetoService;
    }

    @Override
    public void execute() {
        String tituloJanela = "Dashboard de Projetos";
        WindowManager windowManager = WindowManager.getInstance();

        if (windowManager.isFrameAberto(tituloJanela)) {
            windowManager.bringToFront(tituloJanela);
        } else {
            DashBoardProjetoView dashboardView = new DashBoardProjetoView();
            new DashBoardProjetoPresenter(dashboardView, projetoService);
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
