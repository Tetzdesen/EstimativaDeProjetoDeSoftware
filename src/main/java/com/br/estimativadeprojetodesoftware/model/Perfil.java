package com.br.estimativadeprojetodesoftware.model;

import com.br.estimativadeprojetodesoftware.singleton.UsuarioLogadoSingleton;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class Perfil {

    private final UUID id;
    private String nome;
    private Map<String, Integer> tamanhosApp;
    private Map<String, Double> niveisUI;
    private Map<String, Integer> funcionalidades;
    private Map<String, Double> taxasDiarias;
    private boolean isPerfilBackEnd;
    private final LocalDateTime created_at;
    private LocalDateTime update_at;
    private LocalDateTime deleted_at;
    private Usuario usuario;

    public Perfil(String nome) {
        this.id = UUID.randomUUID();
        this.nome = nome;
        this.tamanhosApp = new LinkedHashMap<>();
        this.niveisUI = new LinkedHashMap<>();
        this.funcionalidades = new LinkedHashMap<>();
        this.taxasDiarias = new LinkedHashMap<>();
        this.created_at = LocalDateTime.now();
        this.update_at = null;
        this.deleted_at = null;
        this.usuario = UsuarioLogadoSingleton.getInstancia().getUsuario();
        adicionarCamposDefault();
    }

    public Perfil(UUID id, String nome,
            boolean isPerfilBackEnd,
            LocalDateTime created_at, Usuario usuario
    ) {
        this.id = id;
        this.nome = nome;
        this.isPerfilBackEnd = isPerfilBackEnd;
        this.created_at = created_at;
        this.usuario = usuario;
    }

    public Perfil(UUID id, String nome,
            Map<String, Integer> tamanhosApp,
            Map<String, Double> niveisUI,
            Map<String, Integer> funcionalidades,
            Map<String, Double> taxasDiarias,
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
        this.taxasDiarias = taxasDiarias;
        this.isPerfilBackEnd = isPerfilBackEnd;
        this.created_at = created_at;
        this.update_at = update_at;
        this.deleted_at = deleted_at;
        this.usuario = UsuarioLogadoSingleton.getInstancia().getUsuario();
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

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public LocalDateTime getUpdate_at() {
        return update_at;
    }

    public LocalDateTime getDeleted_at() {
        return deleted_at;
    }

    public Usuario getUsuario() {
        return usuario;
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
        return "Perfil{" + "id=" + id + ", nome=" + nome + ", funcionalidades=" + funcionalidades + ", created_at=" + created_at + ", update_at=" + update_at + ", deleted_at=" + deleted_at + '}';
    }
}
