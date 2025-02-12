package com.br.estimativadeprojetodesoftware.principal;

import java.util.List;
import java.util.ArrayList;

import com.br.estimativadeprojetodesoftware.model.Perfil;
import com.br.estimativadeprojetodesoftware.presenter.PrincipalPresenter;
import com.br.estimativadeprojetodesoftware.presenter.helpers.WindowManager;
import com.br.estimativadeprojetodesoftware.repository.ProjetoRepositoryMock;
import com.br.estimativadeprojetodesoftware.repository.UsuarioRepositoryMock;
import javax.swing.SwingUtilities;

/**
 *
 * @author tetzner
 */
public class Principal {

    public static void main(String[] args) {
        // System.out.println("Hello World!");
        SwingUtilities.invokeLater(() -> {
            PrincipalPresenter presenter = new PrincipalPresenter(new ProjetoRepositoryMock());
            WindowManager.getInstance().initialize(presenter);
        });

    }
}
