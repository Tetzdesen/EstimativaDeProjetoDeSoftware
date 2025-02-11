package com.br.estimativadeprojetodesoftware.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private Map<String, Double> taxasDiarias;
    private Estimativa estimativa;

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
        this.taxasDiarias = new HashMap<>();
        this.estimativa = null;
    }

    public Projeto(UUID id, String nome, String criador, String tipo, LocalDateTime created_at, LocalDateTime update_at, LocalDateTime deleted_at, String status, boolean compartilhado, String compartilhadoPor, List<Perfil> perfis, Map<String, Double> taxasDiarias, Estimativa estimativa) {
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
        this.taxasDiarias = taxasDiarias;
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
    
    public Map<String, Double> getTaxasDiarias() {
        return Collections.unmodifiableMap(taxasDiarias);
    }

    public Estimativa getEstimativa() {
        return estimativa;
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
        if(compartilhadoPor.isEmpty() || compartilhadoPor == null){
            throw new IllegalArgumentException("Erro: Compartilhado Por vazio ou nulo. ");
        }
        this.compartilhadoPor = compartilhadoPor;
    }
    
    public void adicionarPerfil(Perfil perfil){
        if(perfil == null){
            throw new IllegalArgumentException("Erro: Perfil de projeto não pode ser nulo.");
        }   
        perfis.add(perfil);
    }
    
    public void adicionarTaxaDiaria(String nomeTaxa, double valor) {
        if (nome == null || nome.isEmpty()) {
            throw new IllegalArgumentException("Erro: Nome da taxa não pode ser vazia ou nula.");
        }
        if (valor < 0) {
            throw new IllegalArgumentException("Erro: Valor não pode ser negativo. Nome da taxa: " + nome + " Valor: " + valor);
        }
        taxasDiarias.put(nomeTaxa, valor);
    }
}
