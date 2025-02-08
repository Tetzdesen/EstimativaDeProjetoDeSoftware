package com.br.estimativadeprojetodesoftware.model;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Perfil {

    private UUID id;
    private String nome;
    private List<Dispositivo> dispositivos;
    private LocalDateTime created_at;
    private LocalDateTime update_at;
    private LocalDateTime deleted_at;

    public Perfil(String nome, List<Dispositivo> dispositivos) {
        this.id = UUID.randomUUID();
        this.nome = nome;
        for (Dispositivo dispositivo : dispositivos) {
            this.dispositivos.add(dispositivo);
        }
        
        this.created_at = LocalDateTime.now();
    }

    public Perfil(UUID id, String nome, List<Dispositivo> dispositivos) {
        this.id = id;
        this.nome = nome;
        for (Dispositivo dispositivo : dispositivos) {
            this.dispositivos.add(dispositivo);
        }
        
        this.created_at = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public List<Dispositivo> getDispositivos () {
        return dispositivos;
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

    public void adicionarDispositivo(List<Dispositivo> dispositivos) {
        
    }

}
