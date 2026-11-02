package com.br.estimativadeprojetodesoftware.service;

import static org.junit.jupiter.api.Assertions.*;

import com.br.estimativadeprojetodesoftware.model.Tema;
import com.br.estimativadeprojetodesoftware.repository.jdbc.JdbcConfiguracaoRepository;
import com.br.estimativadeprojetodesoftware.suporte.BancoTeste;
import java.nio.file.Path;
import java.sql.Connection;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class PreferenciasServiceTest {

    @TempDir
    Path dir;

    @Test
    void temaPadraoClaroGravadoERelidoAposReiniciar() throws Exception {
        String url = "jdbc:h2:" + dir.resolve("prefs").toAbsolutePath();
        try (Connection c = BancoTeste.abrirUrl("H2", url)) {
            PreferenciasService prefs = new PreferenciasService(new JdbcConfiguracaoRepository(c));
            assertEquals(Tema.CLARO, prefs.getTema(), "primeira execução usa o claro");
            prefs.setTema(Tema.ESCURO);
            assertEquals(Tema.ESCURO, prefs.getTema());
            assertThrows(IllegalArgumentException.class, () -> prefs.setTema(null));
        }
        try (Connection c = BancoTeste.abrirUrl("H2", url)) {
            assertEquals(Tema.ESCURO, new PreferenciasService(new JdbcConfiguracaoRepository(c)).getTema(), "escolha mantida");
        }
    }

    @Test
    void valorInvalidoValeClaro() throws Exception {
        try (Connection c = BancoTeste.abrir("H2", dir)) {
            JdbcConfiguracaoRepository repo = new JdbcConfiguracaoRepository(c);
            repo.salvar(PreferenciasService.CHAVE_TEMA, "roxo");
            assertEquals(Tema.CLARO, new PreferenciasService(repo).getTema());
        }
    }
}
