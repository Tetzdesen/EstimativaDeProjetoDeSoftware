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
import java.time.Clock;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Relatório em PDF gerado pelo Adapter do PDFBox: cabeçalho, indicadores com o
 * preço final em destaque, tabelas que continuam entre páginas repetindo o
 * cabeçalho das colunas e rodapé com "Página N de M".
 */
public class ExportarPDFTemplate extends ExportadorProjeto {

    private static final float MARGEM = 40;
    private static final float RODAPE = 46;
    private static final float ALTURA_LINHA = 17;
    private static final String NORMAL = "HELVETICA";
    private static final String NEGRITO = "HELVETICA_BOLD";
    private static final DateTimeFormatter DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // paleta do relatório (azul do tema claro do sistema e tons de cinza)
    private static final int[] DESTAQUE = {37, 99, 235};
    private static final int[] TEXTO = {15, 23, 42};
    private static final int[] SUAVE = {100, 116, 139};
    private static final int[] FUNDO_CARTAO = {241, 245, 249};
    private static final int[] FUNDO_CABECALHO = {226, 232, 240};
    private static final int[] FUNDO_ZEBRA = {248, 250, 252};
    private static final int[] LINHA = {203, 213, 225};
    private static final int[] BRANCO = {255, 255, 255};

    private final FileExportAdapter adapter;
    private final Clock relogio;
    private float y;
    private float largura;

    public ExportarPDFTemplate() {
        this(new PDFExportAdapter(), Clock.systemDefaultZone());
    }

    public ExportarPDFTemplate(FileExportAdapter adapter) {
        this(adapter, Clock.systemDefaultZone());
    }

    public ExportarPDFTemplate(FileExportAdapter adapter, Clock relogio) {
        this.adapter = adapter;
        this.relogio = relogio;
    }

    @Override
    public FormatoExportacao getFormato() {
        return FormatoExportacao.PDF;
    }

    /** Coluna de tabela: título, peso na largura e alinhamento. */
    private record Coluna(String titulo, float peso, boolean direita) {

        static Coluna texto(String titulo, float peso) {
            return new Coluna(titulo, peso, false);
        }

        static Coluna numero(String titulo, float peso) {
            return new Coluna(titulo, peso, true);
        }
    }

    /** Linha de tabela; {@code destaque} deixa a linha em negrito com fundo (totais). */
    private record Linha(List<String> celulas, boolean destaque) {

        static Linha de(String... celulas) {
            return new Linha(List.of(celulas), false);
        }

        static Linha total(String... celulas) {
            return new Linha(List.of(celulas), true);
        }
    }

    @Override
    protected void gerarArquivo(Projeto projeto, ResultadoEstimativa r, Path arquivo) {
        try {
            adapter.createDocument();
            largura = adapter.getPageWidth() - 2 * MARGEM;
            adapter.addPage();
            Set<Plataforma> marcadas = r.getPorPlataforma().keySet();

            cabecalho(projeto, marcadas);
            indicadores(r);
            resumoFinanceiro(r);
            porPlataforma(r);
            porCategoria(r);
            custosAdicionais(projeto, r);
            funcionalidades(r, marcadas);
            rodapes(projeto);

            adapter.save(arquivo.toString());
        } finally {
            adapter.close();
        }
    }

    // ---------- blocos ----------

    private void cabecalho(Projeto projeto, Set<Plataforma> marcadas) {
        float topo = adapter.getPageHeight();
        float altura = 78;
        adapter.fillRect(0, topo - altura, adapter.getPageWidth(), altura, DESTAQUE[0], DESTAQUE[1], DESTAQUE[2]);
        texto("ESTIMATIVA DE PROJETO DE SOFTWARE", MARGEM, topo - 28, NORMAL, 8.5f, BRANCO);
        texto(projeto.getNome(), MARGEM, topo - 52, NEGRITO, 20, BRANCO);
        String emissao = "Emitido em " + LocalDate.now(relogio).format(DATA);
        texto(emissao, MARGEM + largura - adapter.getTextWidth(emissao, NORMAL, 9), topo - 28, NORMAL, 9, BRANCO);

        y = topo - altura - 22;
        List<String> info = new ArrayList<>();
        if (projeto.getDonoNome() != null && !projeto.getDonoNome().isBlank()) {
            info.add("Dono: " + projeto.getDonoNome());
        }
        info.add("Plataformas: " + String.join(", ", marcadas.stream().map(Plataforma::getDescricao).toList()));
        texto(String.join("     ", info), MARGEM, y, NORMAL, 10, SUAVE);
        y -= 22;
    }

