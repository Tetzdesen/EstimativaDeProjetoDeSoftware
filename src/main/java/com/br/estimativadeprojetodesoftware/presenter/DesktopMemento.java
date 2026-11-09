package com.br.estimativadeprojetodesoftware.presenter;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyVetoException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Estado das janelas internas do MDI: posição, tamanho e se estavam
 * maximizadas. Para janela maximizada guarda o tamanho normal, que é o que
 * ela volta a ter se o usuário a restaurar depois.
 */
public class DesktopMemento implements Serializable {

    private record EstadoJanela(Rectangle bounds, boolean maximizada) implements Serializable {
    }

    private final Map<String, EstadoJanela> estados;

    public DesktopMemento(JDesktopPane desktop) {
        estados = new HashMap<>();
        for (JInternalFrame frame : desktop.getAllFrames()) {
            boolean maximizada = frame.isMaximum();
            Rectangle bounds = maximizada ? frame.getNormalBounds() : frame.getBounds();
            estados.put(frame.getTitle(), new EstadoJanela(new Rectangle(bounds), maximizada));
        }
    }

    public void restore(JDesktopPane desktop) {
        for (JInternalFrame frame : desktop.getAllFrames()) {
            EstadoJanela estado = estados.get(frame.getTitle());
            if (estado == null) {
                continue;
            }
            try {
                if (frame.isMaximum()) {
                    frame.setMaximum(false);
                }
                frame.setBounds(estado.bounds());
                if (estado.maximizada()) {
                    frame.setMaximum(true);
                }
            } catch (PropertyVetoException ignored) {
                // a janela recusou a mudança; fica como está
            }
        }
    }
}
