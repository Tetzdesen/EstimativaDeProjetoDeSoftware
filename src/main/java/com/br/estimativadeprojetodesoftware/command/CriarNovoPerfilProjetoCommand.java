package com.br.estimativadeprojetodesoftware.command;

import javax.swing.JDesktopPane;

import com.br.estimativadeprojetodesoftware.presenter.ManterPerfilPresenter;
import com.br.estimativadeprojetodesoftware.presenter.helpers.WindowManager;
import com.br.estimativadeprojetodesoftware.repository.PerfilRepositoryMock;
import com.br.estimativadeprojetodesoftware.view.ManterPerfilView;

public class CriarNovoPerfilProjetoCommand implements ProjetoCommand {
    private final JDesktopPane desktop;
    private final PerfilRepositoryMock repository;

    public CriarNovoPerfilProjetoCommand(JDesktopPane desktop, PerfilRepositoryMock repository) {
        this.desktop = desktop;
        this.repository = repository;
    }

    @Override
    public void execute() {
        String tituloJanela = "Criar Novo Perfil";
        WindowManager windowManager = WindowManager.getInstance();

        if (windowManager.isFrameAberto(tituloJanela)) {
            windowManager.bringToFront(tituloJanela);
        } else {
            ManterPerfilView manterPerfilView = new ManterPerfilView();
            new ManterPerfilPresenter(manterPerfilView, repository);
            manterPerfilView.setTitle(tituloJanela);
            desktop.add(manterPerfilView);
            manterPerfilView.setVisible(true);
            try {
                manterPerfilView.setMaximum(true);
            } catch (Exception ignored) {
            }
        }
    }
}
