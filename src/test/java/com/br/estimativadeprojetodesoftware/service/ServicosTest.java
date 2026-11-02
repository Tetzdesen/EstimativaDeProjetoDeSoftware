package com.br.estimativadeprojetodesoftware.service;

import static org.junit.jupiter.api.Assertions.*;

import com.br.estimativadeprojetodesoftware.adapter.senha.ValidadorSenhaAdapter;
import com.br.estimativadeprojetodesoftware.model.Funcionalidade;
import com.br.estimativadeprojetodesoftware.model.Perfil;
import com.br.estimativadeprojetodesoftware.model.Plataforma;
import com.br.estimativadeprojetodesoftware.model.Projeto;
import com.br.estimativadeprojetodesoftware.model.TipoSelecao;
import com.br.estimativadeprojetodesoftware.model.TipoValor;
import com.br.estimativadeprojetodesoftware.model.Usuario;
import com.br.estimativadeprojetodesoftware.observer.EventoDominio;
import com.br.estimativadeprojetodesoftware.observer.Observer;
import com.br.estimativadeprojetodesoftware.observer.PublicadorEventos;
import com.br.estimativadeprojetodesoftware.observer.Subject;
import com.br.estimativadeprojetodesoftware.repository.jdbc.JdbcCompartilhamentoRepository;
import com.br.estimativadeprojetodesoftware.repository.jdbc.JdbcPerfilRepository;
import com.br.estimativadeprojetodesoftware.repository.jdbc.JdbcProjetoRepository;
import com.br.estimativadeprojetodesoftware.repository.jdbc.JdbcUsuarioRepository;
import com.br.estimativadeprojetodesoftware.suporte.BancoTeste;
import com.br.estimativadeprojetodesoftware.suporte.LogFalso;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/** Services sobre H2 em memória, com log e publicador de eventos falsos. */
class ServicosTest {

    @TempDir
    Path dir;

    private Connection conexao;
    private LogFalso log;
    private List<EventoDominio> eventos;
    private Subject publicador;
    private UsuarioService usuarios;
    private PerfilService perfis;
    private ProjetoService projetos;
    private CompartilhamentoService compartilhamentos;
    private Usuario ana;
    private Usuario beto;
    private Perfil padrao;

    private static final String SENHA_FORTE = "Xy7#qWz9!";

    @BeforeEach
    void preparar() {
        conexao = BancoTeste.abrir("H2", dir);
        log = new LogFalso();
        eventos = new ArrayList<>();
        publicador = new Subject() {
            private final List<Observer> obs = new ArrayList<>();

            @Override
            public void addObserver(Observer o) {
                obs.add(o);
            }

            @Override
            public void removeObserver(Observer o) {
                obs.remove(o);
            }

            @Override
            public void notifyObservers(EventoDominio e) {
                eventos.add(e);
                obs.forEach(o -> o.update(e));
            }
        };
        var usuarioRepo = new JdbcUsuarioRepository(conexao);
        var perfilRepo = new JdbcPerfilRepository(conexao);
        var projetoRepo = new JdbcProjetoRepository(conexao);
        var compRepo = new JdbcCompartilhamentoRepository(conexao);
        usuarios = new UsuarioService(usuarioRepo, new ValidadorSenhaAdapter(), publicador);
        perfis = new PerfilService(perfilRepo, publicador);
        projetos = new ProjetoService(projetoRepo, perfilRepo, compRepo, log, new CalculoEstimativaService(), publicador);
        compartilhamentos = new CompartilhamentoService(compRepo, projetoRepo, usuarioRepo, log, publicador);
        ana = usuarios.cadastrar("Ana", "ana@x.com", SENHA_FORTE, SENHA_FORTE);
        beto = usuarios.cadastrar("Beto", "beto@x.com", SENHA_FORTE, SENHA_FORTE);
        padrao = perfis.listarVisiveis(ana).stream().filter(Perfil::isPadrao).findFirst().orElseThrow();
        eventos.clear();
    }

    @AfterEach
    void fechar() throws Exception {
        conexao.close();
    }

    // ---------- 4.1 / 4.2 autenticação ----------

