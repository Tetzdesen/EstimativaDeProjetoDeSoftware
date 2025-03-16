package com.br.estimativadeprojetodesoftware.presenter.usuario;

import com.br.estimativadeprojetodesoftware.command.AbrirPrincipalPresenterCommand;
import com.br.estimativadeprojetodesoftware.command.projeto.MostrarMensagemProjetoCommand;
import com.br.estimativadeprojetodesoftware.command.ProjetoCommand;
import com.br.estimativadeprojetodesoftware.command.usuario.RealizarAutenticacaoUsuarioCommand;
import com.br.estimativadeprojetodesoftware.presenter.window_command.FecharJanelaCommand;
import com.br.estimativadeprojetodesoftware.service.IconService;
import com.br.estimativadeprojetodesoftware.singleton.UsuarioLogadoSingleton;
import com.br.estimativadeprojetodesoftware.view.usuario.LoginView;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.SwingUtilities;

/**
 *
 * @author tetzner
 */
public class LoginPresenter {

    private final LoginView view;
    private final PrincipalUsuarioPresenter principalUsuarioPresenter;
    private ProjetoCommand comandoDeLogin;

    public LoginPresenter(LoginView view, PrincipalUsuarioPresenter principalUsuarioPresenter) {
        this.principalUsuarioPresenter = principalUsuarioPresenter;
        this.view = view;
        configuraView();
    }

    private void configuraView() {
        configuraListerns();
        SwingUtilities.invokeLater(() -> {
            view.setVisible(true);
        });
    }

    private void configuraListerns() {
        view.getBtnEntrar().addActionListener(e -> {
            try {
                efetuarLogin();
            } catch (Exception ex) {
                new MostrarMensagemProjetoCommand(ex.getMessage()).execute();
            }
        });

        view.getBtnExibirSenha().addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                view.getBtnExibirSenha().setIcon(IconService.getIcon("olho-exibido"));
                view.getTxtSenha().setEchoChar('\0');
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                view.getBtnExibirSenha().setIcon(IconService.getIcon("olho"));
                view.getTxtSenha().setEchoChar('*');
            }
        });
    }

    private void efetuarLogin() throws Exception {
        String email = view.getTxtEmail().getText();
        String senha = new String(view.getTxtSenha().getPassword());

        this.comandoDeLogin = new RealizarAutenticacaoUsuarioCommand(email, senha);
        comandoDeLogin.execute();

        if (UsuarioLogadoSingleton.getInstancia().getUsuario() != null) {
            principalUsuarioPresenter.getView().dispose();
            new FecharJanelaCommand(view).execute();
            new AbrirPrincipalPresenterCommand().execute();
        }
    }

    public LoginView getView() {
        return view;
    }
}
