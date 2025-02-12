package com.br.estimativadeprojetodesoftware.chain;

import com.br.estimativadeprojetodesoftware.model.Projeto;

public class CalculoTipoDesigner extends FormaCalculoEstimativaHandler {
    @Override
    public void calcularEstimativa(Projeto projeto) {
        int dias = projeto.getEstimativa().getCampos().get("Nível de UI");
        projeto.getEstimativa().adicionarDias(dias);
        //valor = (projeto.getTaxasDiarias().get("Designer UI/UX")) * dias;

        if (proximo != null) {
            proximo.calcularEstimativa(projeto);
        }
    }
}
