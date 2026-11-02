package com.br.estimativadeprojetodesoftware.command.projeto;

import com.br.estimativadeprojetodesoftware.command.Command;
import com.br.estimativadeprojetodesoftware.model.Projeto;
import com.br.estimativadeprojetodesoftware.presenter.projeto.ManterProjetoPresenter;
import com.br.estimativadeprojetodesoftware.state.projeto.EdicaoProjetoState;

/** Cria o projeto a partir dos perfis selecionados (Prototype) e passa para a edição. */
public class CriarProjetoCommand implements Command {

    private final ManterProjetoPresenter presenter;

    public CriarProjetoCommand(ManterProjetoPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void execute() {
        if (!presenter.validarInclusao()) {
            return;
        }
        Projeto projeto = presenter.getProjetoService().criar(presenter.getView().getTxtNome().getText(),
                presenter.idsPerfisSelecionados(), presenter.getUsuario());
        presenter.setProjeto(projeto);
        presenter.setEstado(new EdicaoProjetoState(presenter));
        presenter.sucesso("Projeto \"" + projeto.getNome() + "\" criado. Marque as funcionalidades e calcule a estimativa.");
    }
}
