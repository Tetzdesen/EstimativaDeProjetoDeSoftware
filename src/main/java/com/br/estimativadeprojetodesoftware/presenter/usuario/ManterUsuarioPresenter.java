package com.br.estimativadeprojetodesoftware.presenter.usuario;

import com.br.estimativadeprojetodesoftware.command.MostrarMensagemProjetoCommand;
import com.br.estimativadeprojetodesoftware.command.ProjetoCommand;
import com.br.estimativadeprojetodesoftware.command.usuario.BotaoEditarUsuarioCommand;
import com.br.estimativadeprojetodesoftware.command.usuario.BotaoExcluirUsuarioCommand;
import com.br.estimativadeprojetodesoftware.command.usuario.BotaoSalvarUsuarioCommand;
import com.br.estimativadeprojetodesoftware.model.Usuario;
import com.br.estimativadeprojetodesoftware.presenter.Observer;
import com.br.estimativadeprojetodesoftware.repository.UsuarioRepositoryMock;
import com.br.estimativadeprojetodesoftware.service.BarraService;
import com.br.estimativadeprojetodesoftware.service.IconService;
import com.br.estimativadeprojetodesoftware.singleton.UsuarioLogadoSingleton;
import com.br.estimativadeprojetodesoftware.state.usuario.ManterUsuarioPresenterState;
import com.br.estimativadeprojetodesoftware.state.usuario.VisualizacaoUsuarioState;
import com.br.estimativadeprojetodesoftware.view.usuario.ManterUsuarioView;
import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JPanel;

/**
 *
 * @author tetzner
 */
public class ManterUsuarioPresenter implements Observer {

    private ManterUsuarioView view;
    private Usuario usuario;
    private UsuarioRepositoryMock repository;
    private ManterUsuarioPresenterState estado;
    private final Map<String, ProjetoCommand> comandos;
    private BarraService barraService;

    public ManterUsuarioPresenter(ManterUsuarioView view) {
        this.view = view;
        this.usuario = UsuarioLogadoSingleton.getInstancia().getUsuario();
        this.repository = new UsuarioRepositoryMock();
        this.repository.addObserver(this);
        this.comandos = inicializarComandos();
        barraService = new BarraService(comandos);
        configuraView();
        this.estado = new VisualizacaoUsuarioState(this);
    }

    private Map<String, ProjetoCommand> inicializarComandos() {
        Map<String, ProjetoCommand> comandos = new HashMap<>();
        comandos.put("Salvar usuário", new BotaoSalvarUsuarioCommand(this));
        comandos.put("Editar usuário", new BotaoEditarUsuarioCommand(this));
        comandos.put("Excluir usuário", new BotaoExcluirUsuarioCommand(this));
        return comandos;
    }

    public void salvar() {
        try {
            estado.salvar();
        } catch (Exception ex) {
            new MostrarMensagemProjetoCommand(ex.getMessage()).execute();
        }
    }

    public void excluir() {
        try {
            estado.excluir();
        } catch (Exception ex) {
            new MostrarMensagemProjetoCommand(ex.getMessage()).execute();
        }
    }

    public void editar() {
        try {
            estado.editar();
        } catch (Exception ex) {
            new MostrarMensagemProjetoCommand(ex.getMessage()).execute();
        }
    }

    private void configuraView() {
        JPanel painelPrincipal = new JPanel(new BorderLayout());
        painelPrincipal.add(barraService.criarBarraUsuario(), BorderLayout.NORTH);
        painelPrincipal.add(view.getContentPane(), BorderLayout.CENTER);
        view.setContentPane(painelPrincipal);
        view.setModal(true);
        view.setResizable(false);
        view.getBtnExibirSenha().addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                view.getBtnExibirSenha().setIcon(IconService.getIcon("olho-exibido"));
                view.getTxtSenhaAtual().setEchoChar('\0');
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                view.getBtnExibirSenha().setIcon(IconService.getIcon("olho"));
                view.getTxtSenhaAtual().setEchoChar('*');
            }
        });
        atualizarCampos();
    }

    public ManterUsuarioView getView() {
        return view;
    }

    public String formatarData(LocalDateTime dataHora) {
        return dataHora.getDayOfMonth() + "/" + String.valueOf(dataHora.getMonthValue()) + "/" + dataHora.getYear();
    }

    public BarraService getBarraService() {
        return barraService;
    }

    public int getQtdProjetos() {
        return usuario.getProjetos().size();
    }

    public int getQtdPerfis() {
        return usuario.getPerfis().size();
    }

    public UsuarioRepositoryMock getRepository() {
        return repository;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public ManterUsuarioPresenterState getEstado() {
        return estado;
    }

    public Map<String, ProjetoCommand> getComandos() {
        return comandos;
    }

    public void setState(ManterUsuarioPresenterState estado) {
        this.estado = estado;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public void atualizarCampos() {
        view.getTxtNome().setText(usuario.getNome());
        view.getTxtDataCriacao().setText(formatarData(usuario.getCreated_at()));
        view.getTxtEmail().setText(usuario.getEmail());
        view.getTxtSenhaAtual().setText(usuario.getSenha());
        view.getTxtTotalProjetos().setText(String.valueOf(getQtdProjetos()));
        view.getTxtTotalPerfis().setText(String.valueOf(getQtdPerfis()));
    }

    @Override
    public void update() {
        atualizarCampos();
    }

}
