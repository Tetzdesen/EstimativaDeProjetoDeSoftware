package com.br.estimativadeprojetodesoftware.view.projeto;

import com.br.estimativadeprojetodesoftware.service.IconService;
import com.br.estimativadeprojetodesoftware.view.componentes.CartaoIndicador;
import com.br.estimativadeprojetodesoftware.view.componentes.Estilos;
import com.br.estimativadeprojetodesoftware.view.componentes.Formulario;
import com.br.estimativadeprojetodesoftware.view.componentes.JanelaInterna;
import com.br.estimativadeprojetodesoftware.view.componentes.PainelRolavel;
import com.br.estimativadeprojetodesoftware.view.componentes.Selo;
import com.br.estimativadeprojetodesoftware.view.componentes.TabelaPadrao;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.File;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

/**
 * Tela do projeto de estimativa (inclusão, edição, visualização e somente
 * leitura). Passive View: só componentes e métodos de exibição chamados pelo
 * presenter; os estados decidem o que fica habilitado.
 */
public class ProjetoView extends JanelaInterna {

    private final JButton btnEditar;
    private final JButton btnSalvar;
    private final JButton btnCancelar;
    private final JButton btnCalcular;
    private final JButton btnCompartilhar;
    private final JButton btnExportarPdf;
    private final JButton btnExportarCsv;
    private final JButton btnExcluir;
    private final JButton btnFechar;

    private final Selo selo = new Selo();
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

    private final CartaoIndicador cartaoPrazo = new CartaoIndicador("Prazo");
    private final CartaoIndicador cartaoDesenvolvimento = new CartaoIndicador("Custo de desenvolvimento");
    private final CartaoIndicador cartaoPreco = new CartaoIndicador("Preço final ao cliente");
    private final CartaoIndicador cartaoMedia = new CartaoIndicador("Média mensal");
    private final TabelaPadrao tabelaPlataformas = new TabelaPadrao();
    private final TabelaPadrao tabelaCategorias = new TabelaPadrao();
    private final TabelaPadrao tabelaFinanceiro = new TabelaPadrao();
    private final JLabel lblResultadoVazio = Estilos.aplicar(new JLabel(" ", IconService.getIcon("info", 32), SwingConstants.CENTER),
            "font: +1; foreground: $Estimativa.subtitulo");
    private final JPanel painelResultado = new JPanel(new CardLayout());
    private final JTabbedPane abas = new JTabbedPane();

    public ProjetoView() {
        super("Projeto", " ", "projeto");
        btnEditar = getBarra().adicionar("Editar", "editar");
        btnSalvar = getBarra().adicionarPrincipal("Salvar", "salvar");
        btnCancelar = getBarra().adicionar("Cancelar", "cancelar");
        getBarra().separador();
        btnCalcular = getBarra().adicionar("Calcular", "calcular");
        getBarra().separador();
        btnCompartilhar = getBarra().adicionar("Compartilhar", "compartilhar");
        btnExportarPdf = getBarra().adicionar("PDF", "exportar-pdf");
        btnExportarCsv = getBarra().adicionar("CSV", "exportar-csv");
        btnExportarPdf.setToolTipText("Exportar relatório em PDF");
        btnExportarCsv.setToolTipText("Exportar relatório em CSV");
        getBarra().separador();
        btnExcluir = getBarra().adicionarPerigo("Excluir", "excluir");
        getBarra().empurrarParaDireita();
        btnFechar = getBarra().adicionar("Fechar", "fechar");
        setBotaoSalvar(btnSalvar);
        setExtraCabecalho(selo);

        // dados do projeto
        JPanel plataformas = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        plataformas.setOpaque(false);
        plataformas.add(chkWeb);
        plataformas.add(chkIos);
        plataformas.add(chkAndroid);
        JPanel linhaDados = new JPanel(new BorderLayout(16, 0));
        linhaDados.setOpaque(false);
        linhaDados.add(txtNome, BorderLayout.CENTER);
        JPanel blocoPlataformas = new JPanel(new BorderLayout(8, 0));
        blocoPlataformas.setOpaque(false);
        blocoPlataformas.add(new JLabel("Plataformas"), BorderLayout.WEST);
        blocoPlataformas.add(plataformas, BorderLayout.CENTER);
        linhaDados.add(blocoPlataformas, BorderLayout.EAST);
        formularioDados.adicionarCampo("Nome", linhaDados);

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

        // abas
        abas.addTab("Funcionalidades", IconService.getIcon("projeto", 16), new JScrollPane(tabelaItens));
        abas.addTab("Taxas diárias", IconService.getIcon("calcular", 16), new JScrollPane(tabelaTaxas));
        abas.addTab("Custos, impostos e lucro", IconService.getIcon("adicionar", 16), montarCustos());
        abas.addTab("Resultado", IconService.getIcon("dashboard", 16), montarResultado());

        getConteudo().add(topo, BorderLayout.NORTH);
        getConteudo().add(abas, BorderLayout.CENTER);
        setSize(1160, 720);
    }

