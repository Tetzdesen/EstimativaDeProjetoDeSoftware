package com.br.estimativadeprojetodesoftware.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.WindowEvent;

public class GlobalWindowManager {

    private static JFrame mainWindow;

    public static void initialize(JFrame main) {
        mainWindow = main;
        mainWindow.setExtendedState(JFrame.MAXIMIZED_BOTH);

        Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
            @Override
            public void eventDispatched(AWTEvent event) {
                if (event.getID() == WindowEvent.WINDOW_OPENED) {
                    Window window = (Window) event.getSource();


                    if (window.getClass().getName().contains("Popup") ||
                            window.getClass().getName().contains("Menu")) {
                        return;
                    }

                    if (window != mainWindow) {
                        setLocationOnCurrentMonitor(window);
                    }
                }
            }
        }, AWTEvent.WINDOW_EVENT_MASK);
    }

    private static void setLocationOnCurrentMonitor(Window window) {
        if (mainWindow != null) {
            GraphicsConfiguration currentConfig = mainWindow.getGraphicsConfiguration();
            Rectangle bounds = currentConfig.getBounds();

            int x = bounds.x + (bounds.width - window.getWidth()) / 2;
            int y = bounds.y + (bounds.height - window.getHeight()) / 2;

            window.setLocation(x, y);
        }
    }
}
