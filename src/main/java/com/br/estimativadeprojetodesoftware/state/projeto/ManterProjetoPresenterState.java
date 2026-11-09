package com.br.estimativadeprojetodesoftware.state.projeto;

import com.br.estimativadeprojetodesoftware.presenter.projeto.ManterProjetoPresenter;
import com.br.estimativadeprojetodesoftware.templatemethod.FormatoExportacao;
import com.br.estimativadeprojetodesoftware.view.projeto.ProjetoView;
import javax.swing.JButton;

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
    protected void habilitar(JButton... ativos) {
        ProjetoView v = presenter.getView();
        JButton[] todos = {v.getBtnEditar(), v.getBtnSalvar(), v.getBtnCancelar(), v.getBtnExcluir(),
            v.getBtnCompartilhar(), v.getBtnExportarPdf(), v.getBtnExportarCsv()};
        for (JButton b : todos) {
            b.setEnabled(false);
        }
        for (JButton b : ativos) {
            b.setEnabled(true);
        }
    }
}
