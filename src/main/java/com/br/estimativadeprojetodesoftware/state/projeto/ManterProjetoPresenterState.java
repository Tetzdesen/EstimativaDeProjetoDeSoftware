package com.br.estimativadeprojetodesoftware.state.projeto;

import com.br.estimativadeprojetodesoftware.presenter.projeto.ManterProjetoPresenter;
import com.br.estimativadeprojetodesoftware.templatemethod.FormatoExportacao;
import com.br.estimativadeprojetodesoftware.view.projeto.ProjetoView;

/**
 * State da tela de projeto. Cada estado habilita só as ações válidas e
 * executa cada ação por meio de um Command. Ações não disponíveis no estado
 * são recusadas.
 */
public abstract class ManterProjetoPresenterState {

    protected final ManterProjetoPresenter presenter;

    protected ManterProjetoPresenterState(ManterProjetoPresenter presenter) {
        this.presenter = presenter;
    }

    /** Aplica o estado na tela (botões e campos). */
    public abstract void entrar();

    public abstract String getNome();

    public void salvar() {
        indisponivel("salvar");
    }

    /** Descarta o que não foi salvo (na inclusão, desiste do novo projeto). */
    public void descartar() {
        indisponivel("descartar alterações");
    }

    /**
     * Chamado quando a tela passa a ter (ou deixa de ter) alterações não
     * salvas. Só a edição reage.
     */
    public void atualizarAcoesDeGravacao(boolean alterado) {
    }

    public void excluir() {
        indisponivel("excluir");
    }

    /**
     * Recalcula a estimativa exibida. Fora da edição não faz nada: é disparado
     * por um temporizador, que pode terminar depois de o estado ter mudado.
     */
    public void atualizarEstimativa() {
    }

    public void compartilhar() {
        indisponivel("compartilhar");
    }

    public void exportar(FormatoExportacao formato) {
        indisponivel("exportar");
    }

    private void indisponivel(String acao) {
        throw new IllegalStateException("A ação \"" + acao + "\" não está disponível no modo " + getNome() + ".");
    }

    /** Habilita exatamente os botões informados e desabilita os demais. */
    protected void habilitar(javax.swing.AbstractButton... ativos) {
        ProjetoView v = presenter.getView();
        javax.swing.AbstractButton[] todos = {v.getBtnSalvar(), v.getBtnDescartar(), v.getBtnExcluir(),
            v.getBtnCompartilhar(), v.getBtnExportarPdf(), v.getBtnExportarCsv()};
        for (javax.swing.AbstractButton b : todos) {
            b.setEnabled(false);
        }
        for (javax.swing.AbstractButton b : ativos) {
            b.setEnabled(true);
        }
    }
}
