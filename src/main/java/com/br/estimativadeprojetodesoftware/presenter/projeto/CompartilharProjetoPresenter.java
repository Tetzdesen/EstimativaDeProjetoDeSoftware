package com.br.estimativadeprojetodesoftware.presenter.projeto;

import com.br.estimativadeprojetodesoftware.command.projeto.CompartilharProjetoCommand;
import com.br.estimativadeprojetodesoftware.command.projeto.MostrarMensagemProjetoCommand;
import com.br.estimativadeprojetodesoftware.model.Projeto;
import com.br.estimativadeprojetodesoftware.model.Usuario;
import com.br.estimativadeprojetodesoftware.service.ProjetoRepositoryService;
import com.br.estimativadeprojetodesoftware.service.UsuarioRepositoryService;
import com.br.estimativadeprojetodesoftware.singleton.UsuarioLogadoSingleton;
import com.br.estimativadeprojetodesoftware.view.projeto.CompartilharProjetoView;
import java.util.List;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class CompartilharProjetoPresenter  {

    private final CompartilharProjetoView view;
    private final UsuarioRepositoryService usuarioService;
    private final ProjetoRepositoryService projetoService;
    private List<Usuario> usuarios;
    private final String nomeProjeto;

    public CompartilharProjetoPresenter(ProjetoRepositoryService projetoService, CompartilharProjetoView view, String nomeProjeto) {
        this.view = view;
        this.nomeProjeto = nomeProjeto;
        this.usuarioService = new UsuarioRepositoryService();
        this.projetoService = projetoService;
        this.usuarios = usuarioService.buscarTodos();
        configurarView();
        carregarListaUsuarios();

    }

    private void configurarView() {

        this.view.setModal(true);
        this.view.setSize(1000, 700);
        
        setStatusBotaoCompartilhar(false);
        
        this.view.getTabelaUsuarios().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent evt) {
                setStatusBotaoCompartilhar(true);
            }
        });
        
        this.view.getBtnCompartilharProjeto().addActionListener(e -> {
            
            try {

                if (nomeProjeto == null) {
                    new MostrarMensagemProjetoCommand("Nenhum projeto selecionado.").execute();
                    return;
                }
                Projeto projeto = projetoService.buscarProjetoPorNome(nomeProjeto).get();
                int linhaSelecionada = this.view.getTabelaUsuarios().getSelectedRow();

                if (linhaSelecionada != -1) {
                    Usuario usuario = usuarios.get(linhaSelecionada);
                    new CompartilharProjetoCommand(usuarioService, projetoService, UsuarioLogadoSingleton.getInstancia().getUsuario(), usuario, projeto).execute();
                }
                
            } catch (Exception ex) {
                new MostrarMensagemProjetoCommand(ex.getMessage()).execute();
            }
        }
        );
    }

    private void carregarListaUsuarios() {
        Usuario usuarioLogado = UsuarioLogadoSingleton.getInstancia().getUsuario();

        for (Usuario usuario : usuarios) {
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

    private void setStatusBotaoCompartilhar(boolean status) {
        this.view.getBtnCompartilharProjeto().setEnabled(status);
    }
}
