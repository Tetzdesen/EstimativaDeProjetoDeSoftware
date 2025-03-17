package com.br.estimativadeprojetodesoftware.service;

import com.br.estimativadeprojetodesoftware.chain.calculoestimativa.EstimativaCampoFixoHandler;
import com.br.estimativadeprojetodesoftware.chain.calculoestimativa.EstimativaFuncionalidade;
import com.br.estimativadeprojetodesoftware.chain.calculoestimativa.EstimativaFuncionalidadeHandler;
import com.br.estimativadeprojetodesoftware.chain.calculoestimativa.EstimativaHandler;
import com.br.estimativadeprojetodesoftware.chain.calculoestimativa.EstimativaNivelHandler;
import com.br.estimativadeprojetodesoftware.chain.calculoestimativa.EstimativaTamanhoHandler;
import com.br.estimativadeprojetodesoftware.model.Campo;
import com.br.estimativadeprojetodesoftware.model.PerfilProjeto;
import com.br.estimativadeprojetodesoftware.model.Projeto;
import com.br.estimativadeprojetodesoftware.singleton.UsuarioLogadoSingleton;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EstimaProjetoService {

    private PerfilRepositoryService perfilService;
    private final CampoRepositoryService campoRepositoryService;
    private final List<EstimativaFuncionalidade> estimativas;

    public EstimaProjetoService() {
        this.perfilService = new PerfilRepositoryService();
        this.campoRepositoryService = new CampoRepositoryService();
        this.estimativas = new ArrayList<>();
    }

    public List<EstimativaFuncionalidade> calcularEstimativas(UUID idProjeto, List<PerfilProjeto> perfis, List<Campo> campos) {
        List<EstimativaFuncionalidade> estimativas = new ArrayList<>();
        List<EstimativaHandler> handlers = configurarHandlers();

        for (PerfilProjeto perfil : perfis) {
            for (Campo campo : campos) {
                for (EstimativaHandler handler : handlers) {
                    handler.calcularEstimativa(idProjeto, perfil.getNome(), campo, estimativas);
                }
            }
        }

        return estimativas;
    }

    private List<EstimativaHandler> configurarHandlers() {
        List<EstimativaHandler> handlers = new ArrayList<>();
        handlers.add(new EstimativaTamanhoHandler());
        handlers.add(new EstimativaNivelHandler());
        handlers.add(new EstimativaFuncionalidadeHandler());
        handlers.add(new EstimativaCampoFixoHandler());
        return handlers;
    }

    public double calcularValorTotal(List<EstimativaFuncionalidade> estimativas) {
        return estimativas.stream()
                .mapToDouble(EstimativaFuncionalidade::calcularCustoTotal)
                .sum();
    }

    public double calcularTotalNivelUI(List<EstimativaFuncionalidade> estimativas) {
        int totalPerfis = perfilService.obterQuantidadePerfisPorUsuario(UsuarioLogadoSingleton.getInstancia().getUsuario().getId());

        int diasTamanhoApp = estimativas.stream()
                .filter(e -> e.getTipoCampo().equalsIgnoreCase("tamanho"))
                .mapToInt(EstimativaFuncionalidade::getQuantidadeDias)
                .sum(); 

        System.out.println(diasTamanhoApp);

        double totalAtualUI = estimativas.stream()
                .filter(e -> e.getTipoCampo().equalsIgnoreCase("nivel"))
                .mapToDouble(EstimativaFuncionalidade::getQuantidadeDias)
                .sum(); 

        if (totalPerfis == 0 || diasTamanhoApp == 0) {
            return 0; 
        }

        return ((totalAtualUI / totalPerfis) / 100) * diasTamanhoApp;
    }

    public int calcularDiasFuncionalidades(List<Campo> funcionalidadesEscolhidas) {
        int totalDias = 0;
        for (int i = 0; i < funcionalidadesEscolhidas.size(); i++) {
            totalDias += funcionalidadesEscolhidas.get(i).getDias();
        }
        return totalDias;
    }

    public int calcularDiasTotais(Projeto projeto) {
        return calcularDiasFuncionalidades(projeto.getCampos());
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
