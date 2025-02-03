package com.br.estimativadeprojetodesoftware.model;

import java.util.Map;

public class Perfil {
    private Map<String, Double> tamDispositivos;
    private Map<String, Double> nivelUI;
    private Map<String, Double> funcionalidades;

    public Map<String, Double> getNivelUI() {
        return nivelUI;
    }

    public Map<String, Double> getFuncionalidades() {
        return funcionalidades;
    }

    public Map<String, Double> getTamDispositivos() {
        return tamDispositivos;
    }

    /**
     * TamDispositivos:
     * (pequeno: 10)
     * (médio: 30)
     * (grande: 50)
     * 
     * 
     * nivelUI
     * (MVP: 0.3)
     * (basico: 0.5)
     * profissional: 0.7
     */
}
