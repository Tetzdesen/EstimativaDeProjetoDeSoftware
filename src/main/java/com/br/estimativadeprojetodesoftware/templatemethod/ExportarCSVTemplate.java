package com.br.estimativadeprojetodesoftware.templatemethod;

import static com.br.estimativadeprojetodesoftware.model.ResultadoEstimativa.arredondarDias;
import static com.br.estimativadeprojetodesoftware.model.ResultadoEstimativa.arredondarReais;

import com.br.estimativadeprojetodesoftware.model.CustoAdicional;
import com.br.estimativadeprojetodesoftware.model.Plataforma;
import com.br.estimativadeprojetodesoftware.model.Projeto;
import com.br.estimativadeprojetodesoftware.model.ResultadoEstimativa;
import com.br.estimativadeprojetodesoftware.model.ResultadoEstimativa.DetalhePlataforma;
import com.br.estimativadeprojetodesoftware.model.ResultadoEstimativa.LinhaFuncionalidade;
import com.br.estimativadeprojetodesoftware.model.ResultadoEstimativa.ResumoCategoria;
import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

/** Relatório em CSV (separador ";", números com ponto decimal, UTF-8). */
public class ExportarCSVTemplate extends ExportadorProjeto {

    private static final String SEP = ";";

    @Override
    public FormatoExportacao getFormato() {
        return FormatoExportacao.CSV;
    }

    @Override
    protected void gerarArquivo(Projeto projeto, ResultadoEstimativa r, Path arquivo) throws IOException {
        Set<Plataforma> marcadas = r.getPorPlataforma().keySet();
        try (BufferedWriter w = Files.newBufferedWriter(arquivo, StandardCharsets.UTF_8)) {
            linha(w, "Projeto", projeto.getNome());
            linha(w, "Dono", projeto.getDonoNome() == null ? "" : projeto.getDonoNome());
            linha(w, "Plataformas", String.join(", ", marcadas.stream().map(Plataforma::getDescricao).toList()));
            w.newLine();

            linha(w, "FUNCIONALIDADES SELECIONADAS");
            StringBuilder cab = new StringBuilder("Categoria" + SEP + "Funcionalidade");
            marcadas.forEach(p -> cab.append(SEP).append("Dias ").append(p.getDescricao()));
            w.write(cab.toString());
            w.newLine();
            for (LinhaFuncionalidade l : r.getFuncionalidadesSelecionadas()) {
                StringBuilder sb = new StringBuilder(campo(l.categoria()) + SEP + campo(l.nome()));
                for (Plataforma p : marcadas) {
                    BigDecimal d = l.diasPorPlataforma().get(p);
                    sb.append(SEP).append(d == null ? "NA" : arredondarDias(d).toPlainString());
                }
                w.write(sb.toString());
                w.newLine();
            }
            w.newLine();

            linha(w, "DIAS E CUSTOS POR PLATAFORMA");
            linha(w, "Plataforma", "Dias tamanho", "Dias UI", "Dias funcionalidades", "Dias gerência", "Dias total", "Custo (R$)");
            for (DetalhePlataforma d : r.getPorPlataforma().values()) {
                linha(w, d.plataforma().getDescricao(), dias(d.diasTamanho()), dias(d.diasUI()), dias(d.diasFuncionalidades()),
                        dias(d.diasGerencia()), dias(d.dias()), reais(d.custo()));
            }
            w.newLine();

            linha(w, "RESUMO POR CATEGORIA");
            linha(w, "Categoria", "Dias", "Custo (R$)");
            for (ResumoCategoria c : r.getPorCategoria().values()) {
                linha(w, c.categoria(), dias(c.dias()), reais(c.custo()));
            }
            w.newLine();

            linha(w, "CUSTOS ADICIONAIS");
            linha(w, "Descrição", "Valor (R$)");
            for (CustoAdicional c : projeto.getCustosAdicionais()) {
                linha(w, c.getDescricao(), reais(c.getValor()));
            }
            w.newLine();

            linha(w, "RESUMO FINANCEIRO");
            linha(w, "Custo de desenvolvimento (R$)", reais(r.getCustoDesenvolvimento()));
            linha(w, "Custos adicionais (R$)", reais(r.getTotalCustosAdicionais()));
            linha(w, "Custo total (R$)", reais(r.getCustoTotal()));
            linha(w, "Impostos (%)", percentual(r.getPercentualImpostos()));
            linha(w, "Imposto (R$)", reais(r.getImposto()));
            linha(w, "Valor com impostos (R$)", reais(r.getValorComImposto()));
            linha(w, "Lucro (%)", percentual(r.getPercentualLucro()));
            linha(w, "Lucro (R$)", reais(r.getLucro()));
            linha(w, "Preço final ao cliente (R$)", reais(r.getPrecoFinal()));
            linha(w, "Prazo (dias)", String.valueOf(r.getDiasTotalExibicao()));
            linha(w, "Prazo (meses)", r.getMesesExibicao().toPlainString());
            linha(w, "Média mensal (R$)", reais(r.getMediaMensal()));
        }
    }

    private static String dias(BigDecimal v) {
        return arredondarDias(v).toPlainString();
    }

    private static String reais(BigDecimal v) {
        return arredondarReais(v).toPlainString();
    }

    private static String percentual(BigDecimal fracao) {
        return arredondarReais(fracao.multiply(new BigDecimal(100))).toPlainString();
    }

    private static void linha(BufferedWriter w, String... campos) throws IOException {
        w.write(String.join(SEP, List.of(campos).stream().map(ExportarCSVTemplate::campo).toList()));
        w.newLine();
    }

    private static String campo(String valor) {
        if (valor.contains(SEP) || valor.contains("\"") || valor.contains("\n")) {
            return "\"" + valor.replace("\"", "\"\"") + "\"";
        }
        return valor;
    }
}
