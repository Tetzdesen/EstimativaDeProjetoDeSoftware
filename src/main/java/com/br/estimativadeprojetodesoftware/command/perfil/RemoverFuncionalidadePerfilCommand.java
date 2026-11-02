package com.br.estimativadeprojetodesoftware.command.perfil;

import com.br.estimativadeprojetodesoftware.command.Command;
import com.br.estimativadeprojetodesoftware.model.Funcionalidade;
import com.br.estimativadeprojetodesoftware.presenter.perfil.ManterPerfilPresenter;

public class RemoverFuncionalidadePerfilCommand implements Command {

    private final ManterPerfilPresenter presenter;

    public RemoverFuncionalidadePerfilCommand(ManterPerfilPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void execute() {
        Funcionalidade f = presenter.funcionalidadeSelecionada();
        if (f == null) {
            throw new IllegalArgumentException("Selecione a funcionalidade a remover.");
        }
        presenter.getPerfil().removerFuncionalidade(f.getId());
        presenter.atualizarTabelas();
    }
}
