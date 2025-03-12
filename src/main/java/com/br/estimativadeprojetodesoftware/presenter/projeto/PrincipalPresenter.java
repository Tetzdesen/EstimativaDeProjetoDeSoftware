package com.br.estimativadeprojetodesoftware.presenter.projeto;

import com.br.estimativadeprojetodesoftware.command.usuario.LogoutCommand;
import com.br.estimativadeprojetodesoftware.command.projeto.AbrirCriarProjetoCommand;
import com.br.estimativadeprojetodesoftware.command.usuario.ManterUsuarioCommand;
import com.br.estimativadeprojetodesoftware.command.*;
import com.br.estimativadeprojetodesoftware.command.perfil.VisualizarPerfisProjetoCommand;
import com.br.estimativadeprojetodesoftware.command.projeto.AbrirEdicaoProjetoCommand;
import com.br.estimativadeprojetodesoftware.command.projeto.ExcluirProjetoCommand;
import com.br.estimativadeprojetodesoftware.model.Projeto;
import com.br.estimativadeprojetodesoftware.presenter.DesktopMemento;
import com.br.estimativadeprojetodesoftware.presenter.Observer;
import com.br.estimativadeprojetodesoftware.presenter.Zelador;
import com.br.estimativadeprojetodesoftware.presenter.helpers.WindowManager;
import com.br.estimativadeprojetodesoftware.presenter.window_command.*;
import com.br.estimativadeprojetodesoftware.repository.ProjetoRepositoryMock;
import com.br.estimativadeprojetodesoftware.repository.UsuarioRepositoryMock;
import com.br.estimativadeprojetodesoftware.service.ConstrutorDeArvoreNavegacaoService;
import com.br.estimativadeprojetodesoftware.service.BarraService;
import com.br.estimativadeprojetodesoftware.service.NoArvoreComposite;
import com.br.estimativadeprojetodesoftware.singleton.UsuarioLogadoSingleton;
import com.br.estimativadeprojetodesoftware.view.GlobalWindowManager;
import com.br.estimativadeprojetodesoftware.view.projeto.PrincipalView;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.util.*;

public final class PrincipalPresenter implements Observer {

    private final PrincipalView view;
    private final ProjetoRepositoryMock projetoRepository;
    private final UsuarioRepositoryMock usuarioRepository;
    private final ConstrutorDeArvoreNavegacaoService construtorDeArvoreNavegacaoService;
    private final Map<String, ProjetoCommand> comandos;
    private BarraService criarBarraService;
    private final List<WindowCommand> windowCommands = new ArrayList<>();

    public PrincipalPresenter(ProjetoRepositoryMock projetoRepository, UsuarioRepositoryMock usuarioRepository) {
        this.view = new PrincipalView();
        this.projetoRepository = projetoRepository;
        this.usuarioRepository = usuarioRepository;
        this.projetoRepository.addObserver(this);
        this.usuarioRepository.addObserver(this);

        this.construtorDeArvoreNavegacaoService = new ConstrutorDeArvoreNavegacaoService();

        GlobalWindowManager.initialize(view);
        this.comandos = inicializarComandos();
        inicializarEExecutarWindowCommands();
        view.setVisible(true);
    }

    private void inicializarEExecutarWindowCommands() {
        Arrays.asList(
                new ConfigurarViewCommand(this),
                new ConfigurarMenuJanelaCommand(this),
                new SetLookAndFeelCommand()
        ).forEach(WindowCommand::execute);
    }

    private Map<String, ProjetoCommand> inicializarComandos() {
        Map<String, ProjetoCommand> comandos = new HashMap<>();
        comandos.put("Principal", new AbrirDashboardProjetoCommand(view.getDesktop(), projetoRepository));
        comandos.put("Usuário", new ManterUsuarioCommand(this, view.getDesktop()));
        comandos.put("Ver perfis de projeto", new VisualizarPerfisProjetoCommand(view.getDesktop()));
        comandos.put("Elaborar estimativa", new MostrarMensagemProjetoCommand("Elaborar estimativa ainda não implementada"));
        comandos.put("Visualizar estimativa", new MostrarMensagemProjetoCommand("Visualizar estimativa ainda não implementada"));
        comandos.put("Compartilhar projeto de estimativa", new AbrirCompartilhamentoProjetoCommand(view.getDesktop(), this));
        comandos.put("Exportar projeto de estimativa", new MostrarMensagemProjetoCommand("Exportar ainda não implementado"));
        //  comandos.put("Novo projeto", new CriarProjetoProjetoCommand(projetoRepository, view.getDesktop()));
        comandos.put("Novo projeto", new AbrirCriarProjetoCommand(view.getDesktop(), projetoRepository));
        comandos.put("Excluir projeto", new ExcluirProjetoCommand(projetoRepository));
        comandos.put("Abrir detalhes", new AbrirDetalhesProjetoProjetoCommand(projetoRepository, view.getDesktop()));
        comandos.put("Logout", new LogoutCommand(this.getView().getDesktop()));
        return comandos;
    }

    public void configurarArvore() {
        NoArvoreComposite raiz = construtorDeArvoreNavegacaoService.criarNo("Principal", "principal", comandos.get("Principal"));
        NoArvoreComposite noUsuario = construtorDeArvoreNavegacaoService.criarNo("Usuário", "usuario", comandos.get("Usuário"));
        NoArvoreComposite noPerfis = construtorDeArvoreNavegacaoService.criarNo("Ver perfis de projeto", "perfil", comandos.get("Ver perfis de projeto"));
        NoArvoreComposite noProjetos = construtorDeArvoreNavegacaoService.criarNo("Projetos", "projeto", null);
        NoArvoreComposite noProjetosCompartilhados = construtorDeArvoreNavegacaoService.criarNo("Projetos Compartilhados", "compartilhar-projeto", null);

        noProjetos.setMenuContextual(() -> {
            JPopupMenu menu = new JPopupMenu();
            JMenuItem novoProjetoItem = new JMenuItem("Novo Projeto");
            novoProjetoItem.addActionListener(e -> {
                ProjetoCommand cmd = comandos.get("Novo projeto");
                if (cmd != null) {
                    cmd.execute();
                }
            });
            menu.add(novoProjetoItem);
            return menu;
        });

        raiz.adicionarFilho(noUsuario);
        raiz.adicionarFilho(noPerfis);
        raiz.adicionarFilho(noProjetos);
        raiz.adicionarFilho(noProjetosCompartilhados);

        List<Projeto> listaProjetos = projetoRepository.getProjetos();

        for (final Projeto projeto : listaProjetos) {
            AbrirDetalhesProjetoProjetoCommand cmdDetalhes = new AbrirDetalhesProjetoProjetoCommand(projetoRepository, view.getDesktop()) {
                @Override
                public void execute() {
                    String tituloJanela = "Detalhes do Projeto: " + projeto.getNome();
                    WindowManager windowManager = WindowManager.getInstance();

                    if (!windowManager.isFrameAberto(tituloJanela)) {
                        super.execute();
                        bloquearMinimizacao(tituloJanela);
                    } else {
                        windowManager.bringToFront(tituloJanela);
                    }
                }
            };
            cmdDetalhes.setProjetoNome(projeto.getNome());
            NoArvoreComposite noProjeto = construtorDeArvoreNavegacaoService.criarNo(projeto.getNome(), "projeto", cmdDetalhes);

            adicionarMenuContextual(projeto, noProjeto);

       //     noProjeto.adicionarFilho(construtorDeArvoreNavegacaoService.criarNo("Elaborar estimativa", "action", comandos.get("Elaborar estimativa")));
       //     noProjeto.adicionarFilho(construtorDeArvoreNavegacaoService.criarNo("Visualizar estimativa", "action", comandos.get("Visualizar estimativa")));
         //   noProjeto.adicionarFilho(construtorDeArvoreNavegacaoService.criarNo("Compartilhar projeto de estimativa", "action", comandos.get("Compartilhar projeto de estimativa")));
         //   noProjeto.adicionarFilho(construtorDeArvoreNavegacaoService.criarNo("Exportar projeto de estimativa", "action", comandos.get("Exportar projeto de estimativa")));
            noProjetos.adicionarFilho(noProjeto);

        }

        for (final Projeto projeto : listaProjetos) {
            if (projeto.isCompartilhado() == true) {
                AbrirDetalhesProjetoProjetoCommand cmdDetalhes = new AbrirDetalhesProjetoProjetoCommand(projetoRepository, view.getDesktop()) {
                    @Override
                    public void execute() {
                        String tituloJanela = "Detalhes do Projeto: " + projeto.getNome();
                        WindowManager windowManager = WindowManager.getInstance();

                        if (!windowManager.isFrameAberto(tituloJanela)) {
                            super.execute();
                            bloquearMinimizacao(tituloJanela);
                        } else {
                            windowManager.bringToFront(tituloJanela);
                        }
                    }
                };
                cmdDetalhes.setProjetoNome(projeto.getNome());
                NoArvoreComposite noProjetoCompartilhado = construtorDeArvoreNavegacaoService.criarNo(projeto.getNome(), "projeto", cmdDetalhes);

                adicionarMenuContextual(projeto, noProjetoCompartilhado);

                adicionarMenuContextual(projeto, noProjetoCompartilhado);
                noProjetoCompartilhado.adicionarFilho(construtorDeArvoreNavegacaoService.criarNo("Visualizar estimativa", "action", comandos.get("Visualizar estimativa")));
                noProjetosCompartilhados.adicionarFilho(noProjetoCompartilhado);
            }
        }

        DefaultMutableTreeNode modeloArvore = construtorDeArvoreNavegacaoService.converterParaNoMutavel(raiz);
        JTree arvore = construtorDeArvoreNavegacaoService.criarJTreeDoModelo(modeloArvore);
        view.setTree(arvore);
    }

