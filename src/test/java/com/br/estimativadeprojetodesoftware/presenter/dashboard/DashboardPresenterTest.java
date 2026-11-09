package com.br.estimativadeprojetodesoftware.presenter.dashboard;

import static com.br.estimativadeprojetodesoftware.suporte.AmbienteTeste.naThreadDoSwing;
import static org.junit.jupiter.api.Assertions.*;

import com.br.estimativadeprojetodesoftware.model.Projeto;
import com.br.estimativadeprojetodesoftware.model.ResultadoEstimativa;
import com.br.estimativadeprojetodesoftware.model.Usuario;
import com.br.estimativadeprojetodesoftware.repository.jdbc.JdbcCompartilhamentoRepository;
import com.br.estimativadeprojetodesoftware.repository.jdbc.JdbcPerfilRepository;
import com.br.estimativadeprojetodesoftware.repository.jdbc.JdbcProjetoRepository;
import com.br.estimativadeprojetodesoftware.repository.jdbc.JdbcUsuarioRepository;
import com.br.estimativadeprojetodesoftware.service.CalculoEstimativaService;
import com.br.estimativadeprojetodesoftware.service.DashboardService;
import com.br.estimativadeprojetodesoftware.service.ProjetoService;
import com.br.estimativadeprojetodesoftware.service.RegistroOperacoes;
import com.br.estimativadeprojetodesoftware.singleton.UsuarioLogadoSingleton;
import com.br.estimativadeprojetodesoftware.suporte.AmbienteTeste;
import com.br.estimativadeprojetodesoftware.suporte.BancoTeste;
import com.br.estimativadeprojetodesoftware.suporte.Cenarios;
import com.br.estimativadeprojetodesoftware.view.dashboard.DashboardView;
import com.br.estimativadeprojetodesoftware.view.tema.TemaClaro;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.swing.SwingUtilities;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class DashboardPresenterTest {

    @TempDir
    Path dir;

    private AmbienteTeste ambiente;
    private ProjetoService projetos;
    private JdbcProjetoRepository repo;
    private Usuario ana;
    private DashboardView view;
    private final CalculoEstimativaService calculo = new CalculoEstimativaService();

    @BeforeAll
    static void tema() {
        TemaClaro.setup();
    }

    @BeforeEach
    void preparar() {
        ambiente = new AmbienteTeste();
        Connection c = BancoTeste.abrir("H2", dir);
        repo = new JdbcProjetoRepository(c);
        RegistroOperacoes semLog = new RegistroOperacoes() {
            @Override
            public void sucesso(String operacao, String nomeProjeto, String usuario) {
            }

            @Override
            public void falha(String operacao, String nomeProjeto, String usuario, String mensagem) {
            }
        };
        projetos = new ProjetoService(repo, new JdbcPerfilRepository(c), new JdbcCompartilhamentoRepository(c), semLog, calculo,
                new AmbienteTeste.Publicador());
        ana = new Usuario(UUID.randomUUID(), "Ana", "ana@x.com", "hash", LocalDateTime.now());
        new JdbcUsuarioRepository(c).salvar(ana);
        UsuarioLogadoSingleton.getInstancia().setUsuario(ana);
    }

    @AfterEach
    void limpar() throws Exception {
        if (view != null) {
            naThreadDoSwing(() -> view.dispose());
        }
        UsuarioLogadoSingleton.getInstancia().logout();
        ambiente.close();
    }

    private void abrirDashboard() throws Exception {
        naThreadDoSwing(() -> {
            view = new DashboardView();
            new DashboardPresenter(view, new DashboardService(projetos, calculo));
        });
    }

    @Test
    void semProjetosMostraZeros() throws Exception {
        abrirDashboard();
        assertEquals("0", view.getLblProprios().getText());
        assertEquals("0", view.getLblCalculados().getText());
        assertTrue(ambiente.erros.isEmpty(), ambiente.erros.toString());
    }

    @Test
    void indicadoresDoProjetoCalculado() throws Exception {
        Projeto p = Cenarios.prototipoPlanilha(ana.getId());
        p.setCalculado(true);
        repo.salvar(p);

        abrirDashboard();

        assertEquals("1", view.getLblProprios().getText());
        assertEquals("1", view.getLblCalculados().getText());
        BigDecimal preco = calculo.calcular(p).getPrecoFinal();
        assertEquals(ResultadoEstimativa.formatarReais(preco), view.getLblPreco().getText());
    }

    @Test
    void atualizaQuandoUmProjetoESalvo() throws Exception {
        abrirDashboard();
        assertEquals("0", view.getLblCalculados().getText());

        Projeto p = Cenarios.prototipoPlanilha(ana.getId());
        projetos.salvarComEstimativa(p, ana); // publica o evento que o dashboard observa
        SwingUtilities.invokeAndWait(() -> {
        });
        SwingUtilities.invokeAndWait(() -> {
        });

        assertEquals("1", view.getLblCalculados().getText());
    }
}
