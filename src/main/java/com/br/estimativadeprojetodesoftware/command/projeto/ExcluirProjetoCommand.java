package com.br.estimativadeprojetodesoftware.command.projeto;

import com.br.estimativadeprojetodesoftware.command.Command;
import com.br.estimativadeprojetodesoftware.command.ConfirmarCommand;
import com.br.estimativadeprojetodesoftware.presenter.projeto.ManterProjetoPresenter;

/** Exclui o projeto após confirmação do dono. */
public class ExcluirProjetoCommand implements Command {

    private final ManterProjetoPresenter presenter;

    public ExcluirProjetoCommand(ManterProjetoPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void execute() {
        String nome = presenter.getProjeto().getNome();
        if (!ConfirmarCommand.perguntar("Excluir projeto", "Excluir o projeto \"" + nome + "\"?\nEsta ação não pode ser desfeita.", "Excluir")) {
            return;
        }
        var id = presenter.getProjeto().getId();
        presenter.noBanco("Excluindo…", () -> {
            presenter.getProjetoService().excluir(id, presenter.getUsuario());
            return null;
        }, ok -> {
            presenter.fechar();
            presenter.sucesso("Projeto \"" + nome + "\" excluído.");
        });
    }
}