    private void indicadores(ResultadoEstimativa r) {
        float espaco = 10;
        float altura = 58;
        float[] pesos = {1.3f, 0.85f, 1.2f, 1};
        String[][] dados = {
            {"Preço final ao cliente", formatarReais(r.getPrecoFinal()), "impostos " + formatarPercentual(r.getPercentualImpostos())
                + " · lucro " + formatarPercentual(r.getPercentualLucro())},
            {"Prazo", r.getDiasTotalExibicao() + " dias", formatarMeses(r.getMeses()) + " meses"},
            {"Custo de desenvolvimento", formatarReais(r.getCustoDesenvolvimento()), formatarDias(r.getDiasTotal()) + " dias de trabalho"},
            {"Média mensal", formatarReais(r.getMediaMensal()), "preço final ÷ meses"}};
        float soma = 0;
        for (float p : pesos) {
            soma += p;
        }
        float disponivel = largura - espaco * (pesos.length - 1);
        float x = MARGEM;
        for (int i = 0; i < dados.length; i++) {
            float w = disponivel * pesos[i] / soma;
            adapter.fillRect(x, y - altura, w, altura, FUNDO_CARTAO[0], FUNDO_CARTAO[1], FUNDO_CARTAO[2]);
            if (i == 0) {
                adapter.fillRect(x, y - altura, 3, altura, DESTAQUE[0], DESTAQUE[1], DESTAQUE[2]);
            }
            texto(dados[i][0], x + 10, y - 16, NORMAL, 8.5f, SUAVE);
            texto(dados[i][1], x + 10, y - 35, NEGRITO, i == 0 ? 15 : 13, i == 0 ? DESTAQUE : TEXTO);
            texto(dados[i][2], x + 10, y - 50, NORMAL, 7.5f, SUAVE);
            x += w + espaco;
        }
        y -= altura + 18;
    }

    private void resumoFinanceiro(ResultadoEstimativa r) {
        secao("Resumo financeiro");
        tabela(List.of(Coluna.texto("Item", 3), Coluna.numero("Valor", 1)), List.of(
                Linha.de("Custo de desenvolvimento", formatarReais(r.getCustoDesenvolvimento())),
                Linha.de("Custos adicionais", formatarReais(r.getTotalCustosAdicionais())),
                Linha.de("Custo total", formatarReais(r.getCustoTotal())),
                Linha.de("Impostos (" + formatarPercentual(r.getPercentualImpostos()) + ")", formatarReais(r.getImposto())),
                Linha.de("Valor com impostos", formatarReais(r.getValorComImposto())),
                Linha.de("Lucro (" + formatarPercentual(r.getPercentualLucro()) + ")", formatarReais(r.getLucro())),
                Linha.total("Preço final ao cliente", formatarReais(r.getPrecoFinal()))));
    }

