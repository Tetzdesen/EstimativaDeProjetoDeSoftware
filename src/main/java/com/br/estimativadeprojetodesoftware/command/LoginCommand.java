package com.br.estimativadeprojetodesoftware.command;

import com.br.estimativadeprojetodesoftware.presenter.LoginPresenter;
import com.br.estimativadeprojetodesoftware.presenter.window_command.SetLookAndFeelCommand;
import com.br.estimativadeprojetodesoftware.repository.ProjetoRepositoryMock;
import com.br.estimativadeprojetodesoftware.repository.UsuarioRepositoryMock;
import javax.swing.SwingUtilities;

/**
 *
 * @author tetzner
 */
public class LoginCommand implements ProjetoCommand {

    @Override
    public void execute() {
        SwingUtilities.invokeLater(() -> {
            new SetLookAndFeelCommand().execute();
            new LoginPresenter(new ProjetoRepositoryMock(), new UsuarioRepositoryMock());
        });
        
    }

}
