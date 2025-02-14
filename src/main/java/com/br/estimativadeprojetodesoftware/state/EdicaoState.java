package com.br.estimativadeprojetodesoftware.state;

import com.br.estimativadeprojetodesoftware.command.MostrarMensagemProjetoCommand;
import com.br.estimativadeprojetodesoftware.command.ProjetoCommand;
import com.br.estimativadeprojetodesoftware.model.Usuario;
import com.br.estimativadeprojetodesoftware.presenter.UsuarioPresenter;
import com.br.estimativadeprojetodesoftware.singleton.UsuarioLogadoSingleton;

/**
 *
 * @author tetzner
 */
public class EdicaoState extends UsuarioPresenterState {

    public EdicaoState(UsuarioPresenter usuarioPresenter) {
        super(usuarioPresenter);
    }

    @Override
    public void salvar() {
        String nome = usuarioPresenter.getView().getTxtNome().getText();
        String email = usuarioPresenter.getView().getTxtEmail().getText();

        Usuario usuario = usuarioPresenter.getUsuario();

        Usuario usuarioNovo = new Usuario(usuario.getId(), nome, email, usuario.getSenha(), usuario.getIsAutorizado(), usuario.getCreated_at(), usuario.getUpdate_at(), usuario.getDeleted_at(), usuario.getProjetos(), usuario.getPerfis());

        usuarioPresenter.getRepository().atualizarUsuario(usuario, email, nome);
        UsuarioLogadoSingleton.getInstancia().setUsuario(usuario);
        usuarioPresenter.setUsuario(usuarioNovo);

        new MostrarMensagemProjetoCommand("Usuário salvo com sucesso").execute();
        usuarioPresenter.setState(new VisualizacaoState(usuarioPresenter));

        
        // lançar mensagem JOPtionPane
    //    if (salvarCommand != null) {
          //  salvarCommand.execute();
      //  }
    }
    
    @Override
    public String toString() {
        return "Edição";
    }
}