    private void porPlataforma(ResultadoEstimativa r) {
        secao("Dias e custos por plataforma");
        List<Linha> linhas = new ArrayList<>();
        BigDecimal tamanho = BigDecimal.ZERO;
        BigDecimal ui = BigDecimal.ZERO;
        BigDecimal funcionalidades = BigDecimal.ZERO;
        BigDecimal gerencia = BigDecimal.ZERO;
        for (DetalhePlataforma d : r.getPorPlataforma().values()) {
            linhas.add(Linha.de(d.plataforma().getDescricao(), formatarDias(d.diasTamanho()), formatarDias(d.diasUI()),
                    formatarDias(d.diasFuncionalidades()), formatarDias(d.diasGerencia()), formatarDias(d.dias()),
                    formatarReais(d.custo())));
            tamanho = tamanho.add(d.diasTamanho());
            ui = ui.add(d.diasUI());
            funcionalidades = funcionalidades.add(d.diasFuncionalidades());
            gerencia = gerencia.add(d.diasGerencia());
        }
        linhas.add(Linha.total("Total", formatarDias(tamanho), formatarDias(ui), formatarDias(funcionalidades),
                formatarDias(gerencia), formatarDias(r.getDiasTotal()), formatarReais(r.getCustoDesenvolvimento())));
        tabela(List.of(Coluna.texto("Plataforma", 1.5f), Coluna.numero("Tamanho", 1), Coluna.numero("UI", 1),
                Coluna.numero("Funcionalidades", 1.3f), Coluna.numero("Gerência", 1), Coluna.numero("Dias", 1),
                Coluna.numero("Custo", 1.5f)), linhas);
    }

    private void porCategoria(ResultadoEstimativa r) {
        secao("Resumo por categoria");
        List<Linha> linhas = new ArrayList<>();
        for (ResumoCategoria c : r.getPorCategoria().values()) {
            linhas.add(Linha.de(c.categoria(), formatarDias(c.dias()), formatarReais(c.custo())));
        }
        tabela(List.of(Coluna.texto("Categoria", 3), Coluna.numero("Dias", 1), Coluna.numero("Custo", 1.3f)), linhas);
    }

    private void custosAdicionais(Projeto projeto, ResultadoEstimativa r) {
        secao("Custos adicionais");
        List<Linha> linhas = new ArrayList<>();
        for (CustoAdicional c : projeto.getCustosAdicionais()) {
            linhas.add(Linha.de(c.getDescricao(), formatarReais(c.getValor())));
        }
        linhas.add(Linha.total("Total", formatarReais(r.getTotalCustosAdicionais())));
        tabela(List.of(Coluna.texto("Descrição", 3), Coluna.numero("Valor", 1)), linhas);
    }

    private void funcionalidades(ResultadoEstimativa r, Set<Plataforma> marcadas) {
        secao("Funcionalidades selecionadas (dias por plataforma)");
        List<Coluna> colunas = new ArrayList<>(List.of(Coluna.texto("Categoria", 2.2f), Coluna.texto("Funcionalidade", 2.8f)));
        for (Plataforma p : marcadas) {
            colunas.add(Coluna.numero(p.getDescricao(), 1));
        }
        List<Linha> linhas = new ArrayList<>();
        for (LinhaFuncionalidade l : r.getFuncionalidadesSelecionadas()) {
            List<String> celulas = new ArrayList<>(List.of(l.categoria(), l.nome()));
            for (Plataforma p : marcadas) {
                BigDecimal d = l.diasPorPlataforma().get(p);
                celulas.add(d == null ? "NA" : formatarDias(d));
            }
            linhas.add(new Linha(celulas, false));
        }
        tabela(colunas, linhas);
    }

    /** "Página N de M" em todas as páginas, escrito quando o total já é conhecido. */
    private void rodapes(Projeto projeto) {
        int total = adapter.getPageCount();
        for (int i = 0; i < total; i++) {
            adapter.selectPage(i);
            float base = 26;
            adapter.drawLine(MARGEM, base + 12, MARGEM + largura, base + 12, 0.5f, LINHA[0], LINHA[1], LINHA[2]);
            texto("Estimativa de Projeto de Software · " + projeto.getNome(), MARGEM, base, NORMAL, 8, SUAVE);
            String pagina = "Página " + (i + 1) + " de " + total;
            texto(pagina, MARGEM + largura - adapter.getTextWidth(pagina, NORMAL, 8), base, NORMAL, 8, SUAVE);
        }
    }

    // ---------- desenho ----------

