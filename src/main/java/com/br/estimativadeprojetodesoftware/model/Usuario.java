package com.br.estimativadeprojetodesoftware.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import com.br.estimativadeprojetodesoftware.builder.AndroidBuilder;
import com.br.estimativadeprojetodesoftware.builder.Diretor;
import com.br.estimativadeprojetodesoftware.builder.IosBuilder;
import com.br.estimativadeprojetodesoftware.builder.WebBackEndBuilder;
import com.br.estimativadeprojetodesoftware.service.PerfilRepositoryService;

public class Usuario {

    private UUID id;
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
        this.projetos = new ArrayList<>();
        this.perfis = new ArrayList<>();
        PerfilRepositoryService.getInstancia().salvar(Diretor.build(new WebBackEndBuilder("Web/Back end")));
        PerfilRepositoryService.getInstancia().salvar(Diretor.build(new IosBuilder("iOS")));
        PerfilRepositoryService.getInstancia().salvar(Diretor.build(new AndroidBuilder("Android")));
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
        PerfilRepositoryService.getInstancia().salvar(Diretor.build(new WebBackEndBuilder("Web/Back end")));
        PerfilRepositoryService.getInstancia().salvar(Diretor.build(new IosBuilder("iOS")));
        PerfilRepositoryService.getInstancia().salvar(Diretor.build(new AndroidBuilder("Android")));
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
    public String toString() {
        return "Usuario{" + "id=" + id + ", nome=" + nome + ", email=" + email + ", senha=" + senha + ", created_at=" + created_at + ", log=" + log + ", projetos=" + projetos + ", perfis=" + perfis + '}';
    }

}
