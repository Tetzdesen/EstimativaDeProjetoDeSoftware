package com.br.estimativadeprojetodesoftware.presenter.usuario;

import static com.br.estimativadeprojetodesoftware.suporte.AmbienteTeste.naThreadDoSwing;
import static org.junit.jupiter.api.Assertions.*;

import com.br.estimativadeprojetodesoftware.adapter.senha.ValidadorSenhaAdapter;
import com.br.estimativadeprojetodesoftware.repository.jdbc.JdbcUsuarioRepository;
import com.br.estimativadeprojetodesoftware.seeder.UsuarioSeeder;
import com.br.estimativadeprojetodesoftware.service.UsuarioService;
import com.br.estimativadeprojetodesoftware.singleton.UsuarioLogadoSingleton;
import com.br.estimativadeprojetodesoftware.suporte.AmbienteTeste;
import com.br.estimativadeprojetodesoftware.suporte.BancoTeste;
import com.br.estimativadeprojetodesoftware.view.tema.TemaClaro;
import com.br.estimativadeprojetodesoftware.view.usuario.LoginView;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class LoginPresenterTest {

    @TempDir
    Path dir;

    private AmbienteTeste ambiente;
    private UsuarioService usuarios;
    private LoginView view;
    private final AtomicInteger entradas = new AtomicInteger();

    @BeforeAll
    static void tema() {
        TemaClaro.setup();
    }

    @BeforeEach
    void preparar() throws Exception {
        ambiente = new AmbienteTeste();
        usuarios = new UsuarioService(new JdbcUsuarioRepository(BancoTeste.abrir("H2", dir)), new ValidadorSenhaAdapter(),
                new AmbienteTeste.Publicador());
        UsuarioLogadoSingleton.getInstancia().logout();
        naThreadDoSwing(() -> {
            view = new LoginView();
            new LoginPresenter(view, null, () -> usuarios, entradas::incrementAndGet);
        });
    }

    @AfterEach
    void limpar() throws Exception {
        naThreadDoSwing(() -> view.dispose());
        UsuarioLogadoSingleton.getInstancia().logout();
        ambiente.close();
    }

    private void entrar(String email, String senha) throws Exception {
        naThreadDoSwing(() -> {
            view.getTxtEmail().setText(email);
            view.getTxtSenha().setText(senha);
            view.getBtnEntrar().doClick();
        });
    }

    @Test
    void camposVaziosMostramErroSemAutenticar() throws Exception {
        entrar("", "");
        assertNotNull(view.getFormulario().getMensagemErro(view.getTxtEmail()));
        assertNotNull(view.getFormulario().getMensagemErro(view.getTxtSenha()));
        assertEquals(0, entradas.get());
        assertFalse(UsuarioLogadoSingleton.getInstancia().usuarioLogado());
    }

    @Test
    void senhaErradaMostraErroGeral() throws Exception {
        entrar(UsuarioSeeder.EMAIL_ADMIN, "senha-errada");
        assertTrue(view.getLblErroGeral().getText().contains("inválidos"), view.getLblErroGeral().getText());
        assertEquals(0, entradas.get());
        assertFalse(view.isOcupado(), "volta a aceitar novas tentativas");
        assertTrue(view.getBtnEntrar().isEnabled());
    }

    @Test
    void loginCorretoGuardaOUsuarioEAbreASessao() throws Exception {
        entrar(UsuarioSeeder.EMAIL_ADMIN, "A@#bb");
        assertEquals(1, entradas.get());
        assertEquals(UsuarioSeeder.EMAIL_ADMIN, UsuarioLogadoSingleton.getInstancia().getUsuario().getEmail());
    }
}
