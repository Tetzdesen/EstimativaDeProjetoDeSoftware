package com.br.estimativadeprojetodesoftware.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Estimativa {
    private int id;
    private Map<String, Double> campos;

    public Estimativa() {
        campos = new HashMap<>();
    }

    public int getId() {
        return id;
    }

    public Map<String, Double> getCampos() {
        return Collections.unmodifiableMap(campos);
    }

    public void adicionarCampo(String chave, double valor) {
         if(chave.equals("") || valor < 0){
            throw new IllegalArgumentException("Erro: Dados inválidos" + "Chave: " + chave + "Valor: " + valor);
        }
        campos.put(chave, valor);
    }
    
}
