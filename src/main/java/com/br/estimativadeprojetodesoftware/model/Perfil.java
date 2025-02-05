package com.br.estimativadeprojetodesoftware.model;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;

public class Perfil {
    
    private int id;
    private String nome;
    private Map<String, Double> tamDispositivos;
    private Map<String, Double> nivelUI;
    private Map<String, Double> funcionalidades;
    private LocalDateTime created_at;
    private LocalDateTime update_at;
    private LocalDateTime deleted_at;
    
    public Perfil(int id, String nome, Map<String, Double> tamDispositivos, Map<String, Double> nivelUI, Map<String, Double> funcionalidades) {
        this.id = id;
        this.nome = nome;
        this.tamDispositivos = tamDispositivos;
        this.nivelUI = nivelUI;
        this.funcionalidades = funcionalidades;
        this.created_at = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public Map<String, Double> getTamDispositivos() {
        return Collections.unmodifiableMap(tamDispositivos);
    }

    public Map<String, Double> getNivelUI() {
       return Collections.unmodifiableMap(nivelUI);
    }

    public Map<String, Double> getFuncionalidades() {
       return Collections.unmodifiableMap(funcionalidades);
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public LocalDateTime getUpdate_at() {
        return update_at;
    }
     
    public LocalDateTime getDeleted_at() {
        return deleted_at;
    }

    public void adicionarTamDispositivo(String nome, double valor) {
       tamDispositivos.put(nome, valor);
    }

    public void adicionarNivelUI(String nome, double valor) {
       nivelUI.put(nome, valor);
    }

    public void adicionarFuncionalidades(String nome, double valor) {
        funcionalidades.put(nome, valor);
    }
    
    public void setUpdate_at(LocalDateTime update_at) {
        this.update_at = update_at;
    }

    public void setDeleted_at(LocalDateTime deleted_at) {
        this.deleted_at = deleted_at;
    }

    /**
     * TamDispositivos: (pequeno: 10) (médio: 30) (grande: 50)
     *
     *
     * nivelUI (MVP: 0.3) (basico: 0.5) profissional: 0.7
     */
}
