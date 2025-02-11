package com.br.estimativadeprojetodesoftware.chain;

import com.br.estimativadeprojetodesoftware.model.Projeto;

public abstract class FormaCalculoEstimativaHandler {
    private double valor;
    public abstract void calcularEstimativa(Projeto projeto);

    public void adicionarValor(double valor) {
        this.valor += valor;
    }
}
