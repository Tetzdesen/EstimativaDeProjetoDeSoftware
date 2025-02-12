package com.br.estimativadeprojetodesoftware.chain;

import com.br.estimativadeprojetodesoftware.model.Projeto;

public abstract class FormaCalculoEstimativaHandler {
    protected double valor;
    protected FormaCalculoEstimativaHandler proximo;

    public abstract void calcularEstimativa(Projeto projeto);

    public void setProximo(FormaCalculoEstimativaHandler proximo) {
        this.proximo = proximo;
    }

    public double getValorTotal() {
        double total = valor;
        if (proximo != null) {
            total += proximo.getValorTotal();
        }
        return total;
    }
}
