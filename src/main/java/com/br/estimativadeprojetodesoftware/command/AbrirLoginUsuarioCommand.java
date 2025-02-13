package com.br.estimativadeprojetodesoftware.command;

import com.br.estimativadeprojetodesoftware.presenter.DashBoardProjetoPresenter;
import com.br.estimativadeprojetodesoftware.presenter.LoginPresenter;
import com.br.estimativadeprojetodesoftware.presenter.PrincipalPresenter;
import com.br.estimativadeprojetodesoftware.presenter.helpers.WindowManager;
import com.br.estimativadeprojetodesoftware.repository.UsuarioRepositoryMock;
import com.br.estimativadeprojetodesoftware.view.DashBoardProjetoView;
import com.br.estimativadeprojetodesoftware.view.LoginView;
import javax.swing.JDesktopPane;

/**
 *
 * @author tetzner
 */
public class AbrirLoginUsuarioCommand implements ProjetoCommand {
    
    private final PrincipalPresenter principalPresenter;
    private final JDesktopPane desktop;
    private final UsuarioRepositoryMock usuarioRepository;

    public AbrirLoginUsuarioCommand(PrincipalPresenter principalPresenter, JDesktopPane desktop, UsuarioRepositoryMock usuarioRepository) {
        this.principalPresenter = principalPresenter;
        this.desktop = desktop;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public void execute() {
        String tituloJanela = "Login de Desenvolvedor";
        WindowManager windowManager = WindowManager.getInstance();
        
        if (windowManager.isFrameAberto(tituloJanela)) {
            windowManager.bringToFront(tituloJanela);
        } else {
            LoginView loginView = new LoginView();
            new LoginPresenter(principalPresenter.getProjetoRepository(), usuarioRepository);
            loginView.setTitle(tituloJanela);
            
            desktop.add(loginView);
            loginView.setVisible(true);
            try {
               // loginView.setMaximum(true);
            } catch (Exception ignored) {
            }
        }
    }

}
