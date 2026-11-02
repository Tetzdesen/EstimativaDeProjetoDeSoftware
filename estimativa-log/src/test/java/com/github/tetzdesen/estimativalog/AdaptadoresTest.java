package com.github.tetzdesen.estimativalog;

import static org.junit.jupiter.api.Assertions.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class AdaptadoresTest {

    private static final LocalDateTime QUANDO = LocalDateTime.of(2026, 11, 2, 14, 30, 0);

    @TempDir
    Path dir;

    @Test
    void csvSeparaCamposPorPontoEVirgula() throws Exception {
        Logger logger = LoggerFactory.criar(FormatoLog.CSV, dir.resolve("logs/estimativa"));
        logger.registrar(new LogRegistro("Criação", "App", "Ana", null, QUANDO));
        logger.registrar(new LogRegistro("exportação", "App", "Ana", "disco cheio", QUANDO));

        Path arquivo = dir.resolve("logs/estimativa.csv");
        List<String> linhas = Files.readAllLines(arquivo, StandardCharsets.UTF_8);
        assertEquals(3, linhas.size());
        assertEquals(CsvLoggerAdapter.CABECALHO, linhas.get(0));
        assertEquals("02/11/2026;14:30:00;Ana;Criação;App;SUCESSO;;Criação: App, (02/11/2026, 14:30:00, e Ana)",
                linhas.get(1));
        String[] campos = linhas.get(2).split(";");
        assertEquals(8, campos.length);
        assertEquals("FALHA", campos[5]);
        assertEquals("disco cheio", campos[6]);
    }

    @Test
    void csvProtegeCampoComPontoEVirgula() throws Exception {
        Logger logger = LoggerFactory.criar(FormatoLog.CSV, dir.resolve("log"));
        logger.registrar(new LogRegistro("Criação", "App; v2", "Ana", null, QUANDO));
        String linha = Files.readAllLines(dir.resolve("log.csv")).get(1);
        assertTrue(linha.contains("\"App; v2\""));
    }

    @Test
    void jsonGeraArrayValido() throws Exception {
        Logger logger = LoggerFactory.criar(FormatoLog.JSON, dir.resolve("estimativa"));
        logger.registrar(new LogRegistro("Criação", "App", "Ana", null, QUANDO));
        logger.registrar(new LogRegistro("exclusão", "App", "Ana", "erro X", QUANDO));

        String conteudo = Files.readString(dir.resolve("estimativa.json"), StandardCharsets.UTF_8);
        JsonArray array = JsonParser.parseString(conteudo).getAsJsonArray();
        assertEquals(2, array.size());
        JsonObject primeiro = array.get(0).getAsJsonObject();
        assertEquals("Criação: App, (02/11/2026, 14:30:00, e Ana)", primeiro.get("mensagem").getAsString());
        assertEquals("SUCESSO", primeiro.get("status").getAsString());
        JsonObject segundo = array.get(1).getAsJsonObject();
        assertEquals("Ocorreu a falha erro X ao realizar a exclusão do projeto App, (02/11/2026, 14:30:00, e Ana).",
                segundo.get("mensagem").getAsString());
    }

    @Test
    void fabricaRespeitaFormato() {
        assertEquals(FormatoLog.CSV, LoggerFactory.criar(FormatoLog.CSV, dir.resolve("a")).getFormato());
        assertEquals(FormatoLog.JSON, LoggerFactory.criar(FormatoLog.deTexto("json"), dir.resolve("a")).getFormato());
    }
}
