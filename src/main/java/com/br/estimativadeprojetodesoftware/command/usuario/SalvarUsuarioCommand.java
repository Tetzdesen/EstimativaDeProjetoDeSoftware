package com.br.estimativadeprojetodesoftware.command.usuario;

import com.br.estimativadeprojetodesoftware.command.ExibirMensagemCommand;
import com.br.estimativadeprojetodesoftware.model.Usuario;
import com.br.estimativadeprojetodesoftware.presenter.usuario.ManterUsuarioPresenter;
import com.br.estimativadeprojetodesoftware.service.SenhaHashService;
import com.br.estimativadeprojetodesoftware.singleton.UsuarioLogadoSingleton;
import com.br.estimativadeprojetodesoftware.command.Command;

/**
 *
 * @author tetzner
 */
public class SalvarUsuarioCommand implements Command {

    private final ManterUsuarioPresenter usuarioPresenter;

    public SalvarUsuarioCommand(ManterUsuarioPresenter usuarioPresenter) {
        this.usuarioPresenter = usuarioPresenter;
    }

    @Override
    public void execute() {
        String email = usuarioPresenter.getView().getTxtEmail().getText();
        String nome = usuarioPresenter.getView().getTxtNome().getText();
        char[] senhaArray = usuarioPresenter.getView().getTxtSenhaAtual().getPassword();
        String senhaDigitada = new String(senhaArray);

        Usuario usuario = usuarioPresenter.getUsuario();

        // O campo chega vazio porque a senha guardada é um hash. Só há troca de
        // senha quando o usuário digita uma nova.
        String senhaPersistida = usuario.getSenha();

        if (!senhaDigitada.trim().isEmpty()) {
            usuarioPresenter.getUsuarioService().validarSenha(senhaDigitada);
            senhaPersistida = SenhaHashService.gerarHash(senhaDigitada);
        }

        Usuario usuarioNovo = new Usuario(usuario.getId(), nome, email, senhaPersistida, usuario.getCreated_at());

        usuarioPresenter.getUsuarioService().atualizar(usuarioNovo);
        UsuarioLogadoSingleton.getInstancia().setUsuario(usuarioNovo);
        usuarioPresenter.setUsuario(usuarioNovo);
        ExibirMensagemCommand.sucesso("Usuário salvo com sucesso");
    }

}
