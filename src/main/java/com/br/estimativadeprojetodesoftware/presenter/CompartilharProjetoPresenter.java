package com.br.estimativadeprojetodesoftware.presenter;

import com.br.estimativadeprojetodesoftware.command.MostrarMensagemProjetoCommand;
import com.br.estimativadeprojetodesoftware.model.Usuario;
import com.br.estimativadeprojetodesoftware.repository.UsuarioRepositoryMock;
import com.br.estimativadeprojetodesoftware.singleton.UsuarioLogadoSingleton;
import com.br.estimativadeprojetodesoftware.view.CompartilharProjetoView;
import java.util.List;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class CompartilharProjetoPresenter /*implements Observer*/ {

    private final CompartilharProjetoView view;
    // private final EstimaProjetoService estimaService;
    private final UsuarioRepositoryMock repository;

    public CompartilharProjetoPresenter(CompartilharProjetoView view, UsuarioRepositoryMock repository) {
        this.view = view;
        this.repository = repository;
        //this.estimaService = new EstimaProjetoService();
        // this.repository.addObserver(this);
        configurarView();
        carregarListaUsuarios();

    }

    private void configurarView() {
        setStatusBotaoCompartilhar(false);
        this.view.getTabelaUsuarios().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent evt) {
                setStatusBotaoCompartilhar(true);
            }
        });
        this.view.getBtnCompartilharProjeto().addActionListener(e -> {
            try {
            //   realizarCompartilhamento():
            } catch (Exception ex) {
                new MostrarMensagemProjetoCommand(ex.getMessage()).execute();
            }
        });
    }

    private void carregarListaUsuarios() {
        Usuario usuarioLogado = UsuarioLogadoSingleton.getInstancia().getUsuario();
        List<Usuario> usuarioList = repository.getUsuarios();

        for (Usuario usuario : usuarioList) {
            if (usuario != null && !usuario.getEmail().equals(usuarioLogado.getEmail())) {
                carregarDetalhes(usuario);
            }
        }
    }

    private void carregarDetalhes(Usuario usuario) {
        Object[][] dadosTabela = {
            {usuario.getNome(), usuario.getEmail()}
        };

        view.atualizarTabela(dadosTabela);
    }
    
    private void realizarCompartilhamento() throws Exception{
        
    }

    private void setStatusBotaoCompartilhar(boolean status) {
        this.view.getBtnCompartilharProjeto().setEnabled(status);
    }
    /*
    @Override
    public void update() {
        carregarDetalhes();
    }*/
}
