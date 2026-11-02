package com.br.estimativadeprojetodesoftware.model;

public enum Plataforma {
    WEB_BACKEND("Web/Back-end"),
    IOS("iOS"),
    ANDROID("Android");

    private final String descricao;

    Plataforma(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }
}
