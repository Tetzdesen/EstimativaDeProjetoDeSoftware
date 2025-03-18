package com.br.estimativadeprojetodesoftware.command.projeto;

import com.br.estimativadeprojetodesoftware.presenter.projeto.CadastroProjetoPresenter;
import com.br.estimativadeprojetodesoftware.presenter.helpers.WindowManager;
import com.br.estimativadeprojetodesoftware.service.ProjetoService;
import com.br.estimativadeprojetodesoftware.view.projeto.CadastroProjetoView;
import javax.swing.JDesktopPane;
import com.br.estimativadeprojetodesoftware.command.Command;

/**
 *
 * @author tetzner
 */
public class AbrirCriarProjetoCommand implements Command {

    private final JDesktopPane desktop;
    private final ProjetoService projetoService;

    public AbrirCriarProjetoCommand(JDesktopPane desktop, ProjetoService projetoService) {
        this.desktop = desktop;
        this.projetoService = projetoService;
    }

    @Override
    public void execute() {
        String tituloJanela = "Criar Projeto";
        WindowManager windowManager = WindowManager.getInstance();

        if (windowManager.isFrameAberto(tituloJanela)) {
            windowManager.bringToFront(tituloJanela);
        } else {
            CadastroProjetoView cadastroProjetoView = new CadastroProjetoView();
            new CadastroProjetoPresenter(cadastroProjetoView, projetoService);
            windowManager.registrarDialog(tituloJanela, cadastroProjetoView);
            cadastroProjetoView.setTitle(tituloJanela);
            cadastroProjetoView.setSize(800,400);
            cadastroProjetoView.setVisible(true);
        }
    }

}
