package com.br.estimativadeprojetodesoftware.principal;

import com.br.estimativadeprojetodesoftware.presenter.LoginPresenter;
import com.br.estimativadeprojetodesoftware.presenter.window_command.SetLookAndFeelCommand;
import com.br.estimativadeprojetodesoftware.repository.ProjetoRepositoryMock;
import com.br.estimativadeprojetodesoftware.repository.UsuarioRepositoryMock;
import javax.swing.SwingUtilities;

/**
 *
 * @author tetzner
 */
public class Principal {

    public static void main(String[] args) {
 
        SwingUtilities.invokeLater(() -> {
            new SetLookAndFeelCommand().execute();
            LoginPresenter loginPresenter = new LoginPresenter(new ProjetoRepositoryMock(), new UsuarioRepositoryMock());
        });

    }
}


