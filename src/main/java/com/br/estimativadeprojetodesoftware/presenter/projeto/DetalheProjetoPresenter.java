package com.br.estimativadeprojetodesoftware.presenter.projeto;

import java.util.ArrayList;
import java.util.List;

import com.br.estimativadeprojetodesoftware.model.Perfil;
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

    public DetalheProjetoPresenter(DetalheProjetoView view, String projetoNome) {
        this.view = view;
        this.projetoService = new ProjetoRepositoryService();
        this.projetoNome = projetoNome;
        this.estimaService = new EstimaProjetoService();

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
    // Cria uma lista para acumular as linhas da tabela
        List<Object[]> linhas = new ArrayList<>();
        
        for (Perfil perfil : projeto.getPerfis()) {
            // Para cada perfil, cria as linhas a partir das funcionalidades
            Object[][] dadosPerfil = perfil.getFuncionalidades()
                    .entrySet()
                    .stream()
                    .map(entry -> new Object[]{ perfil.getNome(), entry.getKey(), entry.getValue() })
                    .toArray(Object[][]::new);
            
            // Adiciona cada linha do perfil à lista geral
            for (Object[] linha : dadosPerfil) {
                linhas.add(linha);
            }
        }
        
        // Converte a lista em um array bidimensional
        Object[][] dadosTabela = linhas.toArray(new Object[0][]);
        double valorTotal = calcularValorTotal(projeto);
        
        // Atualiza a tabela com todas as linhas acumuladas
        view.atualizarTabela(dadosTabela, valorTotal);
    }

    private double calcularValorTotal(Projeto projeto) {
        return projeto.getCampos()
                .stream()
                .mapToDouble(campo -> {
                    Integer dias = campo.getDias().intValue();
                    Perfil perfil = projeto.getPerfis().isEmpty() ? null : projeto.getPerfis().get(0);
                    return (perfil != null) ? estimaService.calcularValorUnitario(perfil.getNome(), dias) : 0.0;
                })
                .sum();
    }

    @Override
    public void update() {
        carregarDetalhesProjeto();
    }
}