    @Test
    @DisplayName("4.1 Senha fraca é recusada com as mensagens do validador")
    void senhaFraca() {
        assertFalse(new ValidadorSenhaAdapter().validar("abc").isEmpty());
        assertTrue(new ValidadorSenhaAdapter().validar(SENHA_FORTE).isEmpty());
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> usuarios.cadastrar("Caio", "caio@x.com", "abc", "abc"));
        assertTrue(e.getMessage().startsWith("A senha não atende às regras"));
        assertTrue(usuarios.buscarPorEmail("caio@x.com").isEmpty());
    }

    @Test
    @DisplayName("4.2 E-mail duplicado é recusado com o motivo; senha gravada como hash")
    void emailDuplicado() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> usuarios.cadastrar("Outra Ana", "ANA@x.com", SENHA_FORTE, SENHA_FORTE));
        assertTrue(e.getMessage().contains("já está cadastrado"));
        assertNotEquals(SENHA_FORTE, ana.getSenha());
        assertTrue(SenhaHashService.estaHasheada(ana.getSenha()));
    }

    // ---------- 5.1 observer ----------

    @Test
    @DisplayName("5.1 Services publicam criação, alteração e exclusão")
    void eventos() {
        List<EventoDominio> recebidos = new ArrayList<>();
        Observer janela = recebidos::add;
        projetos.addObserver(janela);
        Projeto p = projetos.criar("Obs", List.of(padrao.getId()), ana);
        p.setNome("Obs renomeado");
        projetos.salvar(p, ana);
        projetos.excluir(p.getId(), ana);
        assertEquals(List.of(EventoDominio.Tipo.CRIACAO, EventoDominio.Tipo.ALTERACAO, EventoDominio.Tipo.EXCLUSAO),
                recebidos.stream().map(EventoDominio::tipo).toList());
        assertTrue(recebidos.stream().allMatch(e -> e.sobre(EventoDominio.Entidade.PROJETO) && e.id().equals(p.getId())));
        projetos.removeObserver(janela);
    }

    @Test
    @DisplayName("5.1 O publicador compartilhado entrega eventos de qualquer instância de service")
    void publicadorCompartilhado() {
        List<EventoDominio> recebidos = new ArrayList<>();
        Observer janela = recebidos::add;
        PublicadorEventos.getInstancia().addObserver(janela);
        try {
            PublicadorEventos.getInstancia().notifyObservers(
                    new EventoDominio(EventoDominio.Tipo.ALTERACAO, EventoDominio.Entidade.PROJETO, UUID.randomUUID()));
            assertEquals(1, recebidos.size());
        } finally {
            PublicadorEventos.getInstancia().removeObserver(janela);
        }
    }

    // ---------- 6.1 perfis ----------

    @Test
    @DisplayName("6.1 Perfil personalizado com funcionalidade nova fica disponível para projetos")
    void criarPerfilPersonalizado() {
        Perfil meu = perfis.criar("Meu perfil", null, ana);
        Funcionalidade f = new Funcionalidade("Extras", "Integração ERP", TipoSelecao.MULTIPLA, TipoValor.DIAS, 1);
        f.setValor(Plataforma.WEB_BACKEND, new BigDecimal("6"));
        meu.adicionarFuncionalidade(f);
        perfis.salvar(meu, ana);
        assertEquals(2, perfis.listarVisiveis(ana).size());
        assertEquals(1, perfis.listarVisiveis(beto).size());
        Projeto p = projetos.criar("Com ERP", List.of(meu.getId()), ana);
        assertTrue(p.buscarItem("Extras", "Integração ERP").isPresent());
    }

    @Test
    @DisplayName("6.1 Perfil padrão não pode ser excluído e o motivo é informado")
    void excluirPerfilPadrao() {
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> perfis.excluir(padrao.getId(), ana));
        assertTrue(e.getMessage().contains("perfil padrão não pode ser excluído"));
        assertTrue(perfis.buscarPorId(padrao.getId()).isPresent());
        Perfil copia = perfis.criar("Cópia", padrao, ana);
        assertThrows(IllegalStateException.class, () -> perfis.excluir(copia.getId(), beto));
        perfis.excluir(copia.getId(), ana);
        assertTrue(perfis.buscarPorId(copia.getId()).isEmpty());
    }

    // ---------- 7.x projetos ----------

    @Test
    @DisplayName("7.2 Criação, alteração e exclusão registradas no log (sucesso e falha)")
    void logDoCrud() {
        Projeto p = projetos.criar("App Loja", List.of(padrao.getId()), ana);
        assertEquals("OK criação | App Loja | Ana", log.ultimo());
        p.setNome("App Loja 2");
        projetos.salvar(p, ana);
        assertEquals("OK alteração | App Loja 2 | Ana", log.ultimo());

        assertThrows(IllegalStateException.class, () -> projetos.salvar(p, beto));
        assertTrue(log.ultimo().startsWith("FALHA alteração | App Loja 2 | Beto | Somente o dono"));
        assertThrows(IllegalArgumentException.class, () -> projetos.criar("App Loja 2", List.of(padrao.getId()), ana));
        assertTrue(log.ultimo().startsWith("FALHA criação | App Loja 2 | Ana"));
        assertThrows(IllegalStateException.class, () -> projetos.excluir(p.getId(), beto));
        assertTrue(log.ultimo().startsWith("FALHA exclusão"));

        projetos.excluir(p.getId(), ana);
        assertEquals("OK exclusão | App Loja 2 | Ana", log.ultimo());
        assertTrue(projetos.listarProprios(ana).isEmpty());
    }

    @Test
    @DisplayName("7.4 Alterar o projeto não altera o perfil de origem nem outros projetos")
    void isolamentoDaCopia() {
        Projeto a = projetos.criar("A", List.of(padrao.getId()), ana);
        Projeto b = projetos.criar("B", List.of(padrao.getId()), ana);
        a.buscarItem("Datas e Localizações", "Calendário").orElseThrow().getFuncionalidade()
                .setValor(Plataforma.WEB_BACKEND, new BigDecimal("30"));
        projetos.salvar(a, ana);

        assertEquals(new BigDecimal("30"), projetos.buscarParaVisualizacao(a.getId(), ana)
                .buscarItem("Datas e Localizações", "Calendário").orElseThrow().getFuncionalidade()
                .getValor(Plataforma.WEB_BACKEND).orElseThrow());
        assertEquals(0, new BigDecimal("7").compareTo(projetos.buscarParaVisualizacao(b.getId(), ana)
                .buscarItem("Datas e Localizações", "Calendário").orElseThrow().getFuncionalidade()
                .getValor(Plataforma.WEB_BACKEND).orElseThrow()));
        Perfil perfilRelido = perfis.buscarPorId(padrao.getId()).orElseThrow();
        assertEquals(0, new BigDecimal("7").compareTo(perfilRelido.getFuncionalidades().stream()
                .filter(f -> f.getNome().equals("Calendário")).findFirst().orElseThrow()
                .getValor(Plataforma.WEB_BACKEND).orElseThrow()));
    }

    @Test
    @DisplayName("Calcular marca o projeto como calculado e persiste")
    void calcularMarcaCalculado() {
        Projeto p = projetos.criar("Calc", List.of(padrao.getId()), ana);
        assertFalse(p.isCalculado());
        projetos.calcular(p, ana);
        assertTrue(projetos.buscarParaVisualizacao(p.getId(), ana).isCalculado());
    }

    // ---------- 9.1 compartilhamento ----------

    @Test
    @DisplayName("9.1 Dono compartilha; destinatário vê a versão atual; log da operação")
    void compartilhar() {
        Projeto p = projetos.criar("Comp", List.of(padrao.getId()), ana);
        assertThrows(IllegalStateException.class, () -> projetos.buscarParaVisualizacao(p.getId(), beto));

        compartilhamentos.compartilhar(p.getId(), beto.getId(), ana);
        assertEquals("OK operação de compartilhamento | Comp | Ana", log.ultimo());
        List<Projeto> deBeto = projetos.listarCompartilhadosComigo(beto);
        assertEquals(1, deBeto.size());
        assertEquals("Ana", deBeto.get(0).getDonoNome());
        assertFalse(projetos.podeEditar(deBeto.get(0), beto));

        p.setNome("Comp v2");
        projetos.calcular(p, ana);
        Projeto vistoPorBeto = projetos.buscarParaVisualizacao(p.getId(), beto);
        assertEquals("Comp v2", vistoPorBeto.getNome());
        assertTrue(vistoPorBeto.isCalculado());
        assertEquals(1, projetos.listarCompartilhadosComigo(beto).size());

        assertThrows(IllegalArgumentException.class, () -> compartilhamentos.compartilhar(p.getId(), beto.getId(), ana));
        assertTrue(log.ultimo().startsWith("FALHA operação de compartilhamento"));
        assertThrows(IllegalStateException.class, () -> compartilhamentos.compartilhar(p.getId(), ana.getId(), beto));
        assertThrows(IllegalStateException.class, () -> projetos.excluir(p.getId(), beto));
        assertTrue(compartilhamentos.candidatos(p.getId(), ana).stream().noneMatch(u -> u.equals(beto) || u.equals(ana)));
    }
}
