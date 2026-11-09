package com.br.estimativadeprojetodesoftware.migracao;

import static org.junit.jupiter.api.Assertions.*;

import com.br.estimativadeprojetodesoftware.model.Projeto;
import com.br.estimativadeprojetodesoftware.model.Usuario;
import com.br.estimativadeprojetodesoftware.repository.jdbc.JdbcCompartilhamentoRepository;
import com.br.estimativadeprojetodesoftware.repository.jdbc.JdbcPerfilRepository;
import com.br.estimativadeprojetodesoftware.repository.jdbc.JdbcProjetoRepository;
import com.br.estimativadeprojetodesoftware.repository.jdbc.JdbcUsuarioRepository;
import com.br.estimativadeprojetodesoftware.service.CalculoEstimativaService;
import com.br.estimativadeprojetodesoftware.singleton.ConexaoSingleton;
import com.br.estimativadeprojetodesoftware.strategy.H2Initializer;
import com.br.estimativadeprojetodesoftware.suporte.BancoTeste;
import com.br.estimativadeprojetodesoftware.suporte.Cenarios;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class MigradorBancoTest {

    @TempDir
    Path dir;

    private final H2Initializer h2 = new H2Initializer();

    /** Destino vazio, só com as tabelas, como o migrador prepara. */
    private Connection destinoVazio(String url) throws Exception {
        Connection c = ConexaoSingleton.conectar(h2, url, null, null);
        h2.criarTabelas(c);
        return c;
    }

    private static String urlH2() {
        return "jdbc:h2:mem:migra" + UUID.randomUUID().toString().replace("-", "") + ";DB_CLOSE_DELAY=-1";
    }

    private static long contar(Connection c, String tabela) throws Exception {
        try (Statement st = c.createStatement(); ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM " + tabela)) {
            rs.next();
            return rs.getLong(1);
        }
    }

    /** SQLite com administrador e perfil padrão (seeders), mais um usuário, um perfil próprio, três projetos e um compartilhamento. */
    private record Origem(Connection conexao, UUID projetoPlanilha) {
    }

    private Origem origemPopulada() {
        Connection sqlite = BancoTeste.abrir("SQLite", dir);
        var usuarios = new JdbcUsuarioRepository(sqlite);
        var perfis = new JdbcPerfilRepository(sqlite);
        var projetos = new JdbcProjetoRepository(sqlite);
        Usuario bia = new Usuario(UUID.randomUUID(), "Bia", "bia@x.com", "hash", LocalDateTime.now());
        usuarios.salvar(bia);
        perfis.salvar(Cenarios.perfilPadrao().clonar("Meu perfil", bia.getId()));
        Projeto planilha = Cenarios.prototipoPlanilha(bia.getId());
        planilha.setCalculado(true);
        projetos.salvar(planilha);
        Projeto segundo = Cenarios.projetoVazio(bia.getId());
        segundo.setNome("Segundo");
        projetos.salvar(segundo);
        Projeto terceiro = Cenarios.projetoVazio(bia.getId());
        terceiro.setNome("Outro");
        projetos.salvar(terceiro);
        Usuario admin = usuarios.buscarTodos().stream().filter(u -> !u.getId().equals(bia.getId())).findFirst().orElseThrow();
        new JdbcCompartilhamentoRepository(sqlite).compartilhar(planilha.getId(), admin.getId());
        return new Origem(sqlite, planilha.getId());
    }

    @Test
    void copiaTudoComAMesmaEstimativa() throws Exception {
        Origem origem = origemPopulada();
        Connection destino = destinoVazio(urlH2());

        Map<String, long[]> contagens = MigradorBanco.migrar(origem.conexao(), destino);

        assertEquals(MigradorBanco.tabelas(), List.copyOf(contagens.keySet()));
        for (var e : contagens.entrySet()) {
            assertEquals(e.getValue()[0], e.getValue()[1], e.getKey());
        }
        assertEquals(2, contar(destino, "usuario"));
        assertEquals(2, contar(destino, "perfil"));
        assertEquals(3, contar(destino, "projeto"));
        assertEquals(1, contar(destino, "compartilhamento"));

        var calculo = new CalculoEstimativaService();
        Projeto antes = new JdbcProjetoRepository(origem.conexao()).buscarPorId(origem.projetoPlanilha()).orElseThrow();
        Projeto depois = new JdbcProjetoRepository(destino).buscarPorId(origem.projetoPlanilha()).orElseThrow();
        assertTrue(depois.isCalculado());
        assertEquals(calculo.calcular(antes).getPrecoFinal(), calculo.calcular(depois).getPrecoFinal());
        assertEquals(calculo.calcular(antes).getDiasTotalExibicao(), calculo.calcular(depois).getDiasTotalExibicao());
    }

    @Test
    void recusaDestinoComDados() throws Exception {
        Origem origem = origemPopulada();
        Connection destino = BancoTeste.abrirUrl("H2", urlH2()); // seeders já criaram administrador e perfil padrão
        long usuarios = contar(destino, "usuario");

        MigradorBanco.MigracaoException e = assertThrows(MigradorBanco.MigracaoException.class,
                () -> MigradorBanco.migrar(origem.conexao(), destino));

        assertTrue(e.getMessage().contains("vazio"), e.getMessage());
        assertEquals(usuarios, contar(destino, "usuario"));
        assertEquals(0, contar(destino, "projeto"));
    }

    @Test
    void falhaNoMeioNaoDeixaDadosParciais() throws Exception {
        Origem origem = origemPopulada();
        try (Statement st = origem.conexao().createStatement()) {
            st.execute("PRAGMA foreign_keys = OFF");
            st.execute("INSERT INTO compartilhamento (projeto_id, usuario_id, compartilhado_em) VALUES ('"
                    + UUID.randomUUID() + "', '" + UUID.randomUUID() + "', '2026-11-09T10:00:00')");
        }
        Connection destino = destinoVazio(urlH2());

        MigradorBanco.MigracaoException e = assertThrows(MigradorBanco.MigracaoException.class,
                () -> MigradorBanco.migrar(origem.conexao(), destino));

        assertTrue(e.getMessage().contains("compartilhamento"), e.getMessage());
        for (String tabela : MigradorBanco.tabelas()) {
            assertEquals(0, contar(destino, tabela), tabela);
        }
    }

    @Test
    void seederNaoDuplicaDepoisDaMigracao() throws Exception {
        Origem origem = origemPopulada();
        String url = urlH2();
        MigradorBanco.migrar(origem.conexao(), destinoVazio(url));

        Connection aberto = BancoTeste.abrirUrl("H2", url); // como no início do sistema: tabelas e seeders

        assertEquals(2, contar(aberto, "usuario"));
        assertEquals(2, contar(aberto, "perfil"));
        try (Statement st = aberto.createStatement(); ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM perfil WHERE padrao = TRUE")) {
            rs.next();
            assertEquals(1, rs.getLong(1));
        }
    }
}
