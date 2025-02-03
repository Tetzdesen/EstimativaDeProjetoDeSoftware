package com.br.estimativadeprojetodesoftware.model;

import java.time.LocalDate;

public class Projeto {
    private String nome;
    private LocalDate dataCriacao;
    private Estimativa estimativa;

    public String getNome() {
        return nome;
    }

    public LocalDate getDataCriacao() {
        return dataCriacao;
    }
    
    public Estimativa getEstimativa() {
        return estimativa;
    }



}
