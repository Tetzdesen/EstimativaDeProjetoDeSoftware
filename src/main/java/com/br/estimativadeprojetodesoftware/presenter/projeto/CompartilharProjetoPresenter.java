package com.br.estimativadeprojetodesoftware.presenter.projeto;

import com.br.estimativadeprojetodesoftware.command.CompartilharProjetoCommand;
import com.br.estimativadeprojetodesoftware.command.MostrarMensagemProjetoCommand;
import com.br.estimativadeprojetodesoftware.model.Projeto;
import com.br.estimativadeprojetodesoftware.model.Usuario;
import com.br.estimativadeprojetodesoftware.repository.ProjetoRepositoryMock;
import com.br.estimativadeprojetodesoftware.repository.UsuarioRepositoryMock;
import com.br.estimativadeprojetodesoftware.singleton.UsuarioLogadoSingleton;
import com.br.estimativadeprojetodesoftware.view.projeto.CompartilharProjetoView;
import java.util.List;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

public class CompartilharProjetoPresenter /*implements Observer*/ {

    private final CompartilharProjetoView view;
    // private final EstimaProjetoService estimaService;
    private String nomeProjeto;
    private final UsuarioRepositoryMock usuarioRepository;
    private final ProjetoRepositoryMock projetoRepository;
    private final PrincipalPresenter principalPresenter;

    public CompartilharProjetoPresenter(CompartilharProjetoView view, PrincipalPresenter principalPresenter) {
        this.view = view;
        this.principalPresenter = principalPresenter;
        this.usuarioRepository = principalPresenter.getUsuarioRepository();
        this.projetoRepository = principalPresenter.getProjetoRepository();
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
                // buscar usuario remetente
                this.nomeProjeto = getProjetoSelecionado();

                if (nomeProjeto == null) {
                    new MostrarMensagemProjetoCommand("Nenhum projeto selecionado.").execute();
                    return;
                }
                Projeto projeto = projetoRepository.getProjetoPorNome(nomeProjeto);
                System.out.println(projeto.getNome());
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

    private String getProjetoSelecionado() {
        Object node = principalPresenter.getView().getTree().getLastSelectedPathComponent();

        if (node instanceof DefaultMutableTreeNode) {
            DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) node;

            // Obtém o nó pai (penúltimo nível)
            DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) treeNode.getParent();

            if (parentNode != null) {
                Object userObject = parentNode.getUserObject();

                if (userObject != null) {
                    return userObject.toString();
                }
            }
        }
        return null;
    }
    /*
    @Override
    public void update() {
        carregarDetalhes();
    }*/
}
