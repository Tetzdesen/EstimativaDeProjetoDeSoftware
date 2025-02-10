package com.br.estimativadeprojetodesoftware.command;

import com.br.estimativadeprojetodesoftware.presenter.DetalheProjetoPresenter;
import com.br.estimativadeprojetodesoftware.repository.ProjetoRepositoryMock;
import com.br.estimativadeprojetodesoftware.view.DetalheProjetoView;

import javax.swing.*;

public class AbrirDetalhesProjetoProjetoCommand implements ProjetoCommand {
    private final ProjetoRepositoryMock repository;
    private final JDesktopPane desktop;
    private String projetoNome;

    public AbrirDetalhesProjetoProjetoCommand(ProjetoRepositoryMock repository, JDesktopPane desktop) {
        this.repository = repository;
        this.desktop = desktop;
    }

    public void setProjetoNome(String projetoNome) {
        this.projetoNome = projetoNome;
    }

    @Override
    public void execute() {
        if (projetoNome == null || projetoNome.isEmpty()) {
            throw new IllegalStateException("O nome do projeto não foi definido para este comando.");
        }

        DetalheProjetoView detalheView = new DetalheProjetoView();

        new DetalheProjetoPresenter(detalheView, repository, projetoNome);

        desktop.add(detalheView);
        detalheView.setTitle("Detalhes do Projeto: " + projetoNome);
        detalheView.setVisible(true);
        try {
            detalheView.setMaximum(true);
        } catch (Exception ignored) {
        }
    }
}
