package com.br.estimativadeprojetodesoftware.view.projeto;

/** Seções da tela de projeto, na ordem da barra lateral. */
public enum SecaoProjeto {
    PLATAFORMAS("Plataformas"),
    TAMANHO_E_UI("Tamanho e UI"),
    FUNCIONALIDADES("Funcionalidades"),
    TAXAS("Taxas diárias"),
    CUSTOS("Custos e preço"),
    DETALHAMENTO("Detalhamento");

    private final String titulo;

    SecaoProjeto(String titulo) {
        this.titulo = titulo;
    }

    public String getTitulo() {
        return titulo;
    }
}
