package com.br.estimativadeprojetodesoftware.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.br.estimativadeprojetodesoftware.model.CustoAdicional;
import com.br.estimativadeprojetodesoftware.model.Funcionalidade;
import com.br.estimativadeprojetodesoftware.model.ItemProjeto;
import com.br.estimativadeprojetodesoftware.model.Papel;
import com.br.estimativadeprojetodesoftware.model.Perfil;
import com.br.estimativadeprojetodesoftware.model.Plataforma;
import com.br.estimativadeprojetodesoftware.model.Projeto;
import com.br.estimativadeprojetodesoftware.model.Usuario;
import com.br.estimativadeprojetodesoftware.repository.jdbc.JdbcCompartilhamentoRepository;
import com.br.estimativadeprojetodesoftware.repository.jdbc.JdbcConfiguracaoRepository;
import com.br.estimativadeprojetodesoftware.repository.jdbc.JdbcPerfilRepository;
import com.br.estimativadeprojetodesoftware.repository.jdbc.JdbcProjetoRepository;
import com.br.estimativadeprojetodesoftware.repository.jdbc.JdbcUsuarioRepository;
import com.br.estimativadeprojetodesoftware.seeder.PerfilPadraoSeeder;
import com.br.estimativadeprojetodesoftware.seeder.UsuarioSeeder;
import com.br.estimativadeprojetodesoftware.suporte.BancoTeste;
import com.br.estimativadeprojetodesoftware.suporte.Cenarios;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/** 3.5–3.9: schema, seeder e repositórios em SQLite (arquivo) e H2 (memória). */
class PersistenciaIntegracaoTest {

    @TempDir
    Path dir;

    private static int linhasDeFuncionalidadeNoCsv() throws Exception {
        try (BufferedReader r = new BufferedReader(new InputStreamReader(
                PersistenciaIntegracaoTest.class.getResourceAsStream(PerfilPadraoSeeder.RECURSO), StandardCharsets.UTF_8))) {
            return (int) r.lines().skip(1).filter(l -> !l.isBlank() && !l.contains(";TAXA;")).count();
        }
    }

