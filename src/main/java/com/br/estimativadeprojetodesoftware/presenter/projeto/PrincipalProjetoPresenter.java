package com.br.estimativadeprojetodesoftware.presenter.projeto;

import com.br.estimativadeprojetodesoftware.command.projeto.MostrarMensagemProjetoCommand;
import com.br.estimativadeprojetodesoftware.command.projeto.AbrirDashboardProjetoCommand;
import com.br.estimativadeprojetodesoftware.command.usuario.RealizarLogoutUsuarioCommand;
import com.br.estimativadeprojetodesoftware.command.usuario.AbrirManterUsuarioCommand;
import com.br.estimativadeprojetodesoftware.command.*;
import com.br.estimativadeprojetodesoftware.command.perfil.VisualizarPerfisProjetoCommand;
import com.br.estimativadeprojetodesoftware.command.projeto.AbrirCriarProjetoCommand;
import com.br.estimativadeprojetodesoftware.command.projeto.ConfigurarArvoreProjetoCommand;
import com.br.estimativadeprojetodesoftware.model.Projeto;
import com.br.estimativadeprojetodesoftware.presenter.DesktopMemento;
import com.br.estimativadeprojetodesoftware.observer.Observer;
import com.br.estimativadeprojetodesoftware.presenter.Zelador;
import com.br.estimativadeprojetodesoftware.presenter.window_command.*;
import com.br.estimativadeprojetodesoftware.service.ConstrutorDeArvoreNavegacaoService;
import com.br.estimativadeprojetodesoftware.service.BarraService;
import com.br.estimativadeprojetodesoftware.service.ProjetoService;
import com.br.estimativadeprojetodesoftware.service.UsuarioService;
import com.br.estimativadeprojetodesoftware.singleton.UsuarioLogadoSingleton;
import com.br.estimativadeprojetodesoftware.view.GlobalWindowManager;
import com.br.estimativadeprojetodesoftware.view.projeto.PrincipalProjetoView;
import javax.swing.*;
import java.util.*;

public final class PrincipalProjetoPresenter implements Observer {

    private final PrincipalProjetoView view;
    private final ProjetoService projetoService;
    private final UsuarioService usuarioService;
    private final Map<String, Command> comandos;
    private BarraService criarBarraService;
    private final List<Command> windowCommands = new ArrayList<>();
    private final ConstrutorDeArvoreNavegacaoService construtorDeArvoreNavegacaoService;

    public PrincipalProjetoPresenter() {
        this.view = new PrincipalProjetoView();
        this.projetoService = new ProjetoService();
        this.usuarioService = new UsuarioService();
        this.projetoService.addObserver(this);
        this.usuarioService.addObserver(this);
        UsuarioLogadoSingleton.getInstancia().addObserver(this);
        construtorDeArvoreNavegacaoService = new ConstrutorDeArvoreNavegacaoService();
        GlobalWindowManager.initialize(view);
        this.comandos = inicializarComandos();
        inicializarEExecutarWindowCommands();
        new ConfigurarArvoreProjetoCommand(projetoService, construtorDeArvoreNavegacaoService, comandos, view).execute();
        view.setVisible(true);
    }

    private void inicializarEExecutarWindowCommands() {
        Arrays.asList(
                new ConfigurarViewCommand(this),
                new ConfigurarMenuJanelaCommand(this),
                new SetLookAndFeelCommand()
        ).forEach(Command::execute);
    }

    private Map<String, Command> inicializarComandos() {
        Map<String, Command> comandos = new HashMap<>();
        comandos.put("Principal", new AbrirDashboardProjetoCommand(view.getDesktop(), projetoService));
        comandos.put("Novo projeto", new AbrirCriarProjetoCommand(view.getDesktop(), projetoService));
        comandos.put("Usuário", new AbrirManterUsuarioCommand(this));
        comandos.put("Ver perfis de projeto", new VisualizarPerfisProjetoCommand(view.getDesktop()));
        comandos.put("Logout", new RealizarLogoutUsuarioCommand());
        return comandos;
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

    @Override
    public void update() {
        if (UsuarioLogadoSingleton.getInstancia().usuarioLogado()) {
            SwingUtilities.invokeLater(() -> {
                List<String> idsProjetos = projetoService.buscarProjetosPorUsuario(UsuarioLogadoSingleton.getInstancia().getUsuario().getId());
                List<Projeto> projetos = new ArrayList<>();
                idsProjetos.forEach((projeto) -> projetos.add(projetoService.buscarPorId(UUID.fromString(projeto)).get()));
                Command fecharJanelasCommand = new FecharJanelasRelacionadasCommand(view.getDesktop(), projetos);
                fecharJanelasCommand.execute();
                new ConfigurarArvoreProjetoCommand(projetoService, construtorDeArvoreNavegacaoService, comandos, view).execute();
                new AtualizarPrincipalProjetoPresenterCommand(this).execute();
            });
        } else {
            this.view.dispose();
        }

    }
}
