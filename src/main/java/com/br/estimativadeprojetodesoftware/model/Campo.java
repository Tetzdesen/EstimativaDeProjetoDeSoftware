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
    private final double dias;

    public Campo(UUID id, String tipo, String nome, double dias) {
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

    public double getDias() {
        return dias;
    }

    @Override
    public String toString() {
        return "Campo{" + "id=" + id + ", tipo=" + tipo + ", nome=" + nome + ", dias=" + dias + '}';
    }
}
