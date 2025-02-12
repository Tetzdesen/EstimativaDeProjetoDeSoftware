package com.br.estimativadeprojetodesoftware.chain;

import com.br.estimativadeprojetodesoftware.model.Projeto;

public class CalculoTipoGerente extends FormaCalculoEstimativaHandler {
    @Override
    public void calcularEstimativa(Projeto projeto) {
        int dias = projeto.getEstimativa().getCampos().get("Gerente de Projeto");
        projeto.getEstimativa().adicionarDias(dias);
        //valor = (projeto.getTaxasDiarias().get("Gerente de Projetos")) * dias;

        if (proximo != null) {
            proximo.calcularEstimativa(projeto);
        }
    }
}
