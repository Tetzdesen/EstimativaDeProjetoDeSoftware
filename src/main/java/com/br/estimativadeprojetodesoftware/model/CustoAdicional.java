package com.br.estimativadeprojetodesoftware.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/** Custo adicional do projeto (BR11). */
public class CustoAdicional {

    private final UUID id;
    private String descricao;
    private BigDecimal valor;

    public CustoAdicional(UUID id, String descricao, BigDecimal valor) {
        this.id = Objects.requireNonNull(id);
        setDescricao(descricao);
        setValor(valor);
    }

    public CustoAdicional(String descricao, BigDecimal valor) {
        this(UUID.randomUUID(), descricao, valor);
    }

    /** Itens iniciais exigidos pela BR11. */
    public static List<CustoAdicional> padroes() {
        List<CustoAdicional> lista = new ArrayList<>();
        lista.add(new CustoAdicional("Hardware e instalações físicas", new BigDecimal("1500.00")));
        lista.add(new CustoAdicional("Softwares", new BigDecimal("6000.00")));
        lista.add(new CustoAdicional("Custo com riscos", new BigDecimal("1500.00")));
        lista.add(new CustoAdicional("Garantia", new BigDecimal("0.00")));
        lista.add(new CustoAdicional("Transporte", new BigDecimal("1000.00")));
        lista.add(new CustoAdicional("Outros custos", new BigDecimal("2000.00")));
        return lista;
    }

    public CustoAdicional copiar() {
        return new CustoAdicional(descricao, valor);
    }

    public UUID getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    public final void setDescricao(String descricao) {
        if (descricao == null || descricao.isBlank()) {
            throw new IllegalArgumentException("A descrição do custo adicional não pode ser vazia");
        }
        this.descricao = descricao.trim();
    }

    public BigDecimal getValor() {
        return valor;
    }

    public final void setValor(BigDecimal valor) {
        if (valor == null || valor.signum() < 0) {
            throw new IllegalArgumentException("O valor de \"" + descricao + "\" deve ser zero ou positivo");
        }
        this.valor = valor;
    }
}
