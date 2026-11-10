package com.br.estimativadeprojetodesoftware.presenter.projeto;

import com.br.estimativadeprojetodesoftware.presenter.helpers.FilaBanco;
import com.br.estimativadeprojetodesoftware.command.ConfirmarCommand;
import com.br.estimativadeprojetodesoftware.command.ExibirMensagemCommand;
import com.br.estimativadeprojetodesoftware.model.Projeto;
import com.br.estimativadeprojetodesoftware.model.Usuario;
import com.br.estimativadeprojetodesoftware.observer.EventoDominio;
import com.br.estimativadeprojetodesoftware.observer.Observer;
import com.br.estimativadeprojetodesoftware.presenter.helpers.Navegador;
import com.br.estimativadeprojetodesoftware.model.ResultadoEstimativa;
import com.br.estimativadeprojetodesoftware.service.CalculoEstimativaService;
import com.br.estimativadeprojetodesoftware.service.ProjetoService;
import com.br.estimativadeprojetodesoftware.view.componentes.TabelaPadrao;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import com.br.estimativadeprojetodesoftware.singleton.UsuarioLogadoSingleton;
import com.br.estimativadeprojetodesoftware.view.projeto.ListaProjetosView;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingUtilities;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.table.DefaultTableModel;

/**
 * Lista de projetos próprios e compartilhados ("Compartilhado por NOME").
 * Observer: atualiza sozinha quando um projeto é criado, alterado, excluído ou
 * compartilhado em outra janela.
 */
public class ListaProjetosPresenter implements Observer {

    private final ListaProjetosView view;
    private final ProjetoService service;
    private final CalculoEstimativaService calculo = new CalculoEstimativaService();
    private final Usuario usuario;
    private final List<Projeto> linhas = new ArrayList<>();

    public ListaProjetosPresenter() {
        this.view = new ListaProjetosView();
        this.service = new ProjetoService();
        this.usuario = UsuarioLogadoSingleton.getInstancia().getUsuario();
        view.getBtnNovo().addActionListener(e -> Navegador.abrirNovoProjeto());
        view.getBtnAbrir().addActionListener(e -> abrirSelecionado());
        view.getBtnExcluir().addActionListener(e -> excluirSelecionado());
        view.getBtnFechar().addActionListener(e -> view.dispose());
        view.getTabela().aoAcionarLinha(this::abrirSelecionado);
        view.getTabela().getSelectionModel().addListSelectionListener(e -> atualizarBotoes());
        service.addObserver(this);
        view.addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameClosed(InternalFrameEvent e) {
                service.removeObserver(ListaProjetosPresenter.this);
            }
        });
        carregar();
    }

    private void carregar() {
        FilaBanco.executar(view, "Carregando…", () -> {
            List<Projeto> todos = new java.util.ArrayList<>(service.listarProprios(usuario));
            todos.addAll(service.listarCompartilhadosComigo(usuario));
            // prazo e preço de cada projeto calculado, para comparar sem abrir um por um
            Map<UUID, ResultadoEstimativa> resultados = new HashMap<>();
            for (Projeto p : todos) {
                if (p.isCalculado()) {
                    try {
                        resultados.put(p.getId(), calculo.calcular(p));
                    } catch (IllegalArgumentException semResultado) {
                        // projeto com dados incompletos: fica sem prazo e preço na lista
                    }
                }
            }
            return new Carga(todos, resultados);
        }, this::exibir, e -> ExibirMensagemCommand.erro(e.getMessage()));
    }

    private record Carga(List<Projeto> projetos, Map<UUID, ResultadoEstimativa> resultados) {
    }

    private void exibir(Carga carga) {
        linhas.clear();
        linhas.addAll(carga.projetos());
        DefaultTableModel modelo = new DefaultTableModel(new Object[]{"Projeto", "Origem", "Prazo (dias)", "Preço final"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        for (Projeto p : linhas) {
            String origem = p.isDono(usuario.getId()) ? "Meu projeto" : "Compartilhado por " + p.getDonoNome();
            ResultadoEstimativa r = carga.resultados().get(p.getId());
            modelo.addRow(new Object[]{p.getNome(), origem, r == null ? "—" : r.getDiasTotal(),
                r == null ? "Não calculado" : r.getPrecoFinal()});
        }
        view.getTabela().setModel(modelo);
        view.getTabela().setFormatoColuna(2, TabelaPadrao.Formato.NUMERO);
        view.getTabela().setFormatoColuna(3, TabelaPadrao.Formato.REAIS);
        atualizarBotoes();
    }

    private Projeto selecionado() {
        int linha = view.getTabela().getSelectedRow();
        return linha < 0 ? null : linhas.get(view.getTabela().convertRowIndexToModel(linha));
    }

    private void atualizarBotoes() {
        Projeto p = selecionado();
        view.getBtnAbrir().setEnabled(p != null);
        view.getBtnExcluir().setEnabled(p != null && p.isDono(usuario.getId()));
    }

    private void abrirSelecionado() {
        Projeto p = selecionado();
        if (p != null) {
            try {
                Navegador.abrirProjeto(p.getId());
            } catch (RuntimeException ex) {
                ExibirMensagemCommand.erro(ex.getMessage());
            }
        }
    }

    private void excluirSelecionado() {
        Projeto p = selecionado();
        if (p == null) {
            return;
        }
        if (!ConfirmarCommand.perguntar("Excluir projeto", "Excluir o projeto \"" + p.getNome() + "\"?\nEsta ação não pode ser desfeita.", "Excluir")) {
            return;
        }
        FilaBanco.executar(view, "Excluindo…", () -> {
            service.excluir(p.getId(), usuario);
            return null;
        }, ok -> {
        }, e -> ExibirMensagemCommand.erro(e.getMessage()));
    }

    @Override
    public void update(EventoDominio evento) {
        if (evento.sobre(EventoDominio.Entidade.PROJETO) || evento.sobre(EventoDominio.Entidade.COMPARTILHAMENTO)) {
            SwingUtilities.invokeLater(() -> {
                if (view.isDisplayable()) {
                    carregar();
                }
            });
        }
    }

    public ListaProjetosView getView() {
        return view;
    }
}
