package com.br.estimativadeprojetodesoftware.state.perfil;

import java.util.ArrayList;
import java.util.List;

import com.br.estimativadeprojetodesoftware.command.projeto.MostrarMensagemProjetoCommand;
import com.br.estimativadeprojetodesoftware.command.perfil.ExcluirPerfilProjetoCommand;
import com.br.estimativadeprojetodesoftware.model.Campo;
import com.br.estimativadeprojetodesoftware.model.Perfil;
import com.br.estimativadeprojetodesoftware.presenter.perfil.ManterPerfilPresenter;
import com.br.estimativadeprojetodesoftware.service.CampoRepositoryService;
import com.br.estimativadeprojetodesoftware.service.PerfilRepositoryService;
import com.br.estimativadeprojetodesoftware.singleton.UsuarioLogadoSingleton;

public class VisualizacaoPerfilState extends ManterPerfilPresenterState {

    public VisualizacaoPerfilState(ManterPerfilPresenter presenter) {
        super(presenter);
        configuraView();
        configuraActionsListerns();
    }

    private void configuraView() {
        Perfil perfil = presenter.getPerfil();

        setCamposPreenchidos(perfil);
        desabilitarCampos();
        
        presenter.carregarCampos(perfil);

        presenter.getView().getBtnEditar().setVisible(true);
        presenter.getView().getBtnExcluir().setVisible(true);
    }

    private void configuraActionsListerns() {
        presenter.getView().getBtnEditar().addActionListener(e -> {
            try {
                editar();
            } catch (Exception ex) {
                new MostrarMensagemProjetoCommand(ex.getMessage()).execute();
            }
        });

        presenter.getView().getBtnExcluir().addActionListener(e -> {
            try {
                excluir();
            } catch (Exception ex) {
                new MostrarMensagemProjetoCommand(ex.getMessage()).execute();
            }
        });
    }

    private void setCamposPreenchidos(Perfil perfil) {
        presenter.getView().getTxtNome().setText(perfil.getNome());
        presenter.getView().getTglBackEnd().setSelected(perfil.isPerfilBackEnd());

        presenter.getView().getJspPequeno().setValue(perfil.getTamanhosApp().get("pequeno"));
        presenter.getView().getJspMedio().setValue(perfil.getTamanhosApp().get("médio"));
        presenter.getView().getJspGrande().setValue(perfil.getTamanhosApp().get("grande"));

        presenter.getView().getTxtMvp().setText(perfil.getNiveisUI().get("mvp").toString());
        presenter.getView().getTxtBasico().setText(perfil.getNiveisUI().get("básico").toString());
        presenter.getView().getTxtProfissional().setText(perfil.getNiveisUI().get("profissional").toString());

        presenter.getView().getTxtDesignerUI().setText((perfil.getTaxasDiarias().get("designer ui/ux")).toString());
        presenter.getView().getTxtGerenciaProjeto().setText((perfil.getTaxasDiarias().get("gerência de projeto")).toString());
        presenter.getView().getTxtDesenvolvimento().setText((perfil.getTaxasDiarias().get("desenvolvimento")).toString());
    }

    private void desabilitarCampos() {
        presenter.getView().getTxtNome().setEnabled(false);
        presenter.getView().getTglBackEnd().setEnabled(false);

        presenter.getView().getJspPequeno().setEnabled(false);
        presenter.getView().getJspMedio().setEnabled(false);
        presenter.getView().getJspGrande().setEnabled(false);
        
        presenter.getView().getTxtMvp().setEnabled(false);
        presenter.getView().getTxtBasico().setEnabled(false);
        presenter.getView().getTxtProfissional().setEnabled(false);

        presenter.getView().getTxtDesignerUI().setEnabled(false);
        presenter.getView().getTxtGerenciaProjeto().setEnabled(false);
        presenter.getView().getTxtDesenvolvimento().setEnabled(false);
    }

    @Override
    public void editar() {
        presenter.setAllBtnVisibleFalse();
        presenter.setEstado(new EdicaoPerfilState(presenter));
    }

    @Override
    public void excluir() {
        new ExcluirPerfilProjetoCommand(presenter).execute();
    }

    @Override
    public String toString() {
        return "Visualização Perfil State";
    }
}
