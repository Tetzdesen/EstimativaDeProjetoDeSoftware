package com.br.estimativadeprojetodesoftware.command.usuario;

import com.br.estimativadeprojetodesoftware.presenter.usuario.PrincipalUsuarioPresenter;
import com.br.estimativadeprojetodesoftware.presenter.window_command.SetLookAndFeelCommand;
import javax.swing.SwingUtilities;
import com.br.estimativadeprojetodesoftware.command.Command;

/**
 *
 * @author tetzner
 */
public class AbrirTelaPrincipalCommand implements Command {

    @Override
    public void execute(){
        SwingUtilities.invokeLater(() -> {
            new SetLookAndFeelCommand().execute();
            new PrincipalUsuarioPresenter();
        });
    }

}
