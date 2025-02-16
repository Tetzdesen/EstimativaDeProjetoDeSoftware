package com.br.estimativadeprojetodesoftware.state;

import com.br.estimativadeprojetodesoftware.presenter.PrincipalPresenter;
import com.br.estimativadeprojetodesoftware.presenter.UsuarioPresenter;

/**
 *
 * @author tetzner
 */
public class UsuarioPresenterState {

    protected UsuarioPresenter usuarioPresenter;

    public UsuarioPresenterState(UsuarioPresenter usuarioPresenter) {
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
