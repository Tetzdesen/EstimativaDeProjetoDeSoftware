package com.br.estimativadeprojetodesoftware.service;

import static org.junit.jupiter.api.Assertions.*;

import com.br.estimativadeprojetodesoftware.model.Plataforma;
import com.br.estimativadeprojetodesoftware.model.Projeto;
import com.br.estimativadeprojetodesoftware.model.ResultadoEstimativa;
import com.br.estimativadeprojetodesoftware.model.ResumoDashboard;
import com.br.estimativadeprojetodesoftware.model.Usuario;
import com.br.estimativadeprojetodesoftware.observer.PublicadorEventos;
import com.br.estimativadeprojetodesoftware.repository.jdbc.JdbcCompartilhamentoRepository;
import com.br.estimativadeprojetodesoftware.repository.jdbc.JdbcPerfilRepository;
import com.br.estimativadeprojetodesoftware.repository.jdbc.JdbcProjetoRepository;
import com.br.estimativadeprojetodesoftware.repository.jdbc.JdbcUsuarioRepository;
import com.br.estimativadeprojetodesoftware.suporte.BancoTeste;
import com.br.estimativadeprojetodesoftware.suporte.Cenarios;
import com.br.estimativadeprojetodesoftware.suporte.LogFalso;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/** Casos de aceitação da spec dashboard, sobre H2 em memória. */
class DashboardServiceTest {

    @TempDir
    Path dir;

    private Connection conexao;
    private JdbcProjetoRepository projetoRepo;
    private ProjetoService projetos;
    private CompartilhamentoService compartilhamentos;
    private DashboardService dashboard;
    private Usuario ana;
    private Usuario beto;

    @BeforeEach
    void preparar() {
        conexao = BancoTeste.abrir("H2", dir);
        var usuarioRepo = new JdbcUsuarioRepository(conexao);
        var perfilRepo = new JdbcPerfilRepository(conexao);
        var compRepo = new JdbcCompartilhamentoRepository(conexao);
        projetoRepo = new JdbcProjetoRepository(conexao);
        LogFalso log = new LogFalso();
        PublicadorEventos publicador = PublicadorEventos.getInstancia();
        projetos = new ProjetoService(projetoRepo, perfilRepo, compRepo, log, new CalculoEstimativaService(), publicador);
        compartilhamentos = new CompartilhamentoService(compRepo, projetoRepo, usuarioRepo, log, publicador);
        dashboard = new DashboardService(projetos, new CalculoEstimativaService());
        ana = new Usuario(UUID.randomUUID(), "Ana", "ana@x.com", "h", LocalDateTime.now());
        beto = new Usuario(UUID.randomUUID(), "Beto", "beto@x.com", "h", LocalDateTime.now());
        usuarioRepo.salvar(ana);
        usuarioRepo.salvar(beto);
    }

    @AfterEach
    void fechar() throws Exception {
        conexao.close();
    }

    private Projeto salvarPrototipo(Usuario dono, String nome, boolean calculado) {
        Projeto p = Cenarios.prototipoPlanilha(dono.getId());
        p.setNome(nome);
        p.setCalculado(calculado);
        projetoRepo.salvar(p);
        return p;
    }

    private static BigDecimal reais(BigDecimal v) {
        return ResultadoEstimativa.arredondarReais(v);
    }

