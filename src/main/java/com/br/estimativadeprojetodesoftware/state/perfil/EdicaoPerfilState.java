package com.br.estimativadeprojetodesoftware.state.perfil;

import com.br.estimativadeprojetodesoftware.command.perfil.AdicionarFuncionalidadePerfilCommand;
import com.br.estimativadeprojetodesoftware.command.perfil.CancelarPerfilCommand;
import com.br.estimativadeprojetodesoftware.command.perfil.RemoverFuncionalidadePerfilCommand;
import com.br.estimativadeprojetodesoftware.command.perfil.SalvarPerfilCommand;
import com.br.estimativadeprojetodesoftware.presenter.perfil.ManterPerfilPresenter;
import com.br.estimativadeprojetodesoftware.view.perfil.ManterPerfilView;

public class EdicaoPerfilState extends ManterPerfilPresenterState {

    public EdicaoPerfilState(ManterPerfilPresenter presenter) {
        super(presenter);
    }

    @Override
    public void entrar() {
        ManterPerfilView v = presenter.getView();
        habilitar(v.getBtnSalvar(), v.getBtnCancelar(), v.getBtnNovaFuncionalidade(), v.getBtnRemoverFuncionalidade());
        presenter.setEditavel(true);
        v.getLblSituacao().setText("Editando: digite os dias, percentuais (ex.: 70%) ou NA");
    }

    @Override
    public String getNome() {
        return "edição";
    }

    @Override
    public void salvar() {
        new SalvarPerfilCommand(presenter).execute();
    }

    @Override
    public void cancelar() {
        new CancelarPerfilCommand(presenter).execute();
    }

    @Override
    public void adicionarFuncionalidade() {
        new AdicionarFuncionalidadePerfilCommand(presenter).execute();
    }

    @Override
    public void removerFuncionalidade() {
        new RemoverFuncionalidadePerfilCommand(presenter).execute();
    }
}
