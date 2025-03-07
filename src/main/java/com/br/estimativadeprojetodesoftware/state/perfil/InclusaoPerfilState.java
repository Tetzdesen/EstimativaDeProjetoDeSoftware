package com.br.estimativadeprojetodesoftware.state.perfil;

import com.br.estimativadeprojetodesoftware.builder.DefaultBuilder;
import com.br.estimativadeprojetodesoftware.builder.Diretor;
import com.br.estimativadeprojetodesoftware.command.MostrarMensagemProjetoCommand;
import com.br.estimativadeprojetodesoftware.command.perfil.SalvarPerfilProjetoCommand;
import com.br.estimativadeprojetodesoftware.model.Perfil;
import com.br.estimativadeprojetodesoftware.presenter.perfil.ManterPerfilPresenter;

public class InclusaoPerfilState extends ManterPerfilPresenterState {

    public InclusaoPerfilState(ManterPerfilPresenter presenter) {
        super(presenter);
        configuraView();
        configuraActionsListerns();
    }

    private void configuraView() {
        presenter.getView().setCellEditable(true);
        Perfil perfil = Diretor.build(new DefaultBuilder("Default"));
        presenter.carregarCampos(perfil);

        presenter.getView().getBtnAdicionarCampo().setVisible(true);
        presenter.getView().getBtnRemoverCampo().setVisible(true);
        presenter.getView().getBtnSalvar().setVisible(true);
    }

    private void configuraActionsListerns() {
        presenter.getView().getBtnSalvar().addActionListener(e -> {
            try {
                salvar();
            } catch (Exception ex) {
                new MostrarMensagemProjetoCommand(ex.getMessage()).execute();
            }
        });

        presenter.getView().getBtnAdicionarCampo().addActionListener(e -> {
            try {
                presenter.adicionarNovoCampo();
            } catch (Exception ex) {
                new MostrarMensagemProjetoCommand(ex.getMessage()).execute();
            }
        });

        presenter.getView().getBtnRemoverCampo().addActionListener(e -> {
            try {
                presenter.removerCampo();
            } catch (Exception ex) {
                new MostrarMensagemProjetoCommand(ex.getMessage()).execute();
            }
        });
    }

    @Override
    public void salvar() {
        new SalvarPerfilProjetoCommand(presenter).execute();
    }

    @Override
    public String toString() {
        return "Inclusão Perfil State";
    }
}
