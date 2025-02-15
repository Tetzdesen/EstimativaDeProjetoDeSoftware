package com.br.estimativadeprojetodesoftware.decorator;

public abstract class Componente {
    protected String nome;
    protected int dias;

    public Componente(String nome, int dias) {
        this.nome = nome;
        this.dias = dias;
    }

    public abstract String getDescricao();
    public abstract int getDias();
}
