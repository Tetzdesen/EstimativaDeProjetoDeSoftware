package com.br.estimativadeprojetodesoftware.singleton;

import static org.junit.jupiter.api.Assertions.*;

import com.br.estimativadeprojetodesoftware.repository.jdbc.JdbcUsuarioRepository;
import com.br.estimativadeprojetodesoftware.seeder.UsuarioSeeder;
import com.br.estimativadeprojetodesoftware.strategy.H2Initializer;
import java.sql.Connection;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ConexaoReconectavelTest {

    private final H2Initializer h2 = new H2Initializer();
    private String url;
    private final List<Connection> abertas = new ArrayList<>();
    private Connection guardiao;

    @BeforeEach
    void preparar() {
        url = "jdbc:h2:mem:" + UUID.randomUUID() + ";DB_CLOSE_DELAY=-1";
        guardiao = ConexaoSingleton.abrir(h2, url, "sa", ""); // cria tabelas e o administrador
    }

    @AfterEach
    void limpar() throws Exception {
        try (var st = guardiao.createStatement()) {
            st.execute("SHUTDOWN");
        }
    }

    private Connection reconectavel(Duration intervalo) {
        return ConexaoReconectavel.criar(() -> {
            Connection c = ConexaoSingleton.conectar(h2, url, "sa", "");
            abertas.add(c);
            return c;
        }, intervalo);
    }

    @Test
    void reabreQuandoAConexaoRealCai() throws Exception {
        Connection conexao = reconectavel(Duration.ZERO);
        JdbcUsuarioRepository usuarios = new JdbcUsuarioRepository(conexao);
        assertTrue(usuarios.buscarPorEmail(UsuarioSeeder.EMAIL_ADMIN).isPresent());

        abertas.get(0).close(); // como se o servidor tivesse encerrado a conexão

        assertTrue(usuarios.buscarPorEmail(UsuarioSeeder.EMAIL_ADMIN).isPresent(), "o mesmo repositório continua funcionando");
        assertEquals(2, abertas.size());
    }

    @Test
    void conexaoValidaNaoEReaberta() {
        Connection conexao = reconectavel(Duration.ZERO);
        JdbcUsuarioRepository usuarios = new JdbcUsuarioRepository(conexao);
        for (int i = 0; i < 5; i++) {
            assertTrue(usuarios.buscarPorEmail(UsuarioSeeder.EMAIL_ADMIN).isPresent());
        }
        assertEquals(1, abertas.size());
    }

    @Test
    void naoVerificaAntesDoIntervalo() throws Exception {
        Connection conexao = reconectavel(Duration.ofHours(1));
        abertas.get(0).close();
        assertThrows(Exception.class, () -> new JdbcUsuarioRepository(conexao).buscarPorEmail(UsuarioSeeder.EMAIL_ADMIN));
        assertEquals(1, abertas.size(), "dentro do intervalo não testa a conexão a cada chamada");
    }

    @Test
    void naoReabreNoMeioDeUmaTransacao() throws Exception {
        Connection conexao = reconectavel(Duration.ZERO);
        conexao.setAutoCommit(false);
        abertas.get(0).close();
        assertThrows(Exception.class, () -> conexao.createStatement());
        assertEquals(1, abertas.size());
    }
}
