package com.br.estimativadeprojetodesoftware.presenter.mensagem;

import com.br.estimativadeprojetodesoftware.view.mensagem.MensagemModalView;

/**
 *
 * @author tetzner
 */
public class MensagemModalPresenter {

    private MensagemModalView view;

    public MensagemModalPresenter(MensagemModalView view) {
        this.view = view;
    }

    // Exibir mensagem de erro
    public void exibirMensagemErro(String mensagem) {
        view = new MensagemModalView(null, mensagem, true);  // Passa true para indicar erro
        view.exibirModal();
    }

    // Exibir mensagem de sucesso
    public void exibirMensagemSucesso(String mensagem) {
        view = new MensagemModalView(null, mensagem, false);  // Passa false para indicar sucesso
        view.exibirModal();
    }
}
