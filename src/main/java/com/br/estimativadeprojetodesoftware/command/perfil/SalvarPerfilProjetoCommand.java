package com.br.estimativadeprojetodesoftware.command.perfil;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import com.br.estimativadeprojetodesoftware.command.ProjetoCommand;
import com.br.estimativadeprojetodesoftware.model.Perfil;
import com.br.estimativadeprojetodesoftware.presenter.perfil.ManterPerfilPresenter;
import com.br.estimativadeprojetodesoftware.state.perfil.VisualizacaoPerfilState;

public class SalvarPerfilProjetoCommand implements ProjetoCommand {
    private final ManterPerfilPresenter presenter;

    public SalvarPerfilProjetoCommand(ManterPerfilPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void execute() {
        String nome = presenter.getView().getTxtNome().getText();
        boolean isBackEnd = presenter.getView().getTglBackEnd().isSelected();
        DefaultTableModel tabela = presenter.getView().getModeloTabela();

        if (presenter.getView().getTabelaDetalhes().isEditing()) {
            presenter.getView().getTabelaDetalhes().getCellEditor().stopCellEditing();
        }

        if (presenter.getPerfil() == null) {
            Perfil perfil = new Perfil(nome);
        
            perfil.setPerfilBackEnd(isBackEnd);

            for (int i=0; i<tabela.getRowCount(); i++) {
                String funcionalidade = tabela.getValueAt(i, 0).toString();
                double numeroDeDias = Double.parseDouble(tabela.getValueAt(i, 1).toString());
                perfil.adicionarFuncionalidade(funcionalidade, numeroDeDias);
            }
            
            presenter.getRepository().setPerfil(perfil);

            JOptionPane.showMessageDialog(presenter.getView(), "Perfil incluído com sucesso!");

            presenter.getView().dispose();

        } else {
            Perfil perfil = presenter.getRepository().buscarPerfilPorId(presenter.getPerfil().getId());
            perfil.removerFuncionalidades();

            perfil.setPerfilBackEnd(isBackEnd);
            perfil.setNome(nome);

            for (int i=0; i<tabela.getRowCount(); i++) {
                String funcionalidade = tabela.getValueAt(i, 0).toString();
                double numeroDeDias = Double.parseDouble(tabela.getValueAt(i, 1).toString());
                perfil.adicionarFuncionalidade(funcionalidade, numeroDeDias);
            }

            JOptionPane.showMessageDialog(presenter.getView(), "Perfil alterado com sucesso!");

            presenter.setAllBtnVisibleFalse();
            presenter.setEstado(new VisualizacaoPerfilState(presenter));
        }

        presenter.getRepository().notifyObservers();
        
    }
}

