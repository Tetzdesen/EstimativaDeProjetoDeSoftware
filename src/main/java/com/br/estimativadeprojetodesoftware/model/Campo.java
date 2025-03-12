package com.br.estimativadeprojetodesoftware.model;

import java.util.Objects;
import java.util.UUID;

/**
 *
 * @author tetzner
 */
public class Campo {
    private UUID id;
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

    public Campo(String tipo, String nome, double dias) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Campo campo = (Campo) o;
        return tipo.equals(campo.tipo) && nome.equals(campo.nome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tipo, nome);
    }

}
