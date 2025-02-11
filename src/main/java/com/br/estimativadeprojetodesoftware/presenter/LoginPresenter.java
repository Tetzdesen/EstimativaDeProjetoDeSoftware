package com.br.estimativadeprojetodesoftware.presenter;

import com.br.estimativadeprojetodesoftware.model.Usuario;
import com.br.estimativadeprojetodesoftware.repository.ProjetoRepositoryMock;
import com.br.estimativadeprojetodesoftware.repository.UsuarioRepositoryMock;
import com.br.estimativadeprojetodesoftware.view.DashBoardProjetoView;
import com.br.estimativadeprojetodesoftware.view.LoginView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;
import javax.swing.JDesktopPane;

/**
 *
 * @author tetzner
 */
public class LoginPresenter {

    private LoginView view;
    private UsuarioRepositoryMock repository;

    public LoginPresenter(LoginView view, UsuarioRepositoryMock repository) {
        this.view = view;
        this.repository = repository;
        configuraView();
    }

    private void configuraView() {
        configuraActionsListerns();
        view.setVisible(true);
    }

    private void configuraActionsListerns() {
        view.getBtnEntrar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // realizar login
            }
        });

        view.getBtnCadastrar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // chamar cadastro command ?
                view.dispose();
            }
        });
    }

    private void realizaLogin() {
        // command
    }

    private void realizaCadastro() {
        // command
    }

    public LoginView getView() {
        return view;
    }

}