    private static JToggleButton plataforma(String texto) {
        JToggleButton b = new JToggleButton(texto);
        b.putClientProperty("JButton.buttonType", "roundRect");
        Estilos.aplicar(b, "selectedBackground: $Estimativa.botaoPrincipal; selectedForeground: $Estimativa.botaoPrincipalTexto");
        return b;
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
        p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        p.add(custos, BorderLayout.CENTER);
        p.add(lateral, BorderLayout.EAST);
        return p;
    }

    private JPanel montarResultado() {
        JPanel cartoes = new JPanel(new GridLayout(1, 4, 12, 0));
        cartoes.setOpaque(false);
        for (CartaoIndicador c : new CartaoIndicador[]{cartaoPrazo, cartaoDesenvolvimento, cartaoPreco, cartaoMedia}) {
            cartoes.add(c);
        }
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
        JPanel superior = new JPanel(new BorderLayout(0, 12));
        superior.setOpaque(false);
        superior.add(cartoes, BorderLayout.NORTH);
        superior.add(bloco("Por plataforma", tabelaPlataformas), BorderLayout.CENTER);
        corpo.add(superior, BorderLayout.NORTH);
        corpo.add(tabelas, BorderLayout.CENTER);
        corpo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

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

    public void exibirCartoesResultado(String prazo, String meses, String desenvolvimento, String dias,
            String precoFinal, String precoDetalhe, String media) {
        cartaoPrazo.setValor(prazo);
        cartaoPrazo.setDetalhe(meses);
        cartaoDesenvolvimento.setValor(desenvolvimento);
        cartaoDesenvolvimento.setDetalhe(dias);
        cartaoPreco.setValor(precoFinal);
        cartaoPreco.setDetalhe(precoDetalhe);
        cartaoMedia.setValor(media);
        ((CardLayout) painelResultado.getLayout()).show(painelResultado, "resultado");
    }

    /** Mensagem no lugar do resultado (projeto não calculado ou inválido). */
    public void exibirResultadoVazio(String mensagem) {
        lblResultadoVazio.setText("<html><div style='text-align:center'>" + mensagem + "</div></html>");
        ((CardLayout) painelResultado.getLayout()).show(painelResultado, "vazio");
    }

    public void mostrarErroCampo(JComponent campo, String mensagem) {
        if (formularioPreco.getRotulo(campo) != null) {
            abas.setSelectedIndex(2);
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

    public void mostrarAbaResultado() {
        abas.setSelectedIndex(abas.getTabCount() - 1);
    }

    public JTabbedPane getAbas() {
        return abas;
    }

    // ---------- componentes ----------

    public JPanel getPainelPerfis() {
        return painelPerfis;
    }

    public JButton getBtnEditar() {
        return btnEditar;
    }

    public JButton getBtnSalvar() {
        return btnSalvar;
    }

    public JButton getBtnCancelar() {
        return btnCancelar;
    }

    public JButton getBtnExcluir() {
        return btnExcluir;
    }

    public JButton getBtnCalcular() {
        return btnCalcular;
    }

    public JButton getBtnCompartilhar() {
        return btnCompartilhar;
    }

    public JButton getBtnExportarPdf() {
        return btnExportarPdf;
    }

    public JButton getBtnExportarCsv() {
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

    public JTable getTabelaItens() {
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

    public CartaoIndicador getCartaoPrazo() {
        return cartaoPrazo;
    }

    public CartaoIndicador getCartaoPreco() {
        return cartaoPreco;
    }

    public CartaoIndicador getCartaoMedia() {
        return cartaoMedia;
    }

    public CartaoIndicador getCartaoDesenvolvimento() {
        return cartaoDesenvolvimento;
    }

    public JLabel getLblResultadoVazio() {
        return lblResultadoVazio;
    }
}
