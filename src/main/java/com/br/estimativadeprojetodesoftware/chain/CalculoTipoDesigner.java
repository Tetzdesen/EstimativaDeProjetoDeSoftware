package com.br.estimativadeprojetodesoftware.chain;

import com.br.estimativadeprojetodesoftware.model.Projeto;

public class CalculoTipoDesigner extends FormaCalculoEstimativaHandler {
    @Override
    public void calcularEstimativa(Projeto projeto) {
        double valor = projeto.getEstimativa().getCampos().get("Nível de UI");
        adicionarValor(valor);
    }
}
