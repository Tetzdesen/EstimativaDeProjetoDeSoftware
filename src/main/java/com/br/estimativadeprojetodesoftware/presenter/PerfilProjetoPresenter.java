package com.br.estimativadeprojetodesoftware.presenter;

import com.br.estimativadeprojetodesoftware.model.Perfil;
import com.br.estimativadeprojetodesoftware.model.Projeto;
import com.br.estimativadeprojetodesoftware.repository.PerfilRepositoryMock;
import com.br.estimativadeprojetodesoftware.repository.ProjetoRepositoryMock;
import com.br.estimativadeprojetodesoftware.service.EstimaProjetoService;
import com.br.estimativadeprojetodesoftware.view.DashBoardProjetoView;
import com.br.estimativadeprojetodesoftware.view.DetalheProjetoView;
import com.br.estimativadeprojetodesoftware.view.PerfilProjetoView;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jfree.data.general.DefaultPieDataset;

public class PerfilProjetoPresenter implements Observer {

    private final PerfilProjetoView view;
    private final PerfilRepositoryMock repository;

    public PerfilProjetoPresenter(PerfilProjetoView view, PerfilRepositoryMock repository) {
        this.view = view;
        this.repository = repository;

        //this.repository.addObserver(this);
        carregarDashboard();
    }

    private void carregarDashboard() {
        List<Perfil> projetos = repository.getPerfis();

        view.exibirDadosConsolidados();
    }

    @Override
    public void update(List<Projeto> projetos) {
        carregarDashboard();
    }
}
