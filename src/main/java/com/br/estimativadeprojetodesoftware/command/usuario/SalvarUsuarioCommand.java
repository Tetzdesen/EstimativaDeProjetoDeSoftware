package com.br.estimativadeprojetodesoftware.command.usuario;

import com.br.estimativadeprojetodesoftware.command.projeto.MostrarMensagemProjetoCommand;
import com.br.estimativadeprojetodesoftware.model.Usuario;
import com.br.estimativadeprojetodesoftware.presenter.usuario.ManterUsuarioPresenter;
import com.br.estimativadeprojetodesoftware.service.ValidadorSenhaService;
import com.br.estimativadeprojetodesoftware.singleton.UsuarioLogadoSingleton;
import com.br.estimativadeprojetodesoftware.command.Command;

/**
 *
 * @author tetzner
 */
public class SalvarUsuarioCommand implements Command {

    private final ManterUsuarioPresenter usuarioPresenter;
    private final ValidadorSenhaService validadorService;

    public SalvarUsuarioCommand(ManterUsuarioPresenter usuarioPresenter) {
        this.usuarioPresenter = usuarioPresenter;
        this.validadorService = new ValidadorSenhaService();
    }

    @Override
    public void execute() {
        String email = usuarioPresenter.getView().getTxtEmail().getText();
        String nome = usuarioPresenter.getView().getTxtNome().getText();
        char[] senhaArray = usuarioPresenter.getView().getTxtSenhaAtual().getPassword();
        String senha = new String(senhaArray);

        try {
            validadorService.validarSenha(senha);
        } catch (Exception ex) {
            throw new RuntimeException("Erro ao validar a senha \n" + ex.getMessage());
        }

        Usuario usuario = usuarioPresenter.getUsuario();

        Usuario usuarioNovo = new Usuario(usuario.getId(), nome, email, senha, usuario.getCreated_at(), usuario.getLog(), usuario.getProjetos(), usuario.getPerfis());

        UsuarioLogadoSingleton.getInstancia().setUsuario(usuarioNovo);
        usuarioPresenter.setUsuario(usuarioNovo);

        usuarioPresenter.getUsuarioService().atualizar(usuarioNovo);
        new MostrarMensagemProjetoCommand("Usuário salvo com sucesso").execute();
    }

}
