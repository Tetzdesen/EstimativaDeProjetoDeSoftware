package com.br.estimativadeprojetodesoftware.command.projeto;

import com.br.estimativadeprojetodesoftware.command.ProjetoCommand;
import com.br.estimativadeprojetodesoftware.model.Projeto;
import com.br.estimativadeprojetodesoftware.presenter.helpers.WindowManager;
import com.br.estimativadeprojetodesoftware.presenter.projeto.CadastroProjetoPresenter;
import com.br.estimativadeprojetodesoftware.presenter.projeto.DashBoardProjetoPresenter;
import com.br.estimativadeprojetodesoftware.presenter.projeto.EdicaoProjetoPresenter;
import com.br.estimativadeprojetodesoftware.presenter.usuario.ManterUsuarioPresenter;
import com.br.estimativadeprojetodesoftware.repository.ProjetoRepositoryMock;
import com.br.estimativadeprojetodesoftware.view.projeto.CadastroProjetoView;
import com.br.estimativadeprojetodesoftware.view.projeto.DashBoardProjetoView;
import com.br.estimativadeprojetodesoftware.view.projeto.EdicaoProjetoView;
import com.br.estimativadeprojetodesoftware.view.usuario.ManterUsuarioView;
import static java.awt.SystemColor.desktop;
import javax.swing.JDesktopPane;

/**
 *
 * @author tetzner
 */
public class AbrirEdicaoProjetoCommand implements ProjetoCommand {

    private final JDesktopPane desktop;
    private final ProjetoRepositoryMock repository;
    private String projetoNome;

    public AbrirEdicaoProjetoCommand(JDesktopPane desktop, ProjetoRepositoryMock repository, String projetoNome) {
        this.desktop = desktop;
        this.repository = repository;
        this.projetoNome = projetoNome;
    }

    public void setProjetoNome(String projetoNome) {
        this.projetoNome = projetoNome;
    }

    @Override
    public void execute() {
        
        String tituloJanela = "Edição de Projeto";
        WindowManager windowManager = WindowManager.getInstance();

        if (windowManager.isFrameAberto(tituloJanela)) {
            windowManager.bringToFront(tituloJanela);
        } else {

            EdicaoProjetoView edicaoProjetoview = new EdicaoProjetoView();

            new EdicaoProjetoPresenter(edicaoProjetoview, repository, projetoNome);

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
