package com.br.estimativadeprojetodesoftware.command.projeto;

import com.br.estimativadeprojetodesoftware.command.ProjetoCommand;
import com.br.estimativadeprojetodesoftware.model.Projeto;
import com.br.estimativadeprojetodesoftware.presenter.helpers.WindowManager;
import com.br.estimativadeprojetodesoftware.repository.ProjetoRepositoryMock;
import com.br.estimativadeprojetodesoftware.service.ConstrutorDeArvoreNavegacaoService;
import com.br.estimativadeprojetodesoftware.service.NoArvoreComposite;
import com.br.estimativadeprojetodesoftware.service.ProjetoRepositoryService;
import com.br.estimativadeprojetodesoftware.singleton.UsuarioLogadoSingleton;
import com.br.estimativadeprojetodesoftware.view.projeto.PrincipalProjetoView;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.JInternalFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author tetzner
 */
public class ConfigurarArvoreProjetoCommand implements ProjetoCommand {

    private final ConstrutorDeArvoreNavegacaoService construtorDeArvoreNavegacaoService;
    private final PrincipalProjetoView view;
    private List<String> nomesProjetos;
    private final Map<String, ProjetoCommand> comandos;
    private ProjetoRepositoryService projetoService;

    public ConfigurarArvoreProjetoCommand(ProjetoRepositoryService projetoService, ConstrutorDeArvoreNavegacaoService construtorDeArvoreNavegacaoService, Map<String, ProjetoCommand> comandos, PrincipalProjetoView view) {
        this.construtorDeArvoreNavegacaoService = construtorDeArvoreNavegacaoService;
        this.view = view; // passar presenter?
        this.projetoService = projetoService;
        this.comandos = comandos;

    }

    @Override
    public void execute() {
        NoArvoreComposite raiz = construtorDeArvoreNavegacaoService.criarNo("Principal", "principal", comandos.get("Principal"));
        NoArvoreComposite noUsuario = construtorDeArvoreNavegacaoService.criarNo("Usuário", "usuario", comandos.get("Usuário"));
        NoArvoreComposite noPerfis = construtorDeArvoreNavegacaoService.criarNo("Ver perfis de projeto", "perfil", comandos.get("Ver perfis de projeto"));
        NoArvoreComposite noProjetos = construtorDeArvoreNavegacaoService.criarNo("Projetos", "projeto", null);
        NoArvoreComposite noProjetosCompartilhados = construtorDeArvoreNavegacaoService.criarNo("Projetos Compartilhados", "compartilhar-projeto", null);

        noProjetos.setMenuContextual(() -> {
            JPopupMenu menu = new JPopupMenu();
            JMenuItem novoProjetoItem = new JMenuItem("Novo Projeto");
            novoProjetoItem.addActionListener(e -> {
                ProjetoCommand cmd = new AbrirCriarProjetoCommand(view.getDesktop(), projetoService);
                cmd.execute();

            });
            menu.add(novoProjetoItem);
            return menu;
        });

        raiz.adicionarFilho(noUsuario);
        raiz.adicionarFilho(noPerfis);
        raiz.adicionarFilho(noProjetos);
        raiz.adicionarFilho(noProjetosCompartilhados);

        nomesProjetos = projetoService.buscarNomesDeProjetosPorUsuario(UsuarioLogadoSingleton.getInstancia().getUsuario().getId());
    
        nomesProjetos.forEach((projeto) -> {
        });
        for (final String projeto : nomesProjetos) {
            AbrirDetalhesProjetoProjetoCommand cmdDetalhes = new AbrirDetalhesProjetoProjetoCommand(projeto, view.getDesktop()) {
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

            //     noProjeto.adicionarFilho(construtorDeArvoreNavegacaoService.criarNo("Elaborar estimativa", "action", comandos.get("Elaborar estimativa")));
            //     noProjeto.adicionarFilho(construtorDeArvoreNavegacaoService.criarNo("Visualizar estimativa", "action", comandos.get("Visualizar estimativa")));
            //   noProjeto.adicionarFilho(construtorDeArvoreNavegacaoService.criarNo("Compartilhar projeto de estimativa", "action", comandos.get("Compartilhar projeto de estimativa")));
            //   noProjeto.adicionarFilho(construtorDeArvoreNavegacaoService.criarNo("Exportar projeto de estimativa", "action", comandos.get("Exportar projeto de estimativa")));
            noProjetos.adicionarFilho(noProjeto);

        }

        /*
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
        }*/
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
                ProjetoCommand cmdEditar = new AbrirEdicaoProjetoCommand(projetoService, nomeProjeto);
                cmdEditar.execute();
            });
            excluirProjetoItem.addActionListener(e -> {
                ProjetoCommand cmdExcluir = new ExcluirProjetoCommand(projetoService, nomeProjeto);
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
