package com.br.estimativadeprojetodesoftware.command;

import com.br.estimativadeprojetodesoftware.presenter.PerfilProjetoPresenter;
import com.br.estimativadeprojetodesoftware.presenter.helpers.WindowManager;
import com.br.estimativadeprojetodesoftware.repository.PerfilRepositoryMock;
import com.br.estimativadeprojetodesoftware.view.PerfilProjetoView;

import javax.swing.*;

public class VisualizarPerfilProjetoCommand implements ProjetoCommand {
    private final JDesktopPane desktop;
    private final String titulo;
    private final PerfilRepositoryMock repository;

    public VisualizarPerfilProjetoCommand(JDesktopPane desktop, String titulo, PerfilRepositoryMock repository) {
        this.desktop = desktop;
        this.titulo = titulo;
        this.repository = repository;
    }

    @Override
    public void execute() {
        WindowManager windowManager = WindowManager.getInstance();

        if (windowManager.isFrameAberto(titulo)) {
            windowManager.bringToFront(titulo);
        } else {
            JInternalFrame frame = new JInternalFrame(titulo, true, true, true, true);
            frame.setSize(desktop.getWidth(), desktop.getHeight());
            frame.setIconifiable(false);
            frame.setVisible(true);
            desktop.add(frame);
            PerfilProjetoView perfilProjetoView = new PerfilProjetoView();
            new PerfilProjetoPresenter(perfilProjetoView, repository);
            try {
                frame.setMaximum(true);
            } catch (Exception ignored) {
            }
        }
    }
}
