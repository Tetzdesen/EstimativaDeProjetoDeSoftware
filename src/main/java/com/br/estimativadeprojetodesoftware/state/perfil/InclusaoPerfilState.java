package com.br.estimativadeprojetodesoftware.state.perfil;

import com.br.estimativadeprojetodesoftware.presenter.perfil.ManterPerfilPresenter;

/** Novo perfil (cópia de outro ainda não salva): igual à edição, mas Salvar inclui e Cancelar fecha. */
public class InclusaoPerfilState extends EdicaoPerfilState {

    public InclusaoPerfilState(ManterPerfilPresenter presenter) {
        super(presenter);
    }

    @Override
    public void entrar() {
        super.entrar();
        presenter.getView().getLblSituacao().setText("Novo perfil: ajuste o nome e os valores e clique em Salvar");
    }

    @Override
    public String getNome() {
        return "inclusão";
    }
}
