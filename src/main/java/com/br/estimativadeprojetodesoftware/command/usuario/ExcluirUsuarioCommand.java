package com.br.estimativadeprojetodesoftware.command.usuario;

import com.br.estimativadeprojetodesoftware.command.projeto.MostrarMensagemProjetoCommand;
import com.br.estimativadeprojetodesoftware.command.ProjetoCommand;
import com.br.estimativadeprojetodesoftware.presenter.usuario.ManterUsuarioPresenter;

/**
 *
 * @author tetzner
 */
public class ExcluirUsuarioCommand implements ProjetoCommand {

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
            new RealizarLogoutUsuarioCommand().execute();
        } else {
            new MostrarMensagemProjetoCommand("Erro ao remover o usuário \"" + usuarioPresenter.getUsuario().getNome() + "\".").execute();
        }
        usuarioPresenter.excluir();
    }
}
