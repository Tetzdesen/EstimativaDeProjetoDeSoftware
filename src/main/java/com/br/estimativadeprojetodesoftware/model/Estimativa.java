package com.br.estimativadeprojetodesoftware.model;

import java.util.HashMap;
import java.util.Map;

public class Estimativa {
    private Map<String, Double> campos;

    public Estimativa() {
        campos = new HashMap<>();
    }

    public void adicionarCampo(String chave, double valor) {
        campos.put(chave, valor);
    }
}
