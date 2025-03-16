package com.br.estimativadeprojetodesoftware.presenter.projeto;

import com.br.estimativadeprojetodesoftware.chain.calculoestimativa.EstimativaFuncionalidade;
import com.br.estimativadeprojetodesoftware.model.Perfil;
import com.br.estimativadeprojetodesoftware.model.Projeto;
import com.br.estimativadeprojetodesoftware.presenter.Observer;
import com.br.estimativadeprojetodesoftware.service.DataHoraService;
import com.br.estimativadeprojetodesoftware.service.EstimaProjetoService;
import com.br.estimativadeprojetodesoftware.service.ProjetoRepositoryService;
import com.br.estimativadeprojetodesoftware.state.projeto.NaoEstimadoState;
import com.br.estimativadeprojetodesoftware.view.projeto.DetalheProjetoView;
import java.awt.event.ActionEvent;

import java.util.*;
import javax.swing.JOptionPane;

public final class DetalheProjetoPresenter implements Observer {

    private final DetalheProjetoView view;
    private final EstimaProjetoService estimaService;
    private final ProjetoRepositoryService projetoService;
    private Projeto projeto;
    private List<EstimativaFuncionalidade> estimativas;
    private static final Map<String, Integer> ORDEM_ESTIMATIVAS = Map.of(
            "tamanho", 1,
            "nivel", 2,
            "funcionalidade", 3,
            "campo fixo", 4
    );

    public DetalheProjetoPresenter(DetalheProjetoView view, String projetoNome) {
        this.view = view;
        this.projetoService = new ProjetoRepositoryService();
        this.projeto = projetoService.buscarProjetoPorNome(projetoNome).get();
        projeto.setEstado(new NaoEstimadoState(projeto));
        this.estimaService = new EstimaProjetoService();
        this.estimativas = new ArrayList<>();
        configurarPresenter();
        carregarDetalhesProjeto();
    }

    public void configurarPresenter() {
        this.projetoService.addObserver(this);
        this.view.getBtnEstimar().setEnabled(true);
        this.view.getBtnCancelar().setEnabled(false);
        this.view.getBtnCompartilhar().setEnabled(false);
        this.view.getBtnExportar().setEnabled(false);
        configurarListeners();
    }

    private void carregarDetalhesProjeto() {
        // verificar se projeto é != null
        carregarCabecalho(projeto);
        carregarDetalhes(projeto);
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

        estimativas.sort(Comparator.comparing(estimativa -> ORDEM_ESTIMATIVAS.getOrDefault(estimativa.getTipoCampo(), Integer.MAX_VALUE)));

        double valorTotal = estimaService.calcularValorTotal(estimativas);

        Object[][] dadosTabela = prepararTabela(projeto, estimativas);
        List<String> perfis = extrairPerfis(projeto);

        view.atualizarTabela(dadosTabela, perfis, valorTotal, isProjetoEstimado());
    }
    
    PrepararTabelaProjetoProjetoCommand
    private Object[][] prepararTabela(Projeto projeto, List<EstimativaFuncionalidade> estimativas) {
        List<String> ordemCategorias = List.of("tamanho", "nivel", "funcionalidade", "taxa diária", "campo fixo");

        Map<String, String> mapeamentoNomes = Map.of(
                "tamanho", "Tamanho do Aplicativo",
                "nivel", "Nível da UI",
                "funcionalidade", "Funcionalidades",
                "taxa diária", "Taxas Diárias",
                "campo fixo", "Adicionais"
        );

        Map<String, List<EstimativaFuncionalidade>> categorias = new LinkedHashMap<>();

        for (EstimativaFuncionalidade estimativa : estimativas) {
            categorias.computeIfAbsent(estimativa.getTipoCampo(), k -> new ArrayList<>()).add(estimativa);
        }

        List<Object[]> linhasTabela = new ArrayList<>();
        List<String> perfis = extrairPerfis(projeto);

        for (String categoria : ordemCategorias) {
            if (categorias.containsKey(categoria)) {
                String nomeCategoria = mapeamentoNomes.getOrDefault(categoria, categoria.toUpperCase());
                Object[] linhaCategoria = new Object[perfis.size() + 3];
                linhaCategoria[0] = nomeCategoria;
                Arrays.fill(linhaCategoria, 1, linhaCategoria.length, "");
                linhasTabela.add(linhaCategoria);

                Set<String> funcionalidadesAdicionadas = new HashSet<>();

                for (EstimativaFuncionalidade estimativa : categorias.get(categoria)) {
                    String funcionalidade = estimativa.getNomeFuncionalidade();

                    if (funcionalidadesAdicionadas.contains(funcionalidade)) {
                        continue;
                    }
                    funcionalidadesAdicionadas.add(funcionalidade);

                    Object[] linha = new Object[perfis.size() + 3];
                    linha[0] = funcionalidade;
                    int totalDias = 0;
                    double valorPorDia = estimativa.getValorPorDia();

                    for (int i = 0; i < perfis.size(); i++) {
                        int dias = estimativa.getQuantidadeDias();
                        linha[i + 1] = dias;
                        totalDias += dias;
                    }

                    // 🛠️ Correção: Agora o Valor Total usa o totalDias * valorPorDia
                    linha[perfis.size() + 1] = totalDias;
                    linha[perfis.size() + 2] = "R$ " + String.format("%.2f", totalDias * valorPorDia);

                    linhasTabela.add(linha);
                }
            }
        }

        return linhasTabela.toArray(new Object[0][]);
    }

    private List<String> extrairPerfis(Projeto projeto) {
        List<String> perfis = new ArrayList<>();
        for (Perfil perfil : projeto.getPerfis()) {
            if (!perfis.contains(perfil.getNome())) {
                perfis.add(perfil.getNome());
            }
        }
        return perfis;
    }

    @Override
    public void update() {
        carregarDetalhesProjeto();
    }

    private void configurarListeners() {

        view.getBtnEstimar().addActionListener((ActionEvent e) -> {
            if (projeto != null) {
                
                RealizarEstimativaProjetoProjetoCommand
                projeto.estimarProjeto();
                carregarDetalhesProjeto();
                atualizarEstadoBotoes(projeto);
                JOptionPane.showMessageDialog(view, "Projeto estimado com sucesso!", "Estimativa", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        view.getBtnCancelar().addActionListener((ActionEvent e) -> {
            if (projeto != null) {
                
                CancelarEstimativaProjetoProjetoCommand
                projeto.cancelarEstimativa();
                carregarDetalhesProjeto();
                atualizarEstadoBotoes(projeto);
                JOptionPane.showMessageDialog(view, "Estimativa cancelada!", "Cancelamento", JOptionPane.WARNING_MESSAGE);
            }
        });

        view.getBtnCompartilhar().addActionListener((ActionEvent e) -> {
            if (projeto != null) {
                
                CompartilharProjetoProjetoCommand

            }
        });

        view.getBtnExportar().addActionListener((ActionEvent e) -> {
            if (projeto != null) {
                
                ExportarProjetoProjetoCommand
                // logica para exportar
            }
        });

    }

    private void atualizarEstadoBotoes(Projeto projeto) {
        boolean isEstimado = isProjetoEstimado();

        view.getBtnEstimar().setEnabled(!isEstimado);
        view.getBtnCancelar().setEnabled(isEstimado);
        view.getBtnCompartilhar().setEnabled(isEstimado);
        view.getBtnExportar().setEnabled(isEstimado);
    }

    private boolean isProjetoEstimado() {
        return projeto.getStatus().equalsIgnoreCase("Estimado");
    }
}
