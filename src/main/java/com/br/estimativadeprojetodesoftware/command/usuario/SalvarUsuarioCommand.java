package com.br.estimativadeprojetodesoftware.command.usuario;

import com.br.estimativadeprojetodesoftware.command.projeto.MostrarMensagemProjetoCommand;
import com.br.estimativadeprojetodesoftware.model.Usuario;
import com.br.estimativadeprojetodesoftware.presenter.usuario.ManterUsuarioPresenter;
import com.br.estimativadeprojetodesoftware.service.SenhaHashService;
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
        String senhaDigitada = new String(senhaArray);

        Usuario usuario = usuarioPresenter.getUsuario();

        // O campo chega vazio porque a senha guardada é um hash. Só há troca de
        // senha quando o usuário digita uma nova.
        String senhaPersistida = usuario.getSenha();

        if (!senhaDigitada.trim().isEmpty()) {
            try {
                validadorService.validarSenha(senhaDigitada);
            } catch (Exception ex) {
                throw new RuntimeException("Erro ao validar a senha \n" + ex.getMessage());
            }
            senhaPersistida = SenhaHashService.gerarHash(senhaDigitada);
        }

        Usuario usuarioNovo = new Usuario(usuario.getId(), nome, email, senhaPersistida, usuario.getCreated_at(), usuario.getLog(), usuario.getProjetos(), usuario.getPerfis());

        UsuarioLogadoSingleton.getInstancia().setUsuario(usuarioNovo);
        usuarioPresenter.setUsuario(usuarioNovo);

        usuarioPresenter.getUsuarioService().atualizar(usuarioNovo);
        new MostrarMensagemProjetoCommand("Usuário salvo com sucesso").execute();
    }

}
