package com.br.estimativadeprojetodesoftware.presenter.projeto;

import com.br.estimativadeprojetodesoftware.presenter.helpers.FilaBanco;
import java.util.List;
import com.br.estimativadeprojetodesoftware.command.Command;
import com.br.estimativadeprojetodesoftware.command.ConfirmarCommand;
import com.br.estimativadeprojetodesoftware.command.ExibirMensagemCommand;
import com.br.estimativadeprojetodesoftware.command.usuario.AbrirManterUsuarioCommand;
import com.br.estimativadeprojetodesoftware.command.usuario.RealizarLogoutUsuarioCommand;
import com.br.estimativadeprojetodesoftware.model.Projeto;
import com.br.estimativadeprojetodesoftware.model.Usuario;
import com.br.estimativadeprojetodesoftware.observer.EventoDominio;
import com.br.estimativadeprojetodesoftware.observer.Observer;
import com.br.estimativadeprojetodesoftware.presenter.DesktopMemento;
import com.br.estimativadeprojetodesoftware.presenter.Zelador;
import com.br.estimativadeprojetodesoftware.presenter.helpers.Navegador;
import com.br.estimativadeprojetodesoftware.presenter.window_command.ConfigurarMenuJanelaCommand;
import com.br.estimativadeprojetodesoftware.presenter.window_command.ConfigurarViewCommand;
import com.br.estimativadeprojetodesoftware.service.BarraService;
import com.br.estimativadeprojetodesoftware.service.LogService;
import com.br.estimativadeprojetodesoftware.singleton.ConexaoSingleton;
import com.br.estimativadeprojetodesoftware.service.ConstrutorDeArvoreNavegacaoService;
import com.br.estimativadeprojetodesoftware.service.NoArvoreComposite;
import com.br.estimativadeprojetodesoftware.service.ProjetoService;
import com.br.estimativadeprojetodesoftware.service.UsuarioService;
import com.br.estimativadeprojetodesoftware.singleton.UsuarioLogadoSingleton;
import com.br.estimativadeprojetodesoftware.view.GlobalWindowManager;
import com.br.estimativadeprojetodesoftware.view.projeto.PrincipalProjetoView;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

/**
 * Janela principal MDI: árvore de navegação, barra de ferramentas, menus e
 * rodapé com o usuário logado. Observer: a árvore acompanha criação, alteração,
 * exclusão e compartilhamento de projetos feitos em qualquer janela.
 */
public final class PrincipalProjetoPresenter implements Observer {

    private final PrincipalProjetoView view;
    private final ProjetoService projetoService;
    private final UsuarioService usuarioService;
    private final Map<String, Command> comandos;
    private final ConstrutorDeArvoreNavegacaoService arvoreService;
    private BarraService criarBarraService;

    public PrincipalProjetoPresenter() {
        this.view = new PrincipalProjetoView();
        this.projetoService = new ProjetoService();
        this.usuarioService = new UsuarioService();
        this.arvoreService = new ConstrutorDeArvoreNavegacaoService();
        Navegador.inicializar(view.getDesktop());
        GlobalWindowManager.initialize(view);
        this.comandos = inicializarComandos();
        Arrays.asList(new ConfigurarViewCommand(this), new ConfigurarMenuJanelaCommand(this))
                .forEach(Command::execute);
        projetoService.addObserver(this);
        UsuarioLogadoSingleton.getInstancia().addObserver(this);
        atualizarRodape();
        montarArvore();
        registrarAtalhos();
        view.setVisible(true);
    }

    private Map<String, Command> inicializarComandos() {
        Map<String, Command> mapa = new LinkedHashMap<>();
        mapa.put("Dashboard", Navegador::abrirDashboard);
        mapa.put("Projetos", Navegador::abrirListaProjetos);
        mapa.put("Novo projeto", Navegador::abrirNovoProjeto);
        mapa.put("Perfis", Navegador::abrirListaPerfis);
        mapa.put("Configuração", Navegador::abrirConfiguracao);
        mapa.put("Usuário", new AbrirManterUsuarioCommand(this));
        mapa.put("Logout", this::sair);
        return mapa;
    }

    public void executarComando(String chave) {
        try {
            comandos.get(chave).execute();
        } catch (RuntimeException ex) {
            ExibirMensagemCommand.erro(ex.getMessage());
        }
    }

    private Command protegido(Command comando) {
        return () -> {
            try {
                comando.execute();
            } catch (RuntimeException ex) {
                ExibirMensagemCommand.erro(ex.getMessage());
            }
        };
    }

    private void sair() {
        if (!ConfirmarCommand.perguntar("Sair", "Sair do sistema e voltar para a tela inicial?", "Sair")) {
            return;
        }
        projetoService.removeObserver(this);
        UsuarioLogadoSingleton.getInstancia().removeObserver(this);
        Navegador.fecharTodas();
        view.dispose();
        new RealizarLogoutUsuarioCommand().execute();
    }

    private record ProjetosDaArvore(List<Projeto> proprios, List<Projeto> compartilhados) {
    }

