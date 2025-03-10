package com.br.estimativadeprojetodesoftware.model;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class Perfil {

    private final UUID id;
    private String nome;
    private Map<String, Integer> tamanhosApp;
    private Map<String, Double> niveisUI;
    private Map<String, Integer> funcionalidades;
    private Map<String, Double> taxasDiarias;
    private boolean isPerfilBackEnd;

    public Perfil(String nome) {
        this.id = UUID.randomUUID();
        this.nome = nome;
        this.tamanhosApp = new LinkedHashMap<>();
        this.niveisUI = new LinkedHashMap<>();
        this.funcionalidades = new LinkedHashMap<>();
        this.taxasDiarias = new LinkedHashMap<>();

        adicionarCamposDefault();
    }

    public Perfil(  UUID id, String nome, 
                    Map<String, Integer> tamanhosApp, 
                    Map<String, Double> niveisUI, 
                    Map<String, Integer> funcionalidades, 
                    Map<String, Double> taxasDiarias,
                    boolean isPerfilBackEnd
        ) {
        this.id = id;
        this.nome = nome;
        this.tamanhosApp = tamanhosApp;
        this.niveisUI = niveisUI;
        this.funcionalidades = funcionalidades;
        this.taxasDiarias = taxasDiarias;
        this.isPerfilBackEnd = isPerfilBackEnd;
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

    public Map<String, Double> getTaxasDiarias() {
        return Collections.unmodifiableMap(taxasDiarias);
    }

    public boolean isPerfilBackEnd() {
        return isPerfilBackEnd;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void adicionarTamanhoApp(String tamanhoApp, int dias) {
        if (tamanhoApp == null || tamanhoApp.isEmpty()) {
            throw new IllegalArgumentException("Erro: Tamanho do App não pode ser vazio ou nula.");
        }
        if (dias < 0) {
            throw new IllegalArgumentException("Erro: dias não pode ser negativo. Tamanho do App: " + tamanhoApp + " dias: " + dias);
        }

        if (!tamanhosApp.containsKey(tamanhoApp.toLowerCase())) {
            throw new IllegalArgumentException("O tamanho do app deve ser: pequeno, médio ou grande");
        }

        tamanhosApp.put(tamanhoApp.toLowerCase(), dias);
    }

    public void adicionarNivelUI(String nivelUI, double valor) {
        if (nivelUI == null || nivelUI.isEmpty()) {
            throw new IllegalArgumentException("Erro: Nível de UI não pode ser vazio ou nula.");
        }
        if (valor < 0) {
            throw new IllegalArgumentException("Erro: valor não pode ser negativo. Nível de UI: " + nivelUI + " valor: " + valor);
        }

        if (!niveisUI.containsKey(nivelUI.toLowerCase())) {
            throw new IllegalArgumentException("O nível de UI deve ser: mvp, básico ou profissional");
        }

        niveisUI.put(nivelUI.toLowerCase(), valor);
    }

    public void adicionarFuncionalidade(String nomeFuncionalidade, int dias) {
        if (nomeFuncionalidade == null || nomeFuncionalidade.isEmpty()) {
            throw new IllegalArgumentException("Erro: Nome da funcionalidade não pode ser vazio ou nula.");
        }
        if (dias < 0) {
            throw new IllegalArgumentException("Erro: dias não pode ser negativo. Nome da funcionalidade: " + nomeFuncionalidade + " dias: " + dias);
        }
        funcionalidades.put(nomeFuncionalidade, dias);
    }

    public void adicionarTaxaDiaria(String taxaDiaria, double valor) {
        if (taxaDiaria == null || taxaDiaria.isEmpty()) {
            throw new IllegalArgumentException("Erro: Taxa Diária não pode ser vazio ou nula.");
        }
        if (valor < 0) {
            throw new IllegalArgumentException("Erro: valor não pode ser negativo. Taxa Diária: " + taxaDiaria + " valor: " + valor);
        }

        if (!taxasDiarias.containsKey(taxaDiaria.toLowerCase())) {
            throw new IllegalArgumentException("A taxa diária deve ser: designer ui/ux, gerência de projeto ou desenvolvimento");
        }

        taxasDiarias.put(taxaDiaria.toLowerCase(), valor);
    }

    public void removerFuncionalidades() {
        funcionalidades.clear();
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
        return true;
    }

    private void adicionarCamposDefault() {
        this.tamanhosApp.put("pequeno", 0);
        this.tamanhosApp.put("médio", 0);
        this.tamanhosApp.put("grande", 0);

        this.niveisUI.put("mvp", 0.0);
        this.niveisUI.put("básico", 0.0);
        this.niveisUI.put("profissional", 0.0);

        this.taxasDiarias.put("designer ui/ux", 0.0);
        this.taxasDiarias.put("gerência de projeto", 0.0);
        this.taxasDiarias.put("desenvolvimento", 0.0);
    }

    @Override
    public String toString() {
        return "Perfil [id=" + id + ", nome=" + nome + ", tamanhosApp=" + tamanhosApp + ", niveisUI=" + niveisUI
                + ", funcionalidades=" + funcionalidades + ", taxasDiarias=" + taxasDiarias + ", isPerfilBackEnd="
                + isPerfilBackEnd + "]";
    }
}
