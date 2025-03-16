package com.br.estimativadeprojetodesoftware.command.perfil;

import com.br.estimativadeprojetodesoftware.command.ProjetoCommand;
import com.br.estimativadeprojetodesoftware.model.PerfilProjeto;
import com.br.estimativadeprojetodesoftware.presenter.helpers.WindowManager;
import com.br.estimativadeprojetodesoftware.presenter.perfil.ManterPerfilPresenter;
import com.br.estimativadeprojetodesoftware.service.PerfilRepositoryService;
import com.br.estimativadeprojetodesoftware.view.perfil.ManterPerfilView;

public class AbrirManterPerfilProjetoCommand implements ProjetoCommand {
    private final PerfilProjeto perfil;
    private final PerfilRepositoryService repository;

    public AbrirManterPerfilProjetoCommand(PerfilProjeto perfil, PerfilRepositoryService repository) {
        this.perfil = perfil;
        this.repository = repository;
    }

    @Override
    public void execute() {
        String tituloJanela = "Manter Perfil de Projeto";
        
        WindowManager windowManager = WindowManager.getInstance();

        if (windowManager.isFrameAberto(tituloJanela)) {
            windowManager.bringToFront(tituloJanela);
        } else {
            ManterPerfilView manterPerfilView = new ManterPerfilView();
            new ManterPerfilPresenter(manterPerfilView, perfil, repository);
            manterPerfilView.setTitle(tituloJanela);

            windowManager.registrarDialog(tituloJanela, manterPerfilView);

            manterPerfilView.setModal(true);
            manterPerfilView.setSize(700, 800);
            manterPerfilView.setVisible(true);
            
        }
    }

}
