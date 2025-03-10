package com.br.estimativadeprojetodesoftware.presenter.usuario;

/**
 *
 * @author tetzner
 */

import com.br.estimativadeprojetodesoftware.presenter.window_command.SetLookAndFeelCommand;
import com.br.estimativadeprojetodesoftware.repository.ProjetoRepositoryMock;
import com.br.estimativadeprojetodesoftware.repository.UsuarioRepositoryMock;
import com.br.estimativadeprojetodesoftware.view.usuario.PrincipalUsuarioView;
import javax.swing.SwingUtilities;

/**
 * @author tetzner
 */
public class PrincipalUsuarioPresenter {

    private final PrincipalUsuarioView view;

    public PrincipalUsuarioPresenter() {
        this.view = new PrincipalUsuarioView();
        configuraView();
    }

    private void configuraView() {
        configuraListeners();

        view.setResizable(false);
        view.setLocationRelativeTo(null);
        view.setVisible(true);
    }

    private void configuraListeners() {

        view.getBtnAutenticarPorSenha().addActionListener(e -> {
            SwingUtilities.invokeLater(() -> {
                new SetLookAndFeelCommand().execute();
                new LoginPresenter(new ProjetoRepositoryMock(), new UsuarioRepositoryMock());
            });
        });
        
        view.getBtnCadastrar().addActionListener(e -> {
            SwingUtilities.invokeLater(() -> {
                new SetLookAndFeelCommand().execute();
                new CadastroPresenter(new UsuarioRepositoryMock());
            });
        });
    }

    public PrincipalUsuarioView getView() {
        return view;
    }
}

