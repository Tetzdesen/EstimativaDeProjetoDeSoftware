package com.br.estimativadeprojetodesoftware.presenter.usuario;

import com.br.authifyjava.ResultadoAutenticacao;
import com.br.estimativadeprojetodesoftware.command.AbrirPrincipalPresenterCommand;
import com.br.estimativadeprojetodesoftware.command.MostrarMensagemProjetoCommand;
import com.br.estimativadeprojetodesoftware.model.Usuario;
import com.br.estimativadeprojetodesoftware.service.AutenticacaoService;
import com.br.estimativadeprojetodesoftware.service.IconService;
import com.br.estimativadeprojetodesoftware.service.UsuarioRepositoryService;
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

    private LoginView view;
    private UsuarioRepositoryService repositoryUsuario;
    private UsuarioLogadoSingleton usuarioLogado;
    private AutenticacaoService autenticacaoService;

    public LoginPresenter() {
        this.view = new LoginView();
        this.repositoryUsuario = new UsuarioRepositoryService();
        this.usuarioLogado = UsuarioLogadoSingleton.getInstancia();
        this.autenticacaoService = new AutenticacaoService();
        configuraView();
    }

    private void configuraView() {
        view.setSize(546, 450);
        view.setLocationRelativeTo(null);
        configuraListerns();
        SwingUtilities.invokeLater(() -> {
            view.setVisible(true); // Força a modalidade após configurar a localização
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

        if (camposInvalidos(email, senha)) {
            throw new IllegalArgumentException("Os campos de nome e senha não podem estar vazios");
        }

        //ResultadoAutenticacao resultadoAutenticacao = autenticacaoService.autenticar(email, senha);

        if (autenticacaoService.autenticar(email, senha)) {
            Usuario usuario = repositoryUsuario.buscarPorEmail(email).get();
            usuarioLogado.setUsuario(usuario);
            view.dispose();
            new AbrirPrincipalPresenterCommand().execute();
        } else {
            throw new IllegalArgumentException("não foi logado");
        }
    }

    public LoginView getView() {
        return view;
    }

    private boolean camposInvalidos(String email, String senha) {
        return email == null || email.trim().isEmpty() || senha == null || senha.trim().isEmpty();
    }

}
