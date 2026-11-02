package com.br.estimativadeprojetodesoftware.presenter.tabela;

import com.br.estimativadeprojetodesoftware.model.Funcionalidade;
import com.br.estimativadeprojetodesoftware.model.Plataforma;
import com.br.estimativadeprojetodesoftware.model.TipoValor;
import java.math.BigDecimal;
import java.math.RoundingMode;

/** Conversão entre os valores do modelo e o texto das tabelas ("NA", "70%", "2,5"). */
public final class FormatoValor {

    public static final String NA = "NA";
    private static final BigDecimal CEM = new BigDecimal(100);

    private FormatoValor() {
    }

    public static String texto(Funcionalidade f, Plataforma p) {
        return f.getValor(p).map(v -> texto(v, f.getTipoValor())).orElse(NA);
    }

    public static String texto(BigDecimal valor, TipoValor tipo) {
        if (tipo == TipoValor.PERCENTUAL) {
            return numero(valor.multiply(CEM)) + "%";
        }
        return numero(valor);
    }

    public static String numero(BigDecimal valor) {
        BigDecimal v = valor.stripTrailingZeros();
        if (v.scale() < 0) {
            v = v.setScale(0);
        }
        return v.toPlainString().replace('.', ',');
    }

    public static String reais(BigDecimal valor) {
        return new java.text.DecimalFormat("#,##0.00", java.text.DecimalFormatSymbols.getInstance(java.util.Locale.forLanguageTag("pt-BR")))
                .format(valor.setScale(2, RoundingMode.HALF_UP));
    }

    /** Texto digitado para valor: "NA" ou vazio vira null (não aplicável). */
    public static BigDecimal interpretar(Object entrada, TipoValor tipo) {
        String texto = entrada == null ? "" : entrada.toString().trim();
        if (texto.isEmpty() || texto.equalsIgnoreCase(NA)) {
            return null;
        }
        BigDecimal valor = decimal(texto.replace("%", ""));
        return tipo == TipoValor.PERCENTUAL ? valor.divide(CEM) : valor;
    }

    /** Número digitado (aceita vírgula ou ponto). */
    public static BigDecimal decimal(String texto) {
        String limpo = texto == null ? "" : texto.trim().replace("R$", "").replace(" ", "");
        if (limpo.contains(",")) {
            limpo = limpo.replace(".", "").replace(',', '.');
        }
        try {
            BigDecimal v = new BigDecimal(limpo);
            if (v.signum() < 0) {
                throw new IllegalArgumentException("Valores negativos não são aceitos: " + texto);
            }
            return v;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Valor inválido: \"" + texto + "\". Use números (ex.: 2,5) ou NA.");
        }
    }

    /** Percentual digitado ("42" ou "42%") como fração (0,42). */
    public static BigDecimal percentual(String texto) {
        return decimal(texto.replace("%", "")).divide(CEM);
    }

    public static String percentual(BigDecimal fracao) {
        return numero(fracao.multiply(CEM));
    }
}
