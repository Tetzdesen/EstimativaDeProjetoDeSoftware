package com.br.estimativadeprojetodesoftware.command.perfil;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import com.br.estimativadeprojetodesoftware.model.PerfilProjeto;
import com.br.estimativadeprojetodesoftware.presenter.perfil.ManterPerfilPresenter;
import com.br.estimativadeprojetodesoftware.service.PerfilProjetoService;
import com.br.estimativadeprojetodesoftware.state.perfil.VisualizacaoPerfilState;
import com.br.estimativadeprojetodesoftware.command.Command;

public class SalvarPerfilProjetoCommand implements Command {

    private final ManterPerfilPresenter presenter;
    private final PerfilProjetoService repository;

    public SalvarPerfilProjetoCommand(ManterPerfilPresenter presenter, PerfilProjetoService repository) {
        this.presenter = presenter;
        this.repository = repository;
    }

    @Override
    public void execute() {
        if (presenter.getView().getTxtNome().getText().trim().isEmpty()) {
            throw new IllegalArgumentException("O campo 'Nome' é obrigatório!");
        }

        if (presenter.getView().getTxtMvp().getText().trim().isEmpty()
                || presenter.getView().getTxtBasico().getText().trim().isEmpty()
                || presenter.getView().getTxtProfissional().getText().trim().isEmpty()) {
            throw new IllegalArgumentException("Preencha todos os campos de nível UI!");      
        }

        if (presenter.getView().getTxtDesignerUI().getText().trim().isEmpty()
                || presenter.getView().getTxtGerenciaProjeto().getText().trim().isEmpty()
                || presenter.getView().getTxtDesenvolvimento().getText().trim().isEmpty()) {
            throw new IllegalArgumentException("Preencha todos os campos de taxa diária!");
        }

        String nome = presenter.getView().getTxtNome().getText();
        boolean isBackEnd = presenter.getView().getTglBackEnd().isSelected();
        DefaultTableModel tabela = presenter.getView().getModeloTabela();

        int pequeno = (int) presenter.getView().getJspPequeno().getValue();
        int medio = (int) presenter.getView().getJspMedio().getValue();
        int grande = (int) presenter.getView().getJspGrande().getValue();

        if (!(pequeno < medio && medio < grande && grande > pequeno && grande > medio)) {
            throw new IllegalArgumentException("Os valores devem seguir a ordem: Pequeno < Médio < Grande, e Grande deve ser o maior.");
        }

        double mvp = Double.parseDouble(presenter.getView().getTxtMvp().getText());
        double basico = Double.parseDouble(presenter.getView().getTxtBasico().getText());
        double profissional = Double.parseDouble(presenter.getView().getTxtProfissional().getText());

        if (!(mvp < basico && basico < profissional && profissional > mvp && profissional > basico)) {
            throw new IllegalArgumentException("Os valores devem seguir a ordem: MVP < Básico < Profissional, e Profissional deve ser o maior.");
        }

        double designer = Double.parseDouble(presenter.getView().getTxtDesignerUI().getText());
        double gerencia = Double.parseDouble(presenter.getView().getTxtGerenciaProjeto().getText());
        double desenvolvimento = Double.parseDouble(presenter.getView().getTxtDesenvolvimento().getText());

        if (presenter.getView().getTabelaDetalhes().isEditing()) {
            presenter.getView().getTabelaDetalhes().getCellEditor().stopCellEditing();
        }

        if (presenter.getPerfil() == null) {
            PerfilProjeto perfil = new PerfilProjeto(nome);

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

            for (int i = 0; i < tabela.getRowCount(); i++) {
                String funcionalidade = tabela.getValueAt(i, 0).toString();
                int numeroDeDias = Integer.parseInt(tabela.getValueAt(i, 1).toString());
                if (funcionalidade.isEmpty() && tabela.getValueAt(i, 1).toString().isEmpty()) {
                    throw new RuntimeException("Campos de funcionalidades vazios");
                }
                perfil.adicionarFuncionalidade(funcionalidade, numeroDeDias);
            }

            repository.salvar(perfil);

            presenter.getView().dispose();

        } else {
            PerfilProjeto perfil = presenter.getRepository().buscarPorId(presenter.getPerfil().getId()).get();
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

            for (int i = 0; i < tabela.getRowCount(); i++) {
                String funcionalidade = tabela.getValueAt(i, 0).toString();
                int numeroDeDias = Integer.parseInt(tabela.getValueAt(i, 1).toString());
                if (funcionalidade.isEmpty() && tabela.getValueAt(i, 1).toString().isEmpty()) {
                    throw new RuntimeException("Campos de funcionalidades vazios");
                }
                perfil.adicionarFuncionalidade(funcionalidade, numeroDeDias);
            }

            repository.atualizar(perfil);

            JOptionPane.showMessageDialog(presenter.getView(), "Perfil alterado com sucesso!");

            presenter.setAllBtnVisibleFalse();
            presenter.setPerfil(perfil);
            presenter.setEstado(new VisualizacaoPerfilState(presenter));
        }

    }
}
