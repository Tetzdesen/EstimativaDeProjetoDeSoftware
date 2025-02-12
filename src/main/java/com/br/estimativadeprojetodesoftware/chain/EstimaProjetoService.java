package com.br.estimativadeprojetodesoftware.chain;

import java.util.Map;

import com.br.estimativadeprojetodesoftware.model.Projeto;

public class EstimaProjetoService {
    private FormaCalculoEstimativaHandler formaCalculoEstimativaHandler;

    public EstimaProjetoService() {
        formaCalculoEstimativaHandler = new CalculoTipoDesenvolvedor();
        FormaCalculoEstimativaHandler tipoDesigner = new CalculoTipoDesigner();
        FormaCalculoEstimativaHandler tipoGerente = new CalculoTipoGerente();

        formaCalculoEstimativaHandler.setProximo(tipoDesigner);
        tipoDesigner.setProximo(tipoGerente);
    }

    public void calcularProjeto(Projeto projeto) {
        if (projeto == null) {
            throw new RuntimeException("Informe o projeto desejado");
        }

        formaCalculoEstimativaHandler.calcularEstimativa(projeto);

        //double valorTotal = formaCalculoEstimativaHandler.getValorTotal();
        System.out.println(projeto.getEstimativa().getDias());
    }

    

    //parte do cleyton

    private static final double VALOR_DIARIA_DESENVOLVIMENTO = 450.0;
    private static final double VALOR_DIARIA_GERENCIA = 300.0;
    private static final double VALOR_DIARIA_UI_UX = 550.0;

    public int calcularDiasTotais(Projeto projeto) {
        return calcularDiasFuncionalidades(projeto);
    }

    public double calcularCusto(Projeto projeto) {
        int diasTotais = calcularDiasTotais(projeto);
        return diasTotais * VALOR_DIARIA_DESENVOLVIMENTO;
    }

    public double calcularValorUnitario(Projeto projeto) {
        var campos = projeto.getEstimativa().getCampos();
        
    }

    public int calcularDiasFuncionalidades(Projeto projeto) {
        return projeto.getEstimativa().getDias();
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


/*
    private static final double VALOR_DIARIA_DESENVOLVIMENTO = 450.0;
    private static final double VALOR_DIARIA_GERENCIA = 300.0;
    private static final double VALOR_DIARIA_UI_UX = 550.0;

    public int calcularDiasTotais(Projeto projeto) {
        return calcularDiasFuncionalidades(projeto.getEstimativa().getCampos());
    }

    public double calcularCusto(Projeto projeto) {
        int diasTotais = calcularDiasTotais(projeto);
        return diasTotais * VALOR_DIARIA_DESENVOLVIMENTO;
    }

    public double calcularValorUnitario(String tipoProjeto, int dias) {
        switch (tipoProjeto) {
            case "Web/Back-end":
            case "Android":
                return dias * VALOR_DIARIA_DESENVOLVIMENTO;
            case "iOS":
                return dias * VALOR_DIARIA_UI_UX;
            default:
                throw new IllegalArgumentException("Tipo de projeto desconhecido: " + tipoProjeto);
        }
    }

    public int calcularDiasFuncionalidades(Map<String, Integer> funcionalidadesEscolhidas) {
        int totalDias = 0;
        for (Integer dias : funcionalidadesEscolhidas.values()) {
            totalDias += dias;
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
 */