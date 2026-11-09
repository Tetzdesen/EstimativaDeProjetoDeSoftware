package com.br.estimativadeprojetodesoftware.presenter.perfil;

import static com.br.estimativadeprojetodesoftware.suporte.AmbienteTeste.naThreadDoSwing;
import static org.junit.jupiter.api.Assertions.*;

import com.br.estimativadeprojetodesoftware.model.Perfil;
import com.br.estimativadeprojetodesoftware.model.Plataforma;
import com.br.estimativadeprojetodesoftware.model.Usuario;
import com.br.estimativadeprojetodesoftware.repository.jdbc.JdbcPerfilRepository;
import com.br.estimativadeprojetodesoftware.repository.jdbc.JdbcUsuarioRepository;
import com.br.estimativadeprojetodesoftware.service.PerfilService;
import com.br.estimativadeprojetodesoftware.state.perfil.InclusaoPerfilState;
import com.br.estimativadeprojetodesoftware.state.perfil.VisualizacaoPerfilState;
import com.br.estimativadeprojetodesoftware.suporte.AmbienteTeste;
import com.br.estimativadeprojetodesoftware.suporte.BancoTeste;
import com.br.estimativadeprojetodesoftware.view.tema.TemaClaro;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class ManterPerfilPresenterTest {

    private static final int COLUNA_WEB = 4;

    @TempDir
    Path dir;

    private AmbienteTeste ambiente;
    private JdbcPerfilRepository repo;
    private PerfilService service;
    private Usuario ana;
    private Perfil padrao;
    private ManterPerfilPresenter presenter;

    @BeforeAll
    static void tema() {
        TemaClaro.setup();
    }

    @BeforeEach
    void preparar() {
        ambiente = new AmbienteTeste();
        Connection c = BancoTeste.abrir("H2", dir);
        repo = new JdbcPerfilRepository(c);
        service = new PerfilService(repo, new AmbienteTeste.Publicador());
        ana = new Usuario(UUID.randomUUID(), "Ana", "ana@x.com", "hash", LocalDateTime.now());
        new JdbcUsuarioRepository(c).salvar(ana);
        padrao = service.listarVisiveis(ana).stream().filter(Perfil::isPadrao).findFirst().orElseThrow();
    }

    @AfterEach
    void limpar() throws Exception {
        if (presenter != null) {
            naThreadDoSwing(() -> presenter.getView().dispose());
        }
        ambiente.close();
    }

    private void copiarPadraoESalvar(String nome) throws Exception {
        naThreadDoSwing(() -> {
            presenter = ManterPerfilPresenter.copiar(new ManterPerfilPresenter(service, ana), padrao);
            assertInstanceOf(InclusaoPerfilState.class, presenter.getEstado());
            presenter.getView().getTxtNome().setText(nome);
            presenter.getView().getBtnSalvar().doClick();
        });
    }

    @Test
    void copiaDoPadraoViraPerfilProprio() throws Exception {
        copiarPadraoESalvar("Meu perfil");

        assertTrue(ambiente.erros.isEmpty(), ambiente.erros.toString());
        assertInstanceOf(VisualizacaoPerfilState.class, presenter.getEstado());
        Perfil salvo = repo.buscarPorId(presenter.getPerfil().getId()).orElseThrow();
        assertEquals("Meu perfil", salvo.getNome());
        assertEquals(ana.getId(), salvo.getDonoId());
        assertEquals(padrao.getFuncionalidades().size(), salvo.getFuncionalidades().size());
    }

    @Test
    void editarValorEGravar() throws Exception {
        copiarPadraoESalvar("Para editar");
        naThreadDoSwing(() -> {
            presenter.getView().getBtnEditar().doClick();
            presenter.getView().getTabelaFuncionalidades().setValueAt("15", 0, COLUNA_WEB);
            presenter.getView().getBtnSalvar().doClick();
        });
        assertTrue(ambiente.erros.isEmpty(), ambiente.erros.toString());
        Perfil salvo = repo.buscarPorId(presenter.getPerfil().getId()).orElseThrow();
        assertEquals(0, new BigDecimal("15").compareTo(salvo.getFuncionalidades().get(0).getValor(Plataforma.WEB_BACKEND).orElseThrow()));
    }

    @Test
    void nomeVazioNaoGrava() throws Exception {
        int antes = service.listarVisiveis(ana).size();
        copiarPadraoESalvar("  ");
        assertNotNull(presenter.getView().getFormulario().getMensagemErro(presenter.getView().getTxtNome()));
        assertEquals(antes, service.listarVisiveis(ana).size());
    }

    @Test
    void excluirPerfilProprioComConfirmacao() throws Exception {
        copiarPadraoESalvar("Descartável");
        UUID id = presenter.getPerfil().getId();
        naThreadDoSwing(() -> presenter.getView().getBtnExcluir().doClick());
        assertEquals(1, ambiente.perguntas.size());
        assertTrue(repo.buscarPorId(id).isEmpty());
        assertTrue(presenter.getView().isClosed());
    }

    @Test
    void perfilPadraoNaoPodeSerExcluido() throws Exception {
        naThreadDoSwing(() -> presenter = ManterPerfilPresenter.abrir(new ManterPerfilPresenter(service, ana), padrao.getId()));
        assertInstanceOf(VisualizacaoPerfilState.class, presenter.getEstado());
        naThreadDoSwing(() -> {
            if (presenter.getView().getBtnExcluir().isEnabled()) {
                presenter.getView().getBtnExcluir().doClick();
            }
        });
        assertTrue(repo.buscarPorId(padrao.getId()).isPresent(), "o perfil padrão continua existindo");
    }
}
