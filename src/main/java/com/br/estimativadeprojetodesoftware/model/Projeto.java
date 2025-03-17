package com.br.estimativadeprojetodesoftware.model;

import java.time.LocalDateTime;
import java.util.*;

public class Projeto {

    private final UUID id;
    private String nome;
    private String criador;
    private String tipo;
    private LocalDateTime created_at;
    private String status;
    private boolean compartilhado;
    private List<PerfilProjeto> perfis;
    private List<Usuario> usuarios;
    private List<Campo> campos;

    public Projeto(String nome, String criador) {
        this(nome, criador, null);
    }

    public Projeto(UUID id, String nome, String status, String criador) {
        this.id = id;
        this.nome = nome;
        this.status = status;
        this.criador = criador;
        this.perfis = new ArrayList<>();
        this.usuarios = new ArrayList<>();
        this.campos = new ArrayList<>();
    }

    public Projeto(String nome, String criador, String tipo) {
        this.id = UUID.randomUUID();
        this.nome = Objects.requireNonNull(nome, "Nome não pode ser nulo");
        this.criador = Objects.requireNonNull(criador, "Criador não pode ser nulo");
        this.tipo = tipo;
        this.created_at = LocalDateTime.now();
        this.status = "Não estimado";
        this.perfis = new ArrayList<>();
        this.usuarios = new ArrayList<>();
        this.campos = new ArrayList<>();
    }

    public Projeto(String nome, String criador, String tipo, List<Campo> campos) {
        this.id = UUID.randomUUID();
        this.nome = Objects.requireNonNull(nome, "Nome não pode ser nulo");
        this.criador = Objects.requireNonNull(criador, "Criador não pode ser nulo");
        this.tipo = tipo;
        this.created_at = LocalDateTime.now();
        this.status = "Não estimado";
        this.perfis = new ArrayList<>();
        this.usuarios = new ArrayList<>();
        this.campos = new ArrayList<>();
    }

    public Projeto(UUID id, String nome, String criador, String tipo, LocalDateTime created_at, String status, boolean compartilhado, List<PerfilProjeto> perfis, List<Usuario> usuarios, List<Campo> campos) {
        this.id = id;
        this.nome = nome;
        this.criador = criador;
        this.tipo = tipo;
        this.created_at = created_at;
        this.status = status;
        this.compartilhado = compartilhado;
        this.perfis = perfis;
        this.usuarios = usuarios;
        this.campos = campos;
    }

    public UUID getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getCriador() {
        return criador;
    }

    public String getTipo() {
        return tipo;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public String getStatus() {
        return status;
    }

    public boolean isCompartilhado() {
        return compartilhado;
    }

    public List<PerfilProjeto> getPerfis() {
        return Collections.unmodifiableList(perfis);
    }

    public List<Usuario> getUsuarios() {
        return Collections.unmodifiableList(usuarios);
    }

    public List<Campo> getCampos() {
        return campos;
    }

    public void setNome(String nome) {
        if (nome.isEmpty()) {
            throw new IllegalArgumentException("Nome vazio");
        }
        this.nome = nome;
    }

    public void setTipo(String tipo) {
        if (tipo.isEmpty()) {
            throw new IllegalArgumentException("Tipo vazio");
        }
        this.tipo = tipo;
    }

    public void setStatus(String status) {
        if (status.isEmpty()) {
            throw new IllegalArgumentException("Status vazio");
        }
        this.status = status;
    }

    public void setCompartilhado(boolean compartilhado) {
        this.compartilhado = compartilhado;
    }

    public void adicionarPerfil(PerfilProjeto perfil) {
        perfis.add(Objects.requireNonNull(perfil, "Perfil não pode ser nulo."));
    }

    public void adicionarUsuario(Usuario usuario) {
        usuarios.add(Objects.requireNonNull(usuario, "Usuário não pode ser nulo."));
    }

    public void adicionarCampo(Campo campo) {
        campos.add(Objects.requireNonNull(campo, "Campo não pode ser nulo."));
    }

    public void removerPerfil(PerfilProjeto perfil) {
        perfis.remove(Objects.requireNonNull(perfil, "Perfil não pode ser nulo."));
    }

    public void removerUsuario(Usuario usuario) {
        usuarios.remove(Objects.requireNonNull(usuario, "Usuário não pode ser nulo."));
    }

    public void removerCampo(Campo campo) {
        campos.remove(Objects.requireNonNull(campo, "Campo não pode ser nulo."));
    }

    @Override
    public String toString() {
        return "Projeto{" + "id=" + id + ", nome=" + nome + ", criador=" + criador + ", tipo=" + tipo + ", created_at=" + created_at + ", status=" + status + ", compartilhado=" + compartilhado + ", perfis=" + perfis + ", usuarios=" + usuarios + ", campos=" + campos + '}';
    }

}
