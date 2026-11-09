package com.br.estimativadeprojetodesoftware.view.projeto;

import com.br.estimativadeprojetodesoftware.service.IconService;
import com.br.estimativadeprojetodesoftware.view.componentes.BarraAcoes;
import com.br.estimativadeprojetodesoftware.view.componentes.BarraResumo;
import com.br.estimativadeprojetodesoftware.view.componentes.Estilos;
import com.br.estimativadeprojetodesoftware.view.componentes.Formulario;
import com.br.estimativadeprojetodesoftware.view.componentes.ItemComContador;
import com.br.estimativadeprojetodesoftware.view.componentes.JanelaInterna;
import com.br.estimativadeprojetodesoftware.view.componentes.PainelRolavel;
import com.br.estimativadeprojetodesoftware.view.componentes.RenderizadorComContador;
import com.br.estimativadeprojetodesoftware.view.componentes.RenderizadorOpcao;
import com.br.estimativadeprojetodesoftware.view.componentes.Selo;
import com.br.estimativadeprojetodesoftware.view.componentes.TabelaPadrao;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

/**
 * Tela do projeto de estimativa (inclusão, edição e somente
 * leitura). Passive View: só componentes e métodos de exibição chamados pelo
 * presenter; os estados decidem o que fica habilitado.
 */
public class ProjetoView extends JanelaInterna {

    private final JButton btnSalvar;
    private final JButton btnDescartar;
    private final JMenuItem btnCompartilhar = BarraAcoes.item("Compartilhar…", "compartilhar");
    private final JMenuItem btnExportarPdf = BarraAcoes.item("Relatório em PDF…", "exportar-pdf");
    private final JMenuItem btnExportarCsv = BarraAcoes.item("Planilha em CSV…", "exportar-csv");
    private final JMenuItem btnExcluir = BarraAcoes.item("Excluir projeto…", "excluir");
    private final JButton btnExportar;
    private final JButton btnMaisOpcoes;
    private final JButton btnFechar;

    private final Selo selo = new Selo();
    private final JTextField txtBuscaFuncionalidades = Formulario.placeholder(new JTextField(26), "Buscar por nome ou categoria");
    private final JToggleButton btnSomenteSelecionadas = plataforma("Somente selecionadas");
    private final JList<ItemComContador> listaSecoes = new JList<>() {
        @Override
        public boolean getScrollableTracksViewportWidth() {
            return true; // nome longo é cortado e a quantidade continua visível
        }
    };
    private final JLabel lblSemPlataforma = Estilos.aplicar(
            new JLabel("Marque ao menos uma plataforma na seção Plataformas para ver e editar os dias de cada funcionalidade."),
            Estilos.AVISO_ESTIMATIVA);
    /** Colunas de plataforma da tabela de funcionalidades, pelo índice no model (4, 5 e 6). */
    private final java.util.Map<Integer, javax.swing.table.TableColumn> colunasPlataforma = new java.util.TreeMap<>();
    private final JLabel lblContadorSelecionadas = Estilos.aplicar(new JLabel(" "), Estilos.SUBTITULO);
    private final JTextField txtNome = Formulario.placeholder(new JTextField(28), "Nome do projeto");
    private final JToggleButton chkWeb = plataforma("Web/Back-end");
    private final JToggleButton chkIos = plataforma("iOS");
    private final JToggleButton chkAndroid = plataforma("Android");
    private final Formulario formularioDados = new Formulario();

    private final JList<String> listaPerfis = new JList<>();
    private final JPanel painelPerfis = new JPanel(new BorderLayout(0, 6));

