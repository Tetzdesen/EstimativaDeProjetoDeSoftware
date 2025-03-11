package com.br.estimativadeprojetodesoftware.presenter.projeto;

import com.br.estimativadeprojetodesoftware.model.Perfil;
import com.br.estimativadeprojetodesoftware.model.Projeto;
import com.br.estimativadeprojetodesoftware.presenter.Observer;
import com.br.estimativadeprojetodesoftware.repository.ProjetoRepositoryMock;
import com.br.estimativadeprojetodesoftware.service.DataHoraService;
import com.br.estimativadeprojetodesoftware.service.EstimaProjetoService;
import com.br.estimativadeprojetodesoftware.view.projeto.DetalheProjetoView;

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
        view.atualizarCabecalho(
                projeto.getNome(),
                projeto.getCriador(),
                DataHoraService.formatarData(projeto.getCreated_at().toLocalDate()),
                projeto.getTipo(),
                projeto.getStatus()
        );
    }

    private void carregarDetalhes(Projeto projeto) {
        int numPerfis = projeto.getPerfis().size();
        
        // Mapeia os dados: cada linha terá 2 colunas fixas + uma coluna para cada perfil
        Object[][] dadosTabela = projeto.getEstimativa().getCampos()
                .entrySet()
                .stream()
                .map(entry -> {
                    String nomeFuncionalidade = entry.getKey();
                    int dias = entry.getValue();
                    
                    // Cria uma linha com tamanho dinâmico: nome, dias e um valor para cada perfil
                    Object[] row = new Object[2 + numPerfis];
                    row[1] = nomeFuncionalidade;
                    row[2] = dias;
                    
                    // Para cada perfil, calcula o valor e o formata
                    for (int i = 0; i < numPerfis; i++) {
                        Perfil perfil = projeto.getPerfis().get(i);
                        double valor = estimaService.calcularValorUnitario(perfil.getNome(), dias);
                        row[i + 3] = String.format("R$ %.2f", valor);
                    }
                    return row;
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
    public void update() {
        carregarDetalhesProjeto();
    }
}
