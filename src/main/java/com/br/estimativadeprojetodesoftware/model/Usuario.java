package com.br.estimativadeprojetodesoftware.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Usuario {

    private UUID id;
    private String nome;
    private String email;
    private String senha;
    private boolean isAutorizado;
    private LocalDateTime created_at;
    private LocalDateTime update_at;
    private LocalDateTime deleted_at;
    private List<Projeto> projetos;
    private List<Perfil> perfis;

    public Usuario(String nome, String email, String senha) {
        this.id = UUID.randomUUID();
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.isAutorizado = false;
        this.created_at = LocalDateTime.now();
        this.update_at = null;
        this.deleted_at = null;
        this.projetos = new ArrayList();
        this.perfis = new ArrayList();
    }

    public Usuario(UUID id, String nome, String email, String senha, boolean isAutorizado, LocalDateTime created_at, LocalDateTime update_at, LocalDateTime deleted_at, List<Projeto> projetos, List<Perfil> perfis) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.isAutorizado = isAutorizado;
        this.created_at = created_at;
        this.update_at = update_at;
        this.deleted_at = deleted_at;
        this.projetos = projetos;
        this.perfis = perfis;
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

    public boolean getIsAutorizado() {
        return isAutorizado;
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

    public List<Projeto> getProjetos() {
        return Collections.unmodifiableList(projetos);
    }

    public List<Perfil> getPerfis() {
        return Collections.unmodifiableList(perfis);
    }

    public void setIsAutorizado(Boolean isAutorizado) {
        this.isAutorizado = isAutorizado;
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

    public void adicionarProjeto(Projeto projeto) {
        if (projeto == null) {
            throw new IllegalArgumentException("Erro: Projeto não pode ser nulo.");
        }
        projetos.add(projeto);
    }

    public void adicionarPerfil(Perfil perfil) {
        if (perfil == null) {
            throw new IllegalArgumentException("Erro: Perfil não pode ser nulo.");
        }
        perfis.add(perfil);
    }

    @Override
    public int hashCode() {
        int hash = 3;
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
        final Usuario other = (Usuario) obj;
        if (this.isAutorizado != other.isAutorizado) {
            return false;
        }
        if (!Objects.equals(this.nome, other.nome)) {
            return false;
        }
        if (!Objects.equals(this.email, other.email)) {
            return false;
        }
        if (!Objects.equals(this.senha, other.senha)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.created_at, other.created_at)) {
            return false;
        }
        if (!Objects.equals(this.update_at, other.update_at)) {
            return false;
        }
        if (!Objects.equals(this.deleted_at, other.deleted_at)) {
            return false;
        }
        if (!Objects.equals(this.projetos, other.projetos)) {
            return false;
        }
        return Objects.equals(this.perfis, other.perfis);
    }

    @Override
    public String toString() {
        return "Usuario{" + "id=" + id + ", nome=" + nome + ", email=" + email + ", senha=" + senha + ", isAutorizado=" + isAutorizado + ", created_at=" + created_at + ", update_at=" + update_at + ", deleted_at=" + deleted_at + ", projetos=" + projetos + ", perfis=" + perfis + '}';
    }

}
