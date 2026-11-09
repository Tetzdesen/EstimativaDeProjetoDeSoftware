package com.br.estimativadeprojetodesoftware.presenter.usuario;

import static com.br.estimativadeprojetodesoftware.suporte.AmbienteTeste.naThreadDoSwing;
import static org.junit.jupiter.api.Assertions.*;

import com.br.estimativadeprojetodesoftware.adapter.senha.ValidadorSenhaAdapter;
import com.br.estimativadeprojetodesoftware.repository.jdbc.JdbcUsuarioRepository;
import com.br.estimativadeprojetodesoftware.service.UsuarioService;
import com.br.estimativadeprojetodesoftware.suporte.AmbienteTeste;
import com.br.estimativadeprojetodesoftware.suporte.BancoTeste;
import com.br.estimativadeprojetodesoftware.view.tema.TemaClaro;
import com.br.estimativadeprojetodesoftware.view.usuario.CadastroUsuarioView;
import java.awt.Frame;
import java.nio.file.Path;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class CadastroUsuarioPresenterTest {

    private static final String SENHA_FORTE = "Est#2026xY";

    @TempDir
    Path dir;

    private AmbienteTeste ambiente;
    private JdbcUsuarioRepository repo;
    private CadastroUsuarioView view;
    private CadastroUsuarioPresenter presenter;

    @BeforeAll
    static void tema() {
        TemaClaro.setup();
    }

    @BeforeEach
    void preparar() throws Exception {
        ambiente = new AmbienteTeste();
        repo = new JdbcUsuarioRepository(BancoTeste.abrir("H2", dir));
        UsuarioService usuarios = new UsuarioService(repo, new ValidadorSenhaAdapter(), new AmbienteTeste.Publicador());
        naThreadDoSwing(() -> {
            view = new CadastroUsuarioView();
            presenter = new CadastroUsuarioPresenter(view, null, usuarios);
        });
    }

    @AfterEach
    void limpar() throws Exception {
        naThreadDoSwing(() -> {
            for (Frame f : Frame.getFrames()) {
                f.dispose(); // fecha também o login aberto depois do cadastro
            }
        });
        ambiente.close();
    }

    private void preencher(String nome, String email, String senha, String confirmacao) throws Exception {
        naThreadDoSwing(() -> {
            view.getTxtNomeUsuario().setText(nome);
            view.getTxtEmail().setText(email);
            view.getTxtSenha().setText(senha);
            view.getTxtConfirmarSenha().setText(confirmacao);
            presenter.salvar();
        });
    }

    private String erro(javax.swing.JComponent campo) {
        return view.getFormulario().getMensagemErro(campo);
    }

    @Test
    void validaCadaCampoSemGravar() throws Exception {
        int antes = repo.buscarTodos().size();
        preencher("", "sem-arroba", "abc", "xyz");
        assertEquals("Informe o nome.", erro(view.getTxtNomeUsuario()));
        assertTrue(erro(view.getTxtEmail()).contains("e-mail válido"));
        assertNotNull(erro(view.getTxtSenha()), "senha fraca");
        assertEquals("As senhas não conferem.", erro(view.getTxtConfirmarSenha()));
        assertEquals(antes, repo.buscarTodos().size());
    }

    @Test
    void cadastraUsuarioComSenhaGuardadaComoHash() throws Exception {
        preencher("Bia", "bia@exemplo.com", SENHA_FORTE, SENHA_FORTE);
        assertNull(erro(view.getTxtSenha()), "senha recusada: " + erro(view.getTxtSenha()));
        assertTrue(ambiente.erros.isEmpty(), ambiente.erros.toString());
        var bia = repo.buscarPorEmail("bia@exemplo.com").orElseThrow();
        assertEquals("Bia", bia.getNome());
        assertNotEquals(SENHA_FORTE, bia.getSenha(), "a senha não é guardada em texto");
        assertTrue(ambiente.erros.isEmpty(), ambiente.erros.toString());
    }

    @Test
    void emailRepetidoApareceNoCampo() throws Exception {
        preencher("Bia", "bia@exemplo.com", SENHA_FORTE, SENHA_FORTE);
        naThreadDoSwing(() -> {
            view = new CadastroUsuarioView();
            presenter = new CadastroUsuarioPresenter(view, null,
                    new UsuarioService(repo, new ValidadorSenhaAdapter(), new AmbienteTeste.Publicador()));
        });
        preencher("Outra Bia", "bia@exemplo.com", SENHA_FORTE, SENHA_FORTE);
        assertTrue(erro(view.getTxtEmail()).contains("já está cadastrado"), erro(view.getTxtEmail()));
    }
}
