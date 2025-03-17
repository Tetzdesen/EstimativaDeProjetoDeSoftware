package com.br.estimativadeprojetodesoftware.command.usuario;

import com.br.estimativadeprojetodesoftware.command.projeto.MostrarMensagemProjetoCommand;
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
            new MostrarMensagemProjetoCommand("Usuário \"" + usuarioPresenter.getUsuario().getNome() + "\" removido com sucesso!").execute();
            usuarioPresenter.setUsuario(null);
            usuarioPresenter.getView().dispose();
            new RealizarLogoutUsuarioCommand().execute();
        } else {
            new MostrarMensagemProjetoCommand("Erro ao remover o usuário \"" + usuarioPresenter.getUsuario().getNome() + "\".").execute();
        }
    }
}
