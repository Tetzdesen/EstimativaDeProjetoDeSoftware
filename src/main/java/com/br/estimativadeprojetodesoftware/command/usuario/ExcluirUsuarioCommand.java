package com.br.estimativadeprojetodesoftware.command.usuario;

import com.br.estimativadeprojetodesoftware.command.ExibirMensagemCommand;
import com.br.estimativadeprojetodesoftware.presenter.usuario.ManterUsuarioPresenter;
import com.br.estimativadeprojetodesoftware.command.Command;

/**
 *
 * @author tetzner
 */
public class ExcluirUsuarioCommand implements Command {

    private final ManterUsuarioPresenter usuarioPresenter;

    public ExcluirUsuarioCommand(ManterUsuarioPresenter usuarioPresenter) {
        this.usuarioPresenter = usuarioPresenter;
    }

    @Override
    public void execute() {
        boolean removido = usuarioPresenter.getUsuarioService().removerPorId(usuarioPresenter.getUsuario().getId());
        if (removido) {
            ExibirMensagemCommand.sucesso("Usuário \"" + usuarioPresenter.getUsuario().getNome() + "\" removido com sucesso!");
            usuarioPresenter.setUsuario(null);
            usuarioPresenter.getView().dispose();
            new RealizarLogoutUsuarioCommand().execute();
        } else {
            ExibirMensagemCommand.erro("Erro ao remover o usuário \"" + usuarioPresenter.getUsuario().getNome() + "\".");
        }
    }
}
