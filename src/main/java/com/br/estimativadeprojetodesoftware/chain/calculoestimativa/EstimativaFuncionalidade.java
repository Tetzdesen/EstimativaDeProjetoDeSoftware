package com.br.estimativadeprojetodesoftware.chain.calculoestimativa;

import java.util.UUID;

import com.br.estimativadeprojetodesoftware.service.CampoService;

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
    private CampoService campoService = new CampoService();

    public EstimativaFuncionalidade(UUID idProjeto, String nomeFuncionalidade, int quantidadeDias, String tipoFuncionalidade, String tipoCampo,
            String perfilNome) {
        this.idProjeto = idProjeto;
        this.nomeFuncionalidade = nomeFuncionalidade;
        this.quantidadeDias = quantidadeDias;
        this.tipoFuncionalidade = tipoFuncionalidade;
        this.tipoCampo = tipoCampo;
        this.valorPorDia = campoService.buscarValorPorNomeProjetoCampo(this.idProjeto, tipoFuncionalidade);
        this.perfilNome = perfilNome;
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

    public void setQuantidadeDias(int quantidadeDias) {
        if(quantidadeDias < 0){
            throw new IllegalArgumentException("Quantidade de dias deve ser maior que 0");
        }
        this.quantidadeDias = quantidadeDias;
    }

    public void setValorPorDia(double valorPorDia) {
        if(valorPorDia < 0){
            throw new IllegalArgumentException("Valor por dia deve ser maior que 0");
        }
        this.valorPorDia = valorPorDia;
    }

    public String getTipoCampo() {
        return tipoCampo;
    }

}
