package com.br.estimativadeprojetodesoftware.presenter.projeto;

import static com.br.estimativadeprojetodesoftware.model.ResultadoEstimativa.formatarDias;
import static com.br.estimativadeprojetodesoftware.model.ResultadoEstimativa.formatarMeses;
import static com.br.estimativadeprojetodesoftware.model.ResultadoEstimativa.formatarPercentual;
import static com.br.estimativadeprojetodesoftware.model.ResultadoEstimativa.formatarReais;

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
import com.br.estimativadeprojetodesoftware.presenter.tabela.CustosTableModel;
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
import com.br.estimativadeprojetodesoftware.state.projeto.VisualizacaoProjetoState;
import com.br.estimativadeprojetodesoftware.templatemethod.FormatoExportacao;
import com.br.estimativadeprojetodesoftware.view.projeto.ProjetoView;
import java.math.BigDecimal;
import javax.swing.table.DefaultTableModel;
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
 * compartilhamento).
 */
public class ManterProjetoPresenter implements Observer {

    private final ProjetoView view;
    private final ProjetoService projetoService;
    private final PerfilService perfilService;
    private final ExportacaoService exportacaoService;
    private final Usuario usuario;
    private Projeto projeto;
    private List<Perfil> perfisDisponiveis = List.of();
    private ManterProjetoPresenterState estado;
    private ItensProjetoTableModel modeloItens;
    private TaxasTableModel modeloTaxas;
    private CustosTableModel modeloCustos;
    private boolean editavel;

