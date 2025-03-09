package com.br.estimativadeprojetodesoftware.model;

import java.awt.Image;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Usuario {

    private UUID id;
    private Image rosto;
    private String nome;
    private String email;
    private String senha;
    private LocalDateTime created_at;
    private String log;
    private List<Projeto> projetos;
    private List<Perfil> perfis;

    public Usuario(String nome, String email, String senha) {
        this.id = UUID.randomUUID();
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.created_at = LocalDateTime.now();
        this.log = "";
        this.projetos = new ArrayList();
        this.perfis = new ArrayList();
    }

    public Usuario(UUID id, String nome, String email, String senha, LocalDateTime created_at, String log, List<Projeto> projetos, List<Perfil> perfis) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.created_at = created_at;
        this.log = log;
        this.projetos = projetos;
        this.perfis = perfis;
    }

    public UUID getId() {
        return id;
    }

    public Image getRosto() {
        return rosto;
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

    public String getLog() {
        return log;
    }

    public List<Projeto> getProjetos() {
        return Collections.unmodifiableList(projetos);
    }

    public List<Perfil> getPerfis() {
        return Collections.unmodifiableList(perfis);
    }

    public String setLog() {
        return log;
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

    public void removerProjeto(Projeto projeto) {
        if (projeto == null) {
            throw new IllegalArgumentException("Erro: Projeto não pode ser nulo.");
        }
        projetos.remove(projeto);
    }

    public void removerPerfil(Perfil perfil) {
        if (perfil == null) {
            throw new IllegalArgumentException("Erro: Perfil não pode ser nulo.");
        }
        perfis.remove(perfil);
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
        if (this.log != other.log) {
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
        if (!Objects.equals(this.rosto, other.rosto)) {
            return false;
        }
        if (!Objects.equals(this.created_at, other.created_at)) {
            return false;
        }
        if (!Objects.equals(this.projetos, other.projetos)) {
            return false;
        }
        return Objects.equals(this.perfis, other.perfis);
    }

    @Override
    public String toString() {
        return "Usuario{" + "id=" + id + ", rosto=" + rosto + ", nome=" + nome + ", email=" + email + ", senha=" + senha + ", created_at=" + created_at + ", log=" + log + ", projetos=" + projetos + ", perfis=" + perfis + '}';
    }

}
