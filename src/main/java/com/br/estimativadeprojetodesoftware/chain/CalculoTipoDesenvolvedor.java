package com.br.estimativadeprojetodesoftware.chain;

import java.util.Map;

import com.br.estimativadeprojetodesoftware.model.Projeto;

public class CalculoTipoDesenvolvedor extends FormaCalculoEstimativaHandler {
    @Override
    public void calcularEstimativa(Projeto projeto) {
        double dias = 0;
        for (Map.Entry<String, Integer> campo : projeto.getEstimativa().getCampos().entrySet()) {
            if (campo.getKey().equals("Nível de UI") || campo.getKey().equals("Gerente de Projeto")) {
                break;
            }
            dias += campo.getValue();
        }
        double valor = dias * projeto.getPerfis().get(0).getTaxasDiarias().get("Desenvolvimento");
        adicionarValor(valor);
    }
}