    private void secao(String titulo) {
        if (y - 28 - 2 * ALTURA_LINHA < RODAPE) { // título sem pelo menos uma linha embaixo vai para a próxima página
            novaPagina();
        }
        y -= 6;
        texto(titulo, MARGEM, y, NEGRITO, 12, TEXTO);
        y -= 6;
        adapter.drawLine(MARGEM, y, MARGEM + 28, y, 2, DESTAQUE[0], DESTAQUE[1], DESTAQUE[2]);
        y -= 10;
    }

    private void tabela(List<Coluna> colunas, List<Linha> linhas) {
        float soma = 0;
        for (Coluna c : colunas) {
            soma += c.peso();
        }
        float[] larguras = new float[colunas.size()];
        for (int i = 0; i < colunas.size(); i++) {
            larguras[i] = largura * colunas.get(i).peso() / soma;
        }
        cabecalhoTabela(colunas, larguras);
        int n = 0;
        for (Linha linha : linhas) {
            if (y - ALTURA_LINHA < RODAPE) {
                novaPagina();
                cabecalhoTabela(colunas, larguras); // a tabela continua com o cabeçalho das colunas
            }
            if (linha.destaque()) {
                adapter.fillRect(MARGEM, y - ALTURA_LINHA, largura, ALTURA_LINHA, FUNDO_CARTAO[0], FUNDO_CARTAO[1], FUNDO_CARTAO[2]);
                adapter.drawLine(MARGEM, y, MARGEM + largura, y, 0.8f, SUAVE[0], SUAVE[1], SUAVE[2]);
            } else if (n++ % 2 == 1) {
                adapter.fillRect(MARGEM, y - ALTURA_LINHA, largura, ALTURA_LINHA, FUNDO_ZEBRA[0], FUNDO_ZEBRA[1], FUNDO_ZEBRA[2]);
            }
            celulas(linha.celulas(), colunas, larguras, linha.destaque() ? NEGRITO : NORMAL, 9,
                    linha.destaque() ? TEXTO : TEXTO);
            y -= ALTURA_LINHA;
        }
        adapter.drawLine(MARGEM, y, MARGEM + largura, y, 0.5f, LINHA[0], LINHA[1], LINHA[2]);
        y -= 16;
    }

    private void cabecalhoTabela(List<Coluna> colunas, float[] larguras) {
        adapter.fillRect(MARGEM, y - ALTURA_LINHA, largura, ALTURA_LINHA, FUNDO_CABECALHO[0], FUNDO_CABECALHO[1], FUNDO_CABECALHO[2]);
        celulas(colunas.stream().map(Coluna::titulo).toList(), colunas, larguras, NEGRITO, 8.5f, TEXTO);
        y -= ALTURA_LINHA;
    }

    private void celulas(List<String> textos, List<Coluna> colunas, float[] larguras, String fonte, float tamanho, int[] cor) {
        float x = MARGEM;
        float base = y - ALTURA_LINHA + 5.5f;
        for (int i = 0; i < colunas.size(); i++) {
            float util = larguras[i] - 12;
            String t = caber(textos.get(i), fonte, tamanho, util);
            float tx = colunas.get(i).direita() ? x + larguras[i] - 6 - adapter.getTextWidth(t, fonte, tamanho) : x + 6;
            texto(t, tx, base, fonte, tamanho, cor);
            x += larguras[i];
        }
    }

    /** Corta o texto com reticências para não invadir a coluna seguinte. */
    private String caber(String texto, String fonte, float tamanho, float largura) {
        if (adapter.getTextWidth(texto, fonte, tamanho) <= largura) {
            return texto;
        }
        String t = texto;
        while (t.length() > 1 && adapter.getTextWidth(t + "...", fonte, tamanho) > largura) {
            t = t.substring(0, t.length() - 1);
        }
        return t.stripTrailing() + "...";
    }

    private void novaPagina() {
        adapter.addPage();
        y = adapter.getPageHeight() - MARGEM;
    }

    private void texto(String texto, float x, float yTexto, String fonte, float tamanho, int[] cor) {
        adapter.addText(texto, x, yTexto, fonte, tamanho, cor[0], cor[1], cor[2]);
    }
}
