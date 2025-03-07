package com.br.estimativadeprojetodesoftware.model;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class Perfil {

    private UUID id;
    private String nome;
    private Map<String, Integer> tamanhosApp;
    private Map<String, Double> niveisUI;
    private Map<String, Integer> funcionalidades;
    private boolean isPerfilBackEnd;
    private LocalDateTime created_at;
    private LocalDateTime update_at;
    private LocalDateTime deleted_at;

    public Perfil(String nome, Map<String, Integer> tamanhosApp, Map<String, Double> niveisUI) {
        validaTamanhosApp(tamanhosApp);
        validaNiveisUI(niveisUI);

        this.id = UUID.randomUUID();
        this.nome = nome;
        this.tamanhosApp = tamanhosApp;
        this.niveisUI = niveisUI;
        this.funcionalidades = new LinkedHashMap<>();
        this.created_at = LocalDateTime.now();
        this.update_at = null;
        this.deleted_at = null;
    }

    public Perfil(  UUID id, String nome, 
                    Map<String, Integer> tamanhosApp, 
                    Map<String, Double> niveisUI, 
                    Map<String, Integer> funcionalidades, 
                    boolean isPerfilBackEnd, 
                    LocalDateTime created_at, 
                    LocalDateTime update_at, 
                    LocalDateTime deleted_at
        ) {
        this.id = id;
        this.nome = nome;
        this.tamanhosApp = tamanhosApp;
        this.niveisUI = niveisUI;
        this.funcionalidades = funcionalidades;
        this.isPerfilBackEnd = isPerfilBackEnd;
        this.created_at = created_at;
        this.update_at = update_at;
        this.deleted_at = deleted_at;
    }

    public UUID getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public Map<String, Integer> getTamanhosApp() {
        return Collections.unmodifiableMap(tamanhosApp);
    }

    public Map<String, Double> getNiveisUI() {
        return Collections.unmodifiableMap(niveisUI);
    }

    public Map<String, Integer> getFuncionalidades() {
        return Collections.unmodifiableMap(funcionalidades);
    }

    public boolean isPerfilBackEnd() {
        return isPerfilBackEnd;
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

    public void setNome(String nome) {
        this.nome = nome;
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

    public void adicionarTamanhoApp(String tamanhoApp, int dias) {
        if (nome == null || nome.isEmpty()) {
            throw new IllegalArgumentException("Erro: Nome da funcionalidade não pode ser vazio ou nula.");
        }
        if (dias < 0) {
            throw new IllegalArgumentException("Erro: dias não pode ser negativo. Nome da funcionalidade: " + nome + " dias: " + dias);
        }

        if (!tamanhosApp.keySet().contains(tamanhoApp)) {
            throw new IllegalArgumentException("O tamanho do app deve ser: pequeno, médio ou grande");
        }

        funcionalidades.putIfAbsent(tamanhoApp, dias);
    }

    public void adicionarNivelUI(String nivelUI, double dias) {
        if (nome == null || nome.isEmpty()) {
            throw new IllegalArgumentException("Erro: Nome da funcionalidade não pode ser vazio ou nula.");
        }
        if (dias < 0) {
            throw new IllegalArgumentException("Erro: dias não pode ser negativo. Nome da funcionalidade: " + nome + " dias: " + dias);
        }

        if (!niveisUI.keySet().contains(nivelUI)) {
            throw new IllegalArgumentException("O nível de UI deve ser: MVP, Básico ou Profissional");
        }

        niveisUI.putIfAbsent(nivelUI, dias);
    }

    public void adicionarFuncionalidade(String nomeFuncionalidade, int dias) {
        if (nome == null || nome.isEmpty()) {
            throw new IllegalArgumentException("Erro: Nome da funcionalidade não pode ser vazio ou nula.");
        }
        if (dias < 0) {
            throw new IllegalArgumentException("Erro: dias não pode ser negativo. Nome da funcionalidade: " + nome + " dias: " + dias);
        }
        funcionalidades.put(nomeFuncionalidade, dias);
    }

    public void removerFuncionalidades() {
        funcionalidades = new LinkedHashMap<>();
    }

    public void setPerfilBackEnd(boolean isPerfilBackEnd) {
        this.isPerfilBackEnd = isPerfilBackEnd;
    }

    @Override
    public int hashCode() {
        int hash = 7;
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
        final Perfil other = (Perfil) obj;
        if (!Objects.equals(this.nome, other.nome)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.funcionalidades, other.funcionalidades)) {
            return false;
        }
        if (!Objects.equals(this.created_at, other.created_at)) {
            return false;
        }
        if (!Objects.equals(this.update_at, other.update_at)) {
            return false;
        }
        return Objects.equals(this.deleted_at, other.deleted_at);
    }

    private void validaTamanhosApp(Map<String, Integer> tamanhosApp) {
        if (!tamanhosApp.keySet().equals(new HashSet<>(Arrays.asList("Pequeno", "Médio", "Grande")))) {
            throw new IllegalArgumentException("Os tamanhos do app devem ser: pequeno, médio e grande");
        }

        for (Integer valor : tamanhosApp.values()) {
            if (valor <= 0) {
                throw new IllegalArgumentException("Os valores de tamanhos do app devem ser maiores do que zero");
            }
        }
    }

    private void validaNiveisUI(Map<String, Double> niveisUI) {
        if (!niveisUI.keySet().equals(new HashSet<>(Arrays.asList("MVP", "Básico", "Profissional")))) {
            throw new IllegalArgumentException("Os níveis de UI devem ser: MVP, Básico e Profissional");
        }

        for (Double valor : niveisUI.values()) {
            if (valor <= 0) {
                throw new IllegalArgumentException("Os valores de tamanhos do app devem ser maiores do que zero");
            }
        }
    }

    @Override
    public String toString() {
        return "Perfil{" + "id=" + id + ", nome=" + nome + ", funcionalidades=" + funcionalidades + ", created_at=" + created_at + ", update_at=" + update_at + ", deleted_at=" + deleted_at + '}';
    }
}
