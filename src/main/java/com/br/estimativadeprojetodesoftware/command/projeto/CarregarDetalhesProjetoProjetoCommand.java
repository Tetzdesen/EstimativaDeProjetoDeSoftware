package com.br.estimativadeprojetodesoftware.command.projeto;

import com.br.estimativadeprojetodesoftware.chain.calculoestimativa.EstimativaFuncionalidade;
import com.br.estimativadeprojetodesoftware.command.ProjetoCommand;
import com.br.estimativadeprojetodesoftware.model.PerfilProjeto;
import com.br.estimativadeprojetodesoftware.model.Projeto;
import com.br.estimativadeprojetodesoftware.service.EstimaProjetoService;
import com.br.estimativadeprojetodesoftware.view.projeto.DetalheProjetoView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author tetzner
 */
public class CarregarDetalhesProjetoProjetoCommand implements ProjetoCommand {

    private final DetalheProjetoView view;
    private final Projeto projeto;
    private List<EstimativaFuncionalidade> estimativas;
    private final EstimaProjetoService estimaService;
    private final boolean isProjetoEstimado;

    private static final Map<String, Integer> ORDEM_ESTIMATIVAS = Map.of(
            "tamanho", 1,
            "nivel", 2,
            "funcionalidade", 3,
            "campo fixo", 4
    );

    public CarregarDetalhesProjetoProjetoCommand(DetalheProjetoView view, Projeto projeto, boolean isProjetoEstimado) {
        this.view = view;
        this.projeto = projeto;
        this.estimativas = new ArrayList<>();
        this.estimaService = new EstimaProjetoService();
        this.isProjetoEstimado = isProjetoEstimado;
    }

    @Override
    public void execute() {
        carregarDetalhes();
    }

    private void carregarDetalhes() {
        
        estimativas = estimaService.calcularEstimativas(projeto.getId(), projeto.getPerfis(), projeto.getCampos());

        estimativas.sort(Comparator.comparing(estimativa -> ORDEM_ESTIMATIVAS.getOrDefault(estimativa.getTipoCampo(), Integer.MAX_VALUE)));

        double valorTotal = estimaService.calcularValorTotal(estimativas);

        Object[][] dadosTabela = prepararTabela(projeto, estimativas);
        List<String> perfis = extrairPerfis(projeto);

        view.atualizarTabela(dadosTabela, perfis, valorTotal, isProjetoEstimado);
    }

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

                    if (estimativa.getTipoCampo().equalsIgnoreCase("nivel")) {
                        totalDias = (int) estimaService.calcularTotalNivelUI(estimativas); 
                    }

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
        for (PerfilProjeto perfil : projeto.getPerfis()) {
            if (!perfis.contains(perfil.getNome())) {
                perfis.add(perfil.getNome());
            }
        }
        return perfis;
    }
}
