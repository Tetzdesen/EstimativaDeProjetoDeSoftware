package com.br.estimativadeprojetodesoftware.command.projeto;

import com.br.estimativadeprojetodesoftware.presenter.projeto.DetalheProjetoPresenter;
import com.br.estimativadeprojetodesoftware.presenter.helpers.WindowManager;
import com.br.estimativadeprojetodesoftware.service.ProjetoService;
import com.br.estimativadeprojetodesoftware.view.projeto.DetalheProjetoView;

import javax.swing.*;
import com.br.estimativadeprojetodesoftware.command.Command;

public class AbrirDetalhesProjetoCommand implements Command {

    private final JDesktopPane desktop;
    private String nomeProjeto;
    private final ProjetoService projetoService;
    
    public AbrirDetalhesProjetoCommand(String nomeProjeto,ProjetoService projetoService, JDesktopPane desktop) {
        this.projetoService = projetoService;
        this.nomeProjeto = nomeProjeto;
        this.desktop = desktop;
    }

    public void setProjetoNome(String nomeProjeto) {
        this.nomeProjeto = nomeProjeto;
    }

    @Override
    public void execute() {
        if (nomeProjeto == null || nomeProjeto.isEmpty()) {
            throw new IllegalStateException("O nome do projeto não foi definido para este comando.");
        }

        String tituloJanela = "Detalhes do Projeto: " + nomeProjeto;
        WindowManager windowManager = WindowManager.getInstance();

        if (windowManager.isFrameAberto(tituloJanela)) {
            windowManager.bringToFront(tituloJanela);
        } else {
            DetalheProjetoView detalheView = new DetalheProjetoView();
            detalheView.setTitle(tituloJanela);
            new DetalheProjetoPresenter(detalheView, projetoService, nomeProjeto);
            desktop.add(detalheView);
            detalheView.setVisible(true);
            try {
                detalheView.setMaximum(true);
            } catch (Exception ignored) {
            }
        }
    }
}