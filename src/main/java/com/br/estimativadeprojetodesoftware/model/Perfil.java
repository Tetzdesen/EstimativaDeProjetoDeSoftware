package com.br.estimativadeprojetodesoftware.model;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class Perfil {

    private UUID id;
    private String nome;
    private Map<String, Integer> tamDispositivos;
    private Map<String, Double> nivelUI;
    private Map<String, Integer> funcionalidades;
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

    public Perfil(UUID id, String nome, Map<String, Integer> tamDispositivos, Map<String, Double> nivelUI, Map<String, Integer> funcionalidades, LocalDateTime created_at, LocalDateTime update_at, LocalDateTime deleted_at) {
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

    public Map<String, Integer> getTamDispositivos() {
        return Collections.unmodifiableMap(tamDispositivos);
    }

    public Map<String, Double> getNivelUI() {
        return Collections.unmodifiableMap(nivelUI);
    }

    public Map<String, Integer> getFuncionalidades() {
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

    public void adicionarTamDispositivo(String nome, int dias) {
        if (nome == null || nome.isEmpty()) {
            throw new IllegalArgumentException("Erro: Nome do tamanho do dispositivo não pode ser vazio ou nula.");
        }
        if (dias < 0) {
            throw new IllegalArgumentException("Erro: dias não pode ser negativo. Nome do tamanho do dispositivo : " + nome + " dias: " + dias);
        }
        tamDispositivos.put(nome, dias);
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

    public void adicionarFuncionalidade(String nomeFuncionalidade, int dias) {
        if (nome == null || nome.isEmpty()) {
            throw new IllegalArgumentException("Erro: Nome da funcionalidade não pode ser vazio ou nula.");
        }
        if (dias < 0) {
            throw new IllegalArgumentException("Erro: dias não pode ser negativo. Nome da funcionalidade: " + nome + " dias: " + dias);
        }
        funcionalidades.put(nomeFuncionalidade, dias);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Perfil other = (Perfil) obj;
        if (!Objects.equals(this.nome, other.nome)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.tamDispositivos, other.tamDispositivos)) {
            return false;
        }
        if (!Objects.equals(this.nivelUI, other.nivelUI)) {
            return false;
        }
        if (!Objects.equals(this.funcionalidades, other.funcionalidades)) {
            return false;
        }
        if (!Objects.equals(this.created_at, other.created_at)) {
            return false;
        }
        if (!Objects.equals(this.update_at, other.update_at)) {
            return false;
        }
        return Objects.equals(this.deleted_at, other.deleted_at);
    }

    @Override
    public String toString() {
        return "Perfil{" + "id=" + id + ", nome=" + nome + ", tamDispositivos=" + tamDispositivos + ", nivelUI=" + nivelUI + ", funcionalidades=" + funcionalidades + ", created_at=" + created_at + ", update_at=" + update_at + ", deleted_at=" + deleted_at + '}';
    }
}
