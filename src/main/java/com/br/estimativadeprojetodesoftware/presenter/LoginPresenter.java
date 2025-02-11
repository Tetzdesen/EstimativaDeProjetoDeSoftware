package com.br.estimativadeprojetodesoftware.presenter;

import com.br.estimativadeprojetodesoftware.model.Usuario;
import com.br.estimativadeprojetodesoftware.view.LoginView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JDesktopPane;

/**
 *
 * @author tetzner
 */
public class LoginPresenter {

    private Usuario usuarioLogado;
    private LoginView view;

    public LoginPresenter(Usuario usuario, LoginView view, JDesktopPane desktopPane) {
        this.usuarioLogado = usuario;
        this.view = new LoginView();
        desktopPane.add(view);
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
    
    private void realizaLogin(){
        // command
    }
    
    private void realizaCadastro(){
        // command
    } 
   
}
