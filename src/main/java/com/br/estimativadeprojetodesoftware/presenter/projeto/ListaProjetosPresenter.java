package com.br.estimativadeprojetodesoftware.presenter.projeto;

import com.br.estimativadeprojetodesoftware.command.ConfirmarCommand;
import com.br.estimativadeprojetodesoftware.command.ExibirMensagemCommand;
import com.br.estimativadeprojetodesoftware.model.Projeto;
import com.br.estimativadeprojetodesoftware.model.Usuario;
import com.br.estimativadeprojetodesoftware.observer.EventoDominio;
import com.br.estimativadeprojetodesoftware.observer.Observer;
import com.br.estimativadeprojetodesoftware.presenter.helpers.Navegador;
import com.br.estimativadeprojetodesoftware.service.ProjetoService;
import com.br.estimativadeprojetodesoftware.singleton.UsuarioLogadoSingleton;
import com.br.estimativadeprojetodesoftware.view.projeto.ListaProjetosView;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
        view.getTabela().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    abrirSelecionado();
                }
            }
        });
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
        linhas.clear();
        linhas.addAll(service.listarProprios(usuario));
        linhas.addAll(service.listarCompartilhadosComigo(usuario));
        DefaultTableModel modelo = new DefaultTableModel(new Object[]{"Projeto", "Origem", "Estimativa"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        for (Projeto p : linhas) {
            String origem = p.isDono(usuario.getId()) ? "Meu projeto" : "Compartilhado por " + p.getDonoNome();
            modelo.addRow(new Object[]{p.getNome(), origem, p.isCalculado() ? "Calculada" : "Não calculada"});
        }
        view.getTabela().setModel(modelo);
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
        try {
            service.excluir(p.getId(), usuario);
        } catch (RuntimeException ex) {
            ExibirMensagemCommand.erro(ex.getMessage());
        }
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
