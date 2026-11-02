package com.br.estimativadeprojetodesoftware.service;

import static org.junit.jupiter.api.Assertions.*;

import com.br.estimativadeprojetodesoftware.repository.IConfiguracaoRepository;
import com.br.estimativadeprojetodesoftware.repository.jdbc.JdbcConfiguracaoRepository;
import com.br.estimativadeprojetodesoftware.suporte.BancoTeste;
import com.github.tetzdesen.estimativalog.FormatoLog;
import com.google.gson.JsonParser;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/** 5.3 / 11.1: troca de formato em execução, persistida na tabela configuracao. */
class LogServiceTest {

    @TempDir
    Path dir;

    @Test
    void trocaDeCsvParaJsonValeNaProximaOperacaoEPersiste() throws Exception {
        try (Connection c = BancoTeste.abrir("H2", dir)) {
            IConfiguracaoRepository config = new JdbcConfiguracaoRepository(c);
            Path base = dir.resolve("logs/estimativa");
            LogService log = new LogService(config, base);
            assertEquals(FormatoLog.JSON, log.getFormato(), "padrão sem configuração salva");

            log.setFormato(FormatoLog.CSV);
            log.sucesso(OperacaoLog.CRIACAO, "App", "Ana");
            List<String> csv = Files.readAllLines(dir.resolve("logs/estimativa.csv"), StandardCharsets.UTF_8);
            assertEquals(2, csv.size());
            assertTrue(csv.get(1).contains(";Ana;criação;App;SUCESSO;;criação: App, ("));
            assertFalse(Files.exists(dir.resolve("logs/estimativa.json")));

            log.setFormato(FormatoLog.JSON);
            log.falha(OperacaoLog.EXCLUSAO, "App", "Ana", "erro X");
            var array = JsonParser.parseString(Files.readString(dir.resolve("logs/estimativa.json"))).getAsJsonArray();
            assertEquals(1, array.size());
            assertTrue(array.get(0).getAsJsonObject().get("mensagem").getAsString()
                    .startsWith("Ocorreu a falha erro X ao realizar a exclusão do projeto App, ("));
            assertEquals(2, Files.readAllLines(dir.resolve("logs/estimativa.csv")).size(), "CSV não recebe mais registros");

            assertEquals("JSON", config.buscar(IConfiguracaoRepository.CHAVE_FORMATO_LOG).orElseThrow());
            log.setFormato(FormatoLog.CSV);
            assertEquals(FormatoLog.CSV, new LogService(config, base).getFormato(), "escolha vale após reiniciar");
        }
    }
}
