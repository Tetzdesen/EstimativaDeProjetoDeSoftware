package com.br.estimativadeprojetodesoftware.chain.calculoestimativa;

import java.util.UUID;

/**
 *
 * @author tetzner
 */
public class EstimativaFuncionalidade {

    private UUID idProjeto;
    private String nomeFuncionalidade;
    private int quantidadeDias;
    private String tipo;
    private double valorPorDia;
    private String perfilNome; // 🔥 Novo atributo


    public EstimativaFuncionalidade(UUID idProjeto, String nomeFuncionalidade, int quantidadeDias, String tipo
    ) {
        this.idProjeto = idProjeto;
        this.nomeFuncionalidade = nomeFuncionalidade;
        this.quantidadeDias = quantidadeDias;
        this.tipo = tipo;
        this.valorPorDia = 10.0;
    }

    public double calcularCustoTotal() {
        return quantidadeDias * valorPorDia;
    }

    public String getNomeFuncionalidade() {
        return nomeFuncionalidade;
    }

    public int getQuantidadeDias() {
        return quantidadeDias;
    }

    public double getValorPorDia() {
        return valorPorDia;
    }
}
