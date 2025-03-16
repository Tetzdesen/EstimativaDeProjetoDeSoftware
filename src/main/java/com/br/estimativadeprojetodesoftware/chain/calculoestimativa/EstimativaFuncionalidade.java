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
    private String tipoFuncionalidade;
    private String tipoCampo;
    private double valorPorDia;
    private String perfilNome;

    public EstimativaFuncionalidade(UUID idProjeto, String nomeFuncionalidade, int quantidadeDias, String tipoFuncionalidade, String tipoCampo,
            String perfilNome) {
        this.idProjeto = idProjeto;
        this.nomeFuncionalidade = nomeFuncionalidade;
        this.quantidadeDias = quantidadeDias;
        this.tipoFuncionalidade = tipoFuncionalidade;
        this.tipoCampo = tipoCampo;
        this.valorPorDia = new CampoRepositoryService().buscarValorPorNomeProjetoCampo(this.idProjeto, tipoFuncionalidade);
        this.perfilNome = perfilNome;
        //this.valorPorDia = 10.0;
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

    public String getPerfilNome() {
        return perfilNome;
    }

    public String getTipoFuncionalidade() {
        return tipoFuncionalidade;
    }

    public String getTipoCampo() {
        return tipoCampo;
    }

}
