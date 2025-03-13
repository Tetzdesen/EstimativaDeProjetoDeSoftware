package com.br.estimativadeprojetodesoftware.presenter.perfil;

import com.br.estimativadeprojetodesoftware.command.MostrarMensagemProjetoCommand;
import com.br.estimativadeprojetodesoftware.command.perfil.AbrirManterPerfilProjetoCommand;
import com.br.estimativadeprojetodesoftware.model.Perfil;
import com.br.estimativadeprojetodesoftware.presenter.Observer;
import com.br.estimativadeprojetodesoftware.presenter.window_command.SetLookAndFeelCommand;
import com.br.estimativadeprojetodesoftware.presenter.window_command.WindowCommand;
import com.br.estimativadeprojetodesoftware.service.PerfilRepositoryService;
import com.br.estimativadeprojetodesoftware.singleton.UsuarioLogadoSingleton;
import com.br.estimativadeprojetodesoftware.view.perfil.PerfilProjetoView;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.util.*;

public class PerfilProjetoPresenter implements Observer {

    private final PerfilProjetoView view;
    private final PerfilRepositoryService repository;

    private final Set<UUID> perfisCarregados = new HashSet<>();

    public PerfilProjetoPresenter(PerfilProjetoView view) {
        this.view = view;
        this.repository = new PerfilRepositoryService();

        this.repository.addObserver(this);
        carregarDetalhesPerfil();

        configuraView();
        inicializarEExecutarWindowCommands();
        configuraActionsListerns();

        view.setVisible(true);
    }

    private void inicializarEExecutarWindowCommands() {
        Arrays.asList(
                new SetLookAndFeelCommand()
        ).forEach(WindowCommand::execute);
    }

    private void configuraActionsListerns() {
        view.getBtnNovoPerfil().addActionListener(e -> {
            try {
                new AbrirManterPerfilProjetoCommand(view.getDesktop(), null).execute();
            } catch (Exception ex) {
                new MostrarMensagemProjetoCommand(ex.getMessage()).execute();
            }
        });

        view.getBtnVisualizar().addActionListener(e -> {
            try {
                int linha = view.getTablePerfis().getSelectedRow();
                DefaultTableModel model = (DefaultTableModel) view.getModeloTabela();
                UUID id = (UUID) model.getValueAt(linha, 0);

                Perfil perfil = repository.buscarPorId(id).orElseThrow(() -> new RuntimeException("Perfil não encontrado"));
                new AbrirManterPerfilProjetoCommand(view.getDesktop(), perfil).execute();
            } catch (Exception ex) {
                new MostrarMensagemProjetoCommand(ex.getMessage()).execute();
            }
        });
    }

    private void carregarDetalhesPerfil() {
        DefaultTableModel modelo = (DefaultTableModel) view.getModeloTabela();
        modelo.setRowCount(0);

        List<Perfil> perfis = repository.buscarTodosPerfisPorIdUsuario(
                UsuarioLogadoSingleton.getInstancia().getUsuario().getId()
        );

        System.out.println(perfis);
        for (Perfil perfil : perfis) {
            if (perfisCarregados.add(perfil.getId())) {
                carregarDetalhes(perfil);
            }
        }
    }

    private void carregarDetalhes(Perfil perfil) {
        Object[] dadosTabela = new Object[]{
            perfil.getId(),
            perfil.getNome()
        };

        view.getModeloTabela().addRow(dadosTabela);
        view.atualizarTabela(dadosTabela);
    }

    private void removeColunaId() {
        TableColumnModel columnModel = view.getTablePerfis().getColumnModel();
        TableColumn colunaId = columnModel.getColumn(0);
        columnModel.removeColumn(colunaId);
    }

    private void configuraView() {
        setStatusBotaoVisualizar(false);

        removeColunaId();
        this.view.getTablePerfis().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent evt) {
                setStatusBotaoVisualizar(true);
            }
        });
    }

    @Override
    public void update() {
        carregarDetalhesPerfil();
    }

    private void setStatusBotaoVisualizar(boolean status) {
        this.view.getBtnVisualizar().setEnabled(status);
    }
}
