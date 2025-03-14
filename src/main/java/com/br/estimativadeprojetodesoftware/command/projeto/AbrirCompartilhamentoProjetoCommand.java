package com.br.estimativadeprojetodesoftware.command.projeto;

import com.br.estimativadeprojetodesoftware.command.ProjetoCommand;
import com.br.estimativadeprojetodesoftware.presenter.projeto.CompartilharProjetoPresenter;
import com.br.estimativadeprojetodesoftware.presenter.projeto.PrincipalProjetoPresenter;
import com.br.estimativadeprojetodesoftware.presenter.helpers.WindowManager;
import com.br.estimativadeprojetodesoftware.view.projeto.CompartilharProjetoView;

import javax.swing.*;

public class AbrirCompartilhamentoProjetoCommand implements ProjetoCommand {

    private final JDesktopPane desktop;
    private final String nomeProjeto;

    public AbrirCompartilhamentoProjetoCommand(JDesktopPane desktop, String nomeProjeto) {
        this.desktop = desktop;
        this.nomeProjeto = nomeProjeto;
    }

    @Override
    public void execute() {
        String tituloJanela = "Compartilhamento de Projetos";
        WindowManager windowManager = WindowManager.getInstance();

        if (windowManager.isFrameAberto(tituloJanela)) {
            windowManager.bringToFront(tituloJanela);
        } else {
            CompartilharProjetoView compartilharView = new CompartilharProjetoView();
            new CompartilharProjetoPresenter(compartilharView, nomeProjeto);
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