    private ManterProjetoPresenter() {
        this.view = new ProjetoView();
        this.projetoService = new ProjetoService();
        this.perfilService = new PerfilService();
        this.exportacaoService = new ExportacaoService();
        this.usuario = UsuarioLogadoSingleton.getInstancia().getUsuario();
        configurarAcoes();
        com.br.estimativadeprojetodesoftware.presenter.helpers.ValidacaoVisual.limparAoEditar(view.getTxtNome(), view::limparErroCampo);
        com.br.estimativadeprojetodesoftware.presenter.helpers.ValidacaoVisual.limparAoEditar(view.getTxtImpostos(), view::limparErroCampo);
        com.br.estimativadeprojetodesoftware.presenter.helpers.ValidacaoVisual.limparAoEditar(view.getTxtLucro(), view::limparErroCampo);
        projetoService.addObserver(this);
        view.addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameClosed(InternalFrameEvent e) {
                projetoService.removeObserver(ManterProjetoPresenter.this);
            }
        });
    }

    public static ManterProjetoPresenter novo() {
        ManterProjetoPresenter p = new ManterProjetoPresenter();
        p.view.setTituloCabecalho("Novo projeto");
        p.perfisDisponiveis = p.perfilService.listarVisiveis(p.usuario);
        p.view.getListaPerfis().setListData(p.perfisDisponiveis.stream().map(Perfil::toString).toArray(String[]::new));
        if (!p.perfisDisponiveis.isEmpty()) {
            p.view.getListaPerfis().setSelectedIndex(0);
        }
        p.setEditavel(false);
        p.view.getTxtNome().setEditable(true);
        p.setEstado(new InclusaoProjetoState(p));
        return p;
    }

    public static ManterProjetoPresenter paraProjeto(UUID id) {
        ManterProjetoPresenter p = new ManterProjetoPresenter();
        p.setProjeto(p.projetoService.buscarParaVisualizacao(id, p.usuario));
        p.setEstado(p.estadoInicial());
        p.mostrarResultadoAtual();
        return p;
    }

    private ManterProjetoPresenterState estadoInicial() {
        return projetoService.podeEditar(projeto, usuario)
                ? new VisualizacaoProjetoState(this) : new SomenteLeituraProjetoState(this);
    }

    private void configurarAcoes() {
        view.getBtnEditar().addActionListener(e -> executar(() -> estado.editar()));
        view.getBtnSalvar().addActionListener(e -> executar(() -> estado.salvar()));
        view.getBtnCancelar().addActionListener(e -> executar(() -> estado.cancelar()));
        view.getBtnExcluir().addActionListener(e -> executar(() -> estado.excluir()));
        view.getBtnCalcular().addActionListener(e -> executar(() -> estado.calcular()));
        view.getBtnCompartilhar().addActionListener(e -> executar(() -> estado.compartilhar()));
        view.getBtnExportarPdf().addActionListener(e -> executar(() -> estado.exportar(FormatoExportacao.PDF)));
        view.getBtnExportarCsv().addActionListener(e -> executar(() -> estado.exportar(FormatoExportacao.CSV)));
        view.getBtnFechar().addActionListener(e -> fechar());
        view.getBtnAdicionarCusto().addActionListener(e -> executar(this::adicionarCusto));
        view.getBtnRemoverCusto().addActionListener(e -> executar(this::removerCusto));
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
        view.setTituloCabecalho(projeto.getNome());
        modeloItens = new ItensProjetoTableModel(projeto, this::erro);
        modeloTaxas = new TaxasTableModel(projeto::getTaxas, this::erro);
        modeloCustos = new CustosTableModel(projeto, this::erro);
        view.getTabelaItens().setModel(modeloItens);
        view.getTabelaTaxas().setModel(modeloTaxas);
        view.getTabelaCustos().setModel(modeloCustos);
        if (view.getTabelaItens().getColumnModel().getColumnCount() > 2) {
            view.getTabelaItens().getColumnModel().getColumn(0).setMaxWidth(90);
            view.getTabelaItens().getColumnModel().getColumn(1).setPreferredWidth(230);
            view.getTabelaItens().getColumnModel().getColumn(2).setPreferredWidth(300);
            view.getTabelaItens().getColumnModel().getColumn(3).setPreferredWidth(110);
        }
        exibirCampos();
        setEditavel(editavel);
    }

    private void exibirCampos() {
        view.getTxtNome().setText(projeto.getNome());
        view.getChkWeb().setSelected(projeto.isPlataformaMarcada(Plataforma.WEB_BACKEND));
        view.getChkIos().setSelected(projeto.isPlataformaMarcada(Plataforma.IOS));
        view.getChkAndroid().setSelected(projeto.isPlataformaMarcada(Plataforma.ANDROID));
        view.getTxtImpostos().setText(FormatoValor.percentual(projeto.getPercentualImpostos()));
        view.getTxtLucro().setText(FormatoValor.percentual(projeto.getPercentualLucro()));
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
            view.mostrarErroCampo(campo, "Informe o percentual de " + nome + " como número, ex.: 42 ou 42,5.");
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
        for (var tabela : new javax.swing.JTable[]{view.getTabelaItens(), view.getTabelaTaxas(), view.getTabelaCustos()}) {
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

    public void recarregar() {
        setProjeto(projetoService.buscarParaVisualizacao(projeto.getId(), usuario));
        mostrarResultadoAtual();
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
    }

    private void removerCusto() {
        int linha = view.getTabelaCustos().getSelectedRow();
        if (linha < 0) {
            throw new IllegalArgumentException("Selecione o custo a remover.");
        }
        projeto.removerCustoAdicional(modeloCustos.getLinha(linha).getId());
        modeloCustos.recarregar();
    }

    /** Mostra o resultado do estado atual quando o projeto já foi calculado. */
    public void mostrarResultadoAtual() {
        if (!projeto.isCalculado()) {
            view.exibirResultadoVazio("A estimativa ainda não foi calculada."
                    + (projetoService.podeEditar(projeto, usuario) ? "<br>Clique em Editar, marque as funcionalidades e clique em Calcular." : ""));
            return;
        }
        try {
            mostrarResultado(projetoService.resultado(projeto));
        } catch (IllegalArgumentException e) {
            view.exibirResultadoVazio(e.getMessage());
        }
    }

    /** Preenche os cartões e as tabelas do resultado. */
    public void mostrarResultado(ResultadoEstimativa r) {
        view.exibirCartoesResultado(r.getDiasTotalExibicao() + " dias", formatarMeses(r.getMeses()) + " meses",
                formatarReais(r.getCustoDesenvolvimento()), formatarDias(r.getDiasTotal()) + " dias de trabalho",
                formatarReais(r.getPrecoFinal()), "impostos " + formatarPercentual(r.getPercentualImpostos())
                + " · lucro " + formatarPercentual(r.getPercentualLucro()),
                formatarReais(r.getMediaMensal()));

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

    public void fechar() {
        projetoService.removeObserver(this);
        view.dispose();
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
            if (!(estado instanceof EdicaoProjetoState) && view.isDisplayable()) {
                try {
                    recarregar();
                    setEstado(estadoInicial());
                } catch (RuntimeException e) {
                    fechar();
                }
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
