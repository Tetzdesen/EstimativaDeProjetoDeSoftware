package com.br.estimativadeprojetodesoftware.state.perfil;

import com.br.estimativadeprojetodesoftware.command.MostrarMensagemProjetoCommand;
import com.br.estimativadeprojetodesoftware.command.perfil.CancelarPerfilProjetoCommand;
import com.br.estimativadeprojetodesoftware.command.perfil.SalvarPerfilProjetoCommand;
import com.br.estimativadeprojetodesoftware.model.Perfil;
import com.br.estimativadeprojetodesoftware.presenter.perfil.ManterPerfilPresenter;

public class EdicaoPerfilState extends ManterPerfilPresenterState {

    public EdicaoPerfilState(ManterPerfilPresenter presenter) {
        super(presenter);

        configuraView();
        configuraActionsListerns();
    }

    private void configuraView() {
        Perfil perfil = presenter.getPerfil();
        presenter.getView().getTxtNome().setText(perfil.getNome());
        presenter.getView().getTglBackEnd().setSelected(perfil.isPerfilBackEnd());

        presenter.getView().getTxtNome().setEnabled(true);
        presenter.getView().getTglBackEnd().setEnabled(true);

        presenter.getView().setCellEditable(true);
        presenter.carregarCampos(perfil);

        presenter.getView().getBtnAdicionarCampo().setVisible(true);
        presenter.getView().getBtnRemoverCampo().setVisible(true);
        presenter.getView().getBtnSalvar().setVisible(true);
        presenter.getView().getBtnCancelar().setVisible(true);
    }

    private void configuraActionsListerns() {
        presenter.getView().getBtnSalvar().addActionListener(e -> {
            try {
                salvar();
            } catch (Exception ex) {
                new MostrarMensagemProjetoCommand(ex.getMessage()).execute();
            }
        });

        presenter.getView().getBtnCancelar().addActionListener(e -> {
            try {
                cancelar();
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
    public void cancelar() {
        new CancelarPerfilProjetoCommand(presenter).execute();
    }

    @Override
    public String toString() {
        return "Edição Perfil State";
    }
}
