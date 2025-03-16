package com.br.estimativadeprojetodesoftware.command.perfil;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;

import com.br.estimativadeprojetodesoftware.command.ProjetoCommand;
import com.br.estimativadeprojetodesoftware.model.PerfilProjeto;
import com.br.estimativadeprojetodesoftware.presenter.helpers.WindowManager;
import com.br.estimativadeprojetodesoftware.presenter.perfil.ManterPerfilPresenter;
import com.br.estimativadeprojetodesoftware.service.PerfilRepositoryService;
import com.br.estimativadeprojetodesoftware.view.perfil.ManterPerfilView;

public class AbrirManterPerfilProjetoCommand implements ProjetoCommand {
    private final JDesktopPane desktop;
    private final PerfilProjeto perfil;
    private final PerfilRepositoryService repository;

    public AbrirManterPerfilProjetoCommand(JDesktopPane desktop, PerfilProjeto perfil, PerfilRepositoryService repository) {
        this.desktop = desktop;
        this.perfil = perfil;
        this.repository = repository;
    }

    @Override
    public void execute() {
        String tituloJanela = "Manter Perfil de Projeto";
        
        WindowManager windowManager = WindowManager.getInstance();

        if (windowManager.isFrameAberto(tituloJanela)) {
            windowManager.bringToFront(tituloJanela);

            try {
                for (JInternalFrame frame : desktop.getAllFrames()) {
                    if (frame.getTitle().equals(tituloJanela) && frame.isMaximum()) {

                        frame.setMaximum(false);
                        frame.setLocation((desktop.getWidth() - frame.getWidth()) / 2,
                                (desktop.getHeight() - frame.getHeight()) / 2);
                    }
                }
            } catch (Exception ignored) {
            }

        } else {
            ManterPerfilView manterPerfilView = new ManterPerfilView(desktop);
            new ManterPerfilPresenter(manterPerfilView, perfil, repository);
            manterPerfilView.setTitle(tituloJanela);

            manterPerfilView.setSize(700, 700);
            int x = (desktop.getWidth() - manterPerfilView.getWidth()) / 2;
            int y = (desktop.getHeight() - manterPerfilView.getHeight()) / 2;
            manterPerfilView.setLocation(x, y);

            manterPerfilView.setModal(true);
            manterPerfilView.setVisible(true);
            
        }
    }

}
