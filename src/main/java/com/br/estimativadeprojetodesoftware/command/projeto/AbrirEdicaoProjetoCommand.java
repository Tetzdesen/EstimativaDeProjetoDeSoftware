package com.br.estimativadeprojetodesoftware.command.projeto;

import com.br.estimativadeprojetodesoftware.command.ProjetoCommand;
import com.br.estimativadeprojetodesoftware.presenter.helpers.WindowManager;
import com.br.estimativadeprojetodesoftware.presenter.projeto.EdicaoProjetoPresenter;
import com.br.estimativadeprojetodesoftware.service.ProjetoRepositoryService;
import com.br.estimativadeprojetodesoftware.view.projeto.EdicaoProjetoView;
import javax.swing.JDesktopPane;

/**
 *
 * @author tetzner
 */
public class AbrirEdicaoProjetoCommand implements ProjetoCommand {
    private final ProjetoRepositoryService projetoService;
    private String nomeProjeto;

    public AbrirEdicaoProjetoCommand(ProjetoRepositoryService projetoService, String nomeProjeto) {
        this.projetoService = projetoService;
        this.nomeProjeto = nomeProjeto;
    }

    public void setProjetoNome(String nomeProjeto) {
        this.nomeProjeto = nomeProjeto;
    }

    @Override
    public void execute() {
        
        String tituloJanela = "Edição de Projeto";
        WindowManager windowManager = WindowManager.getInstance();

        if (windowManager.isFrameAberto(tituloJanela)) {
            windowManager.bringToFront(tituloJanela);
        } else {

            EdicaoProjetoView edicaoProjetoview = new EdicaoProjetoView();

            new EdicaoProjetoPresenter(projetoService, edicaoProjetoview, nomeProjeto);

            edicaoProjetoview.setTitle(tituloJanela);
            edicaoProjetoview.setSize(800, 400);
            edicaoProjetoview.setVisible(true);

            edicaoProjetoview.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent e) {
                    windowManager.fechar(tituloJanela);
                }
            });

        }
    }
}
