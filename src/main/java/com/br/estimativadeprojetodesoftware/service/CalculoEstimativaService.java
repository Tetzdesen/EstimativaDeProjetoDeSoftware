package com.br.estimativadeprojetodesoftware.service;

import com.br.estimativadeprojetodesoftware.model.CustoAdicional;
import com.br.estimativadeprojetodesoftware.model.Funcionalidade;
import com.br.estimativadeprojetodesoftware.model.ItemProjeto;
import com.br.estimativadeprojetodesoftware.model.Papel;
import com.br.estimativadeprojetodesoftware.model.Plataforma;
import com.br.estimativadeprojetodesoftware.model.Projeto;
import com.br.estimativadeprojetodesoftware.model.ResultadoEstimativa;
import com.br.estimativadeprojetodesoftware.model.ResultadoEstimativa.DetalhePlataforma;
import com.br.estimativadeprojetodesoftware.model.ResultadoEstimativa.LinhaFuncionalidade;
import com.br.estimativadeprojetodesoftware.model.ResultadoEstimativa.ResumoCategoria;
import com.br.estimativadeprojetodesoftware.model.TaxaDiaria;
import com.br.estimativadeprojetodesoftware.model.TipoSelecao;
import com.br.estimativadeprojetodesoftware.model.TipoValor;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Cálculo fiel à planilha da Tabela 1 (algoritmo do design.md). Todos os
 * valores em BigDecimal com MathContext.DECIMAL128, sem arredondamento
 * intermediário.
 *
 * <pre>
 * fatorUI = (soma dos percentuais de UI das plataformas marcadas) / 3
 * para cada plataforma marcada p:
 *   base_p = dias do Tamanho do App selecionado
 *   ui_p   = base_p × fatorUI
 *   func_p = dias das funcionalidades selecionadas aplicáveis + dias específicos
 *   gp_p   = dias de gerência (se Gerente de Projeto selecionado)
 *   custo_p = (base_p + func_p) × taxaDev + ui_p × taxaDesign + gp_p × taxaGerencia
 * </pre>
 */
public class CalculoEstimativaService {

    public static final MathContext MC = MathContext.DECIMAL128;
    private static final BigDecimal TRES = new BigDecimal(3);
    private static final BigDecimal TRINTA = new BigDecimal(30);

