package com.br.estimativadeprojetodesoftware.presenter;

import br.projeto.model.Projeto;
import br.projeto.repository.ProjetoRepositoryMock;
import br.projeto.service.EstimaProjetoService;
import br.projeto.view.DetalheProjetoView;

import java.util.List;
import java.util.stream.Collectors;

public class DetalheProjetoPresenter implements Observer {
    private final DetalheProjetoView view;
    private final EstimaProjetoService estimaService;
    private final ProjetoRepositoryMock repository;
    private final String projetoNome;

    public DetalheProjetoPresenter(DetalheProjetoView view, ProjetoRepositoryMock repository, String projetoNome) {
        this.view = view;
        this.repository = repository;
        this.projetoNome = projetoNome;
        this.estimaService = new EstimaProjetoService();

        this.repository.addObserver(this);
        carregarDetalhesProjeto();
    }

    private void carregarDetalhesProjeto() {
        Projeto projeto = repository.getProjetoPorNome(projetoNome);
        if (projeto != null) {
            carregarCabecalho(projeto);
            carregarDetalhes(projeto);
        }
    }

    private void carregarCabecalho(Projeto projeto) {
        String tiposConcatenados = projeto.getPerfis().stream()
                .collect(Collectors.joining(", "));

        view.atualizarCabecalho(
                projeto.getNome(),
                projeto.getCriador(),
                projeto.getDataCriacao(),
                tiposConcatenados,
                projeto.getStatus()
        );
    }

    private void carregarDetalhes(Projeto projeto) {
        Object[][] dadosTabela = projeto.getFuncionalidadesEscolhidas()
                .entrySet()
                .stream()
                .map(entry -> {
                    String nomeFuncionalidade = entry.getKey();
                    int dias = entry.getValue();
                    double valor = estimaService.calcularValorUnitario(projeto.getPerfis().get(0), dias);
                    return new Object[]{nomeFuncionalidade, dias, String.format("R$ %.2f", valor)};
                })
                .toArray(Object[][]::new);

        double valorTotal = calcularValorTotal(projeto);
        view.atualizarTabela(dadosTabela, valorTotal);
    }

    private double calcularValorTotal(Projeto projeto) {
        return projeto.getFuncionalidadesEscolhidas()
                .entrySet()
                .stream()
                .mapToDouble(entry -> {
                    int dias = entry.getValue();
                    return estimaService.calcularValorUnitario(projeto.getPerfis().get(0), dias);
                })
                .sum();
    }

    @Override
    public void update(List<Projeto> projetos) {
        carregarDetalhesProjeto();
    }
}
