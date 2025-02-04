package com.br.estimativadeprojetodesoftware.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Projeto {
    private UUID id;
    private String nome;
    private LocalDateTime created_at;
    private LocalDateTime update_at;
    private LocalDateTime deleted_at;
    private Estimativa estimativa;
    private boolean isCompartilhado;
    private List<Usuario> usuarioCompartilhado;

    public Projeto(String nome) {
        this.id = UUID.randomUUID();
        this.nome = nome;
        this.created_at = LocalDateTime.now();
        this.usuarioCompartilhado = new ArrayList();
    }

    public UUID getIdProjeto() {
        return id;
    }

    public String getNomeProjeto() {
        return nome;
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

    public Estimativa getEstimativa() {
        return estimativa;
    }

    public boolean isIsCompartilhado() {
        return isCompartilhado;
    }

    public List<Usuario> getUsuarioCompartilhado() {
        return Collections.unmodifiableList(usuarioCompartilhado);
    }

    public void setIdProjeto(UUID id) {
        this.id = id;
    }

    public void setNomeProjeto(String nome) {
        this.nome = nome;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public void setDeleted_at(LocalDateTime deleted_at) {
        this.deleted_at = deleted_at;
    }

    public void setEstimativa(Estimativa estimativa) {
        this.estimativa = estimativa;
    }
    
    public void setIsCompartilhado(Boolean isCompartilhado) {
        this.isCompartilhado = isCompartilhado;
    }
    
    public void adicionarUsuarioCompartilhado(Usuario usuario){
        this.usuarioCompartilhado.add(usuario);
    }
    
}
