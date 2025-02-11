package com.br.estimativadeprojetodesoftware.presenter.helpers;

import com.br.estimativadeprojetodesoftware.command.MostrarMensagemProjetoCommand;
import com.br.estimativadeprojetodesoftware.presenter.PrincipalPresenter;
import javax.swing.*;
import java.util.Optional;

/**
 *
 * @author tetzner
 */
public class WindowManager {
    private static WindowManager instance;
    private PrincipalPresenter principalPresenter;

    private WindowManager() {
    }

    public static WindowManager getInstance() {
        if (instance == null) {
            instance = new WindowManager();
        }
        return instance;
    }

    public void initialize(PrincipalPresenter presenter) {
        this.principalPresenter = presenter;
    }

    public Optional<JInternalFrame> getFrameAberto(String titulo) {
        if (principalPresenter == null) {
            throw new IllegalStateException("WindowManager não foi inicializado com PrincipalPresenter.");
        }

        JDesktopPane desktop = principalPresenter.getView().getDesktop();
        JInternalFrame[] frames = desktop.getAllFrames();

        for (JInternalFrame frame : frames) {
            if (frame.getTitle().equals(titulo)) {
                return Optional.of(frame);
            }
        }
        return Optional.empty();
    }

    public void bringToFront(String titulo) {
        getFrameAberto(titulo).ifPresent(frame -> {
            try {
                frame.setIcon(false);
                frame.setMaximum(true);
                frame.moveToFront();
                frame.setSelected(true);
            } catch (Exception e) {
                new MostrarMensagemProjetoCommand("Falha ao trazer janela para frente:\n" + e.getMessage());
            }
        });
    }

    public boolean isFrameAberto(String titulo) {
        return getFrameAberto(titulo).isPresent();
    }
}
