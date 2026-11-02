package com.br.estimativadeprojetodesoftware.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Valores consolidados do dashboard de um usuário. Somas exatas (BigDecimal);
 * o arredondamento acontece só na exibição. Só projetos próprios calculados
 * entram nas somas.
 */
public class ResumoDashboard {

    private final int projetosProprios;
    private final int projetosCompartilhados;
    private final int projetosCalculados;
    private final int projetosComErro;
    private final BigDecimal diasTotal;
    private final BigDecimal precoTotal;
    private final Map<String, BigDecimal> precoPorProjeto;
    private final Map<Plataforma, BigDecimal> diasPorPlataforma;

    public ResumoDashboard(int projetosProprios, int projetosCompartilhados, int projetosCalculados, int projetosComErro,
            BigDecimal diasTotal, BigDecimal precoTotal, Map<String, BigDecimal> precoPorProjeto,
            Map<Plataforma, BigDecimal> diasPorPlataforma) {
        this.projetosProprios = projetosProprios;
        this.projetosCompartilhados = projetosCompartilhados;
        this.projetosCalculados = projetosCalculados;
        this.projetosComErro = projetosComErro;
        this.diasTotal = diasTotal;
        this.precoTotal = precoTotal;
        this.precoPorProjeto = Collections.unmodifiableMap(new LinkedHashMap<>(precoPorProjeto));
        Map<Plataforma, BigDecimal> dias = new EnumMap<>(Plataforma.class);
        for (Plataforma p : Plataforma.values()) {
            dias.put(p, diasPorPlataforma.getOrDefault(p, BigDecimal.ZERO));
        }
        this.diasPorPlataforma = Collections.unmodifiableMap(dias);
    }

    public int getProjetosProprios() {
        return projetosProprios;
    }

    public int getProjetosCompartilhados() {
        return projetosCompartilhados;
    }

    public int getProjetosCalculados() {
        return projetosCalculados;
    }

    /** Projetos marcados como calculados que não passam na validação do estado atual. */
    public int getProjetosComErro() {
        return projetosComErro;
    }

    public BigDecimal getDiasTotal() {
        return diasTotal;
    }

    /** Total de dias inteiro (HALF_UP), como na tela de estimativa. */
    public long getDiasTotalExibicao() {
        return diasTotal.setScale(0, RoundingMode.HALF_UP).longValueExact();
    }

    public BigDecimal getPrecoTotal() {
        return precoTotal;
    }

    /** Preço final por nome de projeto, na ordem da listagem. */
    public Map<String, BigDecimal> getPrecoPorProjeto() {
        return precoPorProjeto;
    }

    /** Dias somados por plataforma; plataforma desmarcada conta 0. Sempre traz as três. */
    public Map<Plataforma, BigDecimal> getDiasPorPlataforma() {
        return diasPorPlataforma;
    }

    public boolean temProjetoCalculado() {
        return !precoPorProjeto.isEmpty();
    }
}
