package com.br.estimativadeprojetodesoftware.state.projeto;

import com.br.estimativadeprojetodesoftware.command.projeto.CriarProjetoCommand;
import com.br.estimativadeprojetodesoftware.command.projeto.FecharProjetoCommand;
import com.br.estimativadeprojetodesoftware.presenter.projeto.ManterProjetoPresenter;
import com.br.estimativadeprojetodesoftware.view.componentes.Selo;

/** Novo projeto: nome e perfis de origem. Salvar cria o projeto; Cancelar fecha. */
public class InclusaoProjetoState extends ManterProjetoPresenterState {

    public InclusaoProjetoState(ManterProjetoPresenter presenter) {
        super(presenter);
    }

    @Override
    public void entrar() {
        habilitar(presenter.getView().getBtnSalvar(), presenter.getView().getBtnCancelar());
        presenter.getView().getPainelPerfis().setVisible(true);
        presenter.getView().getPainelEstimativa().setVisible(false);
        presenter.getView().getPainelSecoes().setVisible(false);
        presenter.getView().getTxtNome().setEditable(true);
        presenter.getView().exibirSituacao("Novo", Selo.Tipo.DESTAQUE, "Informe o nome, escolha um ou mais perfis e clique em Salvar.");
    }

    @Override
    public String getNome() {
        return "inclusão";
    }

    @Override
    public void salvar() {
        new CriarProjetoCommand(presenter).execute();
    }

    @Override
    public void cancelar() {
        new FecharProjetoCommand(presenter).execute();
    }
}
