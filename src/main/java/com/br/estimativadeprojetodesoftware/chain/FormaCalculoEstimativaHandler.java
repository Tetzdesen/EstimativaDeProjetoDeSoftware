package com.br.estimativadeprojetodesoftware.chain;

import com.br.estimativadeprojetodesoftware.model.Projeto;

public abstract class FormaCalculoEstimativaHandler {
    protected FormaCalculoEstimativaHandler proximo;

    public abstract void calcularEstimativa(Projeto projeto);

    public void setProximo(FormaCalculoEstimativaHandler proximo) {
        this.proximo = proximo;
    }
}
