package com.br.estimativadeprojetodesoftware.model;

import java.util.UUID;

/**
 *
 * @author tetzner
 */
public class Campo {
    private final UUID id;
    private final String tipo;
    private final String nome;
    private Double dias;
    
    public Campo(UUID id, String tipo, String nome) {
        this.id = id;
        this.tipo = tipo;
        this.nome = nome;
    }

    public Campo(UUID id, String tipo, String nome, Double dias) {
        this.id = id;
        this.tipo = tipo;
        this.nome = nome;
        this.dias = dias;
    }
    
    public UUID getId() {
        return id;
    }

    public String getTipo() {
        return tipo;
    }

    public String getNome() {
        return nome;
    }

    public Double getDias() {
        return dias;
    }

    public void setDias(Double dias) {
        this.dias = dias;
    }

    @Override
    public String toString() {
        return "Campo{" + "id=" + id + ", tipo=" + tipo + ", nome=" + nome + ", dias=" + dias + '}';
    }
}