    @Test
    @DisplayName("Protótipo da planilha: 1 próprio, 1 calculado, 241 dias, R$ 212.597,67; Web 129,33 / iOS 0 / Android 111,33")
    void casoDeAceitacao() {
        salvarPrototipo(ana, "Protótipo", true);
        ResumoDashboard r = dashboard.resumir(ana);
        assertEquals(1, r.getProjetosProprios());
        assertEquals(1, r.getProjetosCalculados());
        assertEquals(0, r.getProjetosCompartilhados());
        assertEquals(241, r.getDiasTotalExibicao());
        assertEquals(new BigDecimal("212597.67"), reais(r.getPrecoTotal()));
        assertEquals("R$ 212.597,67", ResultadoEstimativa.formatarReais(r.getPrecoTotal()));
        assertEquals(new BigDecimal("212597.67"), reais(r.getPrecoPorProjeto().get("Protótipo")));
        assertEquals(new BigDecimal("129.33"), ResultadoEstimativa.arredondarDias(r.getDiasPorPlataforma().get(Plataforma.WEB_BACKEND)));
        assertEquals(0, r.getDiasPorPlataforma().get(Plataforma.IOS).signum());
        assertEquals(new BigDecimal("111.33"), ResultadoEstimativa.arredondarDias(r.getDiasPorPlataforma().get(Plataforma.ANDROID)));
    }

    @Test
    @DisplayName("Um calculado e um não calculado: 2 próprios, 1 calculado; somas só do calculado")
    void naoCalculadoForaDasSomas() {
        salvarPrototipo(ana, "Calculado", true);
        salvarPrototipo(ana, "Rascunho", false);
        ResumoDashboard r = dashboard.resumir(ana);
        assertEquals(2, r.getProjetosProprios());
        assertEquals(1, r.getProjetosCalculados());
        assertEquals(241, r.getDiasTotalExibicao());
        assertEquals(new BigDecimal("212597.67"), reais(r.getPrecoTotal()));
        assertEquals(1, r.getPrecoPorProjeto().size());
    }

    @Test
    @DisplayName("Projeto compartilhado conta, mas não entra nas somas")
    void compartilhadoForaDasSomas() {
        Projeto doBeto = salvarPrototipo(beto, "Do Beto", true);
        compartilhamentos.compartilhar(doBeto.getId(), ana.getId(), beto);
        ResumoDashboard r = dashboard.resumir(ana);
        assertEquals(0, r.getProjetosProprios());
        assertEquals(1, r.getProjetosCompartilhados());
        assertEquals(0, r.getDiasTotal().signum());
        assertEquals(0, r.getPrecoTotal().signum());
        assertFalse(r.temProjetoCalculado());
    }

    @Test
    @DisplayName("Usuário novo: tudo zero e sem projeto calculado")
    void usuarioNovo() {
        ResumoDashboard r = dashboard.resumir(ana);
        assertEquals(0, r.getProjetosProprios());
        assertEquals(0, r.getProjetosCompartilhados());
        assertEquals(0, r.getProjetosCalculados());
        assertEquals(0, r.getDiasTotalExibicao());
        assertEquals("R$ 0,00", ResultadoEstimativa.formatarReais(r.getPrecoTotal()));
        assertFalse(r.temProjetoCalculado());
        assertEquals(3, r.getDiasPorPlataforma().size());
    }

    @Test
    @DisplayName("Projeto excluído sai das somas")
    void excluido() {
        salvarPrototipo(ana, "A", true);
        Projeto b = salvarPrototipo(ana, "B", true);
        assertEquals(new BigDecimal("425195.33"), reais(dashboard.resumir(ana).getPrecoTotal()));
        projetos.excluir(b.getId(), ana);
        ResumoDashboard r = dashboard.resumir(ana);
        assertEquals(1, r.getProjetosProprios());
        assertEquals(new BigDecimal("212597.67"), reais(r.getPrecoTotal()));
    }

    @Test
    @DisplayName("Projeto calculado mas inválido hoje (sem plataforma) fica fora das somas, sem erro")
    void calculadoInvalido() {
        Projeto p = salvarPrototipo(ana, "Sem plataforma", true);
        for (Plataforma pl : Plataforma.values()) {
            p.setPlataformaMarcada(pl, false);
        }
        projetoRepo.salvar(p);
        ResumoDashboard r = dashboard.resumir(ana);
        assertEquals(1, r.getProjetosCalculados());
        assertEquals(1, r.getProjetosComErro());
        assertEquals(0, r.getPrecoTotal().signum());
    }
}