    /** Busca os projetos fora da thread do Swing e monta a árvore quando chegarem. */
    private void montarArvore() {
        Usuario usuario = UsuarioLogadoSingleton.getInstancia().getUsuario();
        FilaBanco.executar(null, null,
                () -> new ProjetosDaArvore(projetoService.listarProprios(usuario), projetoService.listarCompartilhadosComigo(usuario)),
                this::montarArvore, e -> ExibirMensagemCommand.erro("Não foi possível carregar os projetos: " + e.getMessage()));
    }

    private void montarArvore(ProjetosDaArvore projetos) {
        if (!view.isDisplayable()) {
            return;
        }
        NoArvoreComposite raiz = arvoreService.criarNo("Principal", "dashboard", protegido(Navegador::abrirDashboard));
        NoArvoreComposite noPerfis = arvoreService.criarNo("Perfis de estimativa", "perfil", protegido(Navegador::abrirListaPerfis));
        NoArvoreComposite noProjetos = arvoreService.criarNo("Meus projetos", "projeto", protegido(Navegador::abrirListaProjetos));
        NoArvoreComposite noCompartilhados = arvoreService.criarNo("Compartilhados comigo", "compartilhar",
                protegido(Navegador::abrirListaProjetos));
        NoArvoreComposite noConfig = arvoreService.criarNo("Configuração", "configuracao", protegido(Navegador::abrirConfiguracao));
        NoArvoreComposite noUsuario = arvoreService.criarNo("Usuário", "usuario", protegido(comandos.get("Usuário")));

        noProjetos.setMenuContextual(() -> {
            JPopupMenu menu = new JPopupMenu();
            JMenuItem novo = new JMenuItem("Novo projeto");
            novo.addActionListener(e -> executarComando("Novo projeto"));
            menu.add(novo);
            return menu;
        });

        for (Projeto p : projetos.proprios()) {
            noProjetos.adicionarFilho(arvoreService.criarNo(p.getNome(), "projeto",
                    protegido(() -> Navegador.abrirProjeto(p.getId()))));
        }
        for (Projeto p : projetos.compartilhados()) {
            noCompartilhados.adicionarFilho(arvoreService.criarNo(p.getNome() + " · " + p.getDonoNome(),
                    "compartilhar", protegido(() -> Navegador.abrirProjeto(p.getId()))));
        }

        raiz.adicionarFilho(noProjetos);
        raiz.adicionarFilho(noCompartilhados);
        raiz.adicionarFilho(noPerfis);
        raiz.adicionarFilho(noConfig);
        raiz.adicionarFilho(noUsuario);
        view.setTree(arvoreService.criarJTreeDoModelo(arvoreService.converterParaNoMutavel(raiz)));
        for (int i = 0; i < view.getTree().getRowCount(); i++) {
            view.getTree().expandRow(i);
        }
    }

    private void atualizarRodape() {
        Usuario usuario = UsuarioLogadoSingleton.getInstancia().getUsuario();
        String banco;
        String log;
        try {
            banco = "Banco: " + ConexaoSingleton.getInstancia().getTipo();
            log = "Log: " + LogService.getInstancia().getFormato();
        } catch (RuntimeException e) {
            banco = "Banco: indisponível";
            log = "Log: -";
        }
        view.exibirRodape(usuario == null ? " " : usuario.getNome() + "  <" + usuario.getEmail() + ">", banco, log);
        if (criarBarraService != null && usuario != null) {
            criarBarraService.atualizarLabelUsuarioLogado();
        }
    }

    private void registrarAtalhos() {
        javax.swing.JRootPane raiz = view.getRootPane();
        raiz.getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_DOWN_MASK), "novoProjeto");
        raiz.getActionMap().put("novoProjeto", new javax.swing.AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                executarComando("Novo projeto");
            }
        });
    }

    public void restaurarJanelas() {
        DesktopMemento memento = Zelador.getInstance().restaurarEstado();
        if (memento != null) {
            memento.restore(view.getDesktop());
            view.revalidate();
            view.repaint();
        } else {
            ExibirMensagemCommand.aviso("Nenhum estado anterior salvo para restaurar.");
        }
    }

    @Override
    public void update(EventoDominio evento) {
        if (!UsuarioLogadoSingleton.getInstancia().usuarioLogado()) {
            return;
        }
        if (evento.sobre(EventoDominio.Entidade.PROJETO) || evento.sobre(EventoDominio.Entidade.COMPARTILHAMENTO)
                || evento.sobre(EventoDominio.Entidade.SESSAO) || evento.sobre(EventoDominio.Entidade.TEMA)) {
            SwingUtilities.invokeLater(() -> {
                atualizarRodape();
                montarArvore();
            });
        }
    }

    public Map<String, Command> getComandos() {
        return comandos;
    }

    public ProjetoService getProjetoService() {
        return projetoService;
    }

    public UsuarioService getUsuarioService() {
        return usuarioService;
    }

    public PrincipalProjetoView getView() {
        return view;
    }

    public BarraService getCriarBarraService() {
        return criarBarraService;
    }

    public void setCriarBarraService(BarraService criarBarraService) {
        this.criarBarraService = criarBarraService;
    }
}
