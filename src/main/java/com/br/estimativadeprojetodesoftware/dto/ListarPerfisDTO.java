package com.br.estimativadeprojetodesoftware.dto;

public class ListarPerfisDTO {
    private String id;
    private String nome;

    public ListarPerfisDTO(String id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }
}
