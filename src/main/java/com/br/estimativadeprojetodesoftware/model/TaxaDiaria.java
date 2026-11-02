package com.br.estimativadeprojetodesoftware.model;

import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

/** Tabela de taxas diárias em reais por plataforma e papel. */
public class TaxaDiaria {

    private final Map<Plataforma, Map<Papel, BigDecimal>> valores;

    public TaxaDiaria() {
        this.valores = new EnumMap<>(Plataforma.class);
        for (Plataforma p : Plataforma.values()) {
            Map<Papel, BigDecimal> porPapel = new EnumMap<>(Papel.class);
            for (Papel papel : Papel.values()) {
                porPapel.put(papel, BigDecimal.ZERO);
            }
            valores.put(p, porPapel);
        }
    }

    public BigDecimal get(Plataforma plataforma, Papel papel) {
        return valores.get(plataforma).get(papel);
    }

    public void set(Plataforma plataforma, Papel papel, BigDecimal valor) {
        Objects.requireNonNull(plataforma);
        Objects.requireNonNull(papel);
        if (valor == null || valor.signum() < 0) {
            throw new IllegalArgumentException("A taxa de " + papel + " em " + plataforma + " deve ser zero ou positiva");
        }
        valores.get(plataforma).put(papel, valor);
    }

    public TaxaDiaria copiar() {
        TaxaDiaria copia = new TaxaDiaria();
        for (Plataforma p : Plataforma.values()) {
            for (Papel papel : Papel.values()) {
                copia.set(p, papel, get(p, papel));
            }
        }
        return copia;
    }
}