    private static int contar(Connection c, String tabela) throws Exception {
        try (Statement st = c.createStatement(); ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM " + tabela)) {
            rs.next();
            return rs.getInt(1);
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"SQLite", "H2"})
    void schemaESeederCriamOPerfilPadrao(String banco) throws Exception {
        try (Connection c = BancoTeste.abrir(banco, dir)) {
            JdbcPerfilRepository perfis = new JdbcPerfilRepository(c);
            assertTrue(perfis.existePerfilPadrao());
            UUID qualquer = UUID.randomUUID();
            List<Perfil> visiveis = perfis.buscarVisiveisPara(qualquer);
            assertEquals(1, visiveis.size());
            Perfil padrao = visiveis.get(0);
            assertTrue(padrao.isPadrao());
            assertEquals(linhasDeFuncionalidadeNoCsv(), padrao.getFuncionalidades().size());

            Funcionalidade planos = buscar(padrao, "Planos de Assinatura");
            assertFalse(planos.isAplicavel(Plataforma.IOS));
            assertEquals(0, new BigDecimal("8").compareTo(planos.getValor(Plataforma.WEB_BACKEND).orElseThrow()));
            Funcionalidade erros = buscar(padrao, "Relatório de Erros");
            assertTrue(erros.isAplicavel(Plataforma.WEB_BACKEND));
            assertEquals(0, erros.getValor(Plataforma.WEB_BACKEND).orElseThrow().signum());
            assertEquals(new BigDecimal("0.70"), buscar(padrao, "Profissional").getValor(Plataforma.ANDROID).orElseThrow());

            for (Plataforma p : Plataforma.values()) {
                assertEquals(new BigDecimal("450.00"), padrao.getTaxas().get(p, Papel.DESENVOLVIMENTO));
                assertEquals(new BigDecimal("450.00"), padrao.getTaxas().get(p, Papel.DESIGN));
                assertEquals(new BigDecimal("300.00"), padrao.getTaxas().get(p, Papel.GERENCIA));
            }
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"SQLite", "H2"})
    void reinicioNaoDuplicaOPerfilPadrao(String banco) throws Exception {
        String url = banco.equals("SQLite") ? "jdbc:sqlite:" + dir.resolve("reinicio.db")
                : "jdbc:h2:" + dir.resolve("reinicio").toAbsolutePath();
        try (Connection c = BancoTeste.abrirUrl(banco, url)) {
            assertEquals(1, contar(c, "perfil"));
        }
        try (Connection c = BancoTeste.abrirUrl(banco, url)) {
            new PerfilPadraoSeeder(new JdbcPerfilRepository(c)).executar();
            new UsuarioSeeder(new JdbcUsuarioRepository(c)).executar();
            assertEquals(1, contar(c, "perfil"));
            assertEquals(1, contar(c, "usuario"));
            assertEquals(linhasDeFuncionalidadeNoCsv(), contar(c, "perfil_funcionalidade"));
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"SQLite", "H2"})
    void usuarioEConfiguracao(String banco) throws Exception {
        try (Connection c = BancoTeste.abrir(banco, dir)) {
            JdbcUsuarioRepository usuarios = new JdbcUsuarioRepository(c);
            Usuario u = new Usuario(UUID.randomUUID(), "Ana", "ana@x.com", "hash", LocalDateTime.now());
            usuarios.salvar(u);
            assertEquals("Ana", usuarios.buscarPorEmail("ANA@x.com").orElseThrow().getNome());
            assertThrows(RepositorioException.class, () -> usuarios.salvar(
                    new Usuario(UUID.randomUUID(), "Outra", "ana@x.com", "h", LocalDateTime.now())));

            JdbcConfiguracaoRepository config = new JdbcConfiguracaoRepository(c);
            assertTrue(config.buscar("x").isEmpty());
            config.salvar("x", "CSV");
            config.salvar("x", "JSON");
            assertEquals("JSON", config.buscar("x").orElseThrow());
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"SQLite", "H2"})
    void perfilIdaEVolta(String banco) throws Exception {
        try (Connection c = BancoTeste.abrir(banco, dir)) {
            Usuario dono = novoUsuario(c, "dono@x.com");
            JdbcPerfilRepository repo = new JdbcPerfilRepository(c);
            Perfil p = new Perfil("Meu perfil", dono.getId());
            Funcionalidade f = new Funcionalidade("Nova", "Funcionalidade X",
                    com.br.estimativadeprojetodesoftware.model.TipoSelecao.MULTIPLA,
                    com.br.estimativadeprojetodesoftware.model.TipoValor.DIAS, 1);
            f.setValor(Plataforma.WEB_BACKEND, new BigDecimal("2.5"));
            f.setValor(Plataforma.IOS, BigDecimal.ZERO);
            p.adicionarFuncionalidade(f);
            p.getTaxas().set(Plataforma.IOS, Papel.DESIGN, new BigDecimal("550.00"));
            repo.salvar(p);

            Perfil lido = repo.buscarPorId(p.getId()).orElseThrow();
            Funcionalidade fl = lido.getFuncionalidades().get(0);
            assertEquals(new BigDecimal("2.5"), fl.getValor(Plataforma.WEB_BACKEND).orElseThrow());
            assertEquals(BigDecimal.ZERO, fl.getValor(Plataforma.IOS).orElseThrow());
            assertFalse(fl.isAplicavel(Plataforma.ANDROID));
            assertEquals(new BigDecimal("550.00"), lido.getTaxas().get(Plataforma.IOS, Papel.DESIGN));
            assertEquals(2, repo.buscarVisiveisPara(dono.getId()).size());

            lido.setNome("Renomeado");
            repo.salvar(lido);
            assertEquals("Renomeado", repo.buscarPorId(p.getId()).orElseThrow().getNome());
            assertTrue(repo.remover(p.getId()));
            assertTrue(repo.buscarPorId(p.getId()).isEmpty());
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"SQLite", "H2"})
    void projetoECompartilhamentoIdaEVolta(String banco) throws Exception {
        try (Connection c = BancoTeste.abrir(banco, dir)) {
            Usuario dono = novoUsuario(c, "dono@x.com");
            Usuario outro = novoUsuario(c, "outro@x.com");
            JdbcProjetoRepository repo = new JdbcProjetoRepository(c);
            Projeto p = Cenarios.prototipoPlanilha();
            Projeto meu = Projeto.aPartirDePerfis("Loja", dono.getId(), List.of(Cenarios.perfilPadrao()));
            // copia o estado do protótipo para o projeto do dono
            for (ItemProjeto i : p.getItens()) {
                if (i.isSelecionado()) {
                    meu.selecionar(meu.buscarItem(i.getFuncionalidade().getCategoria(), i.getFuncionalidade().getNome())
                            .orElseThrow().getFuncionalidade().getId(), true);
                }
            }
            meu.setPlataformaMarcada(Plataforma.IOS, false);
            meu.adicionarCustoAdicional(new CustoAdicional("Fundo de reserva", new BigDecimal("2000.00")));
            meu.setCalculado(true);
            repo.salvar(meu);

            Projeto lido = repo.buscarPorId(meu.getId()).orElseThrow();
            assertEquals("Loja", lido.getNome());
            assertEquals(dono.getNome(), lido.getDonoNome());
            assertFalse(lido.isPlataformaMarcada(Plataforma.IOS));
            assertTrue(lido.isPlataformaMarcada(Plataforma.ANDROID));
            assertTrue(lido.isCalculado());
            assertEquals(meu.getItens().size(), lido.getItens().size());
            assertEquals(meu.getItens().stream().filter(ItemProjeto::isSelecionado).count(),
                    lido.getItens().stream().filter(ItemProjeto::isSelecionado).count());
            assertEquals(7, lido.getCustosAdicionais().size());
            assertEquals(new BigDecimal("0.42"), lido.getPercentualImpostos());
            assertTrue(repo.existeNomeParaDono("loja", dono.getId(), null));
            assertFalse(repo.existeNomeParaDono("loja", dono.getId(), meu.getId()));

            JdbcCompartilhamentoRepository comp = new JdbcCompartilhamentoRepository(c);
            comp.compartilhar(meu.getId(), outro.getId());
            assertTrue(comp.estaCompartilhado(meu.getId(), outro.getId()));
            assertEquals(List.of(meu.getId()), comp.buscarProjetosCompartilhadosCom(outro.getId()));

            assertTrue(repo.remover(meu.getId()));
            assertTrue(repo.buscarPorId(meu.getId()).isEmpty());
            assertFalse(comp.estaCompartilhado(meu.getId(), outro.getId()));
            assertEquals(0, contar(c, "projeto_funcionalidade"));
            assertEquals(0, contar(c, "projeto_valor"));
        }
    }

    private static Usuario novoUsuario(Connection c, String email) {
        Usuario u = new Usuario(UUID.randomUUID(), email.substring(0, email.indexOf('@')), email, "hash", LocalDateTime.now());
        new JdbcUsuarioRepository(c).salvar(u);
        return u;
    }

    private static Funcionalidade buscar(Perfil p, String nome) {
        return p.getFuncionalidades().stream().filter(f -> f.getNome().equals(nome)).findFirst().orElseThrow();
    }
}
