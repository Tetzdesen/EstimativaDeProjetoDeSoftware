package com.br.estimativadeprojetodesoftware.exportacao;

import static org.junit.jupiter.api.Assertions.*;

import com.br.estimativadeprojetodesoftware.model.Projeto;
import com.br.estimativadeprojetodesoftware.model.Usuario;
import com.br.estimativadeprojetodesoftware.service.CalculoEstimativaService;
import com.br.estimativadeprojetodesoftware.service.ExportacaoService;
import com.br.estimativadeprojetodesoftware.suporte.Cenarios;
import com.br.estimativadeprojetodesoftware.suporte.LogFalso;
import com.br.estimativadeprojetodesoftware.templatemethod.ExportadorFactory;
import com.br.estimativadeprojetodesoftware.templatemethod.ExportarCSVTemplate;
import com.br.estimativadeprojetodesoftware.templatemethod.ExportarPDFTemplate;
import com.br.estimativadeprojetodesoftware.templatemethod.FormatoExportacao;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.UUID;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/** 10.1–10.3: relatório completo em PDF e CSV, bloqueios e log. */
class ExportacaoTest {

    @TempDir
    Path dir;

    private Usuario dono;
    private Projeto projeto;
    private LogFalso log;
    private ExportacaoService service;

    @BeforeEach
    void preparar() {
        dono = new Usuario(UUID.randomUUID(), "Ana", "ana@x.com", "h", LocalDateTime.now());
        projeto = Cenarios.prototipoPlanilha(dono.getId());
        projeto.setNome("Protótipo Tabela 1");
        projeto.setDonoNome("Ana");
        log = new LogFalso();
        service = new ExportacaoService(new CalculoEstimativaService(), log);
    }

    @Test
    void fabricaCriaUmExportadorPorFormato() {
        assertInstanceOf(ExportarPDFTemplate.class, ExportadorFactory.criar(FormatoExportacao.PDF));
        assertInstanceOf(ExportarCSVTemplate.class, ExportadorFactory.criar(FormatoExportacao.CSV));
    }

    @Test
    void projetoNaoCalculadoERecusado() {
        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> service.exportar(projeto, FormatoExportacao.PDF, dir.resolve("x"), dono));
        assertTrue(e.getMessage().contains("precisa ser calculada"));
        assertTrue(log.ultimo().startsWith("FALHA exportação em PDF | Protótipo Tabela 1 | Ana"));
        assertFalse(Files.exists(dir.resolve("x.pdf")));
    }

    @Test
    void projetoCompartilhadoNaoPodeSerExportadoPeloDestinatario() {
        projeto.setCalculado(true);
        Usuario destinatario = new Usuario(UUID.randomUUID(), "Beto", "b@x.com", "h", LocalDateTime.now());
        assertThrows(IllegalStateException.class,
                () -> service.exportar(projeto, FormatoExportacao.CSV, dir.resolve("x"), destinatario));
    }

    @Test
    void csvTemTodasAsSecoesDoRelatorio() throws Exception {
        projeto.setCalculado(true);
        Path arquivo = service.exportar(projeto, FormatoExportacao.CSV, dir.resolve("relatorio"), dono);
        assertEquals(dir.resolve("relatorio.csv"), arquivo);
        assertEquals("OK exportação em CSV | Protótipo Tabela 1 | Ana", log.ultimo());
        String csv = Files.readString(arquivo, StandardCharsets.UTF_8);
        for (String esperado : new String[]{
            "FUNCIONALIDADES SELECIONADAS", "Usuários e Contas;Cadastro pelo Facebook;2.00;2.00",
            "DIAS E CUSTOS POR PLATAFORMA", "Web/Back-end;50.00;23.33;46.00;10.00;129.33;59033.33",
            "Android;50.00;23.33;28.00;10.00;111.33;50933.33",
            "RESUMO POR CATEGORIA", "Nível de UI;46.67;25666.67",
            "CUSTOS ADICIONAIS", "Fundo de reserva;2000.00",
            "Impostos (%);42.00", "Lucro (%);30.00", "Preço final ao cliente (R$);212597.67",
            "Prazo (dias);241", "Prazo (meses);8.0", "Média mensal (R$);26501.09"}) {
            assertTrue(csv.contains(esperado), "CSV deveria conter: " + esperado + "\n" + csv);
        }
    }

    @Test
    void pdfTemOsValoresDoCasoDeAceitacao() throws Exception {
        projeto.setCalculado(true);
        Path arquivo = service.exportar(projeto, FormatoExportacao.PDF, dir.resolve("relatorio.pdf"), dono);
        assertEquals("OK exportação em PDF | Protótipo Tabela 1 | Ana", log.ultimo());
        String texto;
        try (PDDocument doc = PDDocument.load(arquivo.toFile())) {
            texto = new PDFTextStripper().getText(doc);
        }
        for (String esperado : new String[]{"Protótipo Tabela 1", "Funcionalidades selecionadas", "Cadastro pelo Facebook",
            "Web/Back-end: 129,33 dias", "R$ 59.033,33", "Android: 111,33 dias", "R$ 50.933,33", "Resumo por categoria",
            "Custos adicionais", "Fundo de reserva: R$ 2.000,00", "Impostos (42,00%): R$ 48.370,00",
            "Lucro (30,00%): R$ 49.061,00", "Preço final ao cliente: R$ 212.597,67", "Prazo: 241 dias (8,0 meses)",
            "Média mensal: R$ 26.501,09"}) {
            assertTrue(texto.contains(esperado), "PDF deveria conter: " + esperado + "\n" + texto);
        }
    }
}
