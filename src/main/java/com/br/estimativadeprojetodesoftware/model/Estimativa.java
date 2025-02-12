package com.br.estimativadeprojetodesoftware.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Estimativa {

    private UUID id;
    private LocalDateTime created_at;
    //private Map<String, Integer> campos;
    private List<CampoCalculado> campos;
    private int dias;
    private double valor;
    
    public Estimativa() {
        this.id = UUID.randomUUID();
        this.created_at = LocalDateTime.now();
        this.campos = new ArrayList<>();
    }

    public Estimativa(UUID id, LocalDateTime created_at, List<CampoCalculado> campos) {
        this.id = id;
        this.created_at = created_at;
        this.campos = campos;
    }

    public UUID getId() {
        return id;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public List<CampoCalculado> getCampos() {
        return Collections.unmodifiableList(campos);
    }

    public void adicionarDias(int dias) {
        this.dias = dias;
    }

    public void adicionarValor(double valor) {
        this.valor = valor;
    }

    public int getDias() {
        return dias;
    }

    public double getValor() {
        return valor;
    }

    public void adicionarCampo(String nomeFuncionalidade, int dias, Diaria diaria) {
        if (nomeFuncionalidade == null || nomeFuncionalidade.isEmpty()) {
            throw new IllegalArgumentException("Erro: Nome da funcionalidade não pode ser vazia ou nula.");
        }
        if (dias < 0) {
            throw new IllegalArgumentException("Erro: Quantidade de dias não pode ser negativo. Chave: " + nomeFuncionalidade + " Valor: " + dias);
        }
        //campos.put(nomeFuncionalidade, dias);
        campos.add(new CampoCalculado(nomeFuncionalidade, dias, diaria));
    }
}
