package com.br.estimativadeprojetodesoftware.presenter.projeto;

import static com.br.estimativadeprojetodesoftware.model.ResultadoEstimativa.formatarDias;
import static com.br.estimativadeprojetodesoftware.model.ResultadoEstimativa.formatarMeses;
import static com.br.estimativadeprojetodesoftware.model.ResultadoEstimativa.formatarPercentual;
import static com.br.estimativadeprojetodesoftware.model.ResultadoEstimativa.formatarReais;

import com.br.estimativadeprojetodesoftware.command.ConfirmarCommand;
import com.br.estimativadeprojetodesoftware.command.ExibirMensagemCommand;
import com.br.estimativadeprojetodesoftware.command.PerguntarCommand;
import com.br.estimativadeprojetodesoftware.model.CustoAdicional;
import com.br.estimativadeprojetodesoftware.model.Perfil;
import com.br.estimativadeprojetodesoftware.model.Plataforma;
import com.br.estimativadeprojetodesoftware.model.Projeto;
import com.br.estimativadeprojetodesoftware.model.ResultadoEstimativa;
import com.br.estimativadeprojetodesoftware.model.ResultadoEstimativa.DetalhePlataforma;
import com.br.estimativadeprojetodesoftware.model.ResultadoEstimativa.ResumoCategoria;
import com.br.estimativadeprojetodesoftware.model.Usuario;
import com.br.estimativadeprojetodesoftware.observer.EventoDominio;
import com.br.estimativadeprojetodesoftware.observer.Observer;
import com.br.estimativadeprojetodesoftware.presenter.helpers.RecalculoAgendado;
import com.br.estimativadeprojetodesoftware.presenter.helpers.ValidacaoVisual;
import com.br.estimativadeprojetodesoftware.presenter.tabela.CustosTableModel;
import com.br.estimativadeprojetodesoftware.presenter.helpers.FilaBanco;
import com.br.estimativadeprojetodesoftware.presenter.tabela.NavegacaoProjetoModel;
import com.br.estimativadeprojetodesoftware.presenter.tabela.FiltroFuncionalidades;
import com.br.estimativadeprojetodesoftware.presenter.tabela.FormatoValor;
import com.br.estimativadeprojetodesoftware.presenter.tabela.ItensProjetoTableModel;
import com.br.estimativadeprojetodesoftware.presenter.tabela.TaxasTableModel;
import com.br.estimativadeprojetodesoftware.service.ExportacaoService;
import com.br.estimativadeprojetodesoftware.service.PerfilService;
import com.br.estimativadeprojetodesoftware.service.ProjetoService;
import com.br.estimativadeprojetodesoftware.singleton.UsuarioLogadoSingleton;
import com.br.estimativadeprojetodesoftware.state.projeto.EdicaoProjetoState;
import com.br.estimativadeprojetodesoftware.state.projeto.InclusaoProjetoState;
import com.br.estimativadeprojetodesoftware.state.projeto.ManterProjetoPresenterState;
import com.br.estimativadeprojetodesoftware.state.projeto.SomenteLeituraProjetoState;
import com.br.estimativadeprojetodesoftware.templatemethod.FormatoExportacao;
import com.br.estimativadeprojetodesoftware.model.TipoSelecao;
import com.br.estimativadeprojetodesoftware.view.componentes.TabelaPadrao;
import com.br.estimativadeprojetodesoftware.view.projeto.ProjetoView;
import com.br.estimativadeprojetodesoftware.view.projeto.SecaoProjeto;
import java.math.BigDecimal;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.swing.SwingUtilities;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

/**
 * Presenter da tela de projeto (MVP Passive View). O comportamento de cada
 * modo fica no State atual; cada ação é um Command. Observa mudanças de
 * projeto para mostrar sempre a versão atual (inclusive ao destinatário de um
 * compartilhamento). Em edição, cada alteração agenda um recálculo da
 * estimativa, feito só em memória até o usuário salvar.
 */
public class ManterProjetoPresenter implements Observer {

    private static final int ATRASO_RECALCULO_MS = 250;

