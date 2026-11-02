package com.br.estimativadeprojetodesoftware.model;

import java.util.Objects;

/**
 * Funcionalidade copiada para um projeto, com a indicação de seleção. A cópia é
 * independente do perfil de origem.
 */
public class ItemProjeto {

    private final Funcionalidade funcionalidade;
    private boolean selecionado;

    public ItemProjeto(Funcionalidade funcionalidade, boolean selecionado) {
        this.funcionalidade = Objects.requireNonNull(funcionalidade);
        this.selecionado = selecionado;
    }

    public Funcionalidade getFuncionalidade() {
        return funcionalidade;
    }

    public boolean isSelecionado() {
        return selecionado;
    }

    public void setSelecionado(boolean selecionado) {
        this.selecionado = selecionado;
    }

    public ItemProjeto copiar() {
        return new ItemProjeto(funcionalidade.copiar(), selecionado);
    }

    @Override
    public String toString() {
        return funcionalidade.toString() + (selecionado ? " [x]" : " [ ]");
    }
}
