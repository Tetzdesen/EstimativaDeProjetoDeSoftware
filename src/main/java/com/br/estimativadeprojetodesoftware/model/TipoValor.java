package com.br.estimativadeprojetodesoftware.model;

public enum TipoValor implements Descritivel {
    /** Dias de desenvolvimento. */
    DIAS("Dias"),
    /** Percentual (fração, ex.: 0,70) aplicado aos dias de tamanho. */
    PERCENTUAL("Percentual"),
    /** Dias de gerência de projeto. */
    DIAS_GERENCIA("Dias de gerência");

    private final String descricao;

    TipoValor(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String getDescricao() {
        return descricao;
    }
}