    private final TabelaPadrao tabelaItens = new TabelaPadrao();
    private final TabelaPadrao tabelaTaxas = new TabelaPadrao();
    private final TabelaPadrao tabelaCustos = new TabelaPadrao();
    private final JButton btnAdicionarCusto = new JButton("Adicionar custo", IconService.getIcon("adicionar"));
    private final JButton btnRemoverCusto = new JButton("Remover custo", IconService.getIcon("remover"));
    private final JTextField txtImpostos = Formulario.placeholder(new JTextField(6), "42");
    private final JTextField txtLucro = Formulario.placeholder(new JTextField(6), "30");
    private final Formulario formularioPreco = new Formulario();

    private final BarraResumo barraResumo = new BarraResumo("Preço final ao cliente", "Prazo",
            "Custo de desenvolvimento", "Média mensal");
    private final TabelaPadrao tabelaPlataformas = new TabelaPadrao();
    private final TabelaPadrao tabelaCategorias = new TabelaPadrao();
    private final TabelaPadrao tabelaFinanceiro = new TabelaPadrao();
    private final JLabel lblResultadoVazio = Estilos.aplicar(new JLabel(" ", IconService.getIcon("info", 32), SwingConstants.CENTER),
            "font: +1; foreground: $Estimativa.subtitulo");
    private final JPanel painelResultado = new JPanel(new CardLayout());
    private final JPanel painelConteudoSecoes = new JPanel(new CardLayout());
    private final JPanel painelSecoes = new JPanel(new BorderLayout(14, 0));
    private final JPanel blocosConfiguracao = new PainelRolavel(null);
    private SecaoProjeto secaoVisivel = SecaoProjeto.PLATAFORMAS;
    private List<TabelaPadrao> tabelasConfiguracao = List.of();

