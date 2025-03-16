package com.br.estimativadeprojetodesoftware.command.projeto;

import com.br.estimativadeprojetodesoftware.command.ProjetoCommand;
import com.br.estimativadeprojetodesoftware.presenter.projeto.CompartilharProjetoPresenter;
import com.br.estimativadeprojetodesoftware.presenter.helpers.WindowManager;
import com.br.estimativadeprojetodesoftware.service.ProjetoRepositoryService;
import com.br.estimativadeprojetodesoftware.view.projeto.CompartilharProjetoView;

public class AbrirCompartilhamentoProjetoCommand implements ProjetoCommand {
    private final ProjetoRepositoryService projetoService;
    private final String nomeProjeto;

    public AbrirCompartilhamentoProjetoCommand(ProjetoRepositoryService projetoService, String nomeProjeto) {
        this.projetoService = projetoService;
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
            new CompartilharProjetoPresenter(projetoService, compartilharView, nomeProjeto);
            compartilharView.setTitle(tituloJanela);
            compartilharView.setVisible(true);
        }
    }
}
