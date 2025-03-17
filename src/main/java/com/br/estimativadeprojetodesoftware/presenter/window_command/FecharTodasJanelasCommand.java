package com.br.estimativadeprojetodesoftware.presenter.window_command;

import com.br.estimativadeprojetodesoftware.command.Command;
import com.br.estimativadeprojetodesoftware.command.usuario.AbrirTelaPrincipalCommand;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;

/**
 *
 * @author tetzner
 */
public class FecharTodasJanelasCommand implements Command {

    private final JDesktopPane desktop;
    
    public FecharTodasJanelasCommand(JDesktopPane desktop) {
        this.desktop = desktop;
    }
     
    @Override
    public void execute() {
        JInternalFrame[] quadrosInternos = desktop.getAllFrames();
        for (JInternalFrame quadroInterno : quadrosInternos) {
            quadroInterno.dispose();
        }
        new AbrirTelaPrincipalCommand(); 
    }
    
}
