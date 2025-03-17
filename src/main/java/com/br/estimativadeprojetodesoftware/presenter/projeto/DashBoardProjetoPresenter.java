package com.br.estimativadeprojetodesoftware.presenter.projeto;

import com.br.estimativadeprojetodesoftware.model.Projeto;
import com.br.estimativadeprojetodesoftware.presenter.Observer;
import com.br.estimativadeprojetodesoftware.service.EstimaProjetoService;
import com.br.estimativadeprojetodesoftware.service.ProjetoRepositoryService;
import com.br.estimativadeprojetodesoftware.singleton.UsuarioLogadoSingleton;
import com.br.estimativadeprojetodesoftware.view.projeto.DashBoardProjetoView;
import java.util.ArrayList;
import org.jfree.data.general.DefaultPieDataset;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class DashBoardProjetoPresenter implements Observer {

    private final DashBoardProjetoView view;
    private final EstimaProjetoService estimaService;
    private final ProjetoRepositoryService projetoService;

    public DashBoardProjetoPresenter(DashBoardProjetoView view, ProjetoRepositoryService projetoService) {
        this.view = view;
        this.projetoService = projetoService;
        this.estimaService = new EstimaProjetoService();

        this.projetoService.addObserver(this);
        carregarDashboard();
    }

    private void carregarDashboard() {
        List<String> idsProjetos = projetoService.buscarProjetosPorUsuario(UsuarioLogadoSingleton.getInstancia().getUsuario().getId());

        List<Projeto> projetos = new ArrayList<>();

        idsProjetos.forEach((projeto) -> projetos.add(projetoService.buscarPorId(UUID.fromString(projeto)).get()));

        int totalProjetos = projetos.size();
        int diasTotais = projetos.stream()
                .mapToInt(estimaService::calcularDiasTotais)
                .sum();
        double custoTotal = 0.0;
      //  double custoTotal = projetos.stream()
                //.mapToDouble(estimaService::calcularCusto)
              //  .sum();

        view.exibirDadosConsolidados(totalProjetos, diasTotais, custoTotal);

        DefaultPieDataset datasetCustos = gerarDatasetCustos(projetos);
        DefaultPieDataset datasetProjetos = gerarDatasetProjetos(projetos);

        view.atualizarGraficos(datasetCustos, datasetProjetos);
    }

    private DefaultPieDataset gerarDatasetCustos(List<Projeto> projetos) {
        DefaultPieDataset dataset = new DefaultPieDataset();
        for (Projeto projeto : projetos) {
            //    double custo = estimaService.calcularCusto(projeto);
            double custo = 0;
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
