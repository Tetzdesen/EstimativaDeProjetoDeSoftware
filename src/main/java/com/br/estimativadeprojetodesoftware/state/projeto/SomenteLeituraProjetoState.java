package com.br.estimativadeprojetodesoftware.state.projeto;

import com.br.estimativadeprojetodesoftware.presenter.projeto.ManterProjetoPresenter;
import com.br.estimativadeprojetodesoftware.view.componentes.Selo;

/**
 * Destinatário de um projeto compartilhado: só visualiza a versão atual.
 * Editar, excluir, compartilhar e exportar ficam indisponíveis.
 */
public class SomenteLeituraProjetoState extends ManterProjetoPresenterState {

    public SomenteLeituraProjetoState(ManterProjetoPresenter presenter) {
        super(presenter);
    }

    @Override
    public void entrar() {
        habilitar();
        presenter.getView().getPainelPerfis().setVisible(false);
        presenter.getView().getPainelEstimativa().setVisible(true);
        presenter.getView().getPainelSecoes().setVisible(true);
        presenter.setEditavel(false);
        presenter.getView().exibirSituacao("Somente leitura", Selo.Tipo.AVISO, "Compartilhado por " + presenter.getProjeto().getDonoNome()
                + ". Você pode consultar, mas não editar, excluir ou exportar.");
    }

    @Override
    public String getNome() {
        return "somente leitura";
    }
}
