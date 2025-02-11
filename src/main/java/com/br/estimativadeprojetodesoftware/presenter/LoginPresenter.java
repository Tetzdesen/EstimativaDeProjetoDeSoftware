package com.br.estimativadeprojetodesoftware.presenter;

import com.br.estimativadeprojetodesoftware.model.Usuario;
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

    private Usuario usuarioLogado;
    private LoginView view;
    private JDesktopPane desktopPane;

    public LoginPresenter(JDesktopPane desktopPane) {
        this.desktopPane = desktopPane;
        this.view = new LoginView();
        configuraView();
    }

    private void configuraView() {
        configuraActionsListerns();
    //    desktopPane.add(view);
      //  view.setVisible(true);
    }

    public void exibir() {
        desktopPane.add(view);
        view.setVisible(true);
        try {
            view.setSelected(true);
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }
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

    private void inicializarInterfacePrincipalProjetos() {
        desktopPane.removeAll();
        desktopPane.repaint();
        // new PrincipalPresenter();
    }

    public LoginView getView() {
        return view;
    }

}
