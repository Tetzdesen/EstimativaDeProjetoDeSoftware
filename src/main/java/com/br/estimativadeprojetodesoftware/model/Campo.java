package com.br.estimativadeprojetodesoftware.model;

import java.util.Collections;
import java.util.Map;

public class Campo {
    private String categoria;
    /*
    private String funcionalidade;
    private int dias;
     */
    private Map<String, Double> funcionalidade;
    private String observacao;

    public Campo (String categoria, String funcionalidade, int dias) {
        this.categoria = categoria;
        //this.funcionalidade = funcionalidade;
        //this.dias = dias;
        this.funcionalidade = new HashMap<>();
        this.funcionalidade.put(funcionalidade, dias);

    }

    public Campo (String categoria, String funcionalidade, int dias, String observacao) {
        this.categoria = categoria;
        //this.funcionalidade = funcionalidade;
        //this.dias = dias;
        this.funcionalidade = new HashMap<>();
        this.funcionalidade.put(funcionalidade, dias);
        this.observacao = observacao;
    }
    
    public String getCategoria() {
        return categoria;
    }

    public Map<String, Double> getFuncionalidades() {
        return Collections.unmodifiableMap(funcionalidade);
    }

    public String getObservacao() {
        return observacao;
    }
}