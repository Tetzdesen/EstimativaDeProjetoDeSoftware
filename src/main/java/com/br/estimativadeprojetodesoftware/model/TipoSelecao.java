package com.br.estimativadeprojetodesoftware.model;

public enum TipoSelecao implements Descritivel {
    /** Escolha única dentro da categoria (Tamanho do App, Nível de UI). */
    UNICA("Única"),
    /** Múltipla escolha. */
    MULTIPLA("Múltipla"),
    /** Dias digitados pelo usuário (Desenvolvimento Específico do Aplicativo). */
    LIVRE("Livre"),
    /** Item opcional (Gerente de Projeto). */
    OPCIONAL("Opcional");

    private final String descricao;

    TipoSelecao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String getDescricao() {
        return descricao;
    }
}
