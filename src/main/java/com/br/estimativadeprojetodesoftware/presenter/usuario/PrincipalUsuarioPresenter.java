package com.br.estimativadeprojetodesoftware.presenter.usuario;

/**
 *
 * @author tetzner
 */
import com.br.estimativadeprojetodesoftware.command.usuario.AbrirCadastroUsuarioCommand;
import com.br.estimativadeprojetodesoftware.command.usuario.AbrirLoginUsuarioCommand;
import com.br.estimativadeprojetodesoftware.presenter.window_command.SetLookAndFeelCommand;
import com.br.estimativadeprojetodesoftware.view.usuario.PrincipalUsuarioView;
import javax.swing.SwingUtilities;

/**
 * @author tetzner
 */
public class PrincipalUsuarioPresenter {

    private final PrincipalUsuarioView view;

    // adicionar map de comandos
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
        new SetLookAndFeelCommand().execute();
        view.getBtnAutenticarPorSenha().addActionListener(e -> {
            SwingUtilities.invokeLater(() -> {
                new AbrirLoginUsuarioCommand().execute();
            });
        });

        view.getBtnCadastrar().addActionListener(e -> {
            SwingUtilities.invokeLater(() -> {
                new AbrirCadastroUsuarioCommand().execute();
            });
        });
    }

    public PrincipalUsuarioView getView() {
        return view;
    }
}
