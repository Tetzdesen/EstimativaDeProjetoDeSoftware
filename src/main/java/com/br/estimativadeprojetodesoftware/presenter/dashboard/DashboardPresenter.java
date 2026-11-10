package com.br.estimativadeprojetodesoftware.presenter.dashboard;

import com.br.estimativadeprojetodesoftware.presenter.helpers.FilaBanco;
import com.br.estimativadeprojetodesoftware.command.ExibirMensagemCommand;
import com.br.estimativadeprojetodesoftware.model.Plataforma;
import com.br.estimativadeprojetodesoftware.model.ResultadoEstimativa;
import com.br.estimativadeprojetodesoftware.model.ResumoDashboard;
import com.br.estimativadeprojetodesoftware.model.Usuario;
import com.br.estimativadeprojetodesoftware.observer.EventoDominio;
import com.br.estimativadeprojetodesoftware.observer.Observer;
import com.br.estimativadeprojetodesoftware.service.DashboardService;
import com.br.estimativadeprojetodesoftware.singleton.UsuarioLogadoSingleton;
import com.br.estimativadeprojetodesoftware.view.dashboard.DashboardView;
import java.math.BigDecimal;
import java.util.Map;
import javax.swing.SwingUtilities;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import javax.swing.UIManager;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

/**
 * Presenter do dashboard (MVP Passive View + Observer). Formata os
 * indicadores, monta os gráficos e se atualiza quando projetos,
 * compartilhamentos ou a sessão mudam.
 */
public class DashboardPresenter implements Observer {

    static final String MENSAGEM_VAZIA = "Nenhum projeto calculado ainda. Crie um projeto em \"Novo projeto\", "
            + "marque as funcionalidades e clique em Salvar.";

    private final DashboardView view;
    private final DashboardService service;

    public DashboardPresenter() {
        this(new DashboardView(), new DashboardService());
    }

