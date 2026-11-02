package com.br.estimativadeprojetodesoftware.state.perfil;

import com.br.estimativadeprojetodesoftware.presenter.perfil.ManterPerfilPresenter;
import com.br.estimativadeprojetodesoftware.view.perfil.ManterPerfilView;
import javax.swing.JButton;

/** State da tela de perfil; cada ação disponível é executada por um Command. */
public abstract class ManterPerfilPresenterState {

    protected final ManterPerfilPresenter presenter;

    protected ManterPerfilPresenterState(ManterPerfilPresenter presenter) {
        this.presenter = presenter;
    }

    public abstract void entrar();

    public abstract String getNome();

    public void editar() {
        indisponivel("editar");
    }

    public void salvar() {
        indisponivel("salvar");
    }

    public void cancelar() {
        indisponivel("cancelar");
    }

    public void excluir() {
        indisponivel("excluir");
    }

    public void adicionarFuncionalidade() {
        indisponivel("adicionar funcionalidade");
    }

    public void removerFuncionalidade() {
        indisponivel("remover funcionalidade");
    }

    private void indisponivel(String acao) {
        throw new IllegalStateException("A ação \"" + acao + "\" não está disponível no modo " + getNome() + ".");
    }

    protected void habilitar(JButton... ativos) {
        ManterPerfilView v = presenter.getView();
        for (JButton b : new JButton[]{v.getBtnEditar(), v.getBtnSalvar(), v.getBtnCancelar(), v.getBtnExcluir(),
            v.getBtnNovaFuncionalidade(), v.getBtnRemoverFuncionalidade()}) {
            b.setEnabled(false);
        }
        for (JButton b : ativos) {
            b.setEnabled(true);
        }
    }
}
