package com.br.estimativadeprojetodesoftware.presenter.usuario;

import com.br.estimativadeprojetodesoftware.command.AbrirPrincipalPresenterCommand;
import com.br.estimativadeprojetodesoftware.command.MostrarMensagemProjetoCommand;
import com.br.estimativadeprojetodesoftware.model.Usuario;
import com.br.estimativadeprojetodesoftware.repository.ProjetoRepositoryMock;
import com.br.estimativadeprojetodesoftware.repository.UsuarioRepositoryMock;
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

    public LoginPresenter() {
        this.view = new LoginView();
        this.repositoryUsuario = new UsuarioRepositoryService();
        usuarioLogado = UsuarioLogadoSingleton.getInstancia();
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

        Usuario usuario = repositoryUsuario.buscarPorEmail(email).orElse(null);
        
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não encontrado!");
        }

        if (usuario.getSenha().equals(senha)) {
            usuarioLogado.setUsuario(usuario);
            if (UsuarioLogadoSingleton.getInstancia().getUsuario() != null) {
                view.dispose();
                new AbrirPrincipalPresenterCommand().execute();
            }
        } else {
            throw new IllegalArgumentException("Senha incorreta");
        }
    }

    public LoginView getView() {
        return view;
    }

    private boolean camposInvalidos(String email, String senha) {
        return email == null || email.trim().isEmpty() || senha == null || senha.trim().isEmpty();
    }

}
