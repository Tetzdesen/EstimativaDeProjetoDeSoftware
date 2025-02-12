package com.br.estimativadeprojetodesoftware.chain;

import com.br.estimativadeprojetodesoftware.model.Projeto;

public class CalculoTipoDesigner extends FormaCalculoEstimativaHandler {
    @Override
    public void calcularEstimativa(Projeto projeto) {
        double dias = projeto.getEstimativa().getCampos().get("Nível de UI");
        valor = (projeto.getTaxasDiarias().get("Designer UI/UX")) * dias;

        if (proximo != null) {
            proximo.calcularEstimativa(projeto);
        }
    }
}
