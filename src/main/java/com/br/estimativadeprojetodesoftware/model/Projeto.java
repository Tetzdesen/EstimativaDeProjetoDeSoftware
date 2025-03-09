package com.br.estimativadeprojetodesoftware.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class Projeto {

    private UUID id;
    private String nome;
    private String criador;
    private String tipo;
    private LocalDateTime created_at;
    private LocalDateTime update_at;
    private LocalDateTime deleted_at;
    private String status;
    private boolean compartilhado;
    private String compartilhadoPor;
    private List<Perfil> perfis;
    private List<Usuario> usuarios;
    private Estimativa estimativa;

    public Projeto(String nome, String criador) {
        this.id = UUID.randomUUID();
        this.nome = nome;
        this.criador = criador;
        this.tipo = null;
        this.created_at = LocalDateTime.now();
        this.update_at = null;
        this.deleted_at = null;
        this.status = "Não estimado";
        this.compartilhado = false;
        this.compartilhadoPor = null;
        this.perfis = new ArrayList<>();
        this.usuarios = new ArrayList<>();
        this.estimativa = null;
    }
    
    public Projeto(String nome, String criador, String tipo) {
        this.id = UUID.randomUUID();
        this.nome = nome;
        this.criador = criador;
        this.tipo = tipo;
        this.created_at = LocalDateTime.now();
        this.update_at = null;
        this.deleted_at = null;
        this.status = "Não estimado";
        this.compartilhado = false;
        this.compartilhadoPor = null;
        this.perfis = new ArrayList<>();
        this.usuarios = new ArrayList<>();
        this.estimativa = null;
    }

    public Projeto(UUID id, String nome, String criador, String tipo, LocalDateTime created_at, LocalDateTime update_at, LocalDateTime deleted_at, String status, boolean compartilhado, String compartilhadoPor, List<Perfil> perfis, List<Usuario> usuarios, Estimativa estimativa) {
        this.id = id;
        this.nome = nome;
        this.criador = criador;
        this.tipo = tipo;
        this.created_at = created_at;
        this.update_at = update_at;
        this.deleted_at = deleted_at;
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

    public LocalDateTime getUpdate_at() {
        return update_at;
    }

    public LocalDateTime getDeleted_at() {
        return deleted_at;
    }

    public String getStatus() {
        return status;
    }

    public boolean getCompartilhado() {
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

    public void setCompartilhado(boolean compartilhado) {
        this.compartilhado = compartilhado;
    }

    public void setCompartilhadoPor(String compartilhadoPor) {
        if (compartilhadoPor.isEmpty() || compartilhadoPor == null) {
            throw new IllegalArgumentException("Erro: Compartilhado Por vazio ou nulo. ");
        }
        this.compartilhadoPor = compartilhadoPor;
    }

    public void adicionarPerfil(Perfil perfil) {
        if (perfil == null) {
            throw new IllegalArgumentException("Erro: Perfil de projeto não pode ser nulo.");
        }
        perfis.add(perfil);
    }
    
    public void adicionarUsuario(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("Erro: Perfil de projeto não pode ser nulo.");
        }
        usuarios.add(usuario);
    }
    
    public void removerPerfil(Perfil perfil) {
        if (perfil == null) {
            throw new IllegalArgumentException("Erro: Perfil de projeto não pode ser nulo.");
        }
        perfis.remove(perfil);
    }
    
    public void removerUsuario(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("Erro: Perfil de projeto não pode ser nulo.");
        }
        usuarios.remove(usuario);
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
        final Projeto other = (Projeto) obj;
        if (this.compartilhado != other.compartilhado) {
            return false;
        }
        if (!Objects.equals(this.nome, other.nome)) {
            return false;
        }
        if (!Objects.equals(this.criador, other.criador)) {
            return false;
        }
        if (!Objects.equals(this.tipo, other.tipo)) {
            return false;
        }
        if (!Objects.equals(this.status, other.status)) {
            return false;
        }
        if (!Objects.equals(this.compartilhadoPor, other.compartilhadoPor)) {
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
        if (!Objects.equals(this.perfis, other.perfis)) {
            return false;
        }
        if (!Objects.equals(this.usuarios, other.usuarios)) {
            return false;
        }
        return Objects.equals(this.estimativa, other.estimativa);
    }

    @Override
    public String toString() {
        return "Projeto{" + "id=" + id + ", nome=" + nome + ", criador=" + criador + ", tipo=" + tipo + ", created_at=" + created_at + ", update_at=" + update_at + ", deleted_at=" + deleted_at + ", status=" + status + ", compartilhado=" + compartilhado + ", compartilhadoPor=" + compartilhadoPor + ", perfis=" + perfis + ", usuarios=" + usuarios + ", estimativa=" + estimativa + '}';
    }

}
