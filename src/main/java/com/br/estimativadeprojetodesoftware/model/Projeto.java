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
    private List<Usuario> usuarioCompartilhado;

    public Projeto(String nome) {
        this.id = UUID.randomUUID();
        this.nome = nome;
        this.created_at = LocalDateTime.now();
        this.usuarioCompartilhado = new ArrayList();
    }

    public Projeto(UUID id, String nome, LocalDateTime created_at, LocalDateTime update_at, LocalDateTime deleted_at, Estimativa estimativa, List<Usuario> usuarioCompartilhado) {
        this.id = id;
        this.nome = nome;
        this.created_at = created_at;
        this.update_at = update_at;
        this.deleted_at = deleted_at;
        this.estimativa = estimativa;
        this.usuarioCompartilhado = usuarioCompartilhado;
    }

    public UUID getId() {
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

    public void setEstimativa(Estimativa estimativa) {
        if (estimativa == null) {
            throw new IllegalArgumentException("Erro: Estimativa não pode ser nula.");
        }
        this.estimativa = estimativa;
    }

    public void adicionarUsuarioCompartilhado(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("Erro: Usuário não pode ser nulo.");
        }
        usuarioCompartilhado.add(usuario);
    }
}
