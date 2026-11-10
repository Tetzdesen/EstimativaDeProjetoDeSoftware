package com.br.estimativadeprojetodesoftware.presenter.perfil;

import com.br.estimativadeprojetodesoftware.presenter.helpers.FilaBanco;
import com.br.estimativadeprojetodesoftware.command.ConfirmarCommand;
import com.br.estimativadeprojetodesoftware.command.ExibirMensagemCommand;
import com.br.estimativadeprojetodesoftware.model.Perfil;
import com.br.estimativadeprojetodesoftware.model.Usuario;
import com.br.estimativadeprojetodesoftware.observer.EventoDominio;
import com.br.estimativadeprojetodesoftware.observer.Observer;
import com.br.estimativadeprojetodesoftware.presenter.helpers.Navegador;
import com.br.estimativadeprojetodesoftware.service.PerfilService;
import com.br.estimativadeprojetodesoftware.singleton.UsuarioLogadoSingleton;
import com.br.estimativadeprojetodesoftware.view.perfil.ListaPerfisView;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingUtilities;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.table.DefaultTableModel;

/** Lista de perfis (padrão e do usuário). Observer: atualiza quando um perfil muda. */
public class ListaPerfisPresenter implements Observer {

    private final ListaPerfisView view;
    private final PerfilService service;
    private final Usuario usuario;
    private final List<Perfil> linhas = new ArrayList<>();

    public ListaPerfisPresenter() {
        this.view = new ListaPerfisView();
        this.service = new PerfilService();
        this.usuario = UsuarioLogadoSingleton.getInstancia().getUsuario();
        view.getBtnNovo().addActionListener(e -> novo());
        view.getBtnAbrir().addActionListener(e -> abrir());
        view.getBtnExcluir().addActionListener(e -> excluir());
        view.getBtnFechar().addActionListener(e -> view.dispose());
        view.getTabela().aoAcionarLinha(this::abrir);
        view.getTabela().getSelectionModel().addListSelectionListener(e -> atualizarBotoes());
        service.addObserver(this);
        view.addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameClosed(InternalFrameEvent e) {
                service.removeObserver(ListaPerfisPresenter.this);
            }
        });
        carregar();
    }

    private void carregar() {
        FilaBanco.executar(view, "Carregando…", () -> service.listarVisiveis(usuario), this::exibir,
                e -> ExibirMensagemCommand.erro(e.getMessage()));
    }

    private void exibir(java.util.List<Perfil> perfis) {
        linhas.clear();
        linhas.addAll(perfis);
        DefaultTableModel modelo = new DefaultTableModel(new Object[]{"Perfil", "Tipo", "Funcionalidades"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        for (Perfil p : linhas) {
            modelo.addRow(new Object[]{p.getNome(), p.isPadrao() ? "Padrão (protegido contra exclusão)" : "Personalizado",
                p.getFuncionalidades().size()});
        }
        view.getTabela().setModel(modelo);
        if (!linhas.isEmpty()) {
            view.getTabela().setRowSelectionInterval(0, 0);
        }
        atualizarBotoes();
    }

    /** O perfil padrão (e o de outro usuário) não pode ser excluído: o botão nem fica ativo. */
    private void atualizarBotoes() {
        int linha = view.getTabela().getSelectedRow();
        Perfil p = linha < 0 ? null : linhas.get(view.getTabela().convertRowIndexToModel(linha));
        view.getBtnAbrir().setEnabled(p != null);
        view.getBtnNovo().setEnabled(p != null);
        view.getBtnExcluir().setEnabled(p != null && service.podeExcluir(p, usuario));
        view.getBtnExcluir().setToolTipText(p != null && p.isPadrao() ? "O perfil padrão não pode ser excluído" : null);
    }

    private Perfil selecionado() {
        int linha = view.getTabela().getSelectedRow();
        if (linha < 0) {
            throw new IllegalArgumentException("Selecione um perfil.");
        }
        return linhas.get(view.getTabela().convertRowIndexToModel(linha));
    }

    private void novo() {
        try {
            Navegador.abrirNovoPerfil(selecionado());
        } catch (RuntimeException ex) {
            ExibirMensagemCommand.erro(ex.getMessage());
        }
    }

    private void abrir() {
        try {
            Navegador.abrirPerfil(selecionado().getId());
        } catch (RuntimeException ex) {
            ExibirMensagemCommand.erro(ex.getMessage());
        }
    }

    private void excluir() {
        try {
            Perfil p = selecionado();
            if (ConfirmarCommand.perguntar("Excluir perfil", "Excluir o perfil \"" + p.getNome() + "\"?", "Excluir")) {
                FilaBanco.executar(view, "Excluindo…", () -> {
                    service.excluir(p.getId(), usuario);
                    return null;
                }, ok -> {
                }, e -> ExibirMensagemCommand.erro(e.getMessage()));
            }
        } catch (RuntimeException ex) {
            ExibirMensagemCommand.erro(ex.getMessage());
        }
    }

    @Override
    public void update(EventoDominio evento) {
        if (evento.sobre(EventoDominio.Entidade.PERFIL)) {
            SwingUtilities.invokeLater(() -> {
                if (view.isDisplayable()) {
                    carregar();
                }
            });
        }
    }

    public ListaPerfisView getView() {
        return view;
    }
}
