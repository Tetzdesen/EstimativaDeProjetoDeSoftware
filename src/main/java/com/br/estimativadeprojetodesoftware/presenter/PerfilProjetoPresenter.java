package com.br.estimativadeprojetodesoftware.presenter;

import com.br.estimativadeprojetodesoftware.command.AbrirDashboardProjetoCommand;
import com.br.estimativadeprojetodesoftware.command.AbrirDetalhesProjetoProjetoCommand;
import com.br.estimativadeprojetodesoftware.command.AbrirInternalFrameGenericoProjetoCommand;
import com.br.estimativadeprojetodesoftware.command.CriarNovoPerfilProjetoCommand;
import com.br.estimativadeprojetodesoftware.command.CriarProjetoProjetoCommand;
import com.br.estimativadeprojetodesoftware.command.ExcluirProjetoProjetoCommand;
import com.br.estimativadeprojetodesoftware.command.MostrarMensagemProjetoCommand;
import com.br.estimativadeprojetodesoftware.command.ProjetoCommand;
import com.br.estimativadeprojetodesoftware.command.VisualizarPerfisProjetoCommand;
import com.br.estimativadeprojetodesoftware.model.Perfil;
import com.br.estimativadeprojetodesoftware.model.Projeto;
import com.br.estimativadeprojetodesoftware.repository.PerfilRepositoryMock;
import com.br.estimativadeprojetodesoftware.view.ManterPerfilView;
import com.br.estimativadeprojetodesoftware.view.PerfilProjetoView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    }

    private Map<String, ProjetoCommand> inicializarComandos() {
        Map<String, ProjetoCommand> comandos = new HashMap<>();
        comandos.put("Novo Perfil", new CriarNovoPerfilProjetoCommand(view.getDesktop(), repository));
        comandos.put("Visualizar", new AbrirInternalFrameGenericoProjetoCommand(view.getDesktop(), "Usuário"));
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