    public ResultadoEstimativa calcular(Projeto projeto) {
        validar(projeto);
        Set<Plataforma> marcadas = projeto.getPlataformas();
        TaxaDiaria taxas = projeto.getTaxas();
        List<ItemProjeto> itens = projeto.getItens();

        // Nível de UI: soma dos percentuais selecionados nas plataformas marcadas ÷ 3
        BigDecimal somaPercentuais = BigDecimal.ZERO;
        for (ItemProjeto item : itens) {
            Funcionalidade f = item.getFuncionalidade();
            if (item.isSelecionado() && f.getTipoValor() == TipoValor.PERCENTUAL) {
                for (Plataforma p : marcadas) {
                    somaPercentuais = somaPercentuais.add(f.getValorOuZero(p), MC);
                }
            }
        }
        BigDecimal fatorUI = somaPercentuais.divide(TRES, MC);

        Map<Plataforma, DetalhePlataforma> porPlataforma = new EnumMap<>(Plataforma.class);
        Map<String, BigDecimal[]> categorias = new LinkedHashMap<>();
        Map<String, LinhaFuncionalidade> linhas = new LinkedHashMap<>();

        for (Plataforma p : marcadas) {
            BigDecimal base = BigDecimal.ZERO;
            BigDecimal func = BigDecimal.ZERO;
            BigDecimal gp = BigDecimal.ZERO;
            BigDecimal taxaDev = taxas.get(p, Papel.DESENVOLVIMENTO);
            BigDecimal taxaDesign = taxas.get(p, Papel.DESIGN);
            BigDecimal taxaGerencia = taxas.get(p, Papel.GERENCIA);
            String categoriaUI = null;

            for (ItemProjeto item : itens) {
                Funcionalidade f = item.getFuncionalidade();
                if (f.getTipoValor() == TipoValor.PERCENTUAL) {
                    if (item.isSelecionado()) {
                        categoriaUI = f.getCategoria();
                    }
                    continue;
                }
                boolean conta = f.getTipoSelecao() == TipoSelecao.LIVRE || item.isSelecionado();
                if (!conta || !f.isAplicavel(p)) {
                    continue;
                }
                BigDecimal dias = f.getValorOuZero(p);
                if (f.getTipoSelecao() == TipoSelecao.LIVRE && dias.signum() == 0) {
                    continue;
                }
                BigDecimal custo;
                if (f.getTipoValor() == TipoValor.DIAS_GERENCIA) {
                    gp = gp.add(dias, MC);
                    custo = dias.multiply(taxaGerencia, MC);
                } else if (f.getTipoSelecao() == TipoSelecao.UNICA) {
                    base = base.add(dias, MC);
                    custo = dias.multiply(taxaDev, MC);
                } else {
                    func = func.add(dias, MC);
                    custo = dias.multiply(taxaDev, MC);
                }
                acumular(categorias, f.getCategoria(), dias, custo);
                linhas.computeIfAbsent(f.getId().toString(),
                        k -> new LinhaFuncionalidade(f.getCategoria(), f.getNome(), new EnumMap<>(Plataforma.class)))
                        .diasPorPlataforma().put(p, dias);
            }

            BigDecimal ui = base.multiply(fatorUI, MC);
            if (categoriaUI != null) {
                acumular(categorias, categoriaUI, ui, ui.multiply(taxaDesign, MC));
            }
            BigDecimal diasP = base.add(ui, MC).add(func, MC).add(gp, MC);
            BigDecimal custoP = base.add(func, MC).multiply(taxaDev, MC)
                    .add(ui.multiply(taxaDesign, MC), MC)
                    .add(gp.multiply(taxaGerencia, MC), MC);
            porPlataforma.put(p, new DetalhePlataforma(p, base, ui, func, gp, diasP, custoP));
        }

        // linha do Nível de UI no detalhamento
        for (ItemProjeto item : itens) {
            Funcionalidade f = item.getFuncionalidade();
            if (item.isSelecionado() && f.getTipoValor() == TipoValor.PERCENTUAL) {
                Map<Plataforma, BigDecimal> dias = new EnumMap<>(Plataforma.class);
                porPlataforma.forEach((p, d) -> dias.put(p, d.diasUI()));
                linhas.put(f.getId().toString(), new LinhaFuncionalidade(f.getCategoria(), f.getNome(), dias));
            }
        }

        BigDecimal diasTotal = BigDecimal.ZERO;
        BigDecimal custoDev = BigDecimal.ZERO;
        for (DetalhePlataforma d : porPlataforma.values()) {
            diasTotal = diasTotal.add(d.dias(), MC);
            custoDev = custoDev.add(d.custo(), MC);
        }
        BigDecimal adicionais = BigDecimal.ZERO;
        for (CustoAdicional c : projeto.getCustosAdicionais()) {
            adicionais = adicionais.add(c.getValor(), MC);
        }
        BigDecimal custoTotal = custoDev.add(adicionais, MC);
        BigDecimal imposto = custoTotal.multiply(projeto.getPercentualImpostos(), MC);
        BigDecimal valorComImposto = custoTotal.add(imposto, MC);
        BigDecimal lucro = valorComImposto.multiply(projeto.getPercentualLucro(), MC);
        BigDecimal precoFinal = valorComImposto.add(lucro, MC);
        BigDecimal meses = diasTotal.divide(TRINTA, MC);
        BigDecimal mediaMensal = meses.signum() == 0 ? BigDecimal.ZERO : precoFinal.divide(meses, MC);

        // resumo na ordem em que as categorias aparecem no projeto
        Map<String, ResumoCategoria> resumo = new LinkedHashMap<>();
        for (ItemProjeto item : itens) {
            String nome = item.getFuncionalidade().getCategoria();
            BigDecimal[] v = categorias.get(nome);
            if (v != null && !resumo.containsKey(nome)) {
                resumo.put(nome, new ResumoCategoria(nome, v[0], v[1]));
            }
        }
        List<LinhaFuncionalidade> linhasOrdenadas = new ArrayList<>();
        for (ItemProjeto item : itens) {
            LinhaFuncionalidade l = linhas.get(item.getFuncionalidade().getId().toString());
            if (l != null) {
                linhasOrdenadas.add(l);
            }
        }

        return new ResultadoEstimativa(porPlataforma, resumo, linhasOrdenadas, diasTotal, custoDev,
                adicionais, custoTotal, imposto, valorComImposto, lucro, precoFinal, meses, mediaMensal,
                projeto.getPercentualImpostos(), projeto.getPercentualLucro());
    }

    /**
     * Regras de seleção: ao menos uma plataforma e no máximo um item marcado
     * por categoria de escolha única.
     */
    public void validar(Projeto projeto) {
        if (projeto.getPlataformas().isEmpty()) {
            throw new IllegalArgumentException("Marque ao menos uma plataforma (Web/Back-end, iOS ou Android) para calcular a estimativa.");
        }
        Map<String, Integer> unicas = new LinkedHashMap<>();
        for (ItemProjeto item : projeto.getItens()) {
            Funcionalidade f = item.getFuncionalidade();
            if (item.isSelecionado() && f.getTipoSelecao() == TipoSelecao.UNICA) {
                unicas.merge(f.getCategoria(), 1, Integer::sum);
            }
        }
        unicas.forEach((categoria, qtd) -> {
            if (qtd > 1) {
                throw new IllegalArgumentException("A categoria \"" + categoria + "\" aceita apenas uma opção selecionada.");
            }
        });
    }

    private static void acumular(Map<String, BigDecimal[]> categorias, String categoria, BigDecimal dias, BigDecimal custo) {
        BigDecimal[] v = categorias.computeIfAbsent(categoria, k -> new BigDecimal[]{BigDecimal.ZERO, BigDecimal.ZERO});
        v[0] = v[0].add(dias, MC);
        v[1] = v[1].add(custo, MC);
    }
}
