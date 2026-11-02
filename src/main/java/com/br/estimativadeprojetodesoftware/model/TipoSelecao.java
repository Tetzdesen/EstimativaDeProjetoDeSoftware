package com.br.estimativadeprojetodesoftware.model;

public enum TipoSelecao {
    /** Escolha única dentro da categoria (Tamanho do App, Nível de UI). */
    UNICA,
    /** Múltipla escolha. */
    MULTIPLA,
    /** Dias digitados pelo usuário (Desenvolvimento Específico do Aplicativo). */
    LIVRE,
    /** Item opcional (Gerente de Projeto). */
    OPCIONAL
}
