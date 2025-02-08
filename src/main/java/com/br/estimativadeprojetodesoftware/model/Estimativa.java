package com.br.estimativadeprojetodesoftware.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Estimativa {

    private UUID id;
    private Map<String, Double> campos;

    public Estimativa() {
        this.id = UUID.randomUUID();
        this.campos = new HashMap<>();
    }

    public Estimativa(UUID id, Map<String, Double> campos) {
        this.id = id;
        this.campos = campos;
    }

    public UUID getId() {
        return id;
    }

    public Map<String, Double> getCampos() {
        return Collections.unmodifiableMap(campos);
    }

    public void adicionarCampo(String chave, double valor) {
        if (chave == null || chave.isEmpty()) {
            throw new IllegalArgumentException("Erro: Chave não pode ser vazia ou nula.");
        }
        if (valor < 0) {
            throw new IllegalArgumentException("Erro: Valor não pode ser negativo. Chave: " + chave + " Valor: " + valor);
        }
        campos.put(chave, valor);
    }
}
