package com.br.estimativadeprojetodesoftware.presenter.perfil;

import com.br.estimativadeprojetodesoftware.command.AbrirInternalFrameGenericoProjetoCommand;
import com.br.estimativadeprojetodesoftware.command.MostrarMensagemProjetoCommand;
import com.br.estimativadeprojetodesoftware.command.ProjetoCommand;
import com.br.estimativadeprojetodesoftware.command.perfil.AbrirDetalhesPerfilProjetoCommand;
import com.br.estimativadeprojetodesoftware.command.perfil.CriarNovoPerfilProjetoCommand;
import com.br.estimativadeprojetodesoftware.model.Perfil;
import com.br.estimativadeprojetodesoftware.presenter.Observer;
import com.br.estimativadeprojetodesoftware.presenter.window_command.ConfigurarMenuJanelaCommand;
import com.br.estimativadeprojetodesoftware.presenter.window_command.ConfigurarViewCommand;
import com.br.estimativadeprojetodesoftware.presenter.window_command.SetLookAndFeelCommand;
import com.br.estimativadeprojetodesoftware.presenter.window_command.WindowCommand;
import com.br.estimativadeprojetodesoftware.repository.PerfilRepositoryMock;
import com.br.estimativadeprojetodesoftware.view.perfil.PerfilProjetoView;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JDesktopPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class PerfilProjetoPresenter implements Observer {

    private final PerfilProjetoView view;
    private final PerfilRepositoryMock repository;
    private final Map<String, ProjetoCommand> comandos;

    public PerfilProjetoPresenter(PerfilProjetoView view, PerfilRepositoryMock repository) {
        this.view = view;
        this.repository = repository;

        //this.repository.addObserver(this);
        carregarDetalhesPerfil();
        configuraView();

        this.comandos = inicializarComandos();
        inicializarEExecutarWindowCommands();
        configuraActionsListerns();
        view.setVisible(true);
    }

    private void inicializarEExecutarWindowCommands() {
        Arrays.asList(
                //new ConfigurarViewCommand(this),
                //new ConfigurarMenuJanelaCommand(this),
                new SetLookAndFeelCommand()
        ).forEach(WindowCommand::execute);
    }

    private void configuraActionsListerns() {
        view.getBtnNovoPerfil().addActionListener(e -> {
            try {
                new CriarNovoPerfilProjetoCommand(view.getDesktop(), repository).execute();
            } catch (Exception ex) {
                new MostrarMensagemProjetoCommand(ex.getMessage()).execute();
            }
        });
        view.getBtnVisualizar().addActionListener(e -> {
            try {
                new AbrirDetalhesPerfilProjetoCommand(view.getDesktop(), repository).execute();
            } catch (Exception ex) {
                new MostrarMensagemProjetoCommand(ex.getMessage()).execute();
            }
        });
    }

    private Map<String, ProjetoCommand> inicializarComandos() {
        Map<String, ProjetoCommand> comandos = new HashMap<>();
        comandos.put("Novo Perfil", new CriarNovoPerfilProjetoCommand(view.getDesktop(), repository));
        comandos.put("Visualizar", new AbrirDetalhesPerfilProjetoCommand(view.getDesktop(), repository));
        return comandos;
    }

    private void carregarDetalhesPerfil() {
        List<Perfil> perfis = repository.getPerfis();
        for(Perfil perfil : perfis) {
            carregarDetalhes(perfil);
        }
    }

    private void carregarDetalhes(Perfil perfil) {
        Object[] dadosTabela = new Object[3];
        dadosTabela[0] = perfil.getNome();
        dadosTabela[1] = 10;
        dadosTabela[2] = 50;

        view.atualizarTabela(dadosTabela);
    }

    private void configuraView() {
        setStatusBotaoVisualizar(false);
        this.view.getTablePerfis().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent evt) {
                setStatusBotaoVisualizar(true);
            }
        });
/*
        this.view.getBtnNovoPerfil().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
            }
        });

        this.view.getBtnVisualizar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ManterPerfilPresenter();
            }
        });
         */
    }

    @Override
    public void update() {
        carregarDetalhesPerfil();
    }

    private void setStatusBotaoVisualizar(boolean status) {
        this.view.getBtnVisualizar().setEnabled(status);
    }
}
