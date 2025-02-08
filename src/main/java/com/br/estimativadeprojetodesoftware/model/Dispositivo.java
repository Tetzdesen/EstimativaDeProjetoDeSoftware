package com.br.estimativadeprojetodesoftware.model;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class Dispositivo {
    private String nome;
    private List<Campo> campos;

    public Dispositivo (String nome) {
        this.nome = nome;
        this.campos = new ArrayList<>();
    }

    public void addCampo(Campo campo) {
        campos.add(campo);
    }

    public List<Campo> getCampos() {
        return Collections.unmodifiableList(campos);
    }

}