package com.br.estimativadeprojetodesoftware.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Estimativa {
    private UUID idEstimativa;
    private Map<String, Double> campos;

    public Estimativa() {
        idEstimativa = UUID.randomUUID();
        campos = new HashMap<>();
    }

    public UUID getIdEstimativa() {
        return idEstimativa;
    }

    public Map<String, Double> getCampos() {
        return Collections.unmodifiableMap(campos);
    }

    public void setIdEstimativa(UUID idEstimativa) {
        if(idEstimativa == null){
            throw new RuntimeException("Erro: id da estimativa nulo");
        }
        this.idEstimativa = idEstimativa;
    }

    public void adicionarCampo(String chave, double valor) {
         if(chave.equals("") || valor < 0){
            throw new IllegalArgumentException("Erro: Dados inválidos" + "Chave: " + chave + "Valor: " + valor);
        }
        campos.put(chave, valor);
    }
    
}
