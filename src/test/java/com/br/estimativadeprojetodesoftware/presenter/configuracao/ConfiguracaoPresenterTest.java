package com.br.estimativadeprojetodesoftware.presenter.configuracao;

import static com.br.estimativadeprojetodesoftware.suporte.AmbienteTeste.naThreadDoSwing;
import static org.junit.jupiter.api.Assertions.*;

import com.br.estimativadeprojetodesoftware.model.Tema;
import com.br.estimativadeprojetodesoftware.repository.jdbc.JdbcConfiguracaoRepository;
import com.br.estimativadeprojetodesoftware.service.LogService;
import com.br.estimativadeprojetodesoftware.service.PreferenciasService;
import com.br.estimativadeprojetodesoftware.suporte.AmbienteTeste;
import com.br.estimativadeprojetodesoftware.suporte.BancoTeste;
import com.br.estimativadeprojetodesoftware.view.configuracao.ConfiguracaoView;
import com.br.estimativadeprojetodesoftware.view.tema.TemaClaro;
import com.github.tetzdesen.estimativalog.FormatoLog;
import com.br.estimativadeprojetodesoftware.model.Usuario;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.UUID;
import java.sql.Connection;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class ConfiguracaoPresenterTest {

    @TempDir
    Path dir;

    private AmbienteTeste ambiente;
    private Connection conexao;
    private LogService log;
    private ConfiguracaoView view;
    private final Usuario ana = new Usuario(UUID.randomUUID(), "Ana", "ana@x.com", "hash", LocalDateTime.now());

    @BeforeEach
    void preparar() throws Exception {
        TemaClaro.setup();
        ambiente = new AmbienteTeste();
        conexao = BancoTeste.abrir("H2", dir);
        log = new LogService(new JdbcConfiguracaoRepository(conexao), dir.resolve("log"));
        naThreadDoSwing(() -> {
            view = new ConfiguracaoView();
            new ConfiguracaoPresenter(view, log, new PreferenciasService(new JdbcConfiguracaoRepository(conexao)), ana);
        });
    }

    @AfterEach
    void limpar() throws Exception {
        naThreadDoSwing(() -> {
            view.dispose();
            TemaClaro.setup(); // os outros testes esperam o tema claro
        });
        ambiente.close();
    }

    @Test
    void mostraAConfiguracaoAtual() {
        assertTrue(view.getRbClaro().isSelected());
        assertEquals(log.getFormato() == FormatoLog.CSV, view.getRbCsv().isSelected());
        assertTrue(view.getLblArquivo().getText().contains(dir.toString()));
    }

    @Test
    void salvarTrocaOFormatoDoLogEOTemaEGuarda() throws Exception {
        naThreadDoSwing(() -> {
            view.getRbJson().setSelected(true);
            view.getRbEscuro().setSelected(true);
            view.getBtnSalvar().doClick();
        });
        assertTrue(ambiente.erros.isEmpty(), ambiente.erros.toString());
        assertEquals(FormatoLog.JSON, log.getFormato());
        PreferenciasService prefs = new PreferenciasService(new JdbcConfiguracaoRepository(conexao));
        assertEquals(Tema.ESCURO, prefs.getTema(ana), "guardado para a Ana");
        Usuario beto = new Usuario(UUID.randomUUID(), "Beto", "beto@x.com", "hash", LocalDateTime.now());
        prefs.setTema(beto, Tema.CLARO);
        assertEquals(Tema.ESCURO, prefs.getTema(ana), "outro usuário escolher o claro não muda o da Ana");
        assertTrue(ambiente.sucessos.get(0).contains("JSON"));
    }
}
