package com.br.estimativadeprojetodesoftware.command.perfil;

import com.br.estimativadeprojetodesoftware.presenter.helpers.WindowManager;
import com.br.estimativadeprojetodesoftware.presenter.perfil.PerfilProjetoPresenter;
import com.br.estimativadeprojetodesoftware.view.perfil.PerfilProjetoView;

import javax.swing.*;
import com.br.estimativadeprojetodesoftware.command.Command;

public class VisualizarPerfisProjetoCommand implements Command {
    private final JDesktopPane desktop;

    public VisualizarPerfisProjetoCommand(JDesktopPane desktop) {
        this.desktop = desktop;
    }

    @Override
    public void execute() {
        String tituloJanela = "Ver Perfis de Projeto";
        WindowManager windowManager = WindowManager.getInstance();

        if (windowManager.isFrameAberto(tituloJanela)) {
            windowManager.bringToFront(tituloJanela);
        } else {
            PerfilProjetoView perfilProjetoView = new PerfilProjetoView(desktop);
            new PerfilProjetoPresenter(perfilProjetoView);
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
