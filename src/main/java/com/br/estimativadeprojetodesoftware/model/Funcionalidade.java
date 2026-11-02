package com.br.estimativadeprojetodesoftware.model;

import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * Item de um perfil ou projeto. Guarda um valor por plataforma; plataforma sem
 * valor é "não aplicável", o que é diferente de valor zero.
 */
public class Funcionalidade {

    private final UUID id;
    private String categoria;
    private String nome;
    private TipoSelecao tipoSelecao;
    private TipoValor tipoValor;
    private int ordem;
    private final Map<Plataforma, BigDecimal> valores;

    public Funcionalidade(UUID id, String categoria, String nome, TipoSelecao tipoSelecao, TipoValor tipoValor, int ordem) {
        this.id = Objects.requireNonNull(id, "Id não pode ser nulo");
        setCategoria(categoria);
        setNome(nome);
        this.tipoSelecao = Objects.requireNonNull(tipoSelecao, "Tipo de seleção não pode ser nulo");
        this.tipoValor = Objects.requireNonNull(tipoValor, "Tipo de valor não pode ser nulo");
        this.ordem = ordem;
        this.valores = new EnumMap<>(Plataforma.class);
    }

    public Funcionalidade(String categoria, String nome, TipoSelecao tipoSelecao, TipoValor tipoValor, int ordem) {
        this(UUID.randomUUID(), categoria, nome, tipoSelecao, tipoValor, ordem);
    }

    /** Define o valor na plataforma; {@code null} marca "não aplicável". */
    public void setValor(Plataforma plataforma, BigDecimal valor) {
        Objects.requireNonNull(plataforma, "Plataforma não pode ser nula");
        if (valor == null) {
            valores.remove(plataforma);
            return;
        }
        if (valor.signum() < 0) {
            throw new IllegalArgumentException("O valor de \"" + nome + "\" em " + plataforma + " não pode ser negativo");
        }
        valores.put(plataforma, valor);
    }

    public void marcarNaoAplicavel(Plataforma plataforma) {
        setValor(plataforma, null);
    }

    public boolean isAplicavel(Plataforma plataforma) {
        return valores.containsKey(plataforma);
    }

    public Optional<BigDecimal> getValor(Plataforma plataforma) {
        return Optional.ofNullable(valores.get(plataforma));
    }

    /** Valor na plataforma, ou zero quando não aplicável (para somas). */
    public BigDecimal getValorOuZero(Plataforma plataforma) {
        return valores.getOrDefault(plataforma, BigDecimal.ZERO);
    }

    /** Cópia profunda com novo id (usada pelo Prototype). */
    public Funcionalidade copiar() {
        Funcionalidade copia = new Funcionalidade(categoria, nome, tipoSelecao, tipoValor, ordem);
        copia.valores.putAll(this.valores);
        return copia;
    }

    public boolean mesmaChave(Funcionalidade outra) {
        return categoria.equalsIgnoreCase(outra.categoria) && nome.equalsIgnoreCase(outra.nome);
    }

    public UUID getId() {
        return id;
    }

    public String getCategoria() {
        return categoria;
    }

    public final void setCategoria(String categoria) {
        if (categoria == null || categoria.isBlank()) {
            throw new IllegalArgumentException("Categoria não pode ser vazia");
        }
        this.categoria = categoria.trim();
    }

    public String getNome() {
        return nome;
    }

    public final void setNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome da funcionalidade não pode ser vazio");
        }
        this.nome = nome.trim();
    }

    public TipoSelecao getTipoSelecao() {
        return tipoSelecao;
    }

    public void setTipoSelecao(TipoSelecao tipoSelecao) {
        this.tipoSelecao = Objects.requireNonNull(tipoSelecao);
    }

    public TipoValor getTipoValor() {
        return tipoValor;
    }

    public void setTipoValor(TipoValor tipoValor) {
        this.tipoValor = Objects.requireNonNull(tipoValor);
    }

    public int getOrdem() {
        return ordem;
    }

    public void setOrdem(int ordem) {
        this.ordem = ordem;
    }

    @Override
    public String toString() {
        return categoria + " / " + nome;
    }
}
