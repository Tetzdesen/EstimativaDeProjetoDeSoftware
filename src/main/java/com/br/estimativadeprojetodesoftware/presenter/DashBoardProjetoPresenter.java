package com.br.estimativadeprojetodesoftware.presenter;

import com.br.estimativadeprojetodesoftware.model.Projeto;
import com.br.estimativadeprojetodesoftware.repository.ProjetoRepositoryMock;
import com.br.estimativadeprojetodesoftware.service.EstimaProjetoService;
import com.br.estimativadeprojetodesoftware.view.DashBoardProjetoView;
import org.jfree.data.general.DefaultPieDataset;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DashBoardProjetoPresenter implements Observer {

    private final DashBoardProjetoView view;
    private final EstimaProjetoService estimaService;
    private final ProjetoRepositoryMock repository;

    public DashBoardProjetoPresenter(DashBoardProjetoView view, ProjetoRepositoryMock repository) {
        this.view = view;
        this.repository = repository;
        this.estimaService = new EstimaProjetoService();

        this.repository.addObserver(this);
        carregarDashboard();
    }

    private void carregarDashboard() {
        List<Projeto> projetos = repository.getProjetos();

        int totalProjetos = projetos.size();
        int diasTotais = projetos.stream()
                .mapToInt(estimaService::calcularDiasTotais)
                .sum();
        double custoTotal = projetos.stream()
                .mapToDouble(estimaService::calcularCusto)
                .sum();

        view.exibirDadosConsolidados(totalProjetos, diasTotais, custoTotal);

        DefaultPieDataset datasetCustos = gerarDatasetCustos(projetos);
        DefaultPieDataset datasetProjetos = gerarDatasetProjetos(projetos);

        view.atualizarGraficos(datasetCustos, datasetProjetos);
    }

    private DefaultPieDataset gerarDatasetCustos(List<Projeto> projetos) {
        DefaultPieDataset dataset = new DefaultPieDataset();
        for (Projeto projeto : projetos) {
            double custo = estimaService.calcularCusto(projeto);
            dataset.setValue(projeto.getNome(), custo);
        }
        return dataset;
    }

    private DefaultPieDataset gerarDatasetProjetos(List<Projeto> projetos) {
        DefaultPieDataset dataset = new DefaultPieDataset();
        Map<String, Long> tipos = projetos.stream()
                .flatMap(projeto -> projeto.getPerfis().stream())
                .collect(Collectors.groupingBy(perfil -> perfil.getNome(), Collectors.counting())); 

        for (Map.Entry<String, Long> entry : tipos.entrySet()) {
            dataset.setValue(entry.getKey(), entry.getValue().doubleValue()); 
        }
        return dataset;
    }

    @Override
    public void update() {
        carregarDashboard();
    }
}
