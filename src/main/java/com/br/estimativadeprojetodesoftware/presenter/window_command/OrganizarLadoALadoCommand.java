package com.br.estimativadeprojetodesoftware.presenter.window_command;

import com.br.estimativadeprojetodesoftware.command.Command;
import com.br.estimativadeprojetodesoftware.presenter.DesktopMemento;
import com.br.estimativadeprojetodesoftware.presenter.Zelador;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyVetoException;

/**
 * Organiza as janelas internas em mosaico. Janelas maximizadas ou minimizadas
 * voltam ao estado normal antes, senão o mosaico não teria efeito.
 */
public class OrganizarLadoALadoCommand implements Command {
    private final JDesktopPane desktop;

    public OrganizarLadoALadoCommand(JDesktopPane desktop) {
        this.desktop = desktop;
    }

    @Override
    public void execute() {
        JInternalFrame[] frames = desktop.getAllFrames();
        if (frames.length == 0) {
            return;
        }
        Zelador.getInstance().salvarEstado(new DesktopMemento(desktop));

        int cols = (int) Math.ceil(Math.sqrt(frames.length));
        int rows = (int) Math.ceil((double) frames.length / cols);
        Dimension size = desktop.getSize();

        int w = size.width / cols;
        int h = size.height / rows;

        for (int i = 0; i < frames.length; i++) {
            normalizar(frames[i]);
            int x = (i % cols) * w;
            int y = (i / cols) * h;
            frames[i].setBounds(x, y, w, h);
        }
    }

    private static void normalizar(JInternalFrame frame) {
        try {
            if (frame.isIcon()) {
                frame.setIcon(false);
            }
            if (frame.isMaximum()) {
                frame.setMaximum(false);
            }
        } catch (PropertyVetoException ignored) {
            // a janela recusou a mudança; o setBounds ainda é aplicado
        }
    }
}
