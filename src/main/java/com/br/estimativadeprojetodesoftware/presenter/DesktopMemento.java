package com.br.estimativadeprojetodesoftware.presenter;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class DesktopMemento implements Serializable {
    private final Map<String, Rectangle> frameBounds;

    public DesktopMemento(JDesktopPane desktop) {
        frameBounds = new HashMap<>();
        for (JInternalFrame frame : desktop.getAllFrames()) {
            frameBounds.put(frame.getTitle(), frame.getBounds());
        }
    }

    public void restore(JDesktopPane desktop) {
        for (JInternalFrame frame : desktop.getAllFrames()) {
            Rectangle bounds = frameBounds.get(frame.getTitle());
            if (bounds != null) {
                frame.setBounds(bounds);
            }
        }
    }
}
