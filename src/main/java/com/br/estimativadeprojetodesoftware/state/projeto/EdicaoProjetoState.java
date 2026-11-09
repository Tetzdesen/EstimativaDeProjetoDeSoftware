package com.br.estimativadeprojetodesoftware.state.projeto;

import com.br.estimativadeprojetodesoftware.command.projeto.AtualizarEstimativaCommand;
import com.br.estimativadeprojetodesoftware.command.projeto.CancelarEdicaoProjetoCommand;
import com.br.estimativadeprojetodesoftware.command.projeto.SalvarProjetoCommand;
import com.br.estimativadeprojetodesoftware.presenter.projeto.ManterProjetoPresenter;
import com.br.estimativadeprojetodesoftware.view.componentes.Selo;
import com.br.estimativadeprojetodesoftware.view.projeto.ProjetoView;

/** Dono editando: seleções, valores, taxas, custos, impostos e lucro. */
public class EdicaoProjetoState extends ManterProjetoPresenterState {

    public EdicaoProjetoState(ManterProjetoPresenter presenter) {
        super(presenter);
    }

    @Override
    public void entrar() {
        ProjetoView v = presenter.getView();
        habilitar(v.getBtnSalvar(), v.getBtnCancelar());
        v.getPainelPerfis().setVisible(false);
        v.getPainelEstimativa().setVisible(true);
        v.getPainelSecoes().setVisible(true);
        presenter.setEditavel(true);
        v.exibirSituacao("Editando", Selo.Tipo.DESTAQUE, "A estimativa é atualizada a cada alteração. Clique em Salvar (Ctrl+S) para guardar.");
        atualizarEstimativa();
    }

    @Override
    public String getNome() {
        return "edição";
    }

    @Override
    public void salvar() {
        new SalvarProjetoCommand(presenter).execute();
    }

    @Override
    public void cancelar() {
        new CancelarEdicaoProjetoCommand(presenter).execute();
    }

    @Override
    public void atualizarEstimativa() {
        new AtualizarEstimativaCommand(presenter).execute();
    }
}
