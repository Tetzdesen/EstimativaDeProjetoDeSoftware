package com.br.estimativadeprojetodesoftware.presenter.projeto;

import com.br.estimativadeprojetodesoftware.command.projeto.CompartilharProjetoCommand;
import com.br.estimativadeprojetodesoftware.command.projeto.MostrarMensagemProjetoCommand;
import com.br.estimativadeprojetodesoftware.model.Projeto;
import com.br.estimativadeprojetodesoftware.model.Usuario;
import com.br.estimativadeprojetodesoftware.repository.ProjetoRepositoryMock;
import com.br.estimativadeprojetodesoftware.repository.UsuarioRepositoryMock;
import com.br.estimativadeprojetodesoftware.singleton.UsuarioLogadoSingleton;
import com.br.estimativadeprojetodesoftware.view.projeto.CompartilharProjetoView;
import java.util.List;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class CompartilharProjetoPresenter /*implements Observer*/ {

    private final CompartilharProjetoView view;
    // private final EstimaProjetoService estimaService;
    private final UsuarioRepositoryMock usuarioRepository;
    private final ProjetoRepositoryMock projetoRepository;
    private final String nomeProjeto;

    public CompartilharProjetoPresenter(CompartilharProjetoView view, String nomeProjeto) {
        this.view = view;
        this.nomeProjeto = nomeProjeto;
        this.usuarioRepository = new UsuarioRepositoryMock();
        this.projetoRepository = new ProjetoRepositoryMock();
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

                if (nomeProjeto == null) {
                    new MostrarMensagemProjetoCommand("Nenhum projeto selecionado.").execute();
                    return;
                }
                Projeto projeto = projetoRepository.getProjetoPorNome(nomeProjeto);
                int linhaSelecionada = this.view.getTabelaUsuarios().getSelectedRow();

                if (linhaSelecionada != -1) {
                    Usuario usuario = usuarioRepository.getUsuarios().get(linhaSelecionada);
                    new CompartilharProjetoCommand(usuarioRepository, projetoRepository,  UsuarioLogadoSingleton.getInstancia().getUsuario(), usuario, projeto).execute();
                }
                
            } catch (Exception ex) {
                new MostrarMensagemProjetoCommand(ex.getMessage()).execute();
            }
        }
        );
    }

    private void carregarListaUsuarios() {
        Usuario usuarioLogado = UsuarioLogadoSingleton.getInstancia().getUsuario();
        List<Usuario> usuarioList = usuarioRepository.getUsuarios();

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

    private void realizarCompartilhamento() throws Exception {

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
