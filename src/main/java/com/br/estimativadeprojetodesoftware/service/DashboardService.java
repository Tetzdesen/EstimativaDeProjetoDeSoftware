package com.br.estimativadeprojetodesoftware.service;

import com.br.estimativadeprojetodesoftware.model.Plataforma;
import com.br.estimativadeprojetodesoftware.model.Projeto;
import com.br.estimativadeprojetodesoftware.model.ResultadoEstimativa;
import com.br.estimativadeprojetodesoftware.model.ResumoDashboard;
import com.br.estimativadeprojetodesoftware.model.Usuario;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Consolida os indicadores do dashboard do usuário com o mesmo cálculo da
 * tela de estimativa, sempre sobre o estado atual dos projetos.
 */
public class DashboardService {

    private static final MathContext MC = MathContext.DECIMAL128;

    private final ProjetoService projetos;
    private final CalculoEstimativaService calculo;

    public DashboardService(ProjetoService projetos, CalculoEstimativaService calculo) {
        this.projetos = projetos;
        this.calculo = calculo;
    }

    public DashboardService() {
        this(new ProjetoService(), new CalculoEstimativaService());
    }

    public ResumoDashboard resumir(Usuario usuario) {
        List<Projeto> proprios = projetos.listarProprios(usuario);
        int compartilhados = projetos.listarCompartilhadosComigo(usuario).size();

        int calculados = 0;
        int comErro = 0;
        BigDecimal dias = BigDecimal.ZERO;
        BigDecimal preco = BigDecimal.ZERO;
        Map<String, BigDecimal> precoPorProjeto = new LinkedHashMap<>();
        Map<Plataforma, BigDecimal> diasPorPlataforma = new EnumMap<>(Plataforma.class);

        for (Projeto projeto : proprios) {
            if (!projeto.isCalculado()) {
                continue;
            }
            calculados++;
            ResultadoEstimativa r;
            try {
                r = calculo.calcular(projeto);
            } catch (IllegalArgumentException e) {
                comErro++;
                continue;
            }
            dias = dias.add(r.getDiasTotal(), MC);
            preco = preco.add(r.getPrecoFinal(), MC);
            precoPorProjeto.put(projeto.getNome(), r.getPrecoFinal());
            r.getPorPlataforma().forEach((p, d) -> diasPorPlataforma.merge(p, d.dias(), (a, b) -> a.add(b, MC)));
        }
        return new ResumoDashboard(proprios.size(), compartilhados, calculados, comErro, dias, preco,
                precoPorProjeto, diasPorPlataforma);
    }

    public ProjetoService getProjetoService() {
        return projetos;
    }
}
