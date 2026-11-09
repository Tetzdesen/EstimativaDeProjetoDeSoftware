package com.br.estimativadeprojetodesoftware.presenter.usuario;

import com.br.estimativadeprojetodesoftware.presenter.helpers.FilaBanco;
import com.br.estimativadeprojetodesoftware.command.AbrirPrincipalPresenterCommand;
import com.br.estimativadeprojetodesoftware.command.usuario.AbrirCadastroUsuarioCommand;
import com.br.estimativadeprojetodesoftware.command.usuario.RealizarAutenticacaoUsuarioCommand;
import com.br.estimativadeprojetodesoftware.presenter.helpers.ValidacaoVisual;
import com.br.estimativadeprojetodesoftware.presenter.window_command.FecharJanelaCommand;
import com.br.estimativadeprojetodesoftware.singleton.UsuarioLogadoSingleton;
import com.br.estimativadeprojetodesoftware.view.usuario.LoginView;
import javax.swing.SwingUtilities;

/** Login: valida os campos, autentica e abre a janela principal. */
public class LoginPresenter {

    private final LoginView view;
    private final PrincipalUsuarioPresenter principalUsuarioPresenter;
    private final java.util.function.Supplier<com.br.estimativadeprojetodesoftware.service.UsuarioService> usuarios;
    private final Runnable aoAutenticar;

    public LoginPresenter(LoginView view, PrincipalUsuarioPresenter principalUsuarioPresenter) {
        this(view, principalUsuarioPresenter, com.br.estimativadeprojetodesoftware.service.UsuarioService::new, null);
    }

    /**
     * @param usuarios cria o serviço de usuários (na fila do banco, fora da thread do Swing)
     * @param aoAutenticar o que fazer depois do login; {@code null} abre a janela principal
     */
    public LoginPresenter(LoginView view, PrincipalUsuarioPresenter principalUsuarioPresenter,
            java.util.function.Supplier<com.br.estimativadeprojetodesoftware.service.UsuarioService> usuarios, Runnable aoAutenticar) {
        this.principalUsuarioPresenter = principalUsuarioPresenter;
        this.usuarios = usuarios;
        this.aoAutenticar = aoAutenticar != null ? aoAutenticar : this::abrirJanelaPrincipal;
        this.view = view;
        view.getBtnEntrar().addActionListener(e -> entrar());
        view.getBtnCriarConta().addActionListener(e -> {
            view.dispose();
            new AbrirCadastroUsuarioCommand(principalUsuarioPresenter).execute();
        });
        ValidacaoVisual.limparAoEditar(view.getTxtEmail(), c -> { view.limparErroCampo(c); view.mostrarErroGeral(null); });
        ValidacaoVisual.limparAoEditar(view.getTxtSenha(), c -> { view.limparErroCampo(c); view.mostrarErroGeral(null); });
        SwingUtilities.invokeLater(() -> view.setVisible(true));
    }

    /** Valida o preenchimento, autentica e abre a janela principal; erros aparecem no formulário. */
    public void entrar() {
        view.limparErros();
        boolean ok = true;
        if (ValidacaoVisual.vazio(view.getTxtEmail())) {
            view.mostrarErroCampo(view.getTxtEmail(), "Informe o e-mail.");
            ok = false;
        }
        if (view.getTxtSenha().getPassword().length == 0) {
            view.mostrarErroCampo(view.getTxtSenha(), "Informe a senha.");
            ok = false;
        }
        if (!ok) {
            return;
        }
        String email = view.getTxtEmail().getText();
        String senha = new String(view.getTxtSenha().getPassword());
        FilaBanco.executar(view, "Entrando…", () -> {
            new RealizarAutenticacaoUsuarioCommand(usuarios.get(), email, senha).execute();
            return null;
        }, autenticado -> {
            if (UsuarioLogadoSingleton.getInstancia().getUsuario() != null) {
                aoAutenticar.run();
            }
        }, ex -> view.mostrarErroGeral(ex.getMessage()));
    }

    private void abrirJanelaPrincipal() {
        principalUsuarioPresenter.getView().dispose();
        new FecharJanelaCommand(view).execute();
        new AbrirPrincipalPresenterCommand().execute();
    }

    public LoginView getView() {
        return view;
    }
}
