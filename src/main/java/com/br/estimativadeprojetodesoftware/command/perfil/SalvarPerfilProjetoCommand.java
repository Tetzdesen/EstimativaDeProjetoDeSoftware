package com.br.estimativadeprojetodesoftware.command.perfil;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import com.br.estimativadeprojetodesoftware.command.ProjetoCommand;
import com.br.estimativadeprojetodesoftware.model.Perfil;
import com.br.estimativadeprojetodesoftware.presenter.perfil.ManterPerfilPresenter;
import com.br.estimativadeprojetodesoftware.service.PerfilRepositoryService;
import com.br.estimativadeprojetodesoftware.state.perfil.VisualizacaoPerfilState;

public class SalvarPerfilProjetoCommand implements ProjetoCommand {
    private final ManterPerfilPresenter presenter;
    private final PerfilRepositoryService repository;

    public SalvarPerfilProjetoCommand(ManterPerfilPresenter presenter, PerfilRepositoryService repository) {
        this.presenter = presenter;
        this.repository = repository;
    }

    @Override
    public void execute() {
        String nome = presenter.getView().getTxtNome().getText();
        boolean isBackEnd = presenter.getView().getTglBackEnd().isSelected();
        DefaultTableModel tabela = presenter.getView().getModeloTabela();

        int pequeno = (int) presenter.getView().getJspPequeno().getValue();
        int medio = (int) presenter.getView().getJspMedio().getValue();
        int grande = (int) presenter.getView().getJspGrande().getValue();

        double mvp = Double.parseDouble(presenter.getView().getTxtMvp().getText());
        double basico = Double.parseDouble(presenter.getView().getTxtBasico().getText());
        double profissional = Double.parseDouble(presenter.getView().getTxtProfissional().getText());

        double designer = Double.parseDouble(presenter.getView().getTxtDesignerUI().getText());
        double gerencia = Double.parseDouble(presenter.getView().getTxtGerenciaProjeto().getText());
        double desenvolvimento = Double.parseDouble(presenter.getView().getTxtDesenvolvimento().getText());

        if (presenter.getView().getTabelaDetalhes().isEditing()) {
            presenter.getView().getTabelaDetalhes().getCellEditor().stopCellEditing();
        }

        if (presenter.getPerfil() == null) {
            Perfil perfil = new Perfil(nome);
        
            perfil.setPerfilBackEnd(isBackEnd);

            perfil.adicionarTamanhoApp("pequeno", pequeno);
            perfil.adicionarTamanhoApp("médio", medio);
            perfil.adicionarTamanhoApp("grande", grande);

            perfil.adicionarNivelUI("mvp", (mvp));
            perfil.adicionarNivelUI("básico", (basico));
            perfil.adicionarNivelUI("profissional", (profissional));

            perfil.adicionarTaxaDiaria("designer ui/ux", designer);
            perfil.adicionarTaxaDiaria("gerência de projeto", gerencia);
            perfil.adicionarTaxaDiaria("desenvolvimento", desenvolvimento);

            for (int i=0; i<tabela.getRowCount(); i++) {
                String funcionalidade = tabela.getValueAt(i, 0).toString();
                int numeroDeDias = Integer.parseInt(tabela.getValueAt(i, 1).toString());
                perfil.adicionarFuncionalidade(funcionalidade, numeroDeDias);
            }
            
            //presenter.getRepository().setPerfil(perfil);
            repository.salvar(perfil);

            presenter.getView().dispose();

        } else {
            Perfil perfil = presenter.getRepository().buscarPorId(presenter.getPerfil().getId()).get();
            perfil.removerFuncionalidades();

            perfil.setPerfilBackEnd(isBackEnd);
            perfil.setNome(nome);

            perfil.adicionarTamanhoApp("pequeno", pequeno);
            perfil.adicionarTamanhoApp("médio", medio);
            perfil.adicionarTamanhoApp("grande", grande);

            perfil.adicionarNivelUI("mvp", mvp);
            perfil.adicionarNivelUI("básico", basico);
            perfil.adicionarNivelUI("profissional", profissional);

            perfil.adicionarTaxaDiaria("designer ui/ux", designer);
            perfil.adicionarTaxaDiaria("gerência de projeto", gerencia);
            perfil.adicionarTaxaDiaria("desenvolvimento", desenvolvimento);

            for (int i=0; i<tabela.getRowCount(); i++) {
                String funcionalidade = tabela.getValueAt(i, 0).toString();
                int numeroDeDias = Integer.parseInt(tabela.getValueAt(i, 1).toString());
                perfil.adicionarFuncionalidade(funcionalidade, numeroDeDias);
            }

            repository.atualizar(perfil);

            JOptionPane.showMessageDialog(presenter.getView(), "Perfil alterado com sucesso!");

            presenter.setAllBtnVisibleFalse();
            presenter.setEstado(new VisualizacaoPerfilState(presenter));
        }

    }
}

