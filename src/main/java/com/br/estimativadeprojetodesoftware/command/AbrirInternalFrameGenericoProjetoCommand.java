package com.br.estimativadeprojetodesoftware.command;

import javax.swing.*;

public class AbrirInternalFrameGenericoProjetoCommand implements ProjetoCommand {
    private final JDesktopPane desktop;
    private final String titulo;

    public AbrirInternalFrameGenericoProjetoCommand(JDesktopPane desktop, String titulo) {
        this.desktop = desktop;
        this.titulo = titulo;
    }

    @Override
    public void execute() {
        JInternalFrame frame = new JInternalFrame(titulo, true, true, true, true);
        frame.setSize(desktop.getWidth(), desktop.getHeight());
        frame.setIconifiable(false);
        frame.setVisible(true);
        desktop.add(frame);
        try {
            frame.setMaximum(true);
        } catch (Exception ignored) {
        }
    }
}
