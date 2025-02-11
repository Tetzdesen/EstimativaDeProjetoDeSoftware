package com.br.estimativadeprojetodesoftware.chain;

import com.br.estimativadeprojetodesoftware.model.Projeto;

public class CalculoTipoGerente extends FormaCalculoEstimativaHandler {
    @Override
    public void calcularEstimativa(Projeto projeto) {
        double valor = projeto.getEstimativa().getCampos().get("Gerente de Projeto");
        adicionarValor(valor);
    }
}
