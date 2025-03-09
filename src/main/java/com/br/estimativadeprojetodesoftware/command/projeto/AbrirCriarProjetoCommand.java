package com.br.estimativadeprojetodesoftware.command.projeto;

import com.br.estimativadeprojetodesoftware.command.ProjetoCommand;
import com.br.estimativadeprojetodesoftware.presenter.projeto.CadastroProjetoPresenter;
import com.br.estimativadeprojetodesoftware.presenter.helpers.WindowManager;
import com.br.estimativadeprojetodesoftware.repository.ProjetoRepositoryMock;
import com.br.estimativadeprojetodesoftware.view.projeto.CadastroProjetoView;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;

/**
 *
 * @author tetzner
 */
public class AbrirCriarProjetoCommand implements ProjetoCommand {

    private final JDesktopPane desktop;
    private final ProjetoRepositoryMock repository;

    public AbrirCriarProjetoCommand(JDesktopPane desktop, ProjetoRepositoryMock repository) {
        this.desktop = desktop;
        this.repository = repository;
    }

    @Override
    public void execute() {
        String tituloJanela = "Criar Projeto";
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
            CadastroProjetoView cadastroProjetoView = new CadastroProjetoView();
            new CadastroProjetoPresenter(cadastroProjetoView, repository);
            cadastroProjetoView.setTitle(tituloJanela);
            int x = (desktop.getWidth() - cadastroProjetoView.getWidth()) / 2;
            int y = (desktop.getHeight() - cadastroProjetoView.getHeight()) / 2;
            cadastroProjetoView.setLocation(x, y);
            cadastroProjetoView.setVisible(true);

            try {
     
            } catch (Exception ignored) {
            }
        }
    }

}
