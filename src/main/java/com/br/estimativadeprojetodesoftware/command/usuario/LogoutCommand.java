package com.br.estimativadeprojetodesoftware.command.usuario;

import com.br.estimativadeprojetodesoftware.command.ProjetoCommand;
import com.br.estimativadeprojetodesoftware.presenter.projeto.PrincipalProjetoPresenter;
import com.br.estimativadeprojetodesoftware.presenter.window_command.FecharTodasJanelasCommand;
import com.br.estimativadeprojetodesoftware.singleton.UsuarioLogadoSingleton;
import javax.swing.JDesktopPane;

/**
 *
 * @author tetzner
 */
public class LogoutCommand implements ProjetoCommand {
    
    private final JDesktopPane desktop;
    
    public LogoutCommand(JDesktopPane desktop) {
        this.desktop = desktop;
    }

    @Override
    public void execute() {
        UsuarioLogadoSingleton.getInstancia().logout();
        // Fecha todas as janelas abertas
          new FecharTodasJanelasCommand(desktop).execute();
    }
}
