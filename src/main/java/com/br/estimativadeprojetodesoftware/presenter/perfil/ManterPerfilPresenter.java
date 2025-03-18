package com.br.estimativadeprojetodesoftware.presenter.perfil;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.br.estimativadeprojetodesoftware.command.perfil.AdicionarNovaFuncionalidadePerfilCommand;
import com.br.estimativadeprojetodesoftware.command.perfil.RemoverFuncionalidadePerfilProjetoCommand;
import com.br.estimativadeprojetodesoftware.model.PerfilProjeto;
import com.br.estimativadeprojetodesoftware.service.PerfilProjetoService;
import com.br.estimativadeprojetodesoftware.state.perfil.InclusaoPerfilState;
import com.br.estimativadeprojetodesoftware.state.perfil.ManterPerfilPresenterState;
import com.br.estimativadeprojetodesoftware.state.perfil.VisualizacaoPerfilState;
import com.br.estimativadeprojetodesoftware.view.perfil.ManterPerfilView;

public final class ManterPerfilPresenter {

    private final ManterPerfilView view;
    private final PerfilProjetoService repository;
    private ManterPerfilPresenterState estado;
    private PerfilProjeto perfil;

    public ManterPerfilPresenter(ManterPerfilView view, PerfilProjeto perfil, PerfilProjetoService repository) {
        this.view = view;
        this.repository = repository;
        this.perfil = perfil;
        setStatusBotaoRemover(false);
        configuraActionsListerns();

        setAllBtnVisibleFalse();

        if (perfil == null) {
            this.estado = new InclusaoPerfilState(this);
        } else {
            this.estado = new VisualizacaoPerfilState(this);
        }
    }

    private void configuraActionsListerns() {
        view.getTabelaDetalhes().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent evt) {
                setStatusBotaoRemover(true);
            }
        });

        view.getTglBackEnd().addItemListener(e -> {
            controlaTglBackEnd();
        });
    }

    private void controlaTglBackEnd() {
        if (view.getTglBackEnd().isSelected()) {
            view.getTxtDesignerUI().setEditable(false);
            view.getTxtDesignerUI().setText("0");
        } else {
            view.getTxtDesignerUI().setEditable(true);
        }
    }

    public void AdicionarNovaFuncionalidade() {
        try {
            new AdicionarNovaFuncionalidadePerfilCommand(this).execute();
        } catch(Exception e){
            throw new RuntimeException("Erro ao adicionar campo : Campo vazio!");
        }
    }

    public void removerCampo() {
        new RemoverFuncionalidadePerfilProjetoCommand(this).execute();
    }

    public void carregarCampos(PerfilProjeto perfil) {
        Object[][] dadosTabela = perfil.getFuncionalidades()
                .entrySet()
                .stream()
                .map(entry -> {
                    String nomeFuncionalidade = entry.getKey();
                    int valor = entry.getValue();
                    return new Object[]{nomeFuncionalidade, valor};
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

    public PerfilProjetoService getRepository() {
        return repository;
    }

    public PerfilProjeto getPerfil() {
        return perfil;
    }

    public void setEstado(ManterPerfilPresenterState estado) {
        this.estado = estado;
    }

    public void setPerfil(PerfilProjeto perfil) {
        this.perfil = perfil;
    }

    public ManterPerfilPresenterState getEstado() {
        return estado;
    }
}
