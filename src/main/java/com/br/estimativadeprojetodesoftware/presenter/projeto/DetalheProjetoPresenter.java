package com.br.estimativadeprojetodesoftware.presenter.projeto;

import com.br.estimativadeprojetodesoftware.chain.calculoestimativa.EstimativaFuncionalidade;
import com.br.estimativadeprojetodesoftware.model.Perfil;
import java.util.ArrayList;
import java.util.List;

import com.br.estimativadeprojetodesoftware.model.Projeto;
import com.br.estimativadeprojetodesoftware.presenter.Observer;
import com.br.estimativadeprojetodesoftware.service.DataHoraService;
import com.br.estimativadeprojetodesoftware.service.EstimaProjetoService;
import com.br.estimativadeprojetodesoftware.service.ProjetoRepositoryService;
import com.br.estimativadeprojetodesoftware.view.projeto.DetalheProjetoView;

public class DetalheProjetoPresenter implements Observer {

    private final DetalheProjetoView view;
    private final EstimaProjetoService estimaService;
    private final ProjetoRepositoryService projetoService;
    private final String projetoNome;
    List<EstimativaFuncionalidade> estimativas;

    public DetalheProjetoPresenter(DetalheProjetoView view, String projetoNome) {
        this.view = view;
        this.projetoService = new ProjetoRepositoryService();
        this.projetoNome = projetoNome;
        this.estimaService = new EstimaProjetoService();
        this.estimativas = new ArrayList<>();
        this.projetoService.addObserver(this);
        carregarDetalhesProjeto();
    }

    private void carregarDetalhesProjeto() {
        Projeto projeto = projetoService.buscarProjetoPorNome(projetoNome).get();
        if (projeto != null) {
            carregarCabecalho(projeto);
            carregarDetalhes(projeto);
        }
    }

    private void carregarCabecalho(Projeto projeto) {
        view.atualizarCabecalho(
                projeto.getNome(),
                projeto.getCriador(),
                DataHoraService.formatarData(projeto.getCreated_at().toLocalDate()),
                projeto.getTipo(),
                projeto.getStatus()
        );
    }

    private void carregarDetalhes(Projeto projeto) {
        estimativas = estimaService.calcularEstimativas(projeto.getId(), projeto.getPerfis(), projeto.getCampos());
        // Converte a lista para array
        Object[][] dadosTabela = prepararTabela(projeto, estimativas);

        // Atualiza a view com o valor total
        view.atualizarTabela(dadosTabela, estimaService.calcularValorTotal());
    }

    private Object[][] prepararTabela(Projeto projeto, List<EstimativaFuncionalidade> estimativas) {
        List<Object[]> linhas = new ArrayList<>();

        for (EstimativaFuncionalidade estimativa : estimativas) {
            for (Perfil perfil : projeto.getPerfis()) {
                // Adiciona cada linha na tabela com os valores de estimativa
                linhas.add(new Object[]{
                    perfil.getNome(),
                    estimativa.getNomeFuncionalidade(),
                    estimativa.getQuantidadeDias(),
                    "R$ " + estimativa.calcularCustoTotal()
                });
            }
        }

        return linhas.toArray(new Object[0][]);
    }

    /*
    private double calcularValorTotal() {
        return estimativas.stream()
                .mapToDouble(EstimativaFuncionalidade::calcularCustoTotal)
                .sum();
    }*/
    @Override
    public void update() {
        carregarDetalhesProjeto();
    }
}
