package com.br.estimativadeprojetodesoftware.state.usuario;

import com.br.estimativadeprojetodesoftware.presenter.usuario.ManterUsuarioPresenter;

/**
 *
 * @author tetzner
 */
public abstract class ManterUsuarioPresenterState {

    protected ManterUsuarioPresenter usuarioPresenter;

    public ManterUsuarioPresenterState(ManterUsuarioPresenter usuarioPresenter) {
        this.usuarioPresenter = usuarioPresenter;
    }

    public void salvar() throws Exception {
        throw new RuntimeException("Não é possível salvar a partir do estado " + toString());
    }

    public void editar() throws Exception {
        throw new RuntimeException("Não é possível editar a partir do estado " + toString());
    }

    public void excluir() throws Exception {
        throw new RuntimeException("Não é possível excluir a partir do estado " + toString());
    }

}
