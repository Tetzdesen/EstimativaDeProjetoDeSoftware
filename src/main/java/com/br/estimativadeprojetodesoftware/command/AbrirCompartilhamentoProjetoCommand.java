package com.br.estimativadeprojetodesoftware.command;

import com.br.estimativadeprojetodesoftware.presenter.CompartilharProjetoPresenter;
import com.br.estimativadeprojetodesoftware.presenter.PrincipalPresenter;
import com.br.estimativadeprojetodesoftware.presenter.helpers.WindowManager;
import com.br.estimativadeprojetodesoftware.repository.ProjetoRepositoryMock;
import com.br.estimativadeprojetodesoftware.repository.UsuarioRepositoryMock;
import com.br.estimativadeprojetodesoftware.view.CompartilharProjetoView;

import javax.swing.*;

public class AbrirCompartilhamentoProjetoCommand implements ProjetoCommand {

    private final JDesktopPane desktop;
    private final PrincipalPresenter principalPresenter;

    public AbrirCompartilhamentoProjetoCommand(JDesktopPane desktop, PrincipalPresenter principalPresenter) {
        this.desktop = desktop;
        this.principalPresenter = principalPresenter;
    }

    @Override
    public void execute() {
        String tituloJanela = "Compartilhamento de Projetos";
        WindowManager windowManager = WindowManager.getInstance();

        if (windowManager.isFrameAberto(tituloJanela)) {
            windowManager.bringToFront(tituloJanela);
        } else {
            CompartilharProjetoView compartilharView = new CompartilharProjetoView();
            new CompartilharProjetoPresenter(compartilharView, principalPresenter);
            compartilharView.setTitle(tituloJanela);
            desktop.add(compartilharView);
            compartilharView.setVisible(true);
            try {
                compartilharView.setMaximum(true);
            } catch (Exception ignored) {
            }
        }
    }
}