    public ProjetoView() {
        super("Projeto", " ", "projeto");
        btnSalvar = getBarra().adicionarPrincipal("Salvar", "salvar");
        btnDescartar = getBarra().adicionar("Descartar alterações", "cancelar");
        getBarra().separador();
        btnExportar = getBarra().adicionarMenu("Exportar", "exportar-pdf", btnExportarPdf, btnExportarCsv);
        btnExportar.setToolTipText("Exportar o relatório do projeto salvo");
        Estilos.aplicar(btnExcluir, Estilos.BOTAO_PERIGO);
        btnMaisOpcoes = getBarra().adicionarMenu("Mais", null, btnCompartilhar, btnExcluir);
        btnMaisOpcoes.setToolTipText("Compartilhar ou excluir o projeto");
        getBarra().empurrarParaDireita();
        btnFechar = getBarra().adicionar("Fechar", "fechar");
        setBotaoSalvar(btnSalvar);
        setExtraCabecalho(selo);

        // dados do projeto
        formularioDados.adicionarCampo("Nome", txtNome);

        listaPerfis.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        listaPerfis.setVisibleRowCount(4);
        Estilos.aplicar(painelPerfis, Estilos.CARTAO);
        painelPerfis.add(Estilos.aplicar(new JLabel("Perfis de origem (selecione um ou mais com Ctrl+clique)"), Estilos.SECAO), BorderLayout.NORTH);
        JScrollPane rolagemPerfis = new JScrollPane(listaPerfis);
        painelPerfis.add(rolagemPerfis, BorderLayout.CENTER);

        JPanel topo = new JPanel(new BorderLayout(0, 10));
        topo.setOpaque(false);
        topo.add(formularioDados, BorderLayout.NORTH);
        topo.add(painelPerfis, BorderLayout.CENTER);

        // seções
        for (JTable tabela : new JTable[]{tabelaItens, tabelaTaxas, tabelaCustos}) {
            // clicar fora da célula confirma o valor, e a estimativa acompanha
            tabela.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        }
        adicionarSecao(SecaoProjeto.PLATAFORMAS, montarPlataformas());
        adicionarSecao(SecaoProjeto.TAMANHO_E_UI, montarTamanhoEUi());
        adicionarSecao(SecaoProjeto.FUNCIONALIDADES, montarFuncionalidades());
        adicionarSecao(SecaoProjeto.TAXAS, comTitulo(SecaoProjeto.TAXAS, null, new JScrollPane(tabelaTaxas)));
        adicionarSecao(SecaoProjeto.CUSTOS, montarCustos());
        adicionarSecao(SecaoProjeto.DETALHAMENTO, comTitulo(SecaoProjeto.DETALHAMENTO, null, montarResultado()));

        listaSecoes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaSecoes.setCellRenderer(new RenderizadorComContador());
        listaSecoes.getAccessibleContext().setAccessibleName("Seções do projeto");
        JScrollPane rolagemSecoes = new JScrollPane(listaSecoes,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        rolagemSecoes.setPreferredSize(new Dimension(250, 0));
        painelConteudoSecoes.setOpaque(false);
        painelSecoes.setOpaque(false);
        painelSecoes.add(rolagemSecoes, BorderLayout.WEST);
        painelSecoes.add(painelConteudoSecoes, BorderLayout.CENTER);

        getConteudo().add(topo, BorderLayout.NORTH);
        getConteudo().add(painelSecoes, BorderLayout.CENTER);
        getConteudo().add(barraResumo, BorderLayout.SOUTH);
        setSize(1160, 720);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    }

    private static JToggleButton plataforma(String texto) {
        JToggleButton b = new JToggleButton(texto);
        b.putClientProperty("JButton.buttonType", "roundRect");
        Estilos.aplicar(b, "selectedBackground: $Estimativa.botaoPrincipal; selectedForeground: $Estimativa.botaoPrincipalTexto");
        return b;
    }

    private void adicionarSecao(SecaoProjeto secao, JComponent conteudo) {
        painelConteudoSecoes.add(conteudo, secao.name());
    }

    /** Título da seção, frase de apoio opcional e conteúdo. */
    private static JPanel comTitulo(SecaoProjeto secao, String apoio, JComponent conteudo) {
        JPanel cabecalho = new JPanel(new BorderLayout(0, 2));
        cabecalho.setOpaque(false);
        cabecalho.add(Estilos.aplicar(new JLabel(secao.getTitulo()), Estilos.SECAO), BorderLayout.NORTH);
        if (apoio != null) {
            cabecalho.add(Estilos.aplicar(new JLabel(apoio), Estilos.SUBTITULO), BorderLayout.CENTER);
        }
        JPanel p = new JPanel(new BorderLayout(0, 10));
        p.setOpaque(false);
        p.add(cabecalho, BorderLayout.NORTH);
        p.add(conteudo, BorderLayout.CENTER);
        return p;
    }

    private JPanel montarPlataformas() {
        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        botoes.setOpaque(false);
        botoes.add(chkWeb);
        botoes.add(chkIos);
        botoes.add(chkAndroid);
        JPanel alinhado = new JPanel(new BorderLayout());
        alinhado.setOpaque(false);
        alinhado.add(botoes, BorderLayout.NORTH);
        return comTitulo(SecaoProjeto.PLATAFORMAS,
                "Marque as plataformas do aplicativo. Só as plataformas marcadas entram na estimativa.", alinhado);
    }

    private JPanel montarTamanhoEUi() {
        blocosConfiguracao.setLayout(new BoxLayout(blocosConfiguracao, BoxLayout.Y_AXIS));
        blocosConfiguracao.setOpaque(false);
        JScrollPane rolagem = new JScrollPane(blocosConfiguracao);
        rolagem.setBorder(BorderFactory.createEmptyBorder());
        rolagem.getVerticalScrollBar().setUnitIncrement(24);
        return comTitulo(SecaoProjeto.TAMANHO_E_UI,
                "Base do projeto. Os valores de cada opção por plataforma podem ser ajustados na edição.", rolagem);
    }

    private JPanel montarFuncionalidades() {
        JPanel filtros = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        filtros.setOpaque(false);
        filtros.add(txtBuscaFuncionalidades);
        filtros.add(btnSomenteSelecionadas);
        JPanel barra = new JPanel(new BorderLayout(12, 0));
        barra.setOpaque(false);
        barra.add(filtros, BorderLayout.WEST);
        barra.add(lblContadorSelecionadas, BorderLayout.EAST);
        lblSemPlataforma.setVisible(false);
        JPanel topo = new JPanel(new BorderLayout(0, 6));
        topo.setOpaque(false);
        topo.add(barra, BorderLayout.NORTH);
        topo.add(lblSemPlataforma, BorderLayout.SOUTH);
        JPanel p = new JPanel(new BorderLayout(0, 8));
        p.setOpaque(false);
        p.add(topo, BorderLayout.NORTH);
        p.add(new JScrollPane(tabelaItens), BorderLayout.CENTER);
        return comTitulo(SecaoProjeto.FUNCIONALIDADES, null, p);
    }

    private JPanel montarCustos() {
        JPanel acoes = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        acoes.setOpaque(false);
        acoes.add(btnAdicionarCusto);
        acoes.add(btnRemoverCusto);
        tabelaCustos.setFormatoColuna(1, TabelaPadrao.Formato.REAIS);
        JPanel custos = new JPanel(new BorderLayout(0, 8));
        custos.setOpaque(false);
        custos.add(acoes, BorderLayout.NORTH);
        custos.add(new JScrollPane(tabelaCustos), BorderLayout.CENTER);

        formularioPreco.adicionarSecao("Percentuais do projeto");
        formularioPreco.adicionarCampo("Impostos (%)", txtImpostos);
        formularioPreco.adicionarCampo("Lucro (%)", txtLucro);
        JPanel lateral = new JPanel(new BorderLayout());
        Estilos.aplicar(lateral, Estilos.CARTAO);
        lateral.add(formularioPreco, BorderLayout.NORTH);

        JPanel p = new JPanel(new BorderLayout(14, 0));
        p.setOpaque(false);
        p.add(custos, BorderLayout.CENTER);
        p.add(lateral, BorderLayout.EAST);
        return comTitulo(SecaoProjeto.CUSTOS, "Custos adicionais, impostos e lucro do projeto.", p);
    }

    private JPanel montarResultado() {
        tabelaPlataformas.setFormatoColuna(1, TabelaPadrao.Formato.NUMERO);
        tabelaPlataformas.setFormatoColuna(2, TabelaPadrao.Formato.NUMERO);
        tabelaPlataformas.setFormatoColuna(3, TabelaPadrao.Formato.NUMERO);
        tabelaPlataformas.setFormatoColuna(4, TabelaPadrao.Formato.NUMERO);
        tabelaPlataformas.setFormatoColuna(5, TabelaPadrao.Formato.NUMERO);
        tabelaPlataformas.setFormatoColuna(6, TabelaPadrao.Formato.REAIS);
        tabelaCategorias.setFormatoColuna(1, TabelaPadrao.Formato.NUMERO);
        tabelaCategorias.setFormatoColuna(2, TabelaPadrao.Formato.REAIS);
        tabelaFinanceiro.setFormatoColuna(1, TabelaPadrao.Formato.REAIS);

        JPanel tabelas = new JPanel(new GridLayout(1, 2, 12, 0));
        tabelas.setOpaque(false);
        tabelas.add(bloco("Por categoria", tabelaCategorias));
        tabelas.add(bloco("Resumo financeiro", tabelaFinanceiro));

        PainelRolavel corpo = new PainelRolavel(new BorderLayout(0, 12));
        corpo.add(bloco("Por plataforma", tabelaPlataformas), BorderLayout.NORTH);
        corpo.add(tabelas, BorderLayout.CENTER);

        lblResultadoVazio.setVerticalTextPosition(SwingConstants.BOTTOM);
        lblResultadoVazio.setHorizontalTextPosition(SwingConstants.CENTER);
        JScrollPane rolagem = new JScrollPane(corpo);
        rolagem.setBorder(BorderFactory.createEmptyBorder());
        rolagem.getVerticalScrollBar().setUnitIncrement(24);
        painelResultado.add(rolagem, "resultado");
        painelResultado.add(lblResultadoVazio, "vazio");
        return painelResultado;
    }

    private static JPanel bloco(String titulo, JTable tabela) {
        JPanel p = new JPanel(new BorderLayout(0, 6));
        p.setOpaque(false);
        p.add(Estilos.aplicar(new JLabel(titulo), Estilos.SECAO), BorderLayout.NORTH);
        JScrollPane s = new JScrollPane(tabela);
        s.setPreferredSize(new java.awt.Dimension(10, Math.max(120, tabela.getRowHeight() * 4 + 36)));
        p.add(s, BorderLayout.CENTER);
        return p;
    }

    // ---------- métodos de exibição ----------

    /** Selo de situação no cabeçalho e texto de apoio abaixo do título. */
    public void exibirSituacao(String textoSelo, Selo.Tipo tipo, String detalhe) {
        selo.exibir(textoSelo, tipo);
        setSubtitulo(detalhe);
    }

    public void exibirResumo(String prazo, String meses, String desenvolvimento, String dias,
            String precoFinal, String precoDetalhe, String media) {
        getIndicadorPrazo().setValor(prazo);
        getIndicadorPrazo().setDetalhe(meses);
        getIndicadorDesenvolvimento().setValor(desenvolvimento);
        getIndicadorDesenvolvimento().setDetalhe(dias);
        getIndicadorPreco().setValor(precoFinal);
        getIndicadorPreco().setDetalhe(precoDetalhe);
        getIndicadorMedia().setValor(media);
        getIndicadorMedia().setDetalhe(" ");
        exibirAvisoEstimativa(null);
        ((CardLayout) painelResultado.getLayout()).show(painelResultado, "resultado");
    }

    /** Indicadores indisponíveis e mensagem no lugar do resultado (projeto não calculado ou inválido). */
    public void exibirResultadoVazio(String mensagem) {
        for (int i = 0; i < 4; i++) {
            barraResumo.getIndicador(i).setValor("—");
            barraResumo.getIndicador(i).setDetalhe(" ");
        }
        lblResultadoVazio.setText("<html><div style='text-align:center'>" + mensagem + "</div></html>");
        exibirAvisoEstimativa(mensagem);
        ((CardLayout) painelResultado.getLayout()).show(painelResultado, "vazio");
    }

    /** Texto abaixo dos indicadores; null ou vazio esconde o aviso. */
    public void exibirAvisoEstimativa(String mensagem) {
        barraResumo.exibirAviso(mensagem);
    }

    /**
     * Deixa na tabela de funcionalidades só a marcação, o nome e as colunas de
     * plataforma (sem Categoria e Seleção), com cabeçalhos do tamanho do texto.
     * Chamar depois de definir o model.
     */
    public void configurarTabelaFuncionalidades() {
        javax.swing.table.TableColumnModel colunas = tabelaItens.getColumnModel();
        if (colunas.getColumnCount() < 7) {
            return;
        }
        colunasPlataforma.clear();
        for (int indiceModel = 4; indiceModel <= 6; indiceModel++) {
            colunasPlataforma.put(indiceModel, colunas.getColumn(indiceModel));
        }
        tabelaItens.removeColumn(colunas.getColumn(3)); // Seleção
        tabelaItens.removeColumn(colunas.getColumn(1)); // Categoria (a barra lateral já mostra)
        colunas.getColumn(0).setHeaderValue("Incluir");
        ajustarLarguraPeloCabecalho(colunas.getColumn(0), 0);
        colunas.getColumn(0).setMaxWidth(colunas.getColumn(0).getMinWidth() + 16);
        ajustarLarguraPeloCabecalho(colunas.getColumn(1), 0);
        colunas.getColumn(1).setPreferredWidth(360);
        for (javax.swing.table.TableColumn c : colunasPlataforma.values()) {
            ajustarLarguraPeloCabecalho(c, 20);
            c.setPreferredWidth(Math.max(c.getMinWidth(), 110));
        }
    }

    /** Largura mínima suficiente para o texto do cabeçalho não ser cortado. */
    private void ajustarLarguraPeloCabecalho(javax.swing.table.TableColumn coluna, int folga) {
        java.awt.FontMetrics fm = tabelaItens.getTableHeader().getFontMetrics(tabelaItens.getTableHeader().getFont());
        coluna.setMinWidth(fm.stringWidth(String.valueOf(coluna.getHeaderValue())) + 28 + folga);
    }

    /** Mostra só as colunas das plataformas marcadas, na ordem Web/Back-end, iOS, Android. */
    public void mostrarColunasPlataformas(boolean web, boolean ios, boolean android) {
        boolean[] visiveis = {web, ios, android};
        javax.swing.table.TableColumnModel colunas = tabelaItens.getColumnModel();
        int posicao = 2; // depois de Incluir e Funcionalidade
        int i = 0;
        for (javax.swing.table.TableColumn coluna : colunasPlataforma.values()) {
            boolean presente = indiceDa(colunas, coluna) >= 0;
            if (visiveis[i] && !presente) {
                tabelaItens.addColumn(coluna);
                colunas.moveColumn(colunas.getColumnCount() - 1, posicao);
            } else if (!visiveis[i] && presente) {
                tabelaItens.removeColumn(coluna);
            }
            if (visiveis[i]) {
                posicao++;
            }
            i++;
        }
        lblSemPlataforma.setVisible(!web && !ios && !android);
    }

    private static int indiceDa(javax.swing.table.TableColumnModel colunas, javax.swing.table.TableColumn coluna) {
        for (int i = 0; i < colunas.getColumnCount(); i++) {
            if (colunas.getColumn(i) == coluna) {
                return i;
            }
        }
        return -1;
    }

    public JLabel getLblSemPlataforma() {
        return lblSemPlataforma;
    }

    /** Mostra a seção na área principal; a barra lateral é escolhida pelo presenter. */
    public void mostrarSecao(SecaoProjeto secao) {
        secaoVisivel = secao;
        ((CardLayout) painelConteudoSecoes.getLayout()).show(painelConteudoSecoes, secao.name());
    }

    /** Seção visível na área principal. */
    public SecaoProjeto getSecaoVisivel() {
        return secaoVisivel;
    }

    /**
     * Refaz os blocos da seção Tamanho e UI, um por título, e devolve as
     * tabelas na mesma ordem para o presenter ligar ao model.
     */
    public List<TabelaPadrao> prepararBlocosConfiguracao(List<String> titulos) {
        blocosConfiguracao.removeAll();
        List<TabelaPadrao> tabelas = new ArrayList<>();
        for (String titulo : titulos) {
            TabelaPadrao tabela = new TabelaPadrao();
            tabela.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
            JScrollPane rolagem = new JScrollPane(tabela, JScrollPane.VERTICAL_SCROLLBAR_NEVER,
                    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            JPanel bloco = new JPanel(new BorderLayout(0, 6));
            bloco.setOpaque(false);
            bloco.setAlignmentX(LEFT_ALIGNMENT);
            bloco.setBorder(BorderFactory.createEmptyBorder(0, 0, 16, 0));
            bloco.add(Estilos.aplicar(new JLabel(titulo), "font: bold; foreground: $Estimativa.titulo"), BorderLayout.NORTH);
            bloco.add(rolagem, BorderLayout.CENTER);
            blocosConfiguracao.add(bloco);
            tabelas.add(tabela);
        }
        blocosConfiguracao.revalidate();
        blocosConfiguracao.repaint();
        tabelasConfiguracao = List.copyOf(tabelas);
        return tabelasConfiguracao;
    }

    /**
     * Esconde as colunas Categoria e Seleção de uma tabela da seção Tamanho e
     * UI, desenha a seleção como botão de opção (escolha única) ou caixa
     * (opcional) e ajusta a altura às linhas. Chamar depois de definir o model.
     */
    public void ajustarTabelaConfiguracao(TabelaPadrao tabela, boolean escolhaUnica) {
        if (tabela.getColumnModel().getColumnCount() >= 7) {
            tabela.removeColumn(tabela.getColumnModel().getColumn(3));
            tabela.removeColumn(tabela.getColumnModel().getColumn(1));
        }
        tabela.getColumnModel().getColumn(0).setMaxWidth(70);
        tabela.getColumnModel().getColumn(0).setHeaderValue(escolhaUnica ? "Escolha" : "Incluir");
        tabela.getColumnModel().getColumn(1).setPreferredWidth(300);
        tabela.getColumnModel().getColumn(1).setHeaderValue("Opção");
        if (escolhaUnica) {
            RenderizadorOpcao.aplicar(tabela, 0);
        }
        JComponent bloco = (JComponent) tabela.getParent().getParent().getParent();
        bloco.setToolTipText(escolhaUnica ? "Escolha uma opção; clique de novo na opção marcada para desmarcá-la." : null);
        int altura = tabela.getRowCount() * tabela.getRowHeight() + tabela.getTableHeader().getPreferredSize().height + 4;
        JScrollPane rolagem = (JScrollPane) tabela.getParent().getParent();
        rolagem.setPreferredSize(new Dimension(10, altura));
        bloco.setMaximumSize(new Dimension(Integer.MAX_VALUE, altura + 40));
    }

    /** Erro de percentual durante a digitação (sem trocar de seção). */
    public void mostrarErroPercentual(JComponent campo, String mensagem) {
        formularioPreco.mostrarErro(campo, mensagem);
    }

    public void exibirContadorSelecionadas(String texto) {
        lblContadorSelecionadas.setText(texto);
    }

    public void mostrarErroCampo(JComponent campo, String mensagem) {
        if (formularioPreco.getRotulo(campo) != null) {
            // quem mostra a seção Custos e preço é o presenter, pela barra lateral
            formularioPreco.mostrarErro(campo, mensagem);
        } else {
            formularioDados.mostrarErro(campo == txtNome ? (JComponent) txtNome.getParent() : campo, mensagem);
            campo.putClientProperty(com.formdev.flatlaf.FlatClientProperties.OUTLINE, com.formdev.flatlaf.FlatClientProperties.OUTLINE_ERROR);
        }
    }

    public void limparErroCampo(JComponent campo) {
        formularioPreco.limparErro(campo);
        formularioDados.limparErro(campo == txtNome ? (JComponent) txtNome.getParent() : campo);
        campo.putClientProperty(com.formdev.flatlaf.FlatClientProperties.OUTLINE, null);
    }

    public void limparErros() {
        formularioPreco.limparErros();
        formularioDados.limparErros();
        txtNome.putClientProperty(com.formdev.flatlaf.FlatClientProperties.OUTLINE, null);
    }

    public String getMensagemErro(JComponent campo) {
        String m = formularioPreco.getMensagemErro(campo);
        return m != null ? m : formularioDados.getMensagemErro(campo == txtNome ? (JComponent) txtNome.getParent() : campo);
    }

    /** Escolha do arquivo de destino da exportação; null se cancelar. */
    public File escolherArquivo(String nomeSugerido) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Salvar relatório");
        chooser.setSelectedFile(new File(nomeSugerido));
        return chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION ? chooser.getSelectedFile() : null;
    }

    /** Barra lateral com as seções e as categorias de funcionalidades. */
    public JList<ItemComContador> getListaSecoes() {
        return listaSecoes;
    }

    /** Tabelas da seção Tamanho e UI, uma por categoria de configuração. */
    public List<TabelaPadrao> getTabelasConfiguracao() {
        return tabelasConfiguracao;
    }

    /** Barra lateral e área das seções (ocultas na inclusão). */
    public JPanel getPainelSecoes() {
        return painelSecoes;
    }

    // ---------- componentes ----------

    public JPanel getPainelPerfis() {
        return painelPerfis;
    }

    /** Barra de resumo da estimativa no rodapé. */
    public JPanel getPainelEstimativa() {
        return barraResumo;
    }

    public JLabel getLblAvisoEstimativa() {
        return barraResumo.getLblAviso();
    }

    public JTextField getTxtBuscaFuncionalidades() {
        return txtBuscaFuncionalidades;
    }

    public AbstractButton getBtnSomenteSelecionadas() {
        return btnSomenteSelecionadas;
    }

    public JLabel getLblContadorSelecionadas() {
        return lblContadorSelecionadas;
    }

    public JButton getBtnSalvar() {
        return btnSalvar;
    }

    /** "Descartar alterações" na edição; "Cancelar" na inclusão. */
    public JButton getBtnDescartar() {
        return btnDescartar;
    }

    /** Botões que abrem os menus Exportar e Mais (para mostrar ou testar). */
    public JButton getBtnExportar() {
        return btnExportar;
    }

    public JButton getBtnMaisOpcoes() {
        return btnMaisOpcoes;
    }

    public JMenuItem getBtnExcluir() {
        return btnExcluir;
    }

    public JMenuItem getBtnCompartilhar() {
        return btnCompartilhar;
    }

    public JMenuItem getBtnExportarPdf() {
        return btnExportarPdf;
    }

    public JMenuItem getBtnExportarCsv() {
        return btnExportarCsv;
    }

    public JButton getBtnFechar() {
        return btnFechar;
    }

    public JTextField getTxtNome() {
        return txtNome;
    }

    public AbstractButton getChkWeb() {
        return chkWeb;
    }

    public AbstractButton getChkIos() {
        return chkIos;
    }

    public AbstractButton getChkAndroid() {
        return chkAndroid;
    }

    public Selo getSelo() {
        return selo;
    }

    /** Linha de apoio abaixo do título. */
    public JLabel getLblSituacao() {
        return getLblSubtitulo();
    }

    public JList<String> getListaPerfis() {
        return listaPerfis;
    }

    public TabelaPadrao getTabelaItens() {
        return tabelaItens;
    }

    public JTable getTabelaTaxas() {
        return tabelaTaxas;
    }

    public JTable getTabelaCustos() {
        return tabelaCustos;
    }

    public JTable getTabelaPlataformas() {
        return tabelaPlataformas;
    }

    public JTable getTabelaCategorias() {
        return tabelaCategorias;
    }

    public JTable getTabelaFinanceiro() {
        return tabelaFinanceiro;
    }

    public JButton getBtnAdicionarCusto() {
        return btnAdicionarCusto;
    }

    public JButton getBtnRemoverCusto() {
        return btnRemoverCusto;
    }

    public JTextField getTxtImpostos() {
        return txtImpostos;
    }

    public JTextField getTxtLucro() {
        return txtLucro;
    }

    public BarraResumo.Indicador getIndicadorPreco() {
        return barraResumo.getIndicador(0);
    }

    public BarraResumo.Indicador getIndicadorPrazo() {
        return barraResumo.getIndicador(1);
    }

    public BarraResumo.Indicador getIndicadorDesenvolvimento() {
        return barraResumo.getIndicador(2);
    }

    public BarraResumo.Indicador getIndicadorMedia() {
        return barraResumo.getIndicador(3);
    }

    public JLabel getLblResultadoVazio() {
        return lblResultadoVazio;
    }
}
