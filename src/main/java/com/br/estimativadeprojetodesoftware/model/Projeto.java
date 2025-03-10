package com.br.estimativadeprojetodesoftware.model;

import com.br.estimativadeprojetodesoftware.singleton.UsuarioLogadoSingleton;
import java.time.LocalDateTime;
import java.util.*;

public class Projeto {

    private final UUID id;
    private String nome;
    private String criador;
    private String tipo;
    private final LocalDateTime created_at;
    private String status;
    private boolean compartilhado;
    private String compartilhadoPor;
    private final List<Perfil> perfis;
    private final List<Usuario> usuarios;
    private Estimativa estimativa;
    
    
    // lembrar set para compartilhado, compartilhadoPor e estimativa

    public Projeto(String nome, String criador) {
        this(nome, criador, null);
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
    }
     
    public Projeto(UUID id, String nome, String tipo, LocalDateTime created_at, String status) {
        this.id = Objects.requireNonNull(id, "ID não pode ser nulo");
        this.nome = Objects.requireNonNull(nome, "Nome não pode ser nulo");
        this.criador = UsuarioLogadoSingleton.getInstancia().getUsuario().getNome();
        this.tipo = tipo;
        this.created_at = Objects.requireNonNull(created_at, "Data de criação não pode ser nula");
        this.compartilhado = false;
        this.compartilhadoPor = "";
        this.status = Objects.requireNonNullElse(status, "Não estimado");
        this.perfis = new ArrayList<>();
        this.usuarios = new ArrayList<>();
        this.estimativa = null;
    }

    public Projeto(UUID id, String nome, String criador, String tipo, LocalDateTime created_at, String status, boolean compartilhado, String compartilhadoPor, List<Perfil> perfis, List<Usuario> usuarios, Estimativa estimativa) {
        this.id = id;
        this.nome = nome;
        this.criador = criador;
        this.tipo = tipo;
        this.created_at = created_at;
        this.status = status;
        this.compartilhado = compartilhado;
        this.compartilhadoPor = compartilhadoPor;
        this.perfis = perfis;
        this.usuarios = usuarios;
        this.estimativa = estimativa;
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

    public String getCompartilhadoPor() {
        return compartilhadoPor;
    }

    public List<Perfil> getPerfis() {
        return Collections.unmodifiableList(perfis);
    }

    public List<Usuario> getUsuarios() {
        return Collections.unmodifiableList(usuarios);
    }

    public Estimativa getEstimativa() {
        return estimativa;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setEstimativa(Estimativa estimativa) {
        this.estimativa = Objects.requireNonNull(estimativa, "Estimativa não pode ser nula.");
    }

    public void setCompartilhado(boolean compartilhado) {
        this.compartilhado = compartilhado;
    }

    public void setCompartilhadoPor(String compartilhadoPor) {
        if (compartilhadoPor == null || compartilhadoPor.isEmpty()) {
            throw new IllegalArgumentException("Erro: Compartilhado Por vazio ou nulo.");
        }
        this.compartilhadoPor = Objects.requireNonNullElse(compartilhadoPor, "").trim();
    }

    public void adicionarPerfil(Perfil perfil) {
        perfis.add(Objects.requireNonNull(perfil, "Perfil não pode ser nulo."));
    }

    public void adicionarUsuario(Usuario usuario) {
        usuarios.add(Objects.requireNonNull(usuario, "Usuário não pode ser nulo."));
    }

    public void removerPerfil(Perfil perfil) {
        perfis.remove(Objects.requireNonNull(perfil, "Perfil não pode ser nulo."));
    }

    public void removerUsuario(Usuario usuario) {
        usuarios.remove(Objects.requireNonNull(usuario, "Usuário não pode ser nulo."));
    }

    @Override
    public String toString() {
        return "Projeto{" + "id=" + id + ", nome=" + nome + ", criador=" + criador + ", tipo=" + tipo + ", created_at=" + created_at + ", status=" + status + ", compartilhado=" + compartilhado + ", compartilhadoPor=" + compartilhadoPor + ", perfis=" + perfis + ", usuarios=" + usuarios + ", estimativa=" + estimativa + '}';
    }
}
