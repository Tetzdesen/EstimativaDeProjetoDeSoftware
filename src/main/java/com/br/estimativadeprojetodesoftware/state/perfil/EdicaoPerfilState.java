package com.br.estimativadeprojetodesoftware.state.perfil;

import com.br.estimativadeprojetodesoftware.command.projeto.MostrarMensagemProjetoCommand;
import com.br.estimativadeprojetodesoftware.command.perfil.CancelarPerfilProjetoCommand;
import com.br.estimativadeprojetodesoftware.command.perfil.SalvarPerfilProjetoCommand;
import com.br.estimativadeprojetodesoftware.model.PerfilProjeto;
import com.br.estimativadeprojetodesoftware.presenter.perfil.ManterPerfilPresenter;

public class EdicaoPerfilState extends ManterPerfilPresenterState {

    public EdicaoPerfilState(ManterPerfilPresenter presenter) {
        super(presenter);

        configuraView();
        configuraActionsListerns();
    }

    private void configuraView() {
        PerfilProjeto perfil = presenter.getPerfil();

        carregarCamposObrigatorios(perfil);

        presenter.getView().getTxtNome().setEnabled(true);
        presenter.getView().getTglBackEnd().setEnabled(true);

        presenter.getView().getJspPequeno().setEnabled(true);
        presenter.getView().getJspMedio().setEnabled(true);
        presenter.getView().getJspGrande().setEnabled(true);
        
        presenter.getView().getTxtMvp().setEnabled(true);
        presenter.getView().getTxtBasico().setEnabled(true);
        presenter.getView().getTxtProfissional().setEnabled(true);

        presenter.getView().getTxtDesignerUI().setEnabled(true);
        presenter.getView().getTxtGerenciaProjeto().setEnabled(true);
        presenter.getView().getTxtDesenvolvimento().setEnabled(true);

        presenter.getView().setCellEditable(true);
        presenter.carregarCampos(perfil);

        presenter.getView().getBtnAdicionarCampo().setVisible(true);
        presenter.getView().getBtnRemoverCampo().setVisible(true);
        presenter.getView().getBtnSalvar().setVisible(true);
        presenter.getView().getBtnCancelar().setVisible(true);
    }

    private void carregarCamposObrigatorios(PerfilProjeto perfil) {
        presenter.getView().getTxtNome().setText(perfil.getNome());
        presenter.getView().getTglBackEnd().setSelected(perfil.isPerfilBackEnd());

        presenter.getView().getJspPequeno().setValue(perfil.getTamanhosApp().get("pequeno"));
        presenter.getView().getJspMedio().setValue(perfil.getTamanhosApp().get("médio"));
        presenter.getView().getJspGrande().setValue(perfil.getTamanhosApp().get("grande"));

        presenter.getView().getTxtMvp().setText((perfil.getNiveisUI().get("mvp")).toString());
        presenter.getView().getTxtBasico().setText((perfil.getNiveisUI().get("básico")).toString());
        presenter.getView().getTxtProfissional().setText((perfil.getNiveisUI().get("profissional")).toString());

        presenter.getView().getTxtDesignerUI().setText((perfil.getTaxasDiarias().get("designer ui/ux")).toString());
        presenter.getView().getTxtGerenciaProjeto().setText((perfil.getTaxasDiarias().get("gerência de projeto")).toString());
        presenter.getView().getTxtDesenvolvimento().setText((perfil.getTaxasDiarias().get("desenvolvimento")).toString());
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
                presenter.AdicionarNovaFuncionalidade();
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
        new SalvarPerfilProjetoCommand(presenter, presenter.getRepository()).execute();
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
