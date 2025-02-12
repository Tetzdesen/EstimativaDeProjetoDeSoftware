package com.br.estimativadeprojetodesoftware.model;

public abstract class Diaria {
    private double valor;

    public Diaria (double valor) {
        this.valor = valor;
    }

    public double getValor() {
        return valor;
    }
}
