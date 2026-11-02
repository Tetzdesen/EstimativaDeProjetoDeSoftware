package com.br.estimativadeprojetodesoftware.model;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Usuario {

    private final UUID id;
    private String nome;
    private String email;
    private String senha;
    private final LocalDateTime created_at;

    public Usuario(String nome, String email, String senha) {
        this(UUID.randomUUID(), nome, email, senha, LocalDateTime.now());
    }

    public Usuario(UUID id, String nome, String email, String senha, LocalDateTime created_at) {
        this.id = Objects.requireNonNull(id);
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.created_at = created_at == null ? LocalDateTime.now() : created_at;
    }

    public UUID getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getSenha() {
        return senha;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Usuario outro && outro.id.equals(id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Usuario{" + "id=" + id + ", nome=" + nome + ", email=" + email + ", senha=****, created_at=" + created_at + '}';
    }

}
