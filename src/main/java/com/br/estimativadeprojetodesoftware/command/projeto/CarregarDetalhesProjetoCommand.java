package com.br.estimativadeprojetodesoftware.command.projeto;

import com.br.estimativadeprojetodesoftware.chain.calculoestimativa.EstimativaFuncionalidade;
import com.br.estimativadeprojetodesoftware.model.Campo;
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
import com.br.estimativadeprojetodesoftware.command.Command;

/**
 *
 * @author tetzner
 */
public class CarregarDetalhesProjetoCommand implements Command {

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

    public CarregarDetalhesProjetoCommand(DetalheProjetoView view, Projeto projeto, boolean isProjetoEstimado) {
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
        
        estimativas = estimaService.calcularEstimativas(projeto);

        estimativas.sort(Comparator.comparing(estimativa -> ORDEM_ESTIMATIVAS.getOrDefault(estimativa.getTipoCampo(), Integer.MAX_VALUE)));

        double valorTotal = estimaService.calcularValorTotal(estimativas);

        Object[][] dadosTabela = prepararTabela(projeto, estimativas);
        List<String> perfis = extrairPerfis(projeto);

        view.atualizarTabela(dadosTabela, perfis, valorTotal, isProjetoEstimado);
    }

    private Object[][] prepararTabela(Projeto projeto, List<EstimativaFuncionalidade> estimativas) {
        // 1) Definições para ordem e nomes
        List<String> ordemCategorias = List.of("tamanho", "nivel", "funcionalidade", "taxa diária", "campo fixo");
        Map<String, String> mapeamentoNomes = Map.of(
                "tamanho", "Tamanho do Aplicativo",
                "nivel", "Nível da UI",
                "funcionalidade", "Funcionalidades",
                "taxa diária", "Taxas Diárias",
                "campo fixo", "Adicionais"
        );
    
        // 2) Agrupa as estimativas em um mapa aninhado
        Map<String, Map<String, Map<String, EstimativaFuncionalidade>>> agrupado = new LinkedHashMap<>();
        for (EstimativaFuncionalidade e : estimativas) {
            agrupado
                .computeIfAbsent(e.getTipoCampo(), k -> new LinkedHashMap<>())
                .computeIfAbsent(e.getNomeFuncionalidade(), k -> new LinkedHashMap<>())
                .put(e.getPerfilNome(), e);
        }
    
        // 3) Monta as linhas da tabela
        List<String> perfis = extrairPerfis(projeto);
        List<Object[]> linhasTabela = new ArrayList<>();
    
        for (String categoria : ordemCategorias) {
            if (!agrupado.containsKey(categoria)) {
                continue;
            }
            
            // Linha de "cabeçalho" da categoria
            String nomeCategoria = mapeamentoNomes.getOrDefault(categoria, categoria.toUpperCase());
            Object[] linhaCategoria = new Object[perfis.size() + 3];
            linhaCategoria[0] = nomeCategoria;
            Arrays.fill(linhaCategoria, 1, linhaCategoria.length, "");
            linhasTabela.add(linhaCategoria);
    
            // Pega o mapa de (nomeFuncionalidade -> (perfil -> EstimativaFuncionalidade))
            Map<String, Map<String, EstimativaFuncionalidade>> mapaPorFunc = agrupado.get(categoria);
    
            // Para cada funcionalidade dentro dessa categoria
            for (String nomeFunc : mapaPorFunc.keySet()) {
                Object[] linha = new Object[perfis.size() + 3];
                linha[0] = nomeFunc;
    
                int totalDias = 0;
                double valorPorDia = 0.0;
    
                // Para cada perfil, tenta achar a estimativa correspondente
                for (int i = 0; i < perfis.size(); i++) {
                    String perfilNome = perfis.get(i);
                    EstimativaFuncionalidade est = mapaPorFunc.get(nomeFunc).get(perfilNome);
    
                    if (est != null) {
                        int dias = est.getQuantidadeDias();
                        linha[i + 1] = dias;
                        totalDias += dias;
                        valorPorDia = est.getValorPorDia();
                    } else {
                        linha[i + 1] = 0; // ou ""
                    }
                }
    
                // Se for "nivel", faça seu ajuste de totalDias se necessário
                // (por exemplo, totalDias = (int) estimaService.calcularTotalNivelUI(estimativas))
                if (categoria.equalsIgnoreCase("nivel")) {
                    totalDias = (int) estimaService.calcularTotalNivelUI(estimativas);
                }
    
                linha[perfis.size() + 1] = totalDias;
                linha[perfis.size() + 2] = "R$ " + String.format("%.2f", totalDias * valorPorDia);
    
                linhasTabela.add(linha);
            }
        }
    
        // 4) Retorna em formato de array bidimensional
        return linhasTabela.toArray(new Object[0][]);
    }
    

    // private List<Campo> conversorParaCampos() {
    //     List<Campo> campos = new ArrayList<>();

    //     projeto.getPerfis().forEach(perfil -> {
    //         for (Map.Entry<String, Integer> entry : perfil.getTamanhosApp().entrySet()) {
    //             campos.add(new Campo("tamanho", entry.getKey(), entry.getValue()));
    //         }
    //         for (Map.Entry<String, Double> entry : perfil.getNiveisUI().entrySet()) {
    //             campos.add(new Campo("nivel", entry.getKey(), entry.getValue()));
    //         }
    //         for (Map.Entry<String, Integer> entry : perfil.getFuncionalidades().entrySet()) {
    //             campos.add(new Campo("funcionalidade", entry.getKey(), entry.getValue()));
    //         }
    //         for (Map.Entry<String, Double> entry : perfil.getTaxasDiarias().entrySet()) {
    //             campos.add(new Campo("taxa diária", entry.getKey(), entry.getValue()));
    //         }
    //     });

    //     return campos;
    // }

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
