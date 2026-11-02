package com.br.estimativadeprojetodesoftware.presenter.perfil;

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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
        view.getTabela().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    abrir();
                }
            }
        });
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
        linhas.clear();
        linhas.addAll(service.listarVisiveis(usuario));
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
                service.excluir(p.getId(), usuario);
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
