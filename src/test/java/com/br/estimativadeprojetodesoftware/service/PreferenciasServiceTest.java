package com.br.estimativadeprojetodesoftware.service;

import static org.junit.jupiter.api.Assertions.*;

import com.br.estimativadeprojetodesoftware.model.Tema;
import com.br.estimativadeprojetodesoftware.model.Usuario;
import com.br.estimativadeprojetodesoftware.repository.jdbc.JdbcConfiguracaoRepository;
import com.br.estimativadeprojetodesoftware.suporte.BancoTeste;
import java.nio.file.Path;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class PreferenciasServiceTest {

    @TempDir
    Path dir;

    private static Usuario usuario(String nome) {
        return new Usuario(UUID.randomUUID(), nome, nome + "@x.com", "hash", LocalDateTime.now());
    }

    @Test
    void temaPadraoClaroGravadoERelidoAposReiniciar() throws Exception {
        String url = "jdbc:h2:" + dir.resolve("prefs").toAbsolutePath();
        Usuario ana = usuario("ana");
        try (Connection c = BancoTeste.abrirUrl("H2", url)) {
            PreferenciasService prefs = new PreferenciasService(new JdbcConfiguracaoRepository(c));
            assertEquals(Tema.CLARO, prefs.getTema(), "primeira execução usa o claro");
            assertEquals(Tema.CLARO, prefs.getTema(ana));
            prefs.setTema(ana, Tema.ESCURO);
            assertEquals(Tema.ESCURO, prefs.getTema(ana));
            assertThrows(IllegalArgumentException.class, () -> prefs.setTema(ana, null));
        }
        try (Connection c = BancoTeste.abrirUrl("H2", url)) {
            assertEquals(Tema.ESCURO, new PreferenciasService(new JdbcConfiguracaoRepository(c)).getTema(ana), "escolha mantida");
        }
    }

    @Test
    void cadaUsuarioTemOSeuTema() throws Exception {
        try (Connection c = BancoTeste.abrir("H2", dir)) {
            PreferenciasService prefs = new PreferenciasService(new JdbcConfiguracaoRepository(c));
            Usuario ana = usuario("ana");
            Usuario beto = usuario("beto");
            prefs.setTema(ana, Tema.ESCURO);
            prefs.setTema(beto, Tema.CLARO);

            assertEquals(Tema.ESCURO, prefs.getTema(ana), "a escolha do Beto não muda a da Ana");
            assertEquals(Tema.CLARO, prefs.getTema(beto));
            assertEquals(Tema.CLARO, prefs.getTema(), "as telas de entrada usam o último tema salvo");
        }
    }

    @Test
    void quemNuncaEscolheuFicaComOUltimoTemaUsado() throws Exception {
        try (Connection c = BancoTeste.abrir("H2", dir)) {
            JdbcConfiguracaoRepository repo = new JdbcConfiguracaoRepository(c);
            repo.salvar(PreferenciasService.CHAVE_TEMA, Tema.ESCURO.name()); // banco de uma versão com tema único
            assertEquals(Tema.ESCURO, new PreferenciasService(repo).getTema(usuario("novo")));
        }
    }

    @Test
    void valorInvalidoValeClaro() throws Exception {
        try (Connection c = BancoTeste.abrir("H2", dir)) {
            JdbcConfiguracaoRepository repo = new JdbcConfiguracaoRepository(c);
            repo.salvar(PreferenciasService.CHAVE_TEMA, "roxo");
            assertEquals(Tema.CLARO, new PreferenciasService(repo).getTema());
            Usuario ana = usuario("ana");
            repo.salvar(PreferenciasService.chaveTema(ana), "roxo");
            assertEquals(Tema.CLARO, new PreferenciasService(repo).getTema(ana));
        }
    }
}
