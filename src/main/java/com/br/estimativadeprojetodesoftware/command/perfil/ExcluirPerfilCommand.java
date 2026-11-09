package com.br.estimativadeprojetodesoftware.command.perfil;

import com.br.estimativadeprojetodesoftware.command.Command;
import com.br.estimativadeprojetodesoftware.command.ConfirmarCommand;
import com.br.estimativadeprojetodesoftware.presenter.perfil.ManterPerfilPresenter;

/** Exclui o perfil; o service recusa o perfil padrão com o motivo. */
public class ExcluirPerfilCommand implements Command {

    private final ManterPerfilPresenter presenter;

    public ExcluirPerfilCommand(ManterPerfilPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void execute() {
        String nome = presenter.getPerfil().getNome();
        if (!ConfirmarCommand.perguntar("Excluir perfil", "Excluir o perfil \"" + nome + "\"?", "Excluir")) {
            return;
        }
        var id = presenter.getPerfil().getId();
        presenter.noBanco("Excluindo…", () -> {
            presenter.getService().excluir(id, presenter.getUsuario());
            return null;
        }, ok -> {
            presenter.fechar();
            presenter.sucesso("Perfil \"" + nome + "\" excluído.");
        });
    }
}
