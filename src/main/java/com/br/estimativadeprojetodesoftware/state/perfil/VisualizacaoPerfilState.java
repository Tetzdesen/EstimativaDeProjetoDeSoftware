package com.br.estimativadeprojetodesoftware.state.perfil;

import com.br.estimativadeprojetodesoftware.command.MostrarMensagemProjetoCommand;
import com.br.estimativadeprojetodesoftware.model.Perfil;
import com.br.estimativadeprojetodesoftware.presenter.perfil.ManterPerfilPresenter;

public class VisualizacaoPerfilState extends ManterPerfilPresenterState {
    //private Perfil perfil;

    public VisualizacaoPerfilState(ManterPerfilPresenter presenter) {
        super(presenter);
        //this.perfil = perfil;
        configuraView();
        configuraActionsListerns();
    }

    private void configuraView() {
        Perfil perfil = presenter.getPerfil();

        presenter.getView().getTxtNome().setText(perfil.getNome());
        presenter.getView().getTglBackEnd().setSelected(perfil.isPerfilBackEnd());

        presenter.getView().getTxtNome().setEnabled(false);
        presenter.getView().getTglBackEnd().setEnabled(false);
        
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

    @Override
    public void editar() {
        presenter.setAllBtnVisibleFalse();
        presenter.setEstado(new EdicaoPerfilState(presenter));
    }

    @Override
    public void excluir() {

    }
}
