package com.br.estimativadeprojetodesoftware.presenter.usuario;

import com.br.estimativadeprojetodesoftware.command.ExibirMensagemCommand;
import com.br.estimativadeprojetodesoftware.command.usuario.RealizarCadastroUsuarioCommand;
import com.br.estimativadeprojetodesoftware.presenter.helpers.ValidacaoVisual;
import com.br.estimativadeprojetodesoftware.service.UsuarioService;
import com.br.estimativadeprojetodesoftware.view.usuario.CadastroUsuarioView;
import com.br.estimativadeprojetodesoftware.view.usuario.LoginView;
import java.util.regex.Pattern;
import javax.swing.SwingUtilities;

/**
 * Cadastro de usuário: valida cada campo e mostra o problema junto dele
 * (obrigatórios, e-mail, senhas iguais, regras do validador e e-mail duplicado).
 */
public class CadastroUsuarioPresenter {

    private static final Pattern EMAIL = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

    private final CadastroUsuarioView view;
    private final PrincipalUsuarioPresenter principalUsuarioPresenter;
    private final UsuarioService usuarioService;

    public CadastroUsuarioPresenter(CadastroUsuarioView view, PrincipalUsuarioPresenter principalUsuarioPresenter) {
        this(view, principalUsuarioPresenter, new UsuarioService());
    }

    public CadastroUsuarioPresenter(CadastroUsuarioView view, PrincipalUsuarioPresenter principal, UsuarioService usuarioService) {
        this.view = view;
        this.principalUsuarioPresenter = principal;
        this.usuarioService = usuarioService;
        view.getBtnSalvar().addActionListener(e -> salvar());
        view.getBtnCancelar().addActionListener(e -> view.dispose());
        for (var campo : new javax.swing.text.JTextComponent[]{view.getTxtNomeUsuario(), view.getTxtEmail(),
            view.getTxtSenha(), view.getTxtConfirmarSenha()}) {
            ValidacaoVisual.limparAoEditar(campo, view::limparErroCampo);
        }
        view.setLocationRelativeTo(null);
        view.setVisible(true);
    }

    /** Valida e cria a conta; em caso de sucesso abre o login com o e-mail preenchido. */
    public void salvar() {
        view.limparErros();
        String nome = view.getTxtNomeUsuario().getText();
        String email = view.getTxtEmail().getText().trim();
        String senha = new String(view.getTxtSenha().getPassword());
        String confirmacao = new String(view.getTxtConfirmarSenha().getPassword());
        boolean ok = true;
        if (nome.isBlank()) {
            view.mostrarErroCampo(view.getTxtNomeUsuario(), "Informe o nome.");
            ok = false;
        }
        if (email.isEmpty()) {
            view.mostrarErroCampo(view.getTxtEmail(), "Informe o e-mail.");
            ok = false;
        } else if (!EMAIL.matcher(email).matches()) {
            view.mostrarErroCampo(view.getTxtEmail(), "Informe um e-mail válido (ex.: nome@exemplo.com).");
            ok = false;
        }
        if (senha.isEmpty()) {
            view.mostrarErroCampo(view.getTxtSenha(), "Informe a senha.");
            ok = false;
        } else {
            try {
                usuarioService.validarSenha(senha);
            } catch (IllegalArgumentException e) {
                view.mostrarErroCampo(view.getTxtSenha(), e.getMessage().replace("A senha não atende às regras:\n", ""));
                ok = false;
            }
        }
        if (confirmacao.isEmpty()) {
            view.mostrarErroCampo(view.getTxtConfirmarSenha(), "Repita a senha.");
            ok = false;
        } else if (!senha.isEmpty() && !senha.equals(confirmacao)) {
            view.mostrarErroCampo(view.getTxtConfirmarSenha(), "As senhas não conferem.");
            ok = false;
        }
        if (!ok) {
            return;
        }
        try {
            new RealizarCadastroUsuarioCommand(usuarioService, nome, email, senha, confirmacao).execute();
        } catch (IllegalArgumentException e) {
            if (e.getMessage() != null && e.getMessage().contains("já está cadastrado")) {
                view.mostrarErroCampo(view.getTxtEmail(), e.getMessage());
            } else {
                ExibirMensagemCommand.erro(e.getMessage());
            }
            return;
        } catch (RuntimeException e) {
            ExibirMensagemCommand.erro(e.getMessage());
            return;
        }
        view.dispose();
        LoginView login = new LoginView();
        login.getTxtEmail().setText(email);
        login.setLocationRelativeTo(null);
        new LoginPresenter(login, principalUsuarioPresenter);
        SwingUtilities.invokeLater(() -> {
            login.getTxtSenha().requestFocusInWindow();
            ExibirMensagemCommand.sucesso("Conta criada! Digite a senha para entrar.");
        });
    }

    public CadastroUsuarioView getView() {
        return view;
    }
}
