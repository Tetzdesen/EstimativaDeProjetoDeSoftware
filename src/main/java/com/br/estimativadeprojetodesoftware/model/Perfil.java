package com.br.estimativadeprojetodesoftware.model;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Perfil {

    private UUID id;
    private String nome;
    private Map<String, Double> tamDispositivos;
    private Map<String, Double> nivelUI;
    private Map<String, Double> funcionalidades;
    private LocalDateTime created_at;
    private LocalDateTime update_at;
    private LocalDateTime deleted_at;

    public Perfil(String nome) {
        this.id = UUID.randomUUID();
        this.nome = nome;
        this.tamDispositivos = new HashMap<>();
        this.nivelUI = new HashMap<>();
        this.funcionalidades = new HashMap<>();
        this.created_at = LocalDateTime.now();
        this.update_at = null;
        this.deleted_at = null;
    }

    public Perfil(UUID id, String nome, Map<String, Double> tamDispositivos, Map<String, Double> nivelUI, Map<String, Double> funcionalidades, LocalDateTime created_at, LocalDateTime update_at, LocalDateTime deleted_at) {
        this.id = id;
        this.nome = nome;
        this.tamDispositivos = tamDispositivos;
        this.nivelUI = nivelUI;
        this.funcionalidades = funcionalidades;
        this.created_at = created_at;
        this.update_at = update_at;
        this.deleted_at = deleted_at;
    }

    public UUID getId() {
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

    public void setUpdate_at(LocalDateTime update_at) {
        if (update_at == null) {
            throw new IllegalArgumentException("Erro: Data de atualização não pode ser nula.");
        }
        this.update_at = update_at;
    }

    public void setDeleted_at(LocalDateTime deleted_at) {
        if (deleted_at == null) {
            throw new IllegalArgumentException("Erro: Data de exclusão não pode ser nula.");
        }
        this.deleted_at = deleted_at;
    }

    public void adicionarTamDispositivo(String nome, double valor) {
        if (nome == null || nome.isEmpty()) {
            throw new IllegalArgumentException("Erro: Nome do tamanho do dispositivo não pode ser vazio ou nula.");
        }
        if (valor < 0) {
            throw new IllegalArgumentException("Erro: Valor não pode ser negativo. Nome do tamanho do dispositivo : " + nome + " Valor: " + valor);
        }
        tamDispositivos.put(nome, valor);
    }

    public void adicionarNivelUI(String nome, double valor) {
        if (nome == null || nome.isEmpty()) {
            throw new IllegalArgumentException("Erro: Nome de nível de UI não pode ser vazio ou nula.");
        }
        if (valor < 0) {
            throw new IllegalArgumentException("Erro: Valor não pode ser negativo. Nome da nível UI: " + nome + " Valor: " + valor);
        }
        nivelUI.put(nome, valor);
    }

    public void adicionarFuncionalidade(String nomeFuncionalidade, double valor) {
        if (nome == null || nome.isEmpty()) {
            throw new IllegalArgumentException("Erro: Nome da funcionalidade não pode ser vazio ou nula.");
        }
        if (valor < 0) {
            throw new IllegalArgumentException("Erro: Valor não pode ser negativo. Nome da funcionalidade: " + nome + " Valor: " + valor);
        }
        funcionalidades.put(nomeFuncionalidade, valor);
    }
   
}
