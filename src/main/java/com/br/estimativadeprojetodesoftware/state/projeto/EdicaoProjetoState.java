package com.br.estimativadeprojetodesoftware.state.projeto;

import com.br.estimativadeprojetodesoftware.command.projeto.CalcularProjetoCommand;
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
        habilitar(v.getBtnSalvar(), v.getBtnCancelar(), v.getBtnCalcular());
        v.getPainelPerfis().setVisible(false);
        presenter.setEditavel(true);
        v.exibirSituacao("Editando", Selo.Tipo.DESTAQUE, "Marque as funcionalidades e as plataformas e clique em Calcular ou Salvar (Ctrl+S).");
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
    public void calcular() {
        new CalcularProjetoCommand(presenter).execute();
    }
}
