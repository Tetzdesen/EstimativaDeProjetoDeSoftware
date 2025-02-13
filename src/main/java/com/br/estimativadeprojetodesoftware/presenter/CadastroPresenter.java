package com.br.estimativadeprojetodesoftware.presenter;

import com.br.estimativadeprojetodesoftware.command.MostrarMensagemProjetoCommand;
import com.br.estimativadeprojetodesoftware.model.Usuario;
import com.br.estimativadeprojetodesoftware.repository.UsuarioRepositoryMock;
import com.br.estimativadeprojetodesoftware.service.ValidadorSenhaService;
import com.br.estimativadeprojetodesoftware.view.CadastroView;
import java.util.Optional;

/**
 *
 * @author tetzner
 */
public class CadastroPresenter {

    private CadastroView view;
    private UsuarioRepositoryMock repositoryUsuario;
    private ValidadorSenhaService validadorDeSenha;

    public CadastroPresenter(UsuarioRepositoryMock repositoryUsuario) {
        this.view = new CadastroView();
        this.repositoryUsuario = repositoryUsuario;
        this.validadorDeSenha = new ValidadorSenhaService();
        configuraView();
    }

    private void configuraView() {
        view.setResizable(false);
        view.setLocationRelativeTo(null);
        configuraActionsListerns();
        view.setVisible(true);
    }

    private void configuraActionsListerns() {
        view.getBtnSalvar().addActionListener(e -> {
            try {
                efetuarCadastro();
            } catch (Exception ex) {
                new MostrarMensagemProjetoCommand(ex.getMessage()).execute();
            }
        });
    }

    private void efetuarCadastro() throws Exception {

        String email = view.getTxtEmail().getText();

        Usuario usuario = repositoryUsuario.getUsuarioPorEmail(email);

        if (usuario == null) {
            String nome = view.getTxtNomeUsuario().getText();
            String senha = new String(view.getTxtSenha().getPassword());
            String senhaConfirmada = new String(view.getTxtConfirmarSenha().getPassword());

            if (camposInvalidos(email, nome, senha, senhaConfirmada)) {
                throw new IllegalArgumentException("Os campos não podem estar vazios");
            }

            if (senha.equals(senhaConfirmada)) {
                // usar o validador
                try {
                    if (validadorDeSenha.validarSenha(senha)) {
                        repositoryUsuario.adicionarUsuario(nome, email, senha);

                        view.dispose();
                    }
                } catch (Exception ex) {
                    throw new Exception("Erro na validação da senha:\n" + ex.getMessage());
                }

            } else {
                throw new IllegalArgumentException("Senhas não conferem");
            }
        } else {
            throw new IllegalArgumentException("Usuário já cadastrado no sistema, por favor cadastrar outro usuário");
        }
    }

    public CadastroView getView() {
        return view;
    }

    private boolean camposInvalidos(String email, String nome, String senha, String senhaConfirmada) {
        return email == null || email.trim().isEmpty() || senha == null || senha.trim().isEmpty() || nome == null || nome.trim().isEmpty() || senhaConfirmada == null || senhaConfirmada.isEmpty();
    }
}
