package com.br.estimativadeprojetodesoftware.service;

import com.br.estimativadeprojetodesoftware.model.Campo;
import com.br.estimativadeprojetodesoftware.model.Projeto;
import java.util.List;

import java.util.Map;

public class EstimaProjetoService {
    private static final double VALOR_DIARIA_DESENVOLVIMENTO = 450.0;
    private static final double VALOR_DIARIA_GERENCIA = 300.0;
    private static final double VALOR_DIARIA_UI_UX = 550.0;

    public int calcularDiasTotais(Projeto projeto) {
        return calcularDiasFuncionalidades(projeto.getCampos());
    }

    public double calcularCusto(Projeto projeto) {
        int diasTotais = calcularDiasTotais(projeto);
        return diasTotais * VALOR_DIARIA_DESENVOLVIMENTO;
    }

    public double calcularValorUnitario(String tipoProjeto, int dias) {
        /* 
        switch (tipoProjeto) {
            case "Web/Back-end":
            case "Android":
                return dias * VALOR_DIARIA_DESENVOLVIMENTO;
            case "iOS":
                return dias * VALOR_DIARIA_UI_UX;
            default:
                throw new IllegalArgumentException("Tipo de projeto desconhecido: " + tipoProjeto);
        }
                */
        return 2;
    }

    public int calcularDiasFuncionalidades(List<Campo> funcionalidadesEscolhidas) {
        int totalDias = 0;
        for(int i = 0; i < funcionalidadesEscolhidas.size(); i++){
            totalDias += funcionalidadesEscolhidas.get(i).getDias();
        }
        return totalDias;
    }

    public double calcularCustosAdicionais(double custoHardware, double custoSoftware, double custoRiscos, double custoGarantia, double fundoReserva, double outrosCustos) {
        return custoHardware + custoSoftware + custoRiscos + custoGarantia + fundoReserva + outrosCustos;
    }

    public double calcularImpostos(double subtotal, double percentualImpostos) {
        return subtotal * (percentualImpostos / 100);
    }

    public double calcularLucro(double subtotalComImpostos, double percentualLucro) {
        return subtotalComImpostos * (percentualLucro / 100);
    }

    public double calcularPrecoFinal(double subtotalComImpostos, double lucro) {
        return subtotalComImpostos + lucro;
    }

    public double calcularMediaPorMes(double precoFinal, double meses) {
        return precoFinal / meses;
    }
}