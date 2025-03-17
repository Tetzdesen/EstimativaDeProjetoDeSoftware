package com.br.estimativadeprojetodesoftware.command.projeto;

import java.util.List;
import java.util.Map;

import javax.swing.JInternalFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import com.br.estimativadeprojetodesoftware.presenter.helpers.WindowManager;
import com.br.estimativadeprojetodesoftware.service.ConstrutorDeArvoreNavegacaoService;
import com.br.estimativadeprojetodesoftware.service.NoArvoreComposite;
import com.br.estimativadeprojetodesoftware.service.ProjetoService;
import com.br.estimativadeprojetodesoftware.singleton.UsuarioLogadoSingleton;
import com.br.estimativadeprojetodesoftware.view.projeto.PrincipalProjetoView;
import com.br.estimativadeprojetodesoftware.command.Command;

/**
 *
 * @author tetzner
 */
public class ConfigurarArvoreProjetoCommand implements Command {

    private final ConstrutorDeArvoreNavegacaoService construtorDeArvoreNavegacaoService;
    private final PrincipalProjetoView view;
    private List<String> nomesProjetos;
    private final Map<String, Command> comandos;
    private ProjetoService projetoService;

    public ConfigurarArvoreProjetoCommand(ProjetoService projetoService,
            ConstrutorDeArvoreNavegacaoService construtorDeArvoreNavegacaoService, Map<String, Command> comandos,
            PrincipalProjetoView view) {
        this.construtorDeArvoreNavegacaoService = construtorDeArvoreNavegacaoService;
        this.view = view; // passar presenter?
        this.projetoService = projetoService;
        this.comandos = comandos;

    }

    @Override
    public void execute() {
        NoArvoreComposite raiz = construtorDeArvoreNavegacaoService.criarNo("Principal", "principal",
                comandos.get("Principal"));
        NoArvoreComposite noUsuario = construtorDeArvoreNavegacaoService.criarNo("Usuário", "usuario",
                comandos.get("Usuário"));
        NoArvoreComposite noPerfis = construtorDeArvoreNavegacaoService.criarNo("Ver perfis de projeto", "perfil",
                comandos.get("Ver perfis de projeto"));
        NoArvoreComposite noProjetos = construtorDeArvoreNavegacaoService.criarNo("Projetos", "projeto", null);
        NoArvoreComposite noProjetosCompartilhados = construtorDeArvoreNavegacaoService
                .criarNo("Projetos Compartilhados", "compartilhar-projeto", null);

        noProjetos.setMenuContextual(() -> {
            JPopupMenu menu = new JPopupMenu();
            JMenuItem novoProjetoItem = new JMenuItem("Novo Projeto");
            novoProjetoItem.addActionListener(e -> {
                Command cmd = new AbrirCriarProjetoCommand(view.getDesktop(), projetoService);
                cmd.execute();

            });
            menu.add(novoProjetoItem);
            return menu;
        });

        raiz.adicionarFilho(noUsuario);
        raiz.adicionarFilho(noPerfis);
        raiz.adicionarFilho(noProjetos);
        raiz.adicionarFilho(noProjetosCompartilhados);

        nomesProjetos = projetoService
                .buscarNomesDeProjetosPorUsuario(UsuarioLogadoSingleton.getInstancia().getUsuario().getId());

        nomesProjetos.forEach((projeto) -> {
        });
        for (final String projeto : nomesProjetos) {
            AbrirDetalhesProjetoCommand cmdDetalhes = new AbrirDetalhesProjetoCommand(projeto, projetoService,
                    view.getDesktop()) {
                @Override
                public void execute() {
                    String tituloJanela = "Detalhes do Projeto: " + projeto;
                    WindowManager windowManager = WindowManager.getInstance();

                    if (!windowManager.isFrameAberto(tituloJanela)) {
                        super.execute();
                        bloquearMinimizacao(tituloJanela);
                    } else {
                        windowManager.bringToFront(tituloJanela);
                    }
                }
            };
            cmdDetalhes.setProjetoNome(projeto);
            NoArvoreComposite noProjeto = construtorDeArvoreNavegacaoService.criarNo(projeto, "projeto", cmdDetalhes);

            adicionarMenuContextual(projeto, noProjeto);

            noProjetos.adicionarFilho(noProjeto);

        }

        List<String> projetosCompartilhados = projetoService.buscarNomesDeProjetosCompartilhadosPorUsuario(
            UsuarioLogadoSingleton.getInstancia().getUsuario().getId());

        for (final String projeto : projetosCompartilhados) {

            AbrirDetalhesProjetoCommand cmdDetalhes = new AbrirDetalhesProjetoCommand(projeto, projetoService,
                    view.getDesktop()) {
                @Override
                public void execute() {
                    String tituloJanela = "Detalhes do Projeto: " + projeto;
                    WindowManager windowManager = WindowManager.getInstance();

                    if (!windowManager.isFrameAberto(tituloJanela)) {
                        super.execute();
                        bloquearMinimizacao(tituloJanela);
                    } else {
                        windowManager.bringToFront(tituloJanela);
                    }
                }
            };
            cmdDetalhes.setProjetoNome(projeto);
            
            NoArvoreComposite noProjetoCompartilhado = construtorDeArvoreNavegacaoService.criarNo(projeto, "projeto",
                    cmdDetalhes);

            noProjetosCompartilhados.adicionarFilho(noProjetoCompartilhado);

        }
        DefaultMutableTreeNode modeloArvore = construtorDeArvoreNavegacaoService.converterParaNoMutavel(raiz);
        JTree arvore = construtorDeArvoreNavegacaoService.criarJTreeDoModelo(modeloArvore);
        view.setTree(arvore);
    }

    private void adicionarMenuContextual(String nomeProjeto, NoArvoreComposite noProjeto) {
        noProjeto.setMenuContextual(() -> {
            JPopupMenu menu = new JPopupMenu();
            JMenuItem editarProjetoItem = new JMenuItem("Editar Projeto");
            JMenuItem excluirProjetoItem = new JMenuItem("Excluir Projeto");
            editarProjetoItem.addActionListener(e -> {
                Command cmdEditar = new AbrirEdicaoProjetoCommand(projetoService, nomeProjeto);
                cmdEditar.execute();
            });
            excluirProjetoItem.addActionListener(e -> {
                Command cmdExcluir = new ExcluirProjetoCommand(projetoService, nomeProjeto);
                cmdExcluir.execute();
            });
            menu.add(editarProjetoItem);
            menu.add(excluirProjetoItem);
            return menu;
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

}
