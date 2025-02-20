package com.br.estimativadeprojetodesoftware.presenter.usuario;

import com.br.estimativadeprojetodesoftware.command.MostrarMensagemProjetoCommand;
import com.br.estimativadeprojetodesoftware.model.Usuario;
import com.br.estimativadeprojetodesoftware.repository.UsuarioRepositoryMock;
import com.br.estimativadeprojetodesoftware.service.IconService;
import com.br.estimativadeprojetodesoftware.service.ValidadorSenhaService;
import com.br.estimativadeprojetodesoftware.view.usuario.CadastroUsuarioView;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Optional;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 *
 * @author tetzner
 */
public class CadastroUsuarioPresenter {

    private CadastroUsuarioView view;
    private UsuarioRepositoryMock repositoryUsuario;
    private ValidadorSenhaService validadorDeSenha;

    public CadastroUsuarioPresenter(UsuarioRepositoryMock repositoryUsuario) {
        this.view = new CadastroUsuarioView();
        this.repositoryUsuario = repositoryUsuario;
        this.validadorDeSenha = new ValidadorSenhaService();
        configuraView();
    }

    private void configuraView() {
        view.setResizable(false);
        view.setLocationRelativeTo(null);
        configuraActionsListerns();
        view.setVisible(true);
    }

    private void configuraActionsListerns() {
        view.getBtnSalvar().addActionListener(e -> {
            try {
                efetuarCadastro();
            } catch (Exception ex) {
                exibirMensagem(ex.getMessage());
            }
        });

        view.getBtnCancelar().addActionListener(e -> {
            try {
                view.dispose();
            } catch (Exception ex) {
                exibirMensagem(ex.getMessage());
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

        view.getBtnExibirConfirmarSenha().addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                view.getBtnExibirConfirmarSenha().setIcon(IconService.getIcon("olho-exibido"));
                view.getTxtConfirmarSenha().setEchoChar('\0');
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                view.getBtnExibirConfirmarSenha().setIcon(IconService.getIcon("olho"));
                view.getTxtConfirmarSenha().setEchoChar('*');
            }
        });
    }

    private void efetuarCadastro() throws Exception {

        String email = view.getTxtEmail().getText();

        Usuario usuario = repositoryUsuario.getUsuarioPorEmail(email);

        if (usuario == null) {
            String nome = view.getTxtNomeUsuario().getText();
            String senha = new String(view.getTxtSenha().getPassword());
            String senhaConfirmada = new String(view.getTxtConfirmarSenha().getPassword());

            if (camposInvalidos(email, nome, senha, senhaConfirmada)) {
                throw new IllegalArgumentException("Os campos não podem estar vazios");
            }

            if (senha.equals(senhaConfirmada)) {
                try {
                    if (validadorDeSenha.validarSenha(senha)) {
                        repositoryUsuario.adicionarUsuario(nome, email, senha);
                        exibirMensagem("Cadastro realizado com sucesso!");
                        view.dispose();
                    }
                } catch (Exception ex) {
                    throw new Exception("Erro na validação da senha:\n" + ex.getMessage());
                }

            } else {
                throw new IllegalArgumentException("Senhas não conferem");
            }
        } else {
            throw new IllegalArgumentException("Usuário já cadastrado no sistema, por favor cadastrar outro usuário");
        }
    }

    public CadastroUsuarioView getView() {
        return view;
    }

    private boolean camposInvalidos(String email, String nome, String senha, String senhaConfirmada) {
        return email == null || email.trim().isEmpty() || senha == null || senha.trim().isEmpty() || nome == null || nome.trim().isEmpty() || senhaConfirmada == null || senhaConfirmada.isEmpty();
    }

    public void exibirMensagem(String mensagem) {
        new MostrarMensagemProjetoCommand(mensagem).execute();
    }
}
