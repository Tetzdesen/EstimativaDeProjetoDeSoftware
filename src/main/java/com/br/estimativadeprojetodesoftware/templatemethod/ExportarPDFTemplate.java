package com.br.estimativadeprojetodesoftware.templatemethod;

import static com.br.estimativadeprojetodesoftware.model.ResultadoEstimativa.formatarDias;
import static com.br.estimativadeprojetodesoftware.model.ResultadoEstimativa.formatarMeses;
import static com.br.estimativadeprojetodesoftware.model.ResultadoEstimativa.formatarPercentual;
import static com.br.estimativadeprojetodesoftware.model.ResultadoEstimativa.formatarReais;

import com.br.estimativadeprojetodesoftware.adapter.exportarprojeto.FileExportAdapter;
import com.br.estimativadeprojetodesoftware.adapter.exportarprojeto.PDFExportAdapter;
import com.br.estimativadeprojetodesoftware.model.CustoAdicional;
import com.br.estimativadeprojetodesoftware.model.Plataforma;
import com.br.estimativadeprojetodesoftware.model.Projeto;
import com.br.estimativadeprojetodesoftware.model.ResultadoEstimativa;
import com.br.estimativadeprojetodesoftware.model.ResultadoEstimativa.DetalhePlataforma;
import com.br.estimativadeprojetodesoftware.model.ResultadoEstimativa.LinhaFuncionalidade;
import com.br.estimativadeprojetodesoftware.model.ResultadoEstimativa.ResumoCategoria;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.Set;

/** Relatório em PDF gerado pelo Adapter do PDFBox. */
public class ExportarPDFTemplate extends ExportadorProjeto {

    private static final float MARGEM = 50;
    private static final float ALTURA_LINHA = 14;

    private final FileExportAdapter adapter;
    private float y;

    public ExportarPDFTemplate() {
        this(new PDFExportAdapter());
    }

    public ExportarPDFTemplate(FileExportAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public FormatoExportacao getFormato() {
        return FormatoExportacao.PDF;
    }

    @Override
    protected void gerarArquivo(Projeto projeto, ResultadoEstimativa r, Path arquivo) {
        try {
            adapter.createDocument();
            novaPagina();
            Set<Plataforma> marcadas = r.getPorPlataforma().keySet();

            texto("Estimativa do projeto: " + projeto.getNome(), 0, true, 16);
            y -= 6;
            texto("Dono: " + (projeto.getDonoNome() == null ? "" : projeto.getDonoNome()), 0, false, 11);
            texto("Plataformas: " + String.join(", ", marcadas.stream().map(Plataforma::getDescricao).toList()), 0, false, 11);

            titulo("Funcionalidades selecionadas (dias por plataforma)");
            for (LinhaFuncionalidade l : r.getFuncionalidadesSelecionadas()) {
                StringBuilder dias = new StringBuilder();
                for (Plataforma p : marcadas) {
                    BigDecimal d = l.diasPorPlataforma().get(p);
                    dias.append(p.getDescricao()).append(": ").append(d == null ? "NA" : formatarDias(d)).append("   ");
                }
                texto(l.categoria() + " / " + l.nome(), 0, false, 9);
                texto(dias.toString().trim(), 20, false, 9);
            }

            titulo("Dias e custos por plataforma");
            for (DetalhePlataforma d : r.getPorPlataforma().values()) {
                texto(d.plataforma().getDescricao() + ": " + formatarDias(d.dias()) + " dias  |  " + formatarReais(d.custo()), 0, true, 10);
                texto("Tamanho " + formatarDias(d.diasTamanho()) + "  UI " + formatarDias(d.diasUI())
                        + "  Funcionalidades " + formatarDias(d.diasFuncionalidades())
                        + "  Gerência " + formatarDias(d.diasGerencia()), 20, false, 9);
            }

            titulo("Resumo por categoria");
            for (ResumoCategoria c : r.getPorCategoria().values()) {
                texto(c.categoria() + ": " + formatarDias(c.dias()) + " dias  |  " + formatarReais(c.custo()), 0, false, 10);
            }

            titulo("Custos adicionais");
            for (CustoAdicional c : projeto.getCustosAdicionais()) {
                texto(c.getDescricao() + ": " + formatarReais(c.getValor()), 0, false, 10);
            }

            titulo("Resumo financeiro");
            texto("Custo de desenvolvimento: " + formatarReais(r.getCustoDesenvolvimento()), 0, false, 10);
            texto("Custos adicionais: " + formatarReais(r.getTotalCustosAdicionais()), 0, false, 10);
            texto("Custo total: " + formatarReais(r.getCustoTotal()), 0, false, 10);
            texto("Impostos (" + formatarPercentual(r.getPercentualImpostos()) + "): " + formatarReais(r.getImposto()), 0, false, 10);
            texto("Valor com impostos: " + formatarReais(r.getValorComImposto()), 0, false, 10);
            texto("Lucro (" + formatarPercentual(r.getPercentualLucro()) + "): " + formatarReais(r.getLucro()), 0, false, 10);
            texto("Preço final ao cliente: " + formatarReais(r.getPrecoFinal()), 0, true, 12);
            texto("Prazo: " + r.getDiasTotalExibicao() + " dias (" + formatarMeses(r.getMeses()) + " meses)", 0, true, 11);
            texto("Média mensal: " + formatarReais(r.getMediaMensal()), 0, false, 10);

            adapter.save(arquivo.toString());
        } finally {
            adapter.close();
        }
    }

    private void novaPagina() {
        adapter.addPage();
        y = adapter.getPageHeight() - MARGEM;
    }

    private void titulo(String texto) {
        y -= 8;
        texto(texto, 0, true, 12);
    }

    private void texto(String texto, float recuo, boolean negrito, float tamanho) {
        if (y < MARGEM) {
            novaPagina();
        }
        adapter.addText(texto, MARGEM + recuo, y, negrito ? "HELVETICA_BOLD" : "HELVETICA", tamanho);
        y -= Math.max(ALTURA_LINHA, tamanho + 4);
    }
}
