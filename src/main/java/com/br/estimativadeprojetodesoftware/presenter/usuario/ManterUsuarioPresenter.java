package com.br.estimativadeprojetodesoftware.presenter.usuario;

import com.br.estimativadeprojetodesoftware.command.ExibirMensagemCommand;
import com.br.estimativadeprojetodesoftware.command.usuario.AcionarBotaoEditarUsuarioCommand;
import com.br.estimativadeprojetodesoftware.command.usuario.AcionarBotaoExcluirUsuarioCommand;
import com.br.estimativadeprojetodesoftware.command.usuario.AcionarBotaoSalvarUsuarioCommand;
import com.br.estimativadeprojetodesoftware.model.Usuario;
import com.br.estimativadeprojetodesoftware.observer.Observer;
import com.br.estimativadeprojetodesoftware.presenter.helpers.ValidacaoVisual;
import com.br.estimativadeprojetodesoftware.service.DataHoraService;
import com.br.estimativadeprojetodesoftware.observer.EventoDominio;
import com.br.estimativadeprojetodesoftware.service.PerfilService;
import com.br.estimativadeprojetodesoftware.service.ProjetoService;
import com.br.estimativadeprojetodesoftware.service.UsuarioService;
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
import com.br.estimativadeprojetodesoftware.command.Command;

/**
 *
 * @author tetzner
 */
public class ManterUsuarioPresenter implements Observer {

    private final ManterUsuarioView view;
    private Usuario usuario;
    private final UsuarioService usuarioService;
    private final PerfilService perfilService;
    private final ProjetoService projetoService;
    private ManterUsuarioPresenterState estado;
    private final Map<String, Command> comandos;

    public ManterUsuarioPresenter(UsuarioService usuarioService, ManterUsuarioView view) {
        this.view = view;
        this.usuario = UsuarioLogadoSingleton.getInstancia().getUsuario();
        this.usuarioService = usuarioService;
        this.perfilService = new PerfilService();
        this.projetoService = new ProjetoService();
        this.usuarioService.addObserver(this);
        this.comandos = inicializarComandos();
        configuraView();
        this.estado = new VisualizacaoUsuarioState(this);
    }

    private Map<String, Command> inicializarComandos() {
        Map<String, Command> comandos = new HashMap<>();
        comandos.put("Salvar usuário", new AcionarBotaoSalvarUsuarioCommand(this));
        comandos.put("Editar usuário", new AcionarBotaoEditarUsuarioCommand(this));
        comandos.put("Excluir usuário", new AcionarBotaoExcluirUsuarioCommand(this));
        return comandos;
    }

    public void salvar() {
        view.limparErros();
        boolean ok = true;
        if (ValidacaoVisual.vazio(view.getTxtNome())) {
            view.mostrarErroCampo(view.getTxtNome(), "Informe o nome.");
            ok = false;
        }
        String senha = new String(view.getTxtSenhaAtual().getPassword());
        if (!senha.isBlank()) {
            try {
                usuarioService.validarSenha(senha);
            } catch (IllegalArgumentException e) {
                view.mostrarErroCampo(view.getTxtSenhaAtual(), e.getMessage().replace("A senha não atende às regras:\n", ""));
                ok = false;
            }
        }
        if (!ok) {
            return;
        }
        try {
            estado.salvar();
        } catch (Exception ex) {
            ExibirMensagemCommand.erro(ex.getMessage());
        }
    }

    public void excluir() {
        try {
            estado.excluir();
        } catch (Exception ex) {
            ExibirMensagemCommand.erro(ex.getMessage());
        }
    }

    public void editar() {
        try {
            estado.editar();
        } catch (Exception ex) {
            ExibirMensagemCommand.erro(ex.getMessage());
        }
    }

    private void configuraView() {
        view.setModal(true);
        view.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                usuarioService.removeObserver(ManterUsuarioPresenter.this);
            }
        });
        view.getBtnSalvar().addActionListener(e -> comandos.get("Salvar usuário").execute());
        view.getBtnEditar().addActionListener(e -> comandos.get("Editar usuário").execute());
        view.getBtnExcluir().addActionListener(e -> comandos.get("Excluir usuário").execute());
        ValidacaoVisual.limparAoEditar(view.getTxtNome(), view::limparErroCampo);
        ValidacaoVisual.limparAoEditar(view.getTxtSenhaAtual(), view::limparErroCampo);
        atualizarCampos();
    }

    public ManterUsuarioView getView() {
        return view;
    }

    public String formatarData(LocalDateTime dataHora) {
        return dataHora.getDayOfMonth() + "/" + String.valueOf(dataHora.getMonthValue()) + "/" + dataHora.getYear();
    }


    public int getQtdProjetos() {
        return projetoService.listarProprios(usuario).size();
    }

    public long getQtdPerfis() {
        return perfilService.contarDoUsuario(usuario);
    }

    public UsuarioService getUsuarioService() {
        return usuarioService;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public ManterUsuarioPresenterState getEstado() {
        return estado;
    }

    public Map<String, Command> getComandos() {
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
        view.getTxtDataCriacao().setText(DataHoraService.formatarData(usuario.getCreated_at().toLocalDate()));
        view.getTxtEmail().setText(usuario.getEmail());
        // A senha fica guardada como hash: o campo começa vazio e só é
        // enviado quando o usuário digita uma senha nova.
        view.getTxtSenhaAtual().setText("");
        view.getTxtTotalProjetos().setText(String.valueOf(getQtdProjetos()));
        view.getTxtTotalPerfis().setText(String.valueOf(getQtdPerfis()));
    }

    @Override
    public void update(EventoDominio evento) {
        if (usuario != null && view.isDisplayable()) {
            atualizarCampos();
        }
    }

}
