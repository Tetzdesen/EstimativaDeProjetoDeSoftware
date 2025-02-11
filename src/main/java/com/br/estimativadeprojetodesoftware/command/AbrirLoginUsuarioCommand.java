package com.br.estimativadeprojetodesoftware.command;

import com.br.estimativadeprojetodesoftware.presenter.DashBoardProjetoPresenter;
import com.br.estimativadeprojetodesoftware.presenter.helpers.WindowManager;
import com.br.estimativadeprojetodesoftware.repository.ProjetoRepositoryMock;
import com.br.estimativadeprojetodesoftware.view.DashBoardProjetoView;
import com.br.estimativadeprojetodesoftware.view.LoginView;
import javax.swing.JDesktopPane;

/**
 *
 * @author tetzner
 */
public class AbrirLoginUsuarioCommand implements ProjetoCommand {

    private final JDesktopPane desktop;
    private final ProjetoRepositoryMock repository;

    public AbrirLoginUsuarioCommand(JDesktopPane desktop, ProjetoRepositoryMock repository) {
        this.desktop = desktop;
        this.repository = repository;
    }

    @Override
    public void execute() {
        String tituloJanela = "Login de desenvolvedor";
        WindowManager windowManager = WindowManager.getInstance();

        if (windowManager.isFrameAberto(tituloJanela)) {
            windowManager.bringToFront(tituloJanela);
        } else {
            LoginView loginView = new LoginView();
            new LoginPresenter(loginView, repository);
            loginView.setTitle(tituloJanela);
            desktop.add(loginView);
            loginView.setVisible(true);
            try {
                loginView.setMaximum(true);
            } catch (Exception ignored) {
            }
        }
    }

}
