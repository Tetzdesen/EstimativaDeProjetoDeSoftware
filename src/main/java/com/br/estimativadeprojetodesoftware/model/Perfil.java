package com.br.estimativadeprojetodesoftware.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Perfil de estimativa: modelo de funcionalidades, valores por plataforma e
 * taxas que serve de base para projetos. Participa do padrão Prototype por meio
 * de {@link #copiarParaProjeto()}.
 */
public class Perfil {

    private final UUID id;
    private String nome;
    private final boolean padrao;
    private final UUID donoId;
    private final List<Funcionalidade> funcionalidades;
    private TaxaDiaria taxas;
    private final LocalDateTime criadoEm;

    public Perfil(UUID id, String nome, boolean padrao, UUID donoId, TaxaDiaria taxas, LocalDateTime criadoEm) {
        this.id = Objects.requireNonNull(id);
        setNome(nome);
        this.padrao = padrao;
        this.donoId = donoId;
        this.funcionalidades = new ArrayList<>();
        this.taxas = taxas == null ? new TaxaDiaria() : taxas;
        this.criadoEm = criadoEm == null ? LocalDateTime.now() : criadoEm;
    }

    public Perfil(String nome, UUID donoId) {
        this(UUID.randomUUID(), nome, false, donoId, new TaxaDiaria(), LocalDateTime.now());
    }

    /**
     * Prototype: devolve cópias profundas das funcionalidades, prontas para
     * compor um projeto sem compartilhar estado com o perfil.
     */
    public List<ItemProjeto> copiarParaProjeto() {
        List<ItemProjeto> itens = new ArrayList<>();
        for (Funcionalidade f : getFuncionalidades()) {
            itens.add(new ItemProjeto(f.copiar(), false));
        }
        return itens;
    }

    /** Cópia profunda do perfil inteiro, com novo id, para virar um perfil personalizado. */
    public Perfil clonar(String novoNome, UUID novoDono) {
        Perfil copia = new Perfil(UUID.randomUUID(), novoNome, false, novoDono, taxas.copiar(), LocalDateTime.now());
        for (Funcionalidade f : funcionalidades) {
            copia.adicionarFuncionalidade(f.copiar());
        }
        return copia;
    }

    public void adicionarFuncionalidade(Funcionalidade funcionalidade) {
        Objects.requireNonNull(funcionalidade, "Funcionalidade não pode ser nula");
        for (Funcionalidade f : funcionalidades) {
            if (f.mesmaChave(funcionalidade)) {
                throw new IllegalArgumentException("Já existe a funcionalidade \"" + funcionalidade.getNome()
                        + "\" na categoria \"" + funcionalidade.getCategoria() + "\"");
            }
        }
        funcionalidades.add(funcionalidade);
    }

    public void removerFuncionalidade(UUID idFuncionalidade) {
        funcionalidades.removeIf(f -> f.getId().equals(idFuncionalidade));
    }

    /** Funcionalidades ordenadas pela ordem de exibição. */
    public List<Funcionalidade> getFuncionalidades() {
        List<Funcionalidade> ordenadas = new ArrayList<>(funcionalidades);
        ordenadas.sort(Comparator.comparingInt(Funcionalidade::getOrdem));
        return Collections.unmodifiableList(ordenadas);
    }

    public int proximaOrdem() {
        return funcionalidades.stream().mapToInt(Funcionalidade::getOrdem).max().orElse(0) + 1;
    }

    public UUID getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public final void setNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome do perfil não pode ser vazio");
        }
        this.nome = nome.trim();
    }

    public boolean isPadrao() {
        return padrao;
    }

    public UUID getDonoId() {
        return donoId;
    }

    public TaxaDiaria getTaxas() {
        return taxas;
    }

    public void setTaxas(TaxaDiaria taxas) {
        this.taxas = Objects.requireNonNull(taxas);
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    @Override
    public String toString() {
        // o perfil semeado já se chama "Perfil padrão (...)": não repete a palavra
        return padrao && !nome.toLowerCase().contains("padrão") ? nome + " (padrão)" : nome;
    }
}