    public DashboardPresenter(DashboardView view, DashboardService service) {
        this.view = view;
        this.service = service;
        view.getBtnFechar().addActionListener(e -> view.dispose());
        service.getProjetoService().addObserver(this);
        UsuarioLogadoSingleton.getInstancia().addObserver(this);
        view.addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameClosed(InternalFrameEvent e) {
                pararDeObservar();
            }
        });
        atualizar();
    }

    private void pararDeObservar() {
        service.getProjetoService().removeObserver(this);
        UsuarioLogadoSingleton.getInstancia().removeObserver(this);
    }

    /** Recalcula e exibe os dados do usuário logado. */
    public final void atualizar() {
        Usuario usuario = UsuarioLogadoSingleton.getInstancia().getUsuario();
        if (usuario == null) {
            return;
        }
        FilaBanco.executar(view, "Carregando…", () -> service.resumir(usuario), r -> exibir(usuario, r),
                e -> ExibirMensagemCommand.erro("Não foi possível carregar o dashboard: " + e.getMessage()));
    }

    private void exibir(Usuario usuario, ResumoDashboard r) {
        view.setTitle("Dashboard de " + usuario.getNome());
        view.exibirIndicadores(String.valueOf(r.getProjetosProprios()), String.valueOf(r.getProjetosCompartilhados()),
                String.valueOf(r.getProjetosCalculados()), String.valueOf(r.getDiasTotalExibicao()),
                ResultadoEstimativa.formatarReais(r.getPrecoTotal()));
        view.exibirDetalhes(r.temProjetoCalculado() ? String.format("%s meses", ResultadoEstimativa.formatarMeses(
                r.getDiasTotal().divide(new BigDecimal(30), java.math.MathContext.DECIMAL128))) : "",
                r.temProjetoCalculado() ? r.getPrecoPorProjeto().size() + " projeto(s) calculado(s)" : "");
        view.exibirAviso(r.getProjetosComErro() == 0 ? ""
                : r.getProjetosComErro() + " projeto(s) calculado(s) estão sem plataforma marcada e ficaram fora das somas.");
        if (!r.temProjetoCalculado()) {
            view.exibirMensagemVazia(MENSAGEM_VAZIA);
            return;
        }
        view.exibirGraficos(graficoPreco(r), graficoPlataformas(r));
    }

    private ChartPanel graficoPreco(ResumoDashboard r) {
        DefaultPieDataset<String> dados = new DefaultPieDataset<>();
        for (Map.Entry<String, BigDecimal> e : r.getPrecoPorProjeto().entrySet()) {
            dados.setValue(e.getKey() + " (" + ResultadoEstimativa.formatarReais(e.getValue()) + ")",
                    ResultadoEstimativa.arredondarReais(e.getValue()));
        }
        JFreeChart grafico = ChartFactory.createPieChart("Preço final por projeto", dados, true, true, false);
        PiePlot<?> plot = (PiePlot<?>) grafico.getPlot();
        int i = 0;
        for (Object chave : dados.getKeys()) {
            plot.setSectionPaint((Comparable) chave, cor("Estimativa.grafico" + (i++ % 6 + 1)));
        }
        plot.setShadowPaint(null);
        plot.setSectionOutlinesVisible(true);
        plot.setDefaultSectionOutlinePaint(cor("Estimativa.cartao"));
        plot.setDefaultSectionOutlineStroke(new java.awt.BasicStroke(2f));
        plot.setLabelGenerator(null);
        plot.setInteriorGap(0.06);
        aplicarTema(grafico);
        return painel(grafico);
    }

    private ChartPanel graficoPlataformas(ResumoDashboard r) {
        DefaultCategoryDataset dados = new DefaultCategoryDataset();
        for (Map.Entry<Plataforma, BigDecimal> e : r.getDiasPorPlataforma().entrySet()) {
            dados.addValue(ResultadoEstimativa.arredondarDias(e.getValue()), "Dias", e.getKey().getDescricao());
        }
        JFreeChart grafico = ChartFactory.createBarChart("Dias por plataforma", null, "Dias", dados,
                PlotOrientation.VERTICAL, false, true, false);
        CategoryPlot plot = grafico.getCategoryPlot();
        BarRenderer barras = (BarRenderer) plot.getRenderer();
        barras.setBarPainter(new StandardBarPainter());
        barras.setShadowVisible(false);
        barras.setSeriesPaint(0, cor("Estimativa.grafico1"));
        barras.setMaximumBarWidth(0.18);
        barras.setDefaultItemLabelGenerator(new StandardCategoryItemLabelGenerator("{2}", new java.text.DecimalFormat("#,##0.##",
                java.text.DecimalFormatSymbols.getInstance(java.util.Locale.forLanguageTag("pt-BR")))));
        barras.setDefaultItemLabelsVisible(true);
        barras.setDefaultItemLabelPaint(cor("Label.foreground"));
        plot.setRangeGridlinePaint(cor("Table.gridColor"));
        plot.setOutlineVisible(false);
        plot.getDomainAxis().setTickLabelPaint(cor("Label.foreground"));
        plot.getDomainAxis().setAxisLinePaint(cor("Table.gridColor"));
        plot.getRangeAxis().setTickLabelPaint(cor("Estimativa.subtitulo"));
        plot.getRangeAxis().setLabelPaint(cor("Estimativa.subtitulo"));
        plot.getRangeAxis().setAxisLineVisible(false);
        plot.getRangeAxis().setUpperMargin(0.15);
        aplicarTema(grafico);
        return painel(grafico);
    }

    /** Fundo, título, legenda e fontes do gráfico vindos do tema atual. */
    private static void aplicarTema(JFreeChart grafico) {
        java.awt.Color fundo = cor("Estimativa.cartao");
        java.awt.Font base = UIManager.getFont("Label.font");
        grafico.setBackgroundPaint(fundo);
        grafico.getPlot().setBackgroundPaint(fundo);
        grafico.getPlot().setOutlineVisible(false);
        grafico.getTitle().setPaint(cor("Estimativa.titulo"));
        grafico.getTitle().setFont(base.deriveFont(java.awt.Font.BOLD, base.getSize2D() + 2));
        if (grafico.getLegend() != null) {
            grafico.getLegend().setBackgroundPaint(fundo);
            grafico.getLegend().setItemPaint(cor("Label.foreground"));
            grafico.getLegend().setItemFont(base);
            grafico.getLegend().setFrame(org.jfree.chart.block.BlockBorder.NONE);
        }
        if (grafico.getPlot() instanceof CategoryPlot cp) {
            cp.getDomainAxis().setTickLabelFont(base);
            cp.getRangeAxis().setTickLabelFont(base.deriveFont(base.getSize2D() - 1));
            cp.getRangeAxis().setLabelFont(base);
            ((BarRenderer) cp.getRenderer()).setDefaultItemLabelFont(base.deriveFont(java.awt.Font.BOLD));
        }
    }

    private static ChartPanel painel(JFreeChart grafico) {
        ChartPanel p = new ChartPanel(grafico);
        p.setOpaque(false);
        p.setPopupMenu(null);
        p.setMouseZoomable(false);
        return p;
    }

    private static java.awt.Color cor(String chave) {
        java.awt.Color c = UIManager.getColor(chave);
        return c != null ? c : UIManager.getColor("Label.foreground");
    }

    @Override
    public void update(EventoDominio evento) {
        if (evento.sobre(EventoDominio.Entidade.PROJETO) || evento.sobre(EventoDominio.Entidade.COMPARTILHAMENTO)
                || evento.sobre(EventoDominio.Entidade.SESSAO) || evento.sobre(EventoDominio.Entidade.TEMA)) {
            SwingUtilities.invokeLater(() -> {
                if (!view.isClosed()) {
                    atualizar();
                }
            });
        }
    }

    public DashboardView getView() {
        return view;
    }
}
