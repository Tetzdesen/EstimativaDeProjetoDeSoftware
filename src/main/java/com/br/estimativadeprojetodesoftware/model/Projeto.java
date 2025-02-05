package com.br.estimativadeprojetodesoftware.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Projeto {
    private int id;
    private String nome;
    private LocalDateTime created_at;
    private LocalDateTime update_at;
    private LocalDateTime deleted_at;
    private Estimativa estimativa;
    private List<Usuario> usuarioCompartilhado;

    public Projeto(int id, String nome) {
        this.id = id;
        this.nome = nome;
        this.created_at = LocalDateTime.now();
        this.usuarioCompartilhado = new ArrayList();
    }

    public int getId() {
        return id;
    }

    public String getNome() {
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

    public List<Usuario> getUsuarioCompartilhado() {
        return Collections.unmodifiableList(usuarioCompartilhado);
    }

    public void setUpdate_at(LocalDateTime deleted_at) {
        this.deleted_at = deleted_at;
    }
     
    public void setDeleted_at(LocalDateTime deleted_at) {
        this.deleted_at = deleted_at;
    }

    public void setEstimativa(Estimativa estimativa) {
        this.estimativa = estimativa;
    }
    
    public void adicionarUsuarioCompartilhado(Usuario usuario){
        this.usuarioCompartilhado.add(usuario);
    }
    
}
