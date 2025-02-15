package com.br.estimativadeprojetodesoftware.presenter.window_command;

import com.br.estimativadeprojetodesoftware.presenter.PrincipalPresenter;
import javax.swing.JInternalFrame;

/**
 *
 * @author tetzner
 */
public class FecharTodasJanelasCommand implements WindowCommand {

    private final PrincipalPresenter principalPresenter;
    public FecharTodasJanelasCommand(PrincipalPresenter principalPresenter) {
        this.principalPresenter = principalPresenter;
    }
     
    @Override
    public void execute() {
        JInternalFrame[] quadrosInternos = principalPresenter.getView().getDesktop().getAllFrames();
        for (JInternalFrame quadroInterno : quadrosInternos) {
            quadroInterno.dispose();
        }
        
        principalPresenter.getView().dispose();
        
    }
    
}
