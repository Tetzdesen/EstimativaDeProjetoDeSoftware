package com.br.estimativadeprojetodesoftware.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Usuario {

    private UUID idUsuario;
    private String nome;
    private String email;
    private String senha;
    private Boolean isAutorizado;
    private LocalDateTime created_at;
    private LocalDateTime update_at;
    private LocalDateTime deleted_at;
    private List<Projeto> projetos;
    private List<Perfil> perfis;

    public Usuario(String nome, String email, String senha) {
        this.idUsuario = UUID.randomUUID();
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.isAutorizado = false;
        this.created_at = LocalDateTime.now();
        this.projetos = new ArrayList();
        this.perfis = new ArrayList();
    }

    public UUID getId() {
        return idUsuario;
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

    public Boolean isIsAutorizado() {
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
        return Collections.unmodifiableList(this.projetos);
    }

    public List<Perfil> getPerfis() {
        return Collections.unmodifiableList(this.perfis);
    }

    public void setId(UUID idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setNome(String nome) {
        if (nome == null) {
            throw new RuntimeException("Erro: Nome nula");
        }
        this.nome = nome;
    }

    public void setEmail(String email) {
        if (email == null) {
            throw new RuntimeException("Erro: E-mail nula");
        }
        this.email = email;
    }

    public void setSenha(String senha) {
        if (senha == null) {
            throw new RuntimeException("Erro: Senha nula");
        }
        this.senha = senha;
    }

    public void setIsAutorizado(Boolean isAutorizado) {
        this.isAutorizado = isAutorizado;
    }

    public void setCreated_at(LocalDateTime created_at) {
        if (created_at == null) {
            throw new RuntimeException("Erro: Data e hora de criação nula");
        }
        this.created_at = created_at;
    }
    
    public void setUpdate_at(LocalDateTime created_at) {
        if (created_at == null) {
            throw new RuntimeException("Erro: Data e hora de criação nula");
        }
        this.created_at = created_at;
    }

    public void setDeleted_at(LocalDateTime deleted_at) {
        if (deleted_at == null) {
            throw new RuntimeException("Erro: Data e hora de deletado nula");
        }
        this.deleted_at = deleted_at;
    }
    
    public void adicionarProjeto(Projeto projeto){
        this.projetos.add(projeto);
    }
    
    public void adicionarPerfil(Perfil perfil){
        this.perfis.add(perfil);
    }
    
}
