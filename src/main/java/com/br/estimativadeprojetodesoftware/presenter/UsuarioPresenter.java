package com.br.estimativadeprojetodesoftware.presenter;

import com.br.estimativadeprojetodesoftware.command.EditarUsuarioCommand;
import com.br.estimativadeprojetodesoftware.command.ExcluirUsuarioCommand;
import com.br.estimativadeprojetodesoftware.command.MostrarMensagemProjetoCommand;
import com.br.estimativadeprojetodesoftware.command.ProjetoCommand;
import com.br.estimativadeprojetodesoftware.command.SalvarUsuarioCommand;
import com.br.estimativadeprojetodesoftware.model.Usuario;
import com.br.estimativadeprojetodesoftware.repository.UsuarioRepositoryMock;
import com.br.estimativadeprojetodesoftware.service.BarraService;
import com.br.estimativadeprojetodesoftware.service.IconService;
import com.br.estimativadeprojetodesoftware.singleton.UsuarioLogadoSingleton;
import com.br.estimativadeprojetodesoftware.state.UsuarioPresenterState;
import com.br.estimativadeprojetodesoftware.state.VisualizacaoState;
import com.br.estimativadeprojetodesoftware.view.UsuarioView;
import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JPanel;
import javax.swing.JToolBar;

/**
 *
 * @author tetzner
 */
public class UsuarioPresenter implements Observer {

    private UsuarioView view;
    private Usuario usuario;
    private UsuarioRepositoryMock repository;
    private UsuarioPresenterState estado;
    private PrincipalPresenter principalPresenter;
    private final Map<String, ProjetoCommand> comandos;

    public UsuarioPresenter(PrincipalPresenter principalPresenter) {
        this.view = new UsuarioView();
        this.usuario = UsuarioLogadoSingleton.getInstancia().getUsuario();
        this.principalPresenter = principalPresenter;
        this.repository = principalPresenter.getUsuarioRepository();
        this.repository.addObserver(this);
        this.comandos = inicializarComandos();
        this.estado = new VisualizacaoState(this);
        configuraView();
    }

    private Map<String, ProjetoCommand> inicializarComandos() {
        Map<String, ProjetoCommand> comandos = new HashMap<>();
        comandos.put("Salvar usuário", new SalvarUsuarioCommand(this));
        comandos.put("Editar usuário", new EditarUsuarioCommand(this));
        comandos.put("Excluir usuário", new ExcluirUsuarioCommand(this));
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

        JToolBar toolbar = new BarraService(this.getComandos()).criarBarraUsuario();
        painelPrincipal.add(toolbar, BorderLayout.NORTH);
        painelPrincipal.add(view.getContentPane(), BorderLayout.CENTER);
        view.setContentPane(painelPrincipal);

        view.setClosable(true);
        view.setIconifiable(true);
        view.setResizable(false);
        view.setMaximizable(false);

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

    public UsuarioView getView() {
        return view;
    }

    public PrincipalPresenter getPrincipalPresenter() {
        return principalPresenter;
    }

    public String formatarData(LocalDateTime dataHora) {
        return dataHora.getDayOfMonth() + "/" + String.valueOf(dataHora.getMonthValue()) + "/" + dataHora.getYear();
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

    public UsuarioPresenterState getEstado() {
        return estado;
    }

    public Map<String, ProjetoCommand> getComandos() {
        return comandos;
    }

    public void setState(UsuarioPresenterState estado) {
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