    private void adicionarMenuContextual(Projeto projeto, NoArvoreComposite noProjeto) {
        noProjeto.setMenuContextual(() -> {
            JPopupMenu menu = new JPopupMenu();
            JMenuItem editarProjetoItem = new JMenuItem("Editar Projeto");
            JMenuItem excluirProjetoItem = new JMenuItem("Excluir Projeto");
            editarProjetoItem.addActionListener(e -> {
                ProjetoCommand cmdEditar = new AbrirEdicaoProjetoCommand(view.getDesktop(), projetoRepository, projeto.getNome()); // trocar para editar depois
                cmdEditar.execute();
            });
            excluirProjetoItem.addActionListener(e -> {
                ProjetoCommand cmdExcluir = new ExcluirProjetoCommand(projetoRepository, projeto.getNome());
                cmdExcluir.execute();
            });
            menu.add(editarProjetoItem);
            menu.add(excluirProjetoItem);
            return menu;
        });
    
    }

    @Override
    public void update() {
        SwingUtilities.invokeLater(() -> {
            WindowCommand fecharJanelasCommand = new FecharJanelasRelacionadasCommand(view.getDesktop(), projetoRepository.getProjetos());
            fecharJanelasCommand.execute();
            new AtualizarViewCommand(this).execute();
            configurarArvore();
        });
    }

    private void bloquearMinimizacao(String titulo) {
        JInternalFrame[] frames = view.getDesktop().getAllFrames();
        for (JInternalFrame frame : frames) {
            if (frame.getTitle().equals(titulo)) {
                frame.setIconifiable(false);
            }
        }
    }

    public void restaurarJanelas() {
        DesktopMemento memento = Zelador.getInstance().restaurarEstado();
        if (memento != null) {
            memento.restore(getView().getDesktop());
            getView().revalidate();
            getView().repaint();
        } else {
            new MostrarMensagemProjetoCommand("Nenhum estado anterior salvo para restaurar.").execute();
        }
    }

    public Map<String, ProjetoCommand> getComandos() {
        return comandos;
    }

    public ProjetoRepositoryMock getProjetoRepository() {
        return projetoRepository;
    }

    public UsuarioRepositoryMock getUsuarioRepository() {
        return usuarioRepository;
    }

    public PrincipalView getView() {
        return view;
    }

    public BarraService getCriarBarraService() {
        return criarBarraService;
    }

    public void setCriarBarraService(BarraService criarBarraService) {
        this.criarBarraService = criarBarraService;
    }
}
