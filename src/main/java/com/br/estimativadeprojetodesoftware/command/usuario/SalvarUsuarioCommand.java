package com.br.estimativadeprojetodesoftware.command.usuario;

import com.br.estimativadeprojetodesoftware.builder.AndroidBuilder;
import com.br.estimativadeprojetodesoftware.builder.Diretor;
import com.br.estimativadeprojetodesoftware.builder.IosBuilder;
import com.br.estimativadeprojetodesoftware.builder.WebBackEndBuilder;
import com.br.estimativadeprojetodesoftware.command.MostrarMensagemProjetoCommand;
import com.br.estimativadeprojetodesoftware.command.ProjetoCommand;
import com.br.estimativadeprojetodesoftware.model.Usuario;
import com.br.estimativadeprojetodesoftware.presenter.usuario.ManterUsuarioPresenter;
import com.br.estimativadeprojetodesoftware.service.PerfilRepositoryService;
import com.br.estimativadeprojetodesoftware.service.ValidadorSenhaService;
import com.br.estimativadeprojetodesoftware.singleton.UsuarioLogadoSingleton;

/**
 *
 * @author tetzner
 */
public class SalvarUsuarioCommand implements ProjetoCommand {

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
        String senha = usuarioPresenter.getView().getTxtSenhaAtual().getText();

        try {
            validadorService.validarSenha(senha);
        } catch (Exception ex) {

        }

        Usuario usuario = usuarioPresenter.getUsuario();

        Usuario usuarioNovo = new Usuario(usuario.getId(), nome, email, senha, usuario.getCreated_at(), usuario.getLog(), usuario.getProjetos(), usuario.getPerfis());

        UsuarioLogadoSingleton.getInstancia().setUsuario(usuarioNovo);
        usuarioPresenter.setUsuario(usuarioNovo);
        usuarioPresenter.getRepository().atualizar(usuarioNovo);
        new MostrarMensagemProjetoCommand("Usuário salvo com sucesso").execute();
    }

}
