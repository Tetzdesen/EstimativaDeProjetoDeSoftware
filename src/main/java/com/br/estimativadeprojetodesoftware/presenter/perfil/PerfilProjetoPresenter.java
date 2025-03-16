package com.br.estimativadeprojetodesoftware.presenter.perfil;

import com.br.estimativadeprojetodesoftware.command.projeto.MostrarMensagemProjetoCommand;
import com.br.estimativadeprojetodesoftware.command.perfil.AbrirManterPerfilProjetoCommand;
import com.br.estimativadeprojetodesoftware.model.Campo;
import com.br.estimativadeprojetodesoftware.model.PerfilProjeto;
import com.br.estimativadeprojetodesoftware.presenter.Observer;
import com.br.estimativadeprojetodesoftware.presenter.window_command.SetLookAndFeelCommand;
import com.br.estimativadeprojetodesoftware.presenter.window_command.WindowCommand;
import com.br.estimativadeprojetodesoftware.service.CampoRepositoryService;
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
                new SetLookAndFeelCommand()).forEach(WindowCommand::execute);
    }

    private void configuraActionsListerns() {
        view.getBtnNovoPerfil().addActionListener(e -> {
            try {
                new AbrirManterPerfilProjetoCommand(view.getDesktop(), null, repository).execute();
            } catch (Exception ex) {
                new MostrarMensagemProjetoCommand(ex.getMessage()).execute();
            }
        });

        view.getBtnVisualizar().addActionListener(e -> {
            try {
                processarPerfilSelecionado();
            } catch (Exception ex) {
                new MostrarMensagemProjetoCommand(ex.getMessage()).execute();
            }
        });

        view.getBtnDuplicar().addActionListener(e -> {
            try {
                processarPerfilDuplicar();
            } catch (Exception ex) {
                new MostrarMensagemProjetoCommand(ex.getMessage()).execute();
            }
        });
    }

    private void processarPerfilSelecionado() {
        PerfilProjeto perfil = buscarPerfilSelecionado();
        new AbrirManterPerfilProjetoCommand(view.getDesktop(), perfil, repository).execute();
    }

    private void processarPerfilDuplicar() {
        PerfilProjeto perfil = buscarPerfilSelecionado();
        repository.salvar(perfil.clone());
    }

    private PerfilProjeto buscarPerfilSelecionado() {
        int linha = view.getTablePerfis().getSelectedRow();
        DefaultTableModel model = (DefaultTableModel) view.getModeloTabela();
        UUID id = (UUID) model.getValueAt(linha, 0);

        return repository.buscarPorId(id).orElseThrow(() -> new RuntimeException("Perfil não encontrado"));
    }

    private List<PerfilProjeto> carregarCamposPerfil(List<PerfilProjeto> perfis) {
        List<PerfilProjeto> perfisNovos = new ArrayList<>();

        for (PerfilProjeto perfil : perfis) {

            // buscar nome do campo pelo id do Perfil
            List<Campo> camposTamanhoApp = new CampoRepositoryService().buscarPorIdPerfilTipo(perfil.getId(),
                    "tamanho");

            for (Campo campo : camposTamanhoApp) {
                Double dias = new CampoRepositoryService().buscarDiasPorPerfilCampo(perfil.getId(), campo.getId());
                perfil.adicionarTamanhoApp(campo.getNome(), dias.intValue());

            }

            List<Campo> camposNivelUI = new CampoRepositoryService().buscarPorIdPerfilTipo(perfil.getId(), "nivel");

            for (Campo campo : camposNivelUI) {
                Double dias = new CampoRepositoryService().buscarDiasPorPerfilCampo(perfil.getId(), campo.getId());
                perfil.adicionarNivelUI(campo.getNome(), dias.doubleValue());
            }

            List<Campo> camposFuncionalidades = new CampoRepositoryService().buscarPorIdPerfilTipo(perfil.getId(),
                    "funcionalidade");

            for (Campo campo : camposFuncionalidades) {
                Double dias = new CampoRepositoryService().buscarDiasPorPerfilCampo(perfil.getId(), campo.getId());
                perfil.adicionarFuncionalidade(campo.getNome(), dias.intValue());
            }

            List<Campo> taxasDiarias = new CampoRepositoryService().buscarPorIdPerfilTipo(perfil.getId(),
                    "taxa diária");

            for (Campo campo : taxasDiarias) {
                Double dias = new CampoRepositoryService().buscarDiasPorPerfilCampo(perfil.getId(), campo.getId());
                perfil.adicionarTaxaDiaria(campo.getNome(), dias.doubleValue());
            }

            perfisNovos.add(perfil);
        }

        return perfisNovos;

    }

    private void carregarDetalhesPerfil() {
        DefaultTableModel modelo = (DefaultTableModel) view.getModeloTabela();
        modelo.setRowCount(0);

        List<PerfilProjeto> perfis = repository.buscarTodosPerfisPorIdUsuario(
                UsuarioLogadoSingleton.getInstancia().getUsuario().getId());

        perfis = carregarCamposPerfil(perfis);

        for (PerfilProjeto perfil : perfis) {
            carregarDetalhes(perfil);
        }
    }

    private void carregarDetalhes(PerfilProjeto perfil) {
        Object[] dadosTabela = new Object[] {
                perfil.getId(),
                perfil.getNome()
        };

        view.atualizarTabela(dadosTabela);
    }

    private void removeColunaId() {
        TableColumnModel columnModel = view.getTablePerfis().getColumnModel();
        TableColumn colunaId = columnModel.getColumn(0);
        columnModel.removeColumn(colunaId);
    }

    private void configuraView() {
        setStatusBotaoVisualizar(false);
        setStatusBotaoDuplicar(false);

        removeColunaId();
        this.view.getTablePerfis().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent evt) {
                setStatusBotaoVisualizar(true);
                setStatusBotaoDuplicar(true);
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

    private void setStatusBotaoDuplicar(boolean status) {
        this.view.getBtnVisualizar().setEnabled(status);
    }
}