    private final ProjetoView view;
    private final ProjetoService projetoService;
    private final PerfilService perfilService;
    private final ExportacaoService exportacaoService;
    private final Usuario usuario;
    private Projeto projeto;
    private List<Perfil> perfisDisponiveis = List.of();
    private ManterProjetoPresenterState estado;
    private ItensProjetoTableModel modeloItens;
    private NavegacaoProjetoModel modeloNavegacao;
    private List<TabelaPadrao> tabelasConfiguracao = List.of();
    private TaxasTableModel modeloTaxas;
    private CustosTableModel modeloCustos;
    private boolean editavel;
    private boolean carregando;
    private boolean alterado;
    private BigDecimal ultimoPreco;
    private final RecalculoAgendado recalculo =
            new RecalculoAgendado(ATRASO_RECALCULO_MS, this::aoDispararRecalculo);

    private ManterProjetoPresenter() {
        this(new ProjetoView(), new ProjetoService(), new PerfilService(), new ExportacaoService(),
                UsuarioLogadoSingleton.getInstancia().getUsuario());
    }

    /** Dependências explícitas: usado diretamente nos testes. */
    ManterProjetoPresenter(ProjetoView view, ProjetoService projetoService, PerfilService perfilService,
            ExportacaoService exportacaoService, Usuario usuario) {
        this.view = view;
        this.projetoService = projetoService;
        this.perfilService = perfilService;
        this.exportacaoService = exportacaoService;
        this.usuario = usuario;
        configurarAcoes();
        ValidacaoVisual.limparAoEditar(view.getTxtNome(), view::limparErroCampo);
        ValidacaoVisual.limparAoEditar(view.getTxtImpostos(), view::limparErroCampo);
        ValidacaoVisual.limparAoEditar(view.getTxtLucro(), view::limparErroCampo);
        configurarDeteccaoDeAlteracoes();
        configurarFiltroFuncionalidades();
        projetoService.addObserver(this);
        view.addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameClosing(InternalFrameEvent e) {
                executar(ManterProjetoPresenter.this::solicitarFechamento);
            }

            @Override
            public void internalFrameClosed(InternalFrameEvent e) {
                recalculo.cancelar();
                projetoService.removeObserver(ManterProjetoPresenter.this);
            }
        });
    }

    public static ManterProjetoPresenter novo() {
        ManterProjetoPresenter p = new ManterProjetoPresenter();
        p.iniciarInclusao();
        return p;
    }

    public static ManterProjetoPresenter paraProjeto(UUID id) {
        ManterProjetoPresenter p = new ManterProjetoPresenter();
        p.abrirProjeto(id);
        return p;
    }

    void iniciarInclusao() {
        view.setTituloCabecalho("Novo projeto");
        setEditavel(false);
        view.getTxtNome().setEditable(true);
        setEstado(new InclusaoProjetoState(this));
        noBanco("Carregando perfis…", () -> perfilService.listarVisiveis(usuario), perfis -> {
            perfisDisponiveis = perfis;
            view.getListaPerfis().setListData(perfisDisponiveis.stream().map(Perfil::toString).toArray(String[]::new));
            if (!perfisDisponiveis.isEmpty()) {
                view.getListaPerfis().setSelectedIndex(0);
            }
        });
    }

    void abrirProjeto(UUID id) {
        view.setTituloCabecalho("Abrindo projeto…");
        FilaBanco.executar(view, "Abrindo…", () -> projetoService.buscarParaVisualizacao(id, usuario), p -> {
            setProjeto(p);
            mostrarResultadoAtual();
            setEstado(estadoInicial()); // em edição, recalcula ao vivo por cima do resultado salvo
        }, e -> {
            erro(e.getMessage());
            SwingUtilities.invokeLater(this::fechar); // depois de a janela entrar na área de trabalho
        });
    }

    /**
     * Acesso ao banco fora da thread do Swing, com a janela marcada como
     * ocupada; erros aparecem na mensagem padrão.
     */
    public <T> void noBanco(String mensagem, java.util.concurrent.Callable<T> trabalho, java.util.function.Consumer<T> aoConcluir) {
        FilaBanco.executar(view, mensagem, trabalho, aoConcluir, e -> erro(e.getMessage()));
    }

    private ManterProjetoPresenterState estadoInicial() {
        return projetoService.podeEditar(projeto, usuario)
                ? new EdicaoProjetoState(this) : new SomenteLeituraProjetoState(this);
    }

    private void configurarAcoes() {
        view.getBtnSalvar().addActionListener(e -> executar(() -> estado.salvar()));
        view.getBtnDescartar().addActionListener(e -> executar(() -> estado.descartar()));
        view.getBtnExcluir().addActionListener(e -> executar(() -> estado.excluir()));
        view.getBtnCompartilhar().addActionListener(e -> executar(() -> estado.compartilhar()));
        view.getBtnExportarPdf().addActionListener(e -> executar(() -> estado.exportar(FormatoExportacao.PDF)));
        view.getBtnExportarCsv().addActionListener(e -> executar(() -> estado.exportar(FormatoExportacao.CSV)));
        view.getBtnFechar().addActionListener(e -> executar(this::solicitarFechamento));
        view.getBtnAdicionarCusto().addActionListener(e -> executar(this::adicionarCusto));
        view.getBtnRemoverCusto().addActionListener(e -> executar(this::removerCusto));
    }

    private void configurarDeteccaoDeAlteracoes() {
        for (var plataforma : new javax.swing.AbstractButton[]{view.getChkWeb(), view.getChkIos(), view.getChkAndroid()}) {
            plataforma.addActionListener(e -> {
                atualizarColunasPlataformas();
                aoAlterar();
            });
        }
        ValidacaoVisual.aoEditar(view.getTxtImpostos(), this::aoAlterar);
        ValidacaoVisual.aoEditar(view.getTxtLucro(), this::aoAlterar);
        // o nome não muda a estimativa, mas é uma alteração a salvar
        ValidacaoVisual.aoEditar(view.getTxtNome(), () -> {
            if (editavel && !carregando) {
                setAlterado(true);
            }
        });
    }

    /** A tabela de funcionalidades mostra só as plataformas marcadas na tela. */
    private void atualizarColunasPlataformas() {
        view.mostrarColunasPlataformas(view.getChkWeb().isSelected(), view.getChkIos().isSelected(),
                view.getChkAndroid().isSelected());
        if (modeloNavegacao != null) {
            modeloNavegacao.recontar();
        }
    }

    /** Disparo do temporizador; depois de a janela fechar não faz nada. */
    private void aoDispararRecalculo() {
        if (!view.isClosed()) {
            executar(() -> estado.atualizarEstimativa());
        }
    }

    /** Toda alteração feita pelo usuário na edição passa por aqui. */
    private void aoAlterar() {
        if (editavel && !carregando) {
            setAlterado(true);
            recalculo.agendar();
        }
    }

    private void configurarFiltroFuncionalidades() {
        ValidacaoVisual.aoEditar(view.getTxtBuscaFuncionalidades(), this::aplicarFiltroFuncionalidades);
        view.getBtnSomenteSelecionadas().addActionListener(e -> aplicarFiltroFuncionalidades());
        view.getListaSecoes().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                aoEscolherEntrada();
            }
        });
        view.getTabelaItens().setDestaqueLinha(linha -> modeloItens != null && modeloItens.isSelecionado(linha));
    }

    /** Seção (e, em Funcionalidades, a categoria) escolhida na barra lateral. */
    private void aoEscolherEntrada() {
        int indice = view.getListaSecoes().getSelectedIndex();
        if (modeloNavegacao == null || indice < 0) {
            return;
        }
        view.mostrarSecao(modeloNavegacao.getSecao(indice));
        aplicarFiltroFuncionalidades();
    }

    /** Escolhe uma seção na barra lateral, o que também a mostra. */
    public void escolherSecao(SecaoProjeto secao) {
        if (modeloNavegacao != null) {
            view.getListaSecoes().setSelectedIndex(modeloNavegacao.indiceDe(secao, null));
        }
    }

    private void aplicarFiltroFuncionalidades() {
        if (modeloNavegacao != null && view.getTabelaItens().getRowSorter() instanceof TableRowSorter<?> sorter) {
            @SuppressWarnings("unchecked")
            TableRowSorter<TableModel> ordenador = (TableRowSorter<TableModel>) sorter;
            ordenador.setRowFilter(new FiltroFuncionalidades(view.getTxtBuscaFuncionalidades().getText(),
                    view.getBtnSomenteSelecionadas().isSelected(), categoriaEscolhida(),
                    modeloNavegacao.getCategoriasDeConfiguracao()));
        }
    }

    private String categoriaEscolhida() {
        return modeloNavegacao == null ? null : modeloNavegacao.getCategoria(view.getListaSecoes().getSelectedIndex());
    }

    private void atualizarContadorSelecionadas() {
        modeloNavegacao.recontar();
        int funcionalidades = 0;
        for (int linha = 0; linha < modeloItens.getRowCount(); linha++) {
            if (!modeloNavegacao.getCategoriasDeConfiguracao().contains(modeloItens.getValueAt(linha, 1))) {
                funcionalidades++;
            }
        }
        view.exibirContadorSelecionadas(modeloNavegacao.getSelecionadas(null) + " de " + funcionalidades + " selecionadas");
    }

    /**
     * Uma tabela por categoria de configuração (Tamanho do App, Nível de UI,
     * Gerente de Projeto), todas sobre o mesmo model da tabela de funcionalidades.
     */
    private void montarTabelasConfiguracao() {
        List<String> categorias = modeloNavegacao.getCategoriasDeConfiguracao();
        tabelasConfiguracao = view.prepararBlocosConfiguracao(categorias);
        for (int i = 0; i < categorias.size(); i++) {
            TabelaPadrao tabela = tabelasConfiguracao.get(i);
            tabela.setModel(modeloItens);
            TableRowSorter<TableModel> ordenador = ordenadorNaOrdemDaPlanilha();
            ordenador.setRowFilter(new FiltroFuncionalidades("", false, categorias.get(i)));
            tabela.setRowSorter(ordenador);
            tabela.setDestaqueLinha(linha -> modeloItens.isSelecionado(linha));
            view.ajustarTabelaConfiguracao(tabela, tipoDaCategoria(categorias.get(i)) == TipoSelecao.UNICA);
        }
    }

    private TipoSelecao tipoDaCategoria(String categoria) {
        for (int linha = 0; linha < modeloItens.getRowCount(); linha++) {
            if (categoria.equals(modeloItens.getValueAt(linha, 1))) {
                return modeloItens.getTipoSelecao(linha);
            }
        }
        return TipoSelecao.MULTIPLA;
    }

    private TableRowSorter<TableModel> ordenadorNaOrdemDaPlanilha() {
        TableRowSorter<TableModel> ordenador = new TableRowSorter<>(modeloItens);
        for (int coluna = 0; coluna < modeloItens.getColumnCount(); coluna++) {
            ordenador.setSortable(coluna, false); // mantém a ordem da planilha
        }
        return ordenador;
    }

    /** Executa uma ação mostrando a mensagem de erro, sem fechar a janela. */
    private void executar(Runnable acao) {
        try {
            acao.run();
        } catch (RuntimeException ex) {
            erro(ex.getMessage());
        }
    }

    // ---------- usado pelos estados e commands ----------

    public void setEstado(ManterProjetoPresenterState estado) {
        this.estado = estado;
        estado.entrar();
    }

    public ManterProjetoPresenterState getEstado() {
        return estado;
    }

    public void setProjeto(Projeto projeto) {
        this.projeto = projeto;
        recalculo.cancelar();
        setAlterado(false);
        ultimoPreco = null;
        view.setTituloCabecalho(projeto.getNome());
        modeloItens = new ItensProjetoTableModel(projeto, this::erro);
        modeloTaxas = new TaxasTableModel(projeto::getTaxas, this::erro);
        modeloCustos = new CustosTableModel(projeto, this::erro);
        modeloItens.setAoAlterar(this::aoAlterar);
        modeloTaxas.setAoAlterar(this::aoAlterar);
        modeloCustos.setAoAlterar(this::aoAlterar);
        modeloItens.addTableModelListener(e -> atualizarContadorSelecionadas());
        view.getTabelaItens().setRowSorter(null);
        // ao recarregar (Salvar, Descartar alterações) mantém a entrada escolhida; ao abrir, Funcionalidades
        int indiceAnterior = view.getListaSecoes().getSelectedIndex();
        SecaoProjeto secaoAnterior = modeloNavegacao == null || indiceAnterior < 0
                ? SecaoProjeto.FUNCIONALIDADES : modeloNavegacao.getSecao(indiceAnterior);
        String categoriaAnterior = categoriaEscolhida();
        modeloNavegacao = new NavegacaoProjetoModel(modeloItens);
        modeloNavegacao.setPlataformasMarcadas(() -> view.getChkWeb().isSelected() || view.getChkIos().isSelected()
                || view.getChkAndroid().isSelected());
        view.getTabelaItens().setModel(modeloItens);
        view.getTabelaTaxas().setModel(modeloTaxas);
        view.getTabelaCustos().setModel(modeloCustos);
        view.configurarTabelaFuncionalidades();
        view.getTabelaItens().setRowSorter(ordenadorNaOrdemDaPlanilha());
        montarTabelasConfiguracao();
        view.getListaSecoes().setModel(modeloNavegacao);
        view.getListaSecoes().setSelectedIndex(modeloNavegacao.indiceDe(secaoAnterior, categoriaAnterior));
        aplicarFiltroFuncionalidades();
        atualizarContadorSelecionadas();
        exibirCampos();
        setEditavel(editavel);
    }

    private void exibirCampos() {
        carregando = true;
        view.getTxtNome().setText(projeto.getNome());
        view.getChkWeb().setSelected(projeto.isPlataformaMarcada(Plataforma.WEB_BACKEND));
        view.getChkIos().setSelected(projeto.isPlataformaMarcada(Plataforma.IOS));
        view.getChkAndroid().setSelected(projeto.isPlataformaMarcada(Plataforma.ANDROID));
        view.getTxtImpostos().setText(FormatoValor.percentual(projeto.getPercentualImpostos()));
        view.getTxtLucro().setText(FormatoValor.percentual(projeto.getPercentualLucro()));
        carregando = false;
        atualizarColunasPlataformas();
    }

    /**
     * Copia para o projeto só o que afeta a estimativa, sem exigir nome, encerrar
     * a edição de células ou trocar de aba. Devolve false se algum percentual
     * não puder ser usado.
     */
    public boolean aplicarCamposDeEstimativa() {
        projeto.setPlataformaMarcada(Plataforma.WEB_BACKEND, view.getChkWeb().isSelected());
        projeto.setPlataformaMarcada(Plataforma.IOS, view.getChkIos().isSelected());
        projeto.setPlataformaMarcada(Plataforma.ANDROID, view.getChkAndroid().isSelected());
        BigDecimal impostos = lerPercentualDigitado(view.getTxtImpostos(), "impostos");
        BigDecimal lucro = lerPercentualDigitado(view.getTxtLucro(), "lucro");
        if (impostos == null || lucro == null) {
            return false;
        }
        projeto.setPercentualImpostos(impostos);
        projeto.setPercentualLucro(lucro);
        return true;
    }

    /** Percentual durante a digitação: campo vazio não é erro, só ainda não tem valor. */
    private BigDecimal lerPercentualDigitado(javax.swing.JTextField campo, String nome) {
        if (campo.getText().isBlank()) {
            return null;
        }
        try {
            return FormatoValor.percentual(campo.getText());
        } catch (IllegalArgumentException e) {
            view.mostrarErroPercentual(campo, mensagemPercentual(nome));
            return null;
        }
    }

    private static String mensagemPercentual(String nome) {
        return "Informe o percentual de " + nome + " como número, ex.: 42 ou 42,5.";
    }

    /**
     * Copia os campos simples da tela (nome, plataformas, impostos, lucro) para
     * o projeto. Campos inválidos ficam destacados e o método devolve false.
     */
    public boolean aplicarCamposNoProjeto() {
        if (!editavel) {
            return true;
        }
        pararEdicaoDasTabelas();
        view.limparErros();
        boolean ok = true;
        if (view.getTxtNome().getText().isBlank()) {
            view.mostrarErroCampo(view.getTxtNome(), "Informe o nome do projeto.");
            ok = false;
        }
        java.math.BigDecimal impostos = lerPercentual(view.getTxtImpostos(), "impostos");
        java.math.BigDecimal lucro = lerPercentual(view.getTxtLucro(), "lucro");
        if (!ok || impostos == null || lucro == null) {
            return false;
        }
        projeto.setNome(view.getTxtNome().getText());
        projeto.setPlataformaMarcada(Plataforma.WEB_BACKEND, view.getChkWeb().isSelected());
        projeto.setPlataformaMarcada(Plataforma.IOS, view.getChkIos().isSelected());
        projeto.setPlataformaMarcada(Plataforma.ANDROID, view.getChkAndroid().isSelected());
        projeto.setPercentualImpostos(impostos);
        projeto.setPercentualLucro(lucro);
        return true;
    }

    /** Percentual digitado como fração; null (com o campo destacado) se for inválido. */
    private java.math.BigDecimal lerPercentual(javax.swing.JTextField campo, String nome) {
        try {
            if (campo.getText().isBlank()) {
                throw new IllegalArgumentException("vazio");
            }
            return FormatoValor.percentual(campo.getText());
        } catch (IllegalArgumentException e) {
            escolherSecao(SecaoProjeto.CUSTOS);
            view.mostrarErroCampo(campo, mensagemPercentual(nome));
            return null;
        }
    }

    /** Nome e perfis da inclusão; devolve false (com o erro exibido) se faltar algo. */
    public boolean validarInclusao() {
        view.limparErros();
        boolean ok = true;
        if (view.getTxtNome().getText().isBlank()) {
            view.mostrarErroCampo(view.getTxtNome(), "Informe o nome do projeto.");
            ok = false;
        }
        if (view.getListaPerfis().getSelectedIndices().length == 0) {
            erro("Selecione ao menos um perfil de origem.");
            ok = false;
        }
        return ok;
    }

    private void pararEdicaoDasTabelas() {
        List<javax.swing.JTable> tabelas = new ArrayList<>(List.of(view.getTabelaItens(), view.getTabelaTaxas(), view.getTabelaCustos()));
        tabelas.addAll(tabelasConfiguracao);
        for (var tabela : tabelas) {
            if (tabela.isEditing()) {
                tabela.getCellEditor().stopCellEditing();
            }
        }
    }

    public void setEditavel(boolean editavel) {
        this.editavel = editavel;
        view.getTxtNome().setEditable(editavel);
        view.getChkWeb().setEnabled(editavel);
        view.getChkIos().setEnabled(editavel);
        view.getChkAndroid().setEnabled(editavel);
        view.getTxtImpostos().setEditable(editavel);
        view.getTxtLucro().setEditable(editavel);
        view.getBtnAdicionarCusto().setEnabled(editavel);
        view.getBtnRemoverCusto().setEnabled(editavel);
        if (modeloItens != null) {
            modeloItens.setEditavel(editavel);
            modeloTaxas.setEditavel(editavel);
            modeloCustos.setEditavel(editavel);
        }
    }

    /** Relê o projeto salvo e, depois de exibi-lo, executa {@code depois}. */
    public void recarregar(Runnable depois) {
        UUID id = projeto.getId();
        noBanco("Recarregando…", () -> projetoService.buscarParaVisualizacao(id, usuario), p -> {
            setProjeto(p);
            mostrarResultadoAtual();
            depois.run();
        });
    }

    public List<UUID> idsPerfisSelecionados() {
        List<UUID> ids = new ArrayList<>();
        for (int i : view.getListaPerfis().getSelectedIndices()) {
            ids.add(perfisDisponiveis.get(i).getId());
        }
        return ids;
    }

    private void adicionarCusto() {
        String descricao = PerguntarCommand.perguntar("Novo custo adicional", "Descrição do novo custo adicional:");
        if (descricao == null || descricao.isBlank()) {
            return;
        }
        String valor = PerguntarCommand.perguntar("Novo custo adicional", "Valor (R$) de \"" + descricao + "\":");
        if (valor == null) {
            return;
        }
        projeto.adicionarCustoAdicional(new CustoAdicional(descricao, FormatoValor.decimal(valor)));
        modeloCustos.recarregar();
        aoAlterar();
    }

    private void removerCusto() {
        int linha = view.getTabelaCustos().getSelectedRow();
        if (linha < 0) {
            throw new IllegalArgumentException("Selecione o custo a remover.");
        }
        projeto.removerCustoAdicional(modeloCustos.getLinha(linha).getId());
        modeloCustos.recarregar();
        aoAlterar();
    }

    /** Mostra o resultado do estado atual quando o projeto já foi calculado. */
    public void mostrarResultadoAtual() {
        if (!projeto.isCalculado()) {
            exibirEstimativaIndisponivel("A estimativa ainda não foi calculada."
                    + (projetoService.podeEditar(projeto, usuario)
                    ? "<br>Marque uma plataforma e as funcionalidades: a estimativa aparece na hora."
                    : "<br>O dono ainda não completou a estimativa."));
            return;
        }
        try {
            mostrarResultado(projetoService.resultado(projeto));
        } catch (IllegalArgumentException e) {
            exibirEstimativaIndisponivel(e.getMessage());
        }
    }

    public void exibirEstimativaIndisponivel(String mensagem) {
        ultimoPreco = null;
        view.exibirResultadoVazio(mensagem);
    }

    /** A estimativa na tela não reflete os campos, porque um percentual está vazio ou inválido. */
    public void marcarEstimativaDesatualizada() {
        view.exibirAvisoEstimativa(ultimoPreco == null
                ? "Informe impostos e lucro para ver a estimativa."
                : "Estimativa desatualizada: informe impostos e lucro para atualizá-la.");
    }

    /** "+ R$ 450,00 na última alteração"; null se não houver diferença em reais. */
    private String textoVariacaoPreco(BigDecimal precoAtual) {
        if (ultimoPreco == null) {
            return null;
        }
        BigDecimal diferenca = ResultadoEstimativa.arredondarReais(precoAtual)
                .subtract(ResultadoEstimativa.arredondarReais(ultimoPreco));
        if (diferenca.signum() == 0) {
            return null;
        }
        return (diferenca.signum() > 0 ? "+ " : "- ") + formatarReais(diferenca.abs()) + " na última alteração";
    }

    /** Preenche os cartões e as tabelas do resultado. */
    public void mostrarResultado(ResultadoEstimativa r) {
        String variacao = textoVariacaoPreco(r.getPrecoFinal());
        ultimoPreco = r.getPrecoFinal();
        String detalhePreco = variacao != null ? variacao
                : "impostos " + formatarPercentual(r.getPercentualImpostos()) + " · lucro " + formatarPercentual(r.getPercentualLucro());
        view.exibirResumo(r.getDiasTotalExibicao() + " dias", formatarMeses(r.getMeses()) + " meses",
                formatarReais(r.getCustoDesenvolvimento()), formatarDias(r.getDiasTotal()) + " dias de trabalho",
                formatarReais(r.getPrecoFinal()), detalhePreco, formatarReais(r.getMediaMensal()));

        DefaultTableModel plataformas = somenteLeitura("Plataforma", "Tamanho", "UI", "Funcionalidades", "Gerência", "Dias", "Custo");
        for (DetalhePlataforma d : r.getPorPlataforma().values()) {
            plataformas.addRow(new Object[]{d.plataforma().getDescricao(), d.diasTamanho(), d.diasUI(), d.diasFuncionalidades(),
                d.diasGerencia(), d.dias(), d.custo()});
        }
        plataformas.addRow(new Object[]{"Total", null, null, null, null, r.getDiasTotal(), r.getCustoDesenvolvimento()});
        view.getTabelaPlataformas().setModel(plataformas);

        DefaultTableModel categorias = somenteLeitura("Categoria", "Dias", "Custo");
        for (ResumoCategoria c : r.getPorCategoria().values()) {
            categorias.addRow(new Object[]{c.categoria(), c.dias(), c.custo()});
        }
        view.getTabelaCategorias().setModel(categorias);

        DefaultTableModel financeiro = somenteLeitura("Item", "Valor");
        financeiro.addRow(new Object[]{"Custo de desenvolvimento", r.getCustoDesenvolvimento()});
        for (CustoAdicional c : projeto.getCustosAdicionais()) {
            financeiro.addRow(new Object[]{"  " + c.getDescricao(), c.getValor()});
        }
        financeiro.addRow(new Object[]{"Custo total", r.getCustoTotal()});
        financeiro.addRow(new Object[]{"Impostos (" + formatarPercentual(r.getPercentualImpostos()) + ")", r.getImposto()});
        financeiro.addRow(new Object[]{"Valor com impostos", r.getValorComImposto()});
        financeiro.addRow(new Object[]{"Lucro (" + formatarPercentual(r.getPercentualLucro()) + ")", r.getLucro()});
        financeiro.addRow(new Object[]{"Preço final ao cliente", r.getPrecoFinal()});
        view.getTabelaFinanceiro().setModel(financeiro);
        view.getTabelaPlataformas().getColumnModel().getColumn(0).setPreferredWidth(140);
        view.getTabelaCategorias().getColumnModel().getColumn(0).setPreferredWidth(260);
        view.getTabelaFinanceiro().getColumnModel().getColumn(0).setPreferredWidth(240);
    }

    private static DefaultTableModel somenteLeitura(String... colunas) {
        return new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    public void erro(String texto) {
        ExibirMensagemCommand.erro(texto);
    }

    public void sucesso(String texto) {
        ExibirMensagemCommand.sucesso(texto);
    }

    /** Fechar, X e Esc: pergunta antes de descartar alterações não salvas. */
    public void solicitarFechamento() {
        if (editavel) {
            pararEdicaoDasTabelas();
        }
        if (alterado && !ConfirmarCommand.perguntar("Alterações não salvas",
                "Descartar as alterações do projeto \"" + projeto.getNome() + "\"?", "Descartar")) {
            return;
        }
        fechar();
    }

    public void fechar() {
        recalculo.cancelar();
        projetoService.removeObserver(this);
        view.dispose();
    }

    public void cancelarRecalculoPendente() {
        recalculo.cancelar();
    }

    /** Liga ou desliga a marca de alteração não salva, avisando o estado quando ela muda. */
    private void setAlterado(boolean valor) {
        if (alterado != valor) {
            alterado = valor;
            if (estado != null && projeto != null) {
                estado.atualizarAcoesDeGravacao(valor);
            }
        }
    }

    public boolean isAlterado() {
        return alterado;
    }

    public void marcarSemAlteracoes() {
        setAlterado(false);
    }

    /** Roda já o recálculo que estaria esperando o temporizador (testes). */
    void executarRecalculoPendente() {
        recalculo.executarAgora();
    }

    @Override
    public void update(EventoDominio evento) {
        if (projeto == null || !evento.sobre(EventoDominio.Entidade.PROJETO) || !projeto.getId().equals(evento.id())) {
            return;
        }
        SwingUtilities.invokeLater(() -> {
            if (evento.tipo() == EventoDominio.Tipo.EXCLUSAO) {
                if (view.isDisplayable()) {
                    fechar();
                }
                return;
            }
            // com alterações não salvas, quem decide é o usuário (Salvar ou Descartar)
            if (!alterado && !(estado instanceof InclusaoProjetoState) && view.isDisplayable()) {
                UUID id = projeto.getId();
                FilaBanco.executar(view, "Atualizando…", () -> projetoService.buscarParaVisualizacao(id, usuario), p -> {
                    setProjeto(p);
                    mostrarResultadoAtual();
                    setEstado(estadoInicial());
                }, e -> fechar());
            }
        });
    }

    public ProjetoView getView() {
        return view;
    }

    public Projeto getProjeto() {
        return projeto;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public ProjetoService getProjetoService() {
        return projetoService;
    }

    public ExportacaoService getExportacaoService() {
        return exportacaoService;
    }
}
