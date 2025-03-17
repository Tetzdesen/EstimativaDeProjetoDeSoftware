package com.br.estimativadeprojetodesoftware.presenter.usuario;

import com.br.estimativadeprojetodesoftware.command.projeto.MostrarMensagemProjetoCommand;
import com.br.estimativadeprojetodesoftware.command.usuario.RealizarCadastroUsuarioCommand;
import com.br.estimativadeprojetodesoftware.service.IconService;
import com.br.estimativadeprojetodesoftware.view.usuario.CadastroUsuarioView;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 *
 * @author tetzner
 */
public class CadastroUsuarioPresenter {

    private final CadastroUsuarioView view;
    private final PrincipalUsuarioPresenter principalUsuarioPresenter;
    
    public CadastroUsuarioPresenter(CadastroUsuarioView view, PrincipalUsuarioPresenter principalUsuarioPresenter) {
        this.view = view;
        this.principalUsuarioPresenter = principalUsuarioPresenter;
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
        String nome = view.getTxtNomeUsuario().getText();
        String senha = new String(view.getTxtSenha().getPassword());
        String senhaConfirmada = new String(view.getTxtConfirmarSenha().getPassword());
       
        new RealizarCadastroUsuarioCommand(nome, email, senha, senhaConfirmada).execute();
        view.dispose();         
    }

    public CadastroUsuarioView getView() {
        return view;
    }

    private void exibirMensagem(String mensagem) {
        new MostrarMensagemProjetoCommand(mensagem).execute();
    }
}
