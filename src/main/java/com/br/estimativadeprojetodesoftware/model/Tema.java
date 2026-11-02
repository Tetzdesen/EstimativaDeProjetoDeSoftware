package com.br.estimativadeprojetodesoftware.model;

/** Tema visual da interface. */
public enum Tema {
    CLARO("Claro"),
    ESCURO("Escuro");

    private final String descricao;

    Tema(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    /** Converte o texto salvo; ausente ou inválido vale CLARO. */
    public static Tema deTexto(String texto) {
        if (texto == null) {
            return CLARO;
        }
        try {
            return valueOf(texto.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return CLARO;
        }
    }
}
