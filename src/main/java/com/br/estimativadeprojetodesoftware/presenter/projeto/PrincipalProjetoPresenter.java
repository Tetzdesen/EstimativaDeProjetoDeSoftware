package com.br.estimativadeprojetodesoftware.presenter.projeto;

import com.br.estimativadeprojetodesoftware.command.projeto.MostrarMensagemProjetoCommand;
import com.br.estimativadeprojetodesoftware.command.projeto.AbrirDashboardProjetoCommand;
import com.br.estimativadeprojetodesoftware.command.usuario.LogoutCommand;
import com.br.estimativadeprojetodesoftware.command.usuario.ManterUsuarioCommand;
import com.br.estimativadeprojetodesoftware.command.*;
import com.br.estimativadeprojetodesoftware.command.perfil.VisualizarPerfisProjetoCommand;
import com.br.estimativadeprojetodesoftware.command.projeto.AbrirCriarProjetoCommand;
import com.br.estimativadeprojetodesoftware.command.projeto.ConfigurarArvoreProjetoCommand;
import com.br.estimativadeprojetodesoftware.model.Projeto;
import com.br.estimativadeprojetodesoftware.presenter.DesktopMemento;
import com.br.estimativadeprojetodesoftware.presenter.Observer;
import com.br.estimativadeprojetodesoftware.presenter.Zelador;
import com.br.estimativadeprojetodesoftware.presenter.window_command.*;
import com.br.estimativadeprojetodesoftware.repository.UsuarioRepositoryMock;
import com.br.estimativadeprojetodesoftware.service.ConstrutorDeArvoreNavegacaoService;
import com.br.estimativadeprojetodesoftware.service.BarraService;
import com.br.estimativadeprojetodesoftware.service.ProjetoRepositoryService;
import com.br.estimativadeprojetodesoftware.service.UsuarioRepositoryService;
import com.br.estimativadeprojetodesoftware.singleton.UsuarioLogadoSingleton;
import com.br.estimativadeprojetodesoftware.view.GlobalWindowManager;
import com.br.estimativadeprojetodesoftware.view.projeto.PrincipalProjetoView;
import javax.swing.*;
import java.util.*;

public final class PrincipalProjetoPresenter implements Observer {

    private final PrincipalProjetoView view;
    private final ProjetoRepositoryService projetoService;
    private final UsuarioRepositoryService usuarioService;
    private final Map<String, ProjetoCommand> comandos;
    private BarraService criarBarraService;
    private final List<WindowCommand> windowCommands = new ArrayList<>();
    private final ConstrutorDeArvoreNavegacaoService construtorDeArvoreNavegacaoService;

    public PrincipalProjetoPresenter(ProjetoRepositoryService projetoService, UsuarioRepositoryMock usuarioRepository) {
        this.view = new PrincipalProjetoView();
        this.projetoService = projetoService;
        this.usuarioService = new UsuarioRepositoryService();
        this.projetoService.addObserver(this);
        this.usuarioService.addObserver(this);
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
        ).forEach(WindowCommand::execute);
    }

    private Map<String, ProjetoCommand> inicializarComandos() {
        Map<String, ProjetoCommand> comandos = new HashMap<>();
        comandos.put("Principal", new AbrirDashboardProjetoCommand(view.getDesktop(), projetoService));
        comandos.put("Novo projeto", new AbrirCriarProjetoCommand(view.getDesktop(), projetoService));
        comandos.put("Usuário", new ManterUsuarioCommand(this, view.getDesktop()));
        comandos.put("Ver perfis de projeto", new VisualizarPerfisProjetoCommand(view.getDesktop()));
        comandos.put("Logout", new LogoutCommand(this.getView().getDesktop()));
        return comandos;
    }

    @Override
    public void update() {
        SwingUtilities.invokeLater(() -> {
            List<String> idsProjetos = projetoService.buscarProjetosPorUsuario(UsuarioLogadoSingleton.getInstancia().getUsuario().getId());
            List<Projeto> projetos = new ArrayList<>();
            idsProjetos.forEach((projeto) -> projetos.add(projetoService.buscarPorId(UUID.fromString(projeto)).get()));
            WindowCommand fecharJanelasCommand = new FecharJanelasRelacionadasCommand(view.getDesktop(), projetos);
            fecharJanelasCommand.execute();
            new ConfigurarArvoreProjetoCommand(projetoService, construtorDeArvoreNavegacaoService, comandos, view).execute();
            new AtualizarViewCommand(this).execute();
        });
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

    public ProjetoRepositoryService getProjetoService() {
        return projetoService;
    }

    public UsuarioRepositoryService getUsuarioService() {
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
