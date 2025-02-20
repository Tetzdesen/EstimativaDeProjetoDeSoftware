package com.br.estimativadeprojetodesoftware.state;

import com.br.estimativadeprojetodesoftware.command.MostrarMensagemProjetoCommand;
import com.br.estimativadeprojetodesoftware.model.Subject;
import com.br.estimativadeprojetodesoftware.model.Usuario;
import com.br.estimativadeprojetodesoftware.presenter.Observer;
import com.br.estimativadeprojetodesoftware.presenter.usuario.UsuarioPresenter;
import com.br.estimativadeprojetodesoftware.service.ValidadorSenhaService;
import com.br.estimativadeprojetodesoftware.singleton.UsuarioLogadoSingleton;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tetzner
 */
public class EdicaoState extends UsuarioPresenterState {
    
    private ValidadorSenhaService validadorService;
    
    public EdicaoState(UsuarioPresenter usuarioPresenter) {
        super(usuarioPresenter);
        validadorService = new ValidadorSenhaService();
    }

    @Override
    public void salvar() throws Exception {

        String email = usuarioPresenter.getView().getTxtEmail().getText();
        String nome = usuarioPresenter.getView().getTxtNome().getText();
        String senha = usuarioPresenter.getView().getTxtSenhaAtual().getText();
        
        try {
            validadorService.validarSenha(senha);
        } catch (Exception ex) {
            throw new Exception("Erro na validação da senha:\n" + ex.getMessage());
        }
        
        Usuario usuario = usuarioPresenter.getUsuario();

        Usuario usuarioNovo = new Usuario(usuario.getId(), nome, email, senha, usuario.getIsAutorizado(), usuario.getCreated_at(), usuario.getUpdate_at(), usuario.getDeleted_at(), usuario.getProjetos(), usuario.getProjetosCompartilhados(), usuario.getPerfis());

        UsuarioLogadoSingleton.getInstancia().setUsuario(usuarioNovo);
        usuarioPresenter.setUsuario(usuarioNovo);
        usuarioPresenter.getRepository().atualizarUsuario(usuario, email, nome);
        new MostrarMensagemProjetoCommand("Usuário salvo com sucesso").execute();
        usuarioPresenter.setState(new VisualizacaoState(usuarioPresenter));

        // lançar mensagem JOPtionPane
        //    if (salvarCommand != null) {
        //  salvarCommand.execute();
        //  }
    }

    @Override
    public String toString() {
        return "edição";
    }
}
