package com.br.estimativadeprojetodesoftware.model;

/** Papel profissional que define a taxa diária aplicada a cada parcela de dias. */
public enum Papel {
    DESENVOLVIMENTO("Desenvolvimento"),
    DESIGN("Design UI/UX"),
    GERENCIA("Gerência de Projeto");

    private final String descricao;

    Papel(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public static Papel deDescricao(String descricao) {
        for (Papel p : values()) {
            if (p.descricao.equalsIgnoreCase(descricao.trim())) {
                return p;
            }
        }
        throw new IllegalArgumentException("Papel desconhecido: " + descricao);
    }

    @Override
    public String toString() {
        return descricao;
    }
}
