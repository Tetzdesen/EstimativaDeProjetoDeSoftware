package com.br.estimativadeprojetodesoftware.presenter.perfil;

import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.br.estimativadeprojetodesoftware.command.MostrarMensagemProjetoCommand;
import com.br.estimativadeprojetodesoftware.model.Perfil;
import com.br.estimativadeprojetodesoftware.repository.PerfilRepositoryMock;
import com.br.estimativadeprojetodesoftware.state.perfil.InclusaoPerfilState;
import com.br.estimativadeprojetodesoftware.state.perfil.ManterPerfilPresenterState;
import com.br.estimativadeprojetodesoftware.state.perfil.VisualizacaoPerfilState;
import com.br.estimativadeprojetodesoftware.view.perfil.ManterPerfilView;

public class ManterPerfilPresenter {
    private ManterPerfilView view;
    private PerfilRepositoryMock repository;
    private ManterPerfilPresenterState estado;
    private Perfil perfil;

    public ManterPerfilPresenter(ManterPerfilView view, PerfilRepositoryMock repository, Perfil perfil) {
        this.view = view;
        this.repository = repository;
        this.perfil = perfil;
        configuraView();
        configuraActionsListerns();

        setAllBtnVisibleFalse();
        //carregarCampos();

        if (perfil == null) {
            this.estado = new InclusaoPerfilState(this);
        } else {
            this.estado = new VisualizacaoPerfilState(this);
        }
    }

    private void configuraView() {
        setStatusBotaoRemover(false);
    }

    private void configuraActionsListerns() {
        view.getBtnAdicionarCampo().addActionListener(e -> {
            try {
                adicionarNovoCampo();
            } catch (Exception ex) {
                new MostrarMensagemProjetoCommand(ex.getMessage()).execute();
            }
        });

        view.getBtnRemoverCampo().addActionListener(e -> {
            try {
                removerCampo();
            } catch (Exception ex) {
                new MostrarMensagemProjetoCommand(ex.getMessage()).execute();
            }
        });

        this.view.getTabelaDetalhes().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent evt) {
                setStatusBotaoRemover(true);
            }
        });
    }

    public void adicionarNovoCampo() {
        view.getModeloTabela().addRow(new Object[]{"", "", ""});

        int novaLinha = view.getModeloTabela().getRowCount() - 1;
        
        view.getTabelaDetalhes().setRowSelectionInterval(novaLinha, novaLinha);
            
        view.getTabelaDetalhes().setColumnSelectionInterval(0, 0);
            
        view.getTabelaDetalhes().editCellAt(novaLinha, 0);
            
        view.getTabelaDetalhes().requestFocusInWindow();

        view.getTabelaDetalhes().getCellRect(novaLinha, 0, true);

        view.getTabelaDetalhes().scrollRectToVisible(view.getTabelaDetalhes().getCellRect(novaLinha, 0, true));
    }

    public void removerCampo() {
        int linhaSelecionada = view.getTabelaDetalhes().getSelectedRow();

        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(view, "Selecione uma linha para remover.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int resposta = JOptionPane.showConfirmDialog(
            view, 
            "Tem certeza que deseja remover este campo?", 
            "Confirmação", 
            JOptionPane.YES_NO_OPTION
        );

        if (resposta == JOptionPane.YES_OPTION) {
            view.getModeloTabela().removeRow(linhaSelecionada);
        }
    }

    public void carregarCampos(Perfil perfil) {
        Object[][] dadosTabela = perfil.getFuncionalidades()
                .entrySet()
                .stream()
                .map(entry -> {
                    String nomeFuncionalidade = entry.getKey();
                    double valor = entry.getValue();
                    return new Object[]{ nomeFuncionalidade, valor };
                })
                .toArray(Object[][]::new);

        view.atualizarTabela(dadosTabela);
    }

    private void setStatusBotaoRemover(boolean status) {
        this.view.getBtnRemoverCampo().setEnabled(status);
    }

    public void setAllBtnVisibleFalse() {
        this.view.getBtnAdicionarCampo().setVisible(false);
        this.view.getBtnRemoverCampo().setVisible(false);
        this.view.getBtnSalvar().setVisible(false);
        this.view.getBtnEditar().setVisible(false);
        this.view.getBtnExcluir().setVisible(false);
        this.view.getBtnCancelar().setVisible(false);
    }

    public ManterPerfilView getView() {
        return view;
    }

    public PerfilRepositoryMock getRepository() {
        return repository;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public void setEstado(ManterPerfilPresenterState estado) {
        this.estado = estado;
    }

}
