package com.br.estimativadeprojetodesoftware.model;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Collections;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Resultado do cálculo. Todos os valores são exatos (BigDecimal sem
 * arredondamento intermediário); o arredondamento só acontece nos métodos de exibição.
 */
public class ResultadoEstimativa {

    /** Parcelas de uma plataforma. */
    public record DetalhePlataforma(Plataforma plataforma, BigDecimal diasTamanho, BigDecimal diasUI,
            BigDecimal diasFuncionalidades, BigDecimal diasGerencia, BigDecimal dias, BigDecimal custo) {
    }

    /** Totais de uma categoria (somados em todas as plataformas marcadas). */
    public record ResumoCategoria(String categoria, BigDecimal dias, BigDecimal custo) {
    }

    /** Uma funcionalidade selecionada e os dias que ela soma em cada plataforma. */
    public record LinhaFuncionalidade(String categoria, String nome, Map<Plataforma, BigDecimal> diasPorPlataforma) {
    }

    private final Map<Plataforma, DetalhePlataforma> porPlataforma;
    private final Map<String, ResumoCategoria> porCategoria;
    private final List<LinhaFuncionalidade> funcionalidadesSelecionadas;
    private final BigDecimal diasTotal;
    private final BigDecimal custoDesenvolvimento;
    private final BigDecimal totalCustosAdicionais;
    private final BigDecimal custoTotal;
    private final BigDecimal imposto;
    private final BigDecimal valorComImposto;
    private final BigDecimal lucro;
    private final BigDecimal precoFinal;
    private final BigDecimal meses;
    private final BigDecimal mediaMensal;
    private final BigDecimal percentualImpostos;
    private final BigDecimal percentualLucro;

    public ResultadoEstimativa(Map<Plataforma, DetalhePlataforma> porPlataforma, Map<String, ResumoCategoria> porCategoria,
            List<LinhaFuncionalidade> funcionalidadesSelecionadas, BigDecimal diasTotal, BigDecimal custoDesenvolvimento,
            BigDecimal totalCustosAdicionais, BigDecimal custoTotal, BigDecimal imposto, BigDecimal valorComImposto,
            BigDecimal lucro, BigDecimal precoFinal, BigDecimal meses, BigDecimal mediaMensal,
            BigDecimal percentualImpostos, BigDecimal percentualLucro) {
        this.porPlataforma = Collections.unmodifiableMap(new EnumMap<>(porPlataforma));
        this.porCategoria = Collections.unmodifiableMap(new LinkedHashMap<>(porCategoria));
        this.funcionalidadesSelecionadas = List.copyOf(funcionalidadesSelecionadas);
        this.diasTotal = diasTotal;
        this.custoDesenvolvimento = custoDesenvolvimento;
        this.totalCustosAdicionais = totalCustosAdicionais;
        this.custoTotal = custoTotal;
        this.imposto = imposto;
        this.valorComImposto = valorComImposto;
        this.lucro = lucro;
        this.precoFinal = precoFinal;
        this.meses = meses;
        this.mediaMensal = mediaMensal;
        this.percentualImpostos = percentualImpostos;
        this.percentualLucro = percentualLucro;
    }

    // ---------- valores exatos ----------

    public Map<Plataforma, DetalhePlataforma> getPorPlataforma() {
        return porPlataforma;
    }

    public Map<String, ResumoCategoria> getPorCategoria() {
        return porCategoria;
    }

    public List<LinhaFuncionalidade> getFuncionalidadesSelecionadas() {
        return funcionalidadesSelecionadas;
    }

    public BigDecimal getDiasTotal() {
        return diasTotal;
    }

    public BigDecimal getCustoDesenvolvimento() {
        return custoDesenvolvimento;
    }

    public BigDecimal getTotalCustosAdicionais() {
        return totalCustosAdicionais;
    }

    public BigDecimal getCustoTotal() {
        return custoTotal;
    }

    public BigDecimal getImposto() {
        return imposto;
    }

    public BigDecimal getValorComImposto() {
        return valorComImposto;
    }

    public BigDecimal getLucro() {
        return lucro;
    }

    public BigDecimal getPrecoFinal() {
        return precoFinal;
    }

    public BigDecimal getMeses() {
        return meses;
    }

    public BigDecimal getMediaMensal() {
        return mediaMensal;
    }

    public BigDecimal getPercentualImpostos() {
        return percentualImpostos;
    }

    public BigDecimal getPercentualLucro() {
        return percentualLucro;
    }

    // ---------- exibição (único ponto de arredondamento) ----------

    /** Total de dias inteiro (HALF_UP), como na planilha. */
    public long getDiasTotalExibicao() {
        return diasTotal.setScale(0, RoundingMode.HALF_UP).longValueExact();
    }

    /** Meses com uma casa decimal. */
    public BigDecimal getMesesExibicao() {
        return meses.setScale(1, RoundingMode.HALF_UP);
    }

    public static BigDecimal arredondarReais(BigDecimal valor) {
        return valor.setScale(2, RoundingMode.HALF_UP);
    }

    public static BigDecimal arredondarDias(BigDecimal valor) {
        return valor.setScale(2, RoundingMode.HALF_UP);
    }

    private static final Locale PT_BR = Locale.forLanguageTag("pt-BR");

    /** "R$ 1.234,56" */
    public static String formatarReais(BigDecimal valor) {
        DecimalFormat df = new DecimalFormat("#,##0.00", DecimalFormatSymbols.getInstance(PT_BR));
        return "R$ " + df.format(arredondarReais(valor));
    }

    /** "12,34" */
    public static String formatarDias(BigDecimal valor) {
        DecimalFormat df = new DecimalFormat("#,##0.00", DecimalFormatSymbols.getInstance(PT_BR));
        return df.format(arredondarDias(valor));
    }

    /** Fração como percentual: 0,42 vira "42,00%". */
    public static String formatarPercentual(BigDecimal fracao) {
        DecimalFormat df = new DecimalFormat("#,##0.00", DecimalFormatSymbols.getInstance(PT_BR));
        return df.format(fracao.multiply(new BigDecimal(100), MathContext.DECIMAL128).setScale(2, RoundingMode.HALF_UP)) + "%";
    }

    public static String formatarMeses(BigDecimal meses) {
        DecimalFormat df = new DecimalFormat("#,##0.0", DecimalFormatSymbols.getInstance(PT_BR));
        return df.format(meses.setScale(1, RoundingMode.HALF_UP));
    }
}
