package com.br.estimativadeprojetodesoftware.presenter;

import com.br.estimativadeprojetodesoftware.model.Perfil;
import com.br.estimativadeprojetodesoftware.model.Projeto;
import com.br.estimativadeprojetodesoftware.repository.ProjetoRepositoryMock;
import com.br.estimativadeprojetodesoftware.service.EstimaProjetoService;
import com.br.estimativadeprojetodesoftware.view.DetalheProjetoView;

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
                .map(Perfil::getNome)
                .collect(Collectors.joining(", "));

        view.atualizarCabecalho(
                projeto.getNome(),
                projeto.getCriador(),
                projeto.getCreated_at().toString(),
                tiposConcatenados,
                projeto.getStatus()
        );
    }

    private void carregarDetalhes(Projeto projeto) {
        Object[][] dadosTabela = projeto.getEstimativa().getCampos()
                .entrySet()
                .stream()
                .map(entry -> {
                    String nomeFuncionalidade = entry.getKey();
                    int dias = entry.getValue();
                    
                    Perfil perfil = projeto.getPerfis().isEmpty() ? null : projeto.getPerfis().get(0);

                    double valor = (perfil != null) ? estimaService.calcularValorUnitario(perfil.getNome(), dias) : 0.0;

                    return new Object[]{nomeFuncionalidade, dias, String.format("R$ %.2f", valor)};
                })
                .toArray(Object[][]::new);

        double valorTotal = calcularValorTotal(projeto);
        view.atualizarTabela(dadosTabela, valorTotal);
    }

    private double calcularValorTotal(Projeto projeto) {
        return projeto.getEstimativa().getCampos()
                .entrySet()
                .stream()
                .mapToDouble(entry -> {
                    int dias = entry.getValue();
                    
                    Perfil perfil = projeto.getPerfis().isEmpty() ? null : projeto.getPerfis().get(0);

                    return (perfil != null) ? estimaService.calcularValorUnitario(perfil.getNome(), dias) : 0.0;
                })
                .sum();
    }

    @Override
    public void update(List<Projeto> projetos) {
        carregarDetalhesProjeto();
    }
}
