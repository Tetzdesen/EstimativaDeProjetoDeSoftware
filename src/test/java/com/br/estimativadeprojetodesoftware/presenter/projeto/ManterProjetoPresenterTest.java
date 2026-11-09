package com.br.estimativadeprojetodesoftware.presenter.projeto;

import static org.junit.jupiter.api.Assertions.*;

import com.br.estimativadeprojetodesoftware.command.ConfirmarCommand;
import com.br.estimativadeprojetodesoftware.command.ExibirMensagemCommand;
import com.br.estimativadeprojetodesoftware.command.PerguntarCommand;
import com.br.estimativadeprojetodesoftware.model.Papel;
import com.br.estimativadeprojetodesoftware.model.Perfil;
import com.br.estimativadeprojetodesoftware.model.Projeto;
import com.br.estimativadeprojetodesoftware.model.Usuario;
import com.br.estimativadeprojetodesoftware.observer.EventoDominio;
import com.br.estimativadeprojetodesoftware.presenter.tabela.NavegacaoProjetoModel;
import com.br.estimativadeprojetodesoftware.observer.Observer;
import com.br.estimativadeprojetodesoftware.observer.Subject;
import com.br.estimativadeprojetodesoftware.repository.IProjetoRepository;
import com.br.estimativadeprojetodesoftware.repository.jdbc.JdbcCompartilhamentoRepository;
import com.br.estimativadeprojetodesoftware.repository.jdbc.JdbcPerfilRepository;
import com.br.estimativadeprojetodesoftware.repository.jdbc.JdbcProjetoRepository;
import com.br.estimativadeprojetodesoftware.repository.jdbc.JdbcUsuarioRepository;
import com.br.estimativadeprojetodesoftware.service.CalculoEstimativaService;
import com.br.estimativadeprojetodesoftware.service.CompartilhamentoService;
import com.br.estimativadeprojetodesoftware.service.ExportacaoService;
import com.br.estimativadeprojetodesoftware.service.PerfilService;
import com.br.estimativadeprojetodesoftware.service.ProjetoService;
import com.br.estimativadeprojetodesoftware.suporte.BancoTeste;
import com.br.estimativadeprojetodesoftware.suporte.LogFalso;
import com.br.estimativadeprojetodesoftware.view.componentes.TabelaPadrao;
import com.br.estimativadeprojetodesoftware.view.projeto.ProjetoView;
import com.br.estimativadeprojetodesoftware.view.projeto.SecaoProjeto;
import com.br.estimativadeprojetodesoftware.view.tema.TemaClaro;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.api.io.TempDir;

/**
 * Tela de projeto de ponta a ponta: view real, serviços sobre H2 em memória e
 * alterações feitas pelos mesmos componentes que o usuário usa. Tudo roda na
 * thread do Swing, para o temporizador do recálculo não interferir nas asserções.
 */
class ManterProjetoPresenterTest {

    private static final int COLUNA_SELECIONAR = 0;
    private static final int COLUNA_WEB = 4;

    @TempDir
    Path dir;

    private Connection conexao;
    private LogFalso log;
    private List<EventoDominio> eventos;
    private List<String> mensagensDeErro;
    private List<String> perguntasDeConfirmacao;
    private boolean respostaConfirmacao;
    private AtomicInteger gravacoes;
    private ProjetoService projetos;
    private PerfilService perfis;
    private CompartilhamentoService compartilhamentos;
    private ExportacaoService exportacao;
    private Usuario ana;
    private Usuario beto;
    private Perfil padrao;

    private ProjetoView view;
    private ManterProjetoPresenter presenter;

    @BeforeAll
    static void tema() {
        TemaClaro.setup();
    }

    @BeforeEach
    void preparar() {
        conexao = BancoTeste.abrir("H2", dir);
        log = new LogFalso();
        eventos = new ArrayList<>();
        mensagensDeErro = new ArrayList<>();
        perguntasDeConfirmacao = new ArrayList<>();
        respostaConfirmacao = true;
        gravacoes = new AtomicInteger();
        Subject publicador = new Subject() {
            @Override
            public void addObserver(Observer o) {
            }

            @Override
            public void removeObserver(Observer o) {
            }

            @Override
            public void notifyObservers(EventoDominio e) {
                eventos.add(e);
            }
        };
        var usuarioRepo = new JdbcUsuarioRepository(conexao);
        var perfilRepo = new JdbcPerfilRepository(conexao);
        var compRepo = new JdbcCompartilhamentoRepository(conexao);
        perfis = new PerfilService(perfilRepo, publicador);
        projetos = new ProjetoService(contandoGravacoes(new JdbcProjetoRepository(conexao)), perfilRepo, compRepo, log,
                new CalculoEstimativaService(), publicador);
        compartilhamentos = new CompartilhamentoService(compRepo, new JdbcProjetoRepository(conexao), usuarioRepo, log, publicador);
        exportacao = new ExportacaoService(new CalculoEstimativaService(), log);
        // gravados direto no repositório: a senha não importa aqui e gerar o hash custa caro
        ana = new Usuario(UUID.randomUUID(), "Ana", "ana@x.com", "hash", LocalDateTime.now());
        beto = new Usuario(UUID.randomUUID(), "Beto", "beto@x.com", "hash", LocalDateTime.now());
        usuarioRepo.salvar(ana);
        usuarioRepo.salvar(beto);
        padrao = perfis.listarVisiveis(ana).stream().filter(Perfil::isPadrao).findFirst().orElseThrow();
        ExibirMensagemCommand.setExibidor((tipo, texto) -> {
            if (tipo != com.br.estimativadeprojetodesoftware.command.TipoMensagem.SUCESSO) {
                mensagensDeErro.add(texto);
            }
        });
        ConfirmarCommand.setConfirmador(c -> {
            perguntasDeConfirmacao.add(c.getMensagem());
            return respostaConfirmacao;
        });
    }

    @AfterEach
    void limpar() throws Exception {
        ExibirMensagemCommand.setExibidor(null);
        ConfirmarCommand.setConfirmador(null);
        PerguntarCommand.setPerguntador(null);
        // deixa rodar o que o Observer agendou enquanto o banco ainda está aberto
        SwingUtilities.invokeAndWait(() -> {
        });
        conexao.close();
    }

    private IProjetoRepository contandoGravacoes(IProjetoRepository real) {
        return (IProjetoRepository) Proxy.newProxyInstance(getClass().getClassLoader(), new Class<?>[]{IProjetoRepository.class},
                (proxy, metodo, args) -> {
                    if (metodo.getName().equals("salvar")) {
                        gravacoes.incrementAndGet();
                    }
                    try {
                        return metodo.invoke(real, args);
                    } catch (InvocationTargetException e) {
                        throw e.getCause();
                    }
                });
    }

    private static void naThreadDoSwing(Executable corpo) throws Exception {
        Throwable[] falha = new Throwable[1];
        SwingUtilities.invokeAndWait(() -> {
            try {
                corpo.execute();
            } catch (Throwable t) {
                falha[0] = t;
            }
        });
        if (falha[0] instanceof Error erro) {
            throw erro;
        }
        if (falha[0] instanceof Exception excecao) {
            throw excecao;
        }
    }

    // ---------- apoio: abrir o projeto e agir como o usuário ----------

    private void abrir(UUID id, Usuario usuario) {
        view = new ProjetoView();
        presenter = new ManterProjetoPresenter(view, projetos, perfis, exportacao, usuario);
        presenter.abrirProjeto(id);
    }

    private Projeto criarEAbrirEmEdicao() {
        Projeto p = projetos.criar("Loja", List.of(padrao.getId()), ana);
        abrir(p.getId(), ana);
        view.getBtnEditar().doClick();
        return p;
    }

    /** Linha de uma opção pelo nome, na tabela de funcionalidades ou numa tabela da seção Tamanho e UI. */
    private record Celula(JTable tabela, int linha, int colunaWeb) {
    }

    private Celula celula(String nome) {
        for (int i = 0; i < tabelaItens().getRowCount(); i++) {
            if (nome.equals(tabelaItens().getValueAt(i, 2))) {
                return new Celula(tabelaItens(), i, COLUNA_WEB);
            }
        }
        for (JTable tabela : view.getTabelasConfiguracao()) {
            for (int i = 0; i < tabela.getRowCount(); i++) {
                if (nome.equals(tabela.getValueAt(i, 1))) {
                    return new Celula(tabela, i, 2); // sem as colunas Categoria e Seleção
                }
            }
        }
        throw new IllegalArgumentException("Linha não encontrada: " + nome);
    }

    private void marcarFuncionalidade(String nome) {
        Celula c = celula(nome);
        c.tabela().setValueAt(true, c.linha(), COLUNA_SELECIONAR);
    }

    private void desmarcarFuncionalidade(String nome) {
        Celula c = celula(nome);
        c.tabela().setValueAt(false, c.linha(), COLUNA_SELECIONAR);
    }

    private Object selecao(String nome) {
        Celula c = celula(nome);
        return c.tabela().getValueAt(c.linha(), COLUNA_SELECIONAR);
    }

    private TabelaPadrao tabelaItens() {
        return view.getTabelaItens();
    }

    private int linhaDe(String nome) {
        return linhaDe(tabelaItens(), 2, nome);
    }

    private int linhaDe(String categoria, String nome) {
        for (int i = 0; i < tabelaItens().getRowCount(); i++) {
            if (categoria.equals(tabelaItens().getValueAt(i, 1)) && nome.equals(tabelaItens().getValueAt(i, 2))) {
                return i;
            }
        }
        throw new IllegalArgumentException("Linha não encontrada: " + categoria + " / " + nome);
    }

    private static int linhaDe(JTable tabela, int coluna, String texto) {
        for (int i = 0; i < tabela.getRowCount(); i++) {
            if (texto.equals(String.valueOf(tabela.getValueAt(i, coluna)))) {
                return i;
            }
        }
        throw new IllegalArgumentException("Linha não encontrada: " + texto);
    }

    private void definirTaxa(Papel papel, int colunaPlataforma, String valor) {
        view.getTabelaTaxas().setValueAt(valor, papel.ordinal(), colunaPlataforma);
    }

    private void recalcular() {
        presenter.executarRecalculoPendente();
    }

    private String preco() {
        return view.getIndicadorPreco().getLblValor().getText();
    }

    private String prazo() {
        return view.getIndicadorPrazo().getLblValor().getText();
    }

    private static String reais(Object valor) {
        return TabelaPadrao.formatar(valor, TabelaPadrao.Formato.REAIS);
    }

    private static String dias(Object valor) {
        return TabelaPadrao.formatar(valor, TabelaPadrao.Formato.NUMERO);
    }

    private void montarPrototipoDaPlanilha() {
        view.getChkIos().doClick();
        marcarFuncionalidade("Grande");
        marcarFuncionalidade("Profissional");
        marcarFuncionalidade("Gerente de Projeto");
        for (String nome : List.of("Cadastro por E-mail e Senha", "Cadastro pelo Facebook", "Cadastro pelo Twitter",
                "E-mails de Convite de Usuário", "Contas Multi-tenant", "Painel (Dashboard)", "E-mails Transacionais",
                "Tags", "Pesquisa de Texto Livre", "Fóruns ou Comentários", "Suporte Multilíngue",
                "Segurança baseada em Certificado SSL", "Proteção contra DoS", "Autenticação em Duas Etapas")) {
            marcarFuncionalidade(nome);
        }
        // o mesmo nome existe na categoria do iOS
        tabelaItens().setValueAt(true, linhaDe("Recursos Móveis Específicos (Android)", "Códigos de Barras ou QR Codes"), COLUNA_SELECIONAR);
        for (int coluna = 1; coluna <= 3; coluna++) {
            definirTaxa(Papel.DESENVOLVIMENTO, coluna, "450");
            definirTaxa(Papel.DESIGN, coluna, "550");
            definirTaxa(Papel.GERENCIA, coluna, "300");
        }
        String[] custos = {"1200", "2000", "0", "0", "0", "2000"};
        assertEquals(custos.length, view.getTabelaCustos().getRowCount());
        for (int i = 0; i < custos.length; i++) {
            view.getTabelaCustos().setValueAt(custos[i], i, 1);
        }
        view.getTxtImpostos().setText("42");
        view.getTxtLucro().setText("30");
    }

    // ---------- casos de aceitação ----------

    @Test
    @DisplayName("Caso de aceitação da Tabela 1 feito só por alterações na tela")
    void casoDeAceitacaoAoVivo() throws Exception {
        naThreadDoSwing(() -> {
            criarEAbrirEmEdicao();
            montarPrototipoDaPlanilha();
            recalcular();

            assertEquals("241 dias", prazo());
            assertEquals("8,0 meses", view.getIndicadorPrazo().getLblDetalhe().getText());
            assertEquals("R$ 109.966,67", view.getIndicadorDesenvolvimento().getLblValor().getText());
            assertEquals("R$ 212.597,67", preco());
            assertEquals("R$ 26.501,09", view.getIndicadorMedia().getLblValor().getText());

            JTable plataformas = view.getTabelaPlataformas();
            int web = linhaDe(plataformas, 0, "Web/Back-end");
            int android = linhaDe(plataformas, 0, "Android");
            int total = linhaDe(plataformas, 0, "Total");
            assertEquals("129,33", dias(plataformas.getValueAt(web, 5)));
            assertEquals("R$ 59.033,33", reais(plataformas.getValueAt(web, 6)));
            assertEquals("111,33", dias(plataformas.getValueAt(android, 5)));
            assertEquals("R$ 50.933,33", reais(plataformas.getValueAt(android, 6)));
            assertEquals("240,67", dias(plataformas.getValueAt(total, 5)));
            assertEquals("R$ 109.966,67", reais(plataformas.getValueAt(total, 6)));

            JTable financeiro = view.getTabelaFinanceiro();
            assertEquals("R$ 115.166,67", reais(financeiro.getValueAt(linhaDe(financeiro, 0, "Custo total"), 1)));
            assertEquals("R$ 212.597,67", reais(financeiro.getValueAt(linhaDe(financeiro, 0, "Preço final ao cliente"), 1)));
            assertTrue(mensagensDeErro.isEmpty());
        });
    }

    @Test
    @DisplayName("Dias de UI com uma, duas e três plataformas")
    void diasDeUiPorQuantidadeDePlataformas() throws Exception {
        naThreadDoSwing(() -> {
            criarEAbrirEmEdicao();
            marcarFuncionalidade("Profissional");
            marcarFuncionalidade("Pequeno");

            recalcular();
            assertEquals("21", somaDiasUi(), "três plataformas");

            view.getChkIos().doClick();
            view.getChkAndroid().doClick();
            recalcular();
            assertEquals("2,33", somaDiasUi(), "uma plataforma");

            marcarFuncionalidade("Grande");
            view.getChkAndroid().doClick();
            recalcular();
            assertEquals("46,67", somaDiasUi(), "duas plataformas");
        });
    }

    private String somaDiasUi() {
        JTable t = view.getTabelaPlataformas();
        BigDecimal soma = BigDecimal.ZERO;
        for (int i = 0; i < t.getRowCount(); i++) {
            if (!"Total".equals(t.getValueAt(i, 0))) {
                soma = soma.add((BigDecimal) t.getValueAt(i, 2));
            }
        }
        return dias(soma);
    }

    // ---------- recálculo por origem ----------

    @Test
    @DisplayName("Marcar funcionalidade soma nas plataformas marcadas, sem trocar de seção")
    void marcarFuncionalidadeAtualiza() throws Exception {
        naThreadDoSwing(() -> {
            criarEAbrirEmEdicao();
            view.getChkIos().doClick();
            recalcular();
            assertEquals("0 dias", prazo());
            SecaoProjeto secao = view.getSecaoVisivel();

            marcarFuncionalidade("Cadastro por E-mail e Senha");
            recalcular();

            assertEquals("2 dias", prazo());
            assertEquals(secao, view.getSecaoVisivel());
        });
    }

    @Test
    @DisplayName("Toda origem de alteração muda a estimativa")
    void todasAsOrigensAtualizam() throws Exception {
        naThreadDoSwing(() -> {
            criarEAbrirEmEdicao();
            marcarFuncionalidade("Pequeno");
            recalcular();

            String anterior = preco();
            view.getChkIos().doClick();
            recalcular();
            assertNotEquals(anterior, preco(), "plataforma");
            assertEquals(-1, indiceDaPlataforma("iOS"), "linha da plataforma desmarcada sai do detalhamento");

            anterior = preco();
            definirTaxa(Papel.DESENVOLVIMENTO, 1, "999");
            recalcular();
            assertNotEquals(anterior, preco(), "taxa");

            anterior = preco();
            Celula pequeno = celula("Pequeno");
            pequeno.tabela().setValueAt("77", pequeno.linha(), pequeno.colunaWeb());
            recalcular();
            assertNotEquals(anterior, preco(), "dias da funcionalidade");

            anterior = preco();
            view.getTxtLucro().setText("10");
            recalcular();
            assertNotEquals(anterior, preco(), "lucro");
            assertEquals("10", view.getTxtLucro().getText(), "o recálculo não reescreve o campo em digitação");

            anterior = preco();
            view.getTxtImpostos().setText("10");
            recalcular();
            assertNotEquals(anterior, preco(), "impostos");

            anterior = preco();
            view.getTabelaCustos().setValueAt("1234,00", 0, 1);
            recalcular();
            assertNotEquals(anterior, preco(), "valor de custo");

            anterior = preco();
            view.getTabelaCustos().setRowSelectionInterval(0, 0);
            view.getBtnRemoverCusto().doClick();
            recalcular();
            assertNotEquals(anterior, preco(), "remover custo");

            List<String> respostas = new ArrayList<>(List.of("Fundo de reserva", "2000"));
            PerguntarCommand.setPerguntador(p -> respostas.remove(0));
            anterior = preco();
            view.getBtnAdicionarCusto().doClick();
            recalcular();
            assertNotEquals(anterior, preco(), "adicionar custo");
            JTable financeiro = view.getTabelaFinanceiro();
            assertEquals("R$ 2.000,00", reais(financeiro.getValueAt(linhaDe(financeiro, 0, "  Fundo de reserva"), 1)));
            assertTrue(mensagensDeErro.isEmpty());
        });
    }

    private int indiceDaPlataforma(String nome) {
        JTable t = view.getTabelaPlataformas();
        for (int i = 0; i < t.getRowCount(); i++) {
            if (nome.equals(t.getValueAt(i, 0))) {
                return i;
            }
        }
        return -1;
    }

    @Test
    @DisplayName("Uma alteração agenda o recálculo, que roda sozinho em até 500 ms")
    void recalculoRodaSozinho() throws Exception {
        naThreadDoSwing(() -> {
            criarEAbrirEmEdicao();
            view.getChkIos().doClick();
            recalcular();
            marcarFuncionalidade("Cadastro por E-mail e Senha");
            assertEquals("0 dias", prazo(), "ainda não rodou");
        });
        Thread.sleep(500);
        naThreadDoSwing(() -> assertEquals("2 dias", prazo()));
    }

    @Test
    @DisplayName("Temporizador que termina depois de cancelar a edição não faz nada")
    void disparoAtrasadoForaDaEdicao() throws Exception {
        naThreadDoSwing(() -> {
            criarEAbrirEmEdicao();
            marcarFuncionalidade("Pequeno");
            view.getBtnCancelar().doClick();
            recalcular();
            assertEquals("—", preco());
        });
    }

    // ---------- dados inválidos ----------

    @Test
    @DisplayName("Sem plataforma: motivo na tela, sem diálogo; volta ao remarcar")
    void semPlataforma() throws Exception {
        naThreadDoSwing(() -> {
            criarEAbrirEmEdicao();
            view.getChkWeb().doClick();
            view.getChkIos().doClick();
            view.getChkAndroid().doClick();
            recalcular();

            assertEquals("—", preco());
            assertTrue(view.getLblAvisoEstimativa().isVisible());
            assertTrue(view.getLblAvisoEstimativa().getText().contains("Marque ao menos uma plataforma"));
            assertTrue(mensagensDeErro.isEmpty());

            view.getChkAndroid().doClick();
            recalcular();
            assertNotEquals("—", preco());
            assertFalse(view.getLblAvisoEstimativa().isVisible());
            assertNotEquals(-1, indiceDaPlataforma("Android"));
            assertEquals(-1, indiceDaPlataforma("iOS"));
        });
    }

    @Test
    @DisplayName("Percentual inválido destaca o campo, mantém a estimativa e não troca de seção")
    void percentualInvalido() throws Exception {
        naThreadDoSwing(() -> {
            criarEAbrirEmEdicao();
            marcarFuncionalidade("Pequeno");
            recalcular();
            String anterior = preco();
            presenter.escolherSecao(SecaoProjeto.FUNCIONALIDADES);

            view.getTxtImpostos().setText("abc");
            recalcular();

            assertEquals(anterior, preco());
            assertNotNull(view.getMensagemErro(view.getTxtImpostos()));
            assertTrue(view.getLblAvisoEstimativa().getText().contains("desatualizada"));
            assertEquals(SecaoProjeto.FUNCIONALIDADES, view.getSecaoVisivel());
            assertTrue(mensagensDeErro.isEmpty());

            view.getTxtImpostos().setText("10");
            recalcular();
            assertNotEquals(anterior, preco());
            assertNull(view.getMensagemErro(view.getTxtImpostos()));
            assertFalse(view.getLblAvisoEstimativa().isVisible());
        });
    }

    @Test
    @DisplayName("Campo apagado para redigitar: sem erro, estimativa mantida como desatualizada")
    void campoVazioDuranteDigitacao() throws Exception {
        naThreadDoSwing(() -> {
            criarEAbrirEmEdicao();
            marcarFuncionalidade("Pequeno");
            recalcular();
            String anterior = preco();

            view.getTxtLucro().setText("");
            recalcular();

            assertEquals(anterior, preco());
            assertNull(view.getMensagemErro(view.getTxtLucro()));
            assertTrue(view.getLblAvisoEstimativa().getText().contains("desatualizada"));

            view.getTxtLucro().setText("25");
            recalcular();
            assertTrue(linhaDe(view.getTabelaFinanceiro(), 0, "Lucro (25,00%)") >= 0);
            assertFalse(view.getLblAvisoEstimativa().isVisible());
        });
    }

    @Test
    @DisplayName("Salvar com percentual inválido ou vazio destaca o campo e não grava")
    void salvarComPercentualInvalido() throws Exception {
        naThreadDoSwing(() -> {
            criarEAbrirEmEdicao();
            int antes = gravacoes.get();

            view.getTxtImpostos().setText("abc");
            view.getBtnSalvar().doClick();
            assertEquals(antes, gravacoes.get());
            assertNotNull(view.getMensagemErro(view.getTxtImpostos()));
            assertEquals(SecaoProjeto.CUSTOS, view.getSecaoVisivel(), "mostra a seção do campo com erro");
            assertEquals("Custos e preço", view.getListaSecoes().getSelectedValue().rotulo());

            view.getTxtImpostos().setText("");
            view.getBtnSalvar().doClick();
            assertEquals(antes, gravacoes.get());
            assertNotNull(view.getMensagemErro(view.getTxtImpostos()));
            assertTrue(view.getBtnSalvar().isEnabled(), "continua em edição");
        });
    }

    @Test
    @DisplayName("Nome em branco não impede a estimativa, só o salvamento")
    void nomeEmBrancoNaoTravaEstimativa() throws Exception {
        naThreadDoSwing(() -> {
            criarEAbrirEmEdicao();
            view.getChkIos().doClick();
            recalcular();
            view.getTxtNome().setText("");
            marcarFuncionalidade("Cadastro por E-mail e Senha");
            recalcular();
            assertEquals("2 dias", prazo());

            int antes = gravacoes.get();
            view.getBtnSalvar().doClick();
            assertEquals(antes, gravacoes.get());
        });
    }

    // ---------- persistência ----------

    @Test
    @DisplayName("Dez alterações e Salvar: uma gravação, um registro no log, um evento")
    void variasAlteracoesUmaGravacao() throws Exception {
        naThreadDoSwing(() -> {
            Projeto p = criarEAbrirEmEdicao();
            int gravacoesAntes = gravacoes.get();
            int registrosAntes = log.registros.size();
            eventos.clear();

            for (int i = 1; i <= 10; i++) {
                view.getTabelaTaxas().setValueAt(String.valueOf(100 + i), Papel.DESENVOLVIMENTO.ordinal(), 1);
                recalcular();
            }
            assertEquals(gravacoesAntes, gravacoes.get(), "recálculo não grava");
            assertEquals(registrosAntes, log.registros.size(), "recálculo não registra no log");
            assertTrue(eventos.isEmpty(), "recálculo não notifica outras janelas");
            assertFalse(projetos.buscarParaVisualizacao(p.getId(), ana).isCalculado());

            view.getBtnSalvar().doClick();

            assertEquals(gravacoesAntes + 1, gravacoes.get());
            assertEquals(registrosAntes + 1, log.registros.size());
            assertEquals(1, eventos.size());
            assertTrue(projetos.buscarParaVisualizacao(p.getId(), ana).isCalculado());
            assertEquals(0, new BigDecimal("110").compareTo(projetos.buscarParaVisualizacao(p.getId(), ana)
                    .getTaxas().get(com.br.estimativadeprojetodesoftware.model.Plataforma.WEB_BACKEND, Papel.DESENVOLVIMENTO)));
            assertTrue(view.getBtnExportarPdf().isEnabled());
            assertFalse(view.getBtnSalvar().isEnabled());
        });
    }

    @Test
    @DisplayName("Cancelar devolve a estimativa salva sem gravar")
    void cancelarDevolveEstimativaSalva() throws Exception {
        naThreadDoSwing(() -> {
            Projeto p = criarEAbrirEmEdicao();
            marcarFuncionalidade("Pequeno");
            view.getBtnSalvar().doClick();
            String salvo = preco();
            int antes = gravacoes.get();

            view.getBtnEditar().doClick();
            marcarFuncionalidade("Grande");
            view.getTxtLucro().setText("90");
            recalcular();
            assertNotEquals(salvo, preco());

            view.getBtnCancelar().doClick();

            assertEquals(salvo, preco());
            assertEquals(antes, gravacoes.get());
            assertEquals("30", view.getTxtLucro().getText());
            assertTrue(projetos.buscarParaVisualizacao(p.getId(), ana)
                    .buscarItem("Tamanho do App", "Pequeno").orElseThrow().isSelecionado());
        });
    }

    @Test
    @DisplayName("Salvar sem plataforma fica não calculado e não exporta")
    void salvarSemPlataforma() throws Exception {
        naThreadDoSwing(() -> {
            Projeto p = criarEAbrirEmEdicao();
            view.getChkWeb().doClick();
            view.getChkIos().doClick();
            view.getChkAndroid().doClick();

            view.getBtnSalvar().doClick();

            assertFalse(projetos.buscarParaVisualizacao(p.getId(), ana).isCalculado());
            assertEquals("—", preco());
            assertFalse(view.getBtnExportarPdf().isEnabled());
            assertTrue(mensagensDeErro.isEmpty());
        });
    }

    // ---------- painel ----------

    @Test
    @DisplayName("Resumo no rodapé, fora das seções; barra lateral e resumo ocultos só na inclusão")
    void resumoNoRodape() throws Exception {
        naThreadDoSwing(() -> {
            criarEAbrirEmEdicao();
            assertFalse(SwingUtilities.isDescendingFrom(view.getPainelEstimativa(), view.getPainelSecoes()));
            assertTrue(SwingUtilities.isDescendingFrom(view.getIndicadorPreco(), view.getPainelEstimativa()));
            assertTrue(view.getPainelEstimativa().isVisible());
            assertTrue(view.getPainelSecoes().isVisible());

            ProjetoView viewInclusao = new ProjetoView();
            new ManterProjetoPresenter(viewInclusao, projetos, perfis, exportacao, ana).iniciarInclusao();
            assertFalse(viewInclusao.getPainelEstimativa().isVisible());
            assertFalse(viewInclusao.getPainelSecoes().isVisible());
            assertTrue(viewInclusao.getPainelPerfis().isVisible());
            assertTrue(viewInclusao.getTxtNome().isEditable());
        });
    }

    @Test
    @DisplayName("Projeto não calculado em visualização: indicadores indisponíveis e orientação")
    void naoCalculadoEmVisualizacao() throws Exception {
        naThreadDoSwing(() -> {
            Projeto p = projetos.criar("Novo", List.of(padrao.getId()), ana);
            abrir(p.getId(), ana);

            assertTrue(view.getPainelEstimativa().isVisible());
            assertEquals("—", preco());
            assertEquals("—", prazo());
            assertTrue(view.getLblAvisoEstimativa().getText().contains("Editar"));
        });
    }

    @Test
    @DisplayName("Destinatário vê a estimativa salva e não altera nada")
    void somenteLeitura() throws Exception {
        naThreadDoSwing(() -> {
            Projeto p = criarEAbrirEmEdicao();
            marcarFuncionalidade("Pequeno");
            view.getBtnSalvar().doClick();
            String salvo = preco();
            compartilhamentos.compartilhar(p.getId(), beto.getId(), ana);

            abrir(p.getId(), beto);

            assertEquals(salvo, preco());
            assertFalse(view.getChkWeb().isEnabled());
            assertFalse(view.getBtnEditar().isEnabled());
            assertFalse(tabelaItens().isCellEditable(0, COLUNA_SELECIONAR));
            assertFalse(view.getTxtLucro().isEditable());

            // filtros também valem aqui
            view.getTxtBuscaFuncionalidades().setText("twitter");
            assertEquals(1, tabelaItens().getRowCount());
        });
    }

    @Test
    @DisplayName("Variação do preço: aparece quando muda e some quando não muda")
    void variacaoDoPreco() throws Exception {
        naThreadDoSwing(() -> {
            criarEAbrirEmEdicao();
            view.getTxtLucro().setText("0");
            view.getTxtImpostos().setText("0");
            recalcular();

            List<String> respostas = new ArrayList<>(List.of("Fundo", "450"));
            PerguntarCommand.setPerguntador(p -> respostas.remove(0));
            view.getBtnAdicionarCusto().doClick();
            recalcular();
            assertTrue(view.getIndicadorPreco().getLblDetalhe().getText().startsWith("+ R$ 450,00"));

            view.getTabelaCustos().setValueAt("Fundo 2", view.getTabelaCustos().getRowCount() - 1, 0);
            recalcular();
            assertEquals("impostos 0,00% · lucro 0,00%", view.getIndicadorPreco().getLblDetalhe().getText(), "alteração sem efeito no preço");

            view.getTabelaCustos().setValueAt("0", view.getTabelaCustos().getRowCount() - 1, 1);
            recalcular();
            assertTrue(view.getIndicadorPreco().getLblDetalhe().getText().startsWith("- R$ 450,00"));
        });
    }

    @Test
    @DisplayName("Abrir ou recarregar um projeto zera a referência da variação")
    void variacaoZeradaAoCarregar() throws Exception {
        naThreadDoSwing(() -> {
            criarEAbrirEmEdicao();
            marcarFuncionalidade("Pequeno");
            view.getBtnSalvar().doClick();
            view.getBtnEditar().doClick();
            marcarFuncionalidade("Grande");
            recalcular();
            assertTrue(view.getIndicadorPreco().getLblDetalhe().getText().startsWith("+ R$"));

            view.getBtnCancelar().doClick();
            assertFalse(view.getIndicadorPreco().getLblDetalhe().getText().contains("R$"));
        });
    }

    // ---------- localizar funcionalidades ----------

    @Test
    @DisplayName("Busca ignora maiúsculas, olha nome e categoria e preserva ordem e seleção")
    void busca() throws Exception {
        naThreadDoSwing(() -> {
            criarEAbrirEmEdicao();
            int total = tabelaItens().getRowCount();
            String primeira = String.valueOf(tabelaItens().getValueAt(0, 2));
            marcarFuncionalidade("Cadastro por E-mail e Senha");

            view.getTxtBuscaFuncionalidades().setText("CADASTRO");
            assertTrue(tabelaItens().getRowCount() > 0 && tabelaItens().getRowCount() < total);
            for (int i = 0; i < tabelaItens().getRowCount(); i++) {
                assertTrue(String.valueOf(tabelaItens().getValueAt(i, 2)).toLowerCase().contains("cadastro"));
            }

            view.getTxtBuscaFuncionalidades().setText("segurança");
            assertTrue(tabelaItens().getRowCount() >= 3, "busca pela categoria");

            view.getTxtBuscaFuncionalidades().setText("");
            assertEquals(total, tabelaItens().getRowCount());
            assertEquals(primeira, tabelaItens().getValueAt(0, 2));
            assertEquals(Boolean.TRUE, tabelaItens().getValueAt(linhaDe("Cadastro por E-mail e Senha"), COLUNA_SELECIONAR));
        });
    }

    @Test
    @DisplayName("Marcar uma linha filtrada seleciona a funcionalidade certa")
    void marcarLinhaFiltrada() throws Exception {
        naThreadDoSwing(() -> {
            Projeto p = criarEAbrirEmEdicao();
            view.getChkIos().doClick();
            recalcular();
            view.getTxtBuscaFuncionalidades().setText("Cadastro pelo Twitter");
            assertEquals(1, tabelaItens().getRowCount());

            tabelaItens().setValueAt(true, 0, COLUNA_SELECIONAR);
            recalcular();

            assertTrue(presenter.getProjeto().buscarItem("Usuários e Contas", "Cadastro pelo Twitter").orElseThrow().isSelecionado());
            assertEquals(1, presenter.getProjeto().getItens().stream().filter(i -> i.isSelecionado()).count());
            assertTrue(view.getLblContadorSelecionadas().getText().startsWith("1 de "));
            assertNotEquals("0 dias", prazo());
        });
    }

    @Test
    @DisplayName("Somente selecionadas: linha desmarcada sai da tabela e o contador diminui")
    void somenteSelecionadas() throws Exception {
        naThreadDoSwing(() -> {
            criarEAbrirEmEdicao();
            marcarFuncionalidade("Cadastro por E-mail e Senha");
            marcarFuncionalidade("Cadastro pelo Twitter");
            assertTrue(view.getLblContadorSelecionadas().getText().startsWith("2 de "));

            ((JToggleButton) view.getBtnSomenteSelecionadas()).doClick();
            assertEquals(2, tabelaItens().getRowCount());

            desmarcarFuncionalidade("Cadastro pelo Twitter");
            assertEquals(1, tabelaItens().getRowCount());
            assertTrue(view.getLblContadorSelecionadas().getText().startsWith("1 de "));
        });
    }

    @Test
    @DisplayName("Filtro continua valendo depois de cancelar a edição")
    void filtroSobreviveAoRecarregar() throws Exception {
        naThreadDoSwing(() -> {
            criarEAbrirEmEdicao();
            view.getTxtBuscaFuncionalidades().setText("twitter");
            assertEquals(1, tabelaItens().getRowCount());

            view.getBtnCancelar().doClick();

            assertEquals(1, tabelaItens().getRowCount());
        });
    }

    // ---------- seções ----------

    private NavegacaoProjetoModel navegacao() {
        return (NavegacaoProjetoModel) view.getListaSecoes().getModel();
    }

    private String entradaEscolhida() {
        return view.getListaSecoes().getSelectedValue().rotulo();
    }

    private void escolherCategoria(String categoria) {
        view.getListaSecoes().setSelectedIndex(navegacao().indiceDe(SecaoProjeto.FUNCIONALIDADES, categoria));
        assertEquals(categoria == null ? "Funcionalidades" : categoria, entradaEscolhida());
        assertEquals(SecaoProjeto.FUNCIONALIDADES, view.getSecaoVisivel());
    }

    private int contagemCategoria(String categoria) {
        return navegacao().getSelecionadas(categoria);
    }

    private void assertSoDaCategoria(String categoria) {
        assertTrue(tabelaItens().getRowCount() > 0);
        for (int i = 0; i < tabelaItens().getRowCount(); i++) {
            assertEquals(categoria, tabelaItens().getValueAt(i, 1));
        }
    }

    private int diasDoPrazo() {
        return Integer.parseInt(prazo().replace(" dias", "").replace(".", ""));
    }

    @Test
    @DisplayName("Ao abrir, a seção Funcionalidades está escolhida e mostra todas as categorias")
    void aberturaNaSecaoFuncionalidades() throws Exception {
        naThreadDoSwing(() -> {
            criarEAbrirEmEdicao();
            assertEquals(SecaoProjeto.FUNCIONALIDADES, view.getSecaoVisivel());
            assertEquals("Funcionalidades", entradaEscolhida());
            assertTrue(tabelaItens().getRowCount() > 60);
        });
    }

    @Test
    @DisplayName("Escolher uma seção mostra só ela, sem alterar dados")
    void umaSecaoPorVez() throws Exception {
        naThreadDoSwing(() -> {
            criarEAbrirEmEdicao();
            int antes = gravacoes.get();

            presenter.escolherSecao(SecaoProjeto.TAXAS);

            assertEquals(SecaoProjeto.TAXAS, view.getSecaoVisivel());
            assertEquals("Taxas diárias", entradaEscolhida());
            assertEquals(antes, gravacoes.get());
            view.getBtnFechar().doClick();
            assertTrue(perguntasDeConfirmacao.isEmpty(), "trocar de seção não é alteração");
        });
    }

    @Test
    @DisplayName("Plataformas ficam na própria seção e continuam mudando a estimativa")
    void plataformasEmSecaoPropria() throws Exception {
        naThreadDoSwing(() -> {
            criarEAbrirEmEdicao();
            assertTrue(SwingUtilities.isDescendingFrom(view.getChkIos(), view.getPainelSecoes()));
            if (!view.getChkIos().isSelected()) {
                view.getChkIos().doClick();
            }
            marcarFuncionalidade("Cadastro por E-mail e Senha");
            recalcular();
            int comIos = diasDoPrazo();
            presenter.escolherSecao(SecaoProjeto.PLATAFORMAS);

            view.getChkIos().doClick();
            recalcular();

            assertEquals(comIos - 1, diasDoPrazo());
        });
    }

    // ---------- seção Tamanho e UI ----------

    @Test
    @DisplayName("Tamanho do App, Nível de UI e Gerente de Projeto ficam fora da tabela de funcionalidades")
    void configuracaoForaDaTabela() throws Exception {
        naThreadDoSwing(() -> {
            criarEAbrirEmEdicao();
            List<String> configuracao = List.of("Tamanho do App", "Nível de UI", "Gerente de Projeto");
            for (int i = 0; i < tabelaItens().getRowCount(); i++) {
                assertFalse(configuracao.contains(tabelaItens().getValueAt(i, 1)));
            }
            view.getTxtBuscaFuncionalidades().setText("grande");
            assertEquals(0, tabelaItens().getRowCount(), "nem pela busca");

            assertEquals(3, view.getTabelasConfiguracao().size());
            assertEquals(3, view.getTabelasConfiguracao().get(0).getRowCount());
            assertEquals("Pequeno", view.getTabelasConfiguracao().get(0).getValueAt(0, 1));
            assertInstanceOf(com.br.estimativadeprojetodesoftware.view.componentes.RenderizadorOpcao.class,
                    view.getTabelasConfiguracao().get(0).getColumnModel().getColumn(0).getCellRenderer(), "escolha única");
            assertFalse(view.getTabelasConfiguracao().get(2).getColumnModel().getColumn(0).getCellRenderer()
                    instanceof com.br.estimativadeprojetodesoftware.view.componentes.RenderizadorOpcao, "Gerente de Projeto é caixa");
        });
    }

    @Test
    @DisplayName("Escolher o tamanho desmarca o anterior e recalcula")
    void escolherTamanho() throws Exception {
        naThreadDoSwing(() -> {
            criarEAbrirEmEdicao();
            marcarFuncionalidade("Pequeno");
            recalcular();
            int comPequeno = diasDoPrazo();

            marcarFuncionalidade("Grande");
            recalcular();

            assertEquals(Boolean.FALSE, selecao("Pequeno"));
            assertEquals(Boolean.TRUE, selecao("Grande"));
            assertTrue(diasDoPrazo() > comPequeno);
        });
    }

    @Test
    @DisplayName("Clicar no tamanho já marcado o desmarca")
    void desmarcarTamanho() throws Exception {
        naThreadDoSwing(() -> {
            criarEAbrirEmEdicao();
            marcarFuncionalidade("Grande");
            recalcular();
            int comGrande = diasDoPrazo();

            desmarcarFuncionalidade("Grande");
            recalcular();

            for (String tamanho : List.of("Pequeno", "Médio", "Grande")) {
                assertEquals(Boolean.FALSE, selecao(tamanho));
            }
            assertTrue(diasDoPrazo() < comGrande);
        });
    }

    @Test
    @DisplayName("Editar os dias de uma opção na seção Tamanho e UI muda o prazo")
    void editarDiasDoTamanho() throws Exception {
        naThreadDoSwing(() -> {
            criarEAbrirEmEdicao();
            for (var plataforma : List.of(view.getChkWeb(), view.getChkIos())) {
                if (plataforma.isSelected()) {
                    plataforma.doClick();
                }
            }
            if (!view.getChkAndroid().isSelected()) {
                view.getChkAndroid().doClick();
            }
            marcarFuncionalidade("Médio");
            recalcular();
            int antes = diasDoPrazo();

            Celula medio = celula("Médio");
            medio.tabela().setValueAt("35", medio.linha(), medio.colunaWeb() + 2); // Android
            recalcular();

            assertEquals(antes + 5, diasDoPrazo());
        });
    }

    @Test
    @DisplayName("Marcar Gerente de Projeto inclui a gerência na estimativa")
    void gerenteDeProjeto() throws Exception {
        naThreadDoSwing(() -> {
            criarEAbrirEmEdicao();
            marcarFuncionalidade("Médio");
            recalcular();
            String sem = preco();

            marcarFuncionalidade("Gerente de Projeto");
            recalcular();

            assertEquals(Boolean.TRUE, selecao("Gerente de Projeto"));
            assertNotEquals(sem, preco());
        });
    }

    @Test
    @DisplayName("Destinatário vê as opções marcadas da seção Tamanho e UI sem poder alterá-las")
    void tamanhoEmSomenteLeitura() throws Exception {
        naThreadDoSwing(() -> {
            Projeto p = criarEAbrirEmEdicao();
            marcarFuncionalidade("Médio");
            view.getBtnSalvar().doClick();
            compartilhamentos.compartilhar(p.getId(), beto.getId(), ana);

            abrir(p.getId(), beto);
            presenter.escolherSecao(SecaoProjeto.TAMANHO_E_UI);

            assertEquals(SecaoProjeto.TAMANHO_E_UI, view.getSecaoVisivel());
            assertEquals(Boolean.TRUE, selecao("Médio"));
            for (JTable tabela : view.getTabelasConfiguracao()) {
                for (int linha = 0; linha < tabela.getRowCount(); linha++) {
                    for (int coluna = 0; coluna < tabela.getColumnCount(); coluna++) {
                        assertFalse(tabela.isCellEditable(linha, coluna));
                    }
                }
            }
        });
    }

    // ---------- navegar pela barra lateral ----------

    @Test
    @DisplayName("Escolher uma categoria filtra a tabela; Funcionalidades devolve tudo com a seleção intacta")
    void escolherCategoria() throws Exception {
        naThreadDoSwing(() -> {
            criarEAbrirEmEdicao();
            int total = tabelaItens().getRowCount();
            String primeira = String.valueOf(tabelaItens().getValueAt(0, 2));
            marcarFuncionalidade("Cadastro pelo Twitter");

            escolherCategoria("Usuários e Contas");
            assertSoDaCategoria("Usuários e Contas");
            assertEquals(10, tabelaItens().getRowCount());
            assertEquals("Cadastro por E-mail e Senha", tabelaItens().getValueAt(0, 2), "ordem da planilha");

            escolherCategoria(null);
            assertEquals(total, tabelaItens().getRowCount());
            assertEquals(primeira, tabelaItens().getValueAt(0, 2));
            assertEquals(Boolean.TRUE, selecao("Cadastro pelo Twitter"));
        });
    }

    @Test
    @DisplayName("Contadores da barra lateral na abertura, sem as categorias de configuração")
    void contadoresNaAbertura() throws Exception {
        naThreadDoSwing(() -> {
            Projeto p = criarEAbrirEmEdicao();
            marcarFuncionalidade("Médio");
            marcarFuncionalidade("Cadastro por E-mail e Senha");
            marcarFuncionalidade("Cadastro pelo Facebook");
            marcarFuncionalidade("Cadastro pelo Twitter");
            view.getBtnSalvar().doClick();

            abrir(p.getId(), ana);

            assertEquals("Funcionalidades", entradaEscolhida());
            assertEquals(3, contagemCategoria("Usuários e Contas"));
            assertEquals(0, contagemCategoria("Segurança"));
            assertEquals(3, contagemCategoria(null));
            assertThrows(IllegalArgumentException.class, () -> contagemCategoria("Tamanho do App"));
            assertTrue(view.getLblContadorSelecionadas().getText().startsWith("3 de "));
        });
    }

    @Test
    @DisplayName("Marcar atualiza o contador da categoria e o de Funcionalidades sem trocar a entrada")
    void marcarAtualizaContadorDaCategoria() throws Exception {
        naThreadDoSwing(() -> {
            criarEAbrirEmEdicao();
            escolherCategoria("Segurança");
            int total = contagemCategoria(null);

            tabelaItens().setValueAt(true, linhaDe("Autenticação em Duas Etapas"), COLUNA_SELECIONAR);

            assertEquals(1, contagemCategoria("Segurança"));
            assertEquals(total + 1, contagemCategoria(null));
            assertEquals("Segurança", entradaEscolhida());
            assertSoDaCategoria("Segurança");
        });
    }

    @Test
    @DisplayName("Categoria combinada com a busca")
    void categoriaComBusca() throws Exception {
        naThreadDoSwing(() -> {
            criarEAbrirEmEdicao();
            view.getTxtBuscaFuncionalidades().setText("e-mail");
            int emTodas = tabelaItens().getRowCount();

            escolherCategoria("Usuários e Contas");

            assertEquals(2, tabelaItens().getRowCount());
            assertTrue(emTodas > tabelaItens().getRowCount(), "outras categorias também têm e-mail");
            assertSoDaCategoria("Usuários e Contas");
        });
    }

    @Test
    @DisplayName("Categoria combinada com Somente selecionadas")
    void categoriaComSomenteSelecionadas() throws Exception {
        naThreadDoSwing(() -> {
            criarEAbrirEmEdicao();
            marcarFuncionalidade("Autenticação em Duas Etapas");
            marcarFuncionalidade("Cadastro pelo Twitter");
            escolherCategoria("Segurança");
            ((JToggleButton) view.getBtnSomenteSelecionadas()).doClick();
            assertEquals(1, tabelaItens().getRowCount());

            tabelaItens().setValueAt(false, 0, COLUNA_SELECIONAR);

            assertEquals(0, tabelaItens().getRowCount());
            assertEquals(0, contagemCategoria("Segurança"));
            assertEquals("Segurança", entradaEscolhida());
        });
    }

    @Test
    @DisplayName("Destinatário em somente leitura navega pela barra lateral sem alterar valores")
    void categoriaEmSomenteLeitura() throws Exception {
        naThreadDoSwing(() -> {
            Projeto p = criarEAbrirEmEdicao();
            marcarFuncionalidade("Autenticação em Duas Etapas");
            view.getBtnSalvar().doClick();
            compartilhamentos.compartilhar(p.getId(), beto.getId(), ana);

            abrir(p.getId(), beto);
            assertTrue(view.getListaSecoes().isEnabled());
            escolherCategoria("Segurança");

            assertEquals(3, tabelaItens().getRowCount());
            for (int i = 0; i < tabelaItens().getRowCount(); i++) {
                assertFalse(tabelaItens().isCellEditable(i, COLUNA_SELECIONAR));
            }
            assertEquals(1, contagemCategoria("Segurança"));
        });
    }

    @Test
    @DisplayName("Cancelar a edição mantém a categoria ou a seção escolhida")
    void entradaSobreviveAoRecarregar() throws Exception {
        naThreadDoSwing(() -> {
            criarEAbrirEmEdicao();
            escolherCategoria("Segurança");
            view.getBtnCancelar().doClick();
            assertEquals("Segurança", entradaEscolhida());
            assertSoDaCategoria("Segurança");

            view.getBtnEditar().doClick();
            presenter.escolherSecao(SecaoProjeto.TAXAS);
            view.getBtnCancelar().doClick();
            assertEquals(SecaoProjeto.TAXAS, view.getSecaoVisivel());
        });
    }

    // ---------- fechar com alterações ----------

    @Test
    @DisplayName("Fechar com alterações pergunta; Descartar fecha sem gravar")
    void fecharComAlteracoesDescartando() throws Exception {
        naThreadDoSwing(() -> {
            Projeto p = criarEAbrirEmEdicao();
            int antes = gravacoes.get();
            marcarFuncionalidade("Pequeno");

            view.doDefaultCloseAction();

            assertEquals(1, perguntasDeConfirmacao.size());
            assertTrue(perguntasDeConfirmacao.get(0).contains("Loja"));
            assertTrue(view.isClosed());
            assertEquals(antes, gravacoes.get());
            assertFalse(projetos.buscarParaVisualizacao(p.getId(), ana)
                    .buscarItem("Tamanho do App", "Pequeno").orElseThrow().isSelecionado());
        });
    }

    @Test
    @DisplayName("Recusar a confirmação mantém a janela em edição, com as alterações")
    void fecharComAlteracoesRecusando() throws Exception {
        naThreadDoSwing(() -> {
            criarEAbrirEmEdicao();
            marcarFuncionalidade("Pequeno");
            respostaConfirmacao = false;

            view.getBtnFechar().doClick();

            assertEquals(1, perguntasDeConfirmacao.size());
            assertFalse(view.isClosed());
            assertTrue(view.getBtnSalvar().isEnabled());
            assertEquals(Boolean.TRUE, selecao("Pequeno"));
        });
    }

    @Test
    @DisplayName("Sem alteração fecha sem perguntar: entrar em edição e confirmar o mesmo valor não contam")
    void fecharSemAlteracoes() throws Exception {
        naThreadDoSwing(() -> {
            criarEAbrirEmEdicao();
            view.getTabelaTaxas().setValueAt(view.getTabelaTaxas().getValueAt(0, 1), 0, 1);

            view.getBtnFechar().doClick();

            assertTrue(perguntasDeConfirmacao.isEmpty());
            assertTrue(view.isClosed());
        });
    }

    @Test
    @DisplayName("Entrar em edição sem mexer em nada fecha sem perguntar")
    void entrarEmEdicaoNaoMarcaAlteracao() throws Exception {
        naThreadDoSwing(() -> {
            criarEAbrirEmEdicao();
            view.doDefaultCloseAction();
            assertTrue(perguntasDeConfirmacao.isEmpty());
            assertTrue(view.isClosed());
        });
    }

    @Test
    @DisplayName("Fechar depois de salvar não pergunta")
    void fecharDepoisDeSalvar() throws Exception {
        naThreadDoSwing(() -> {
            criarEAbrirEmEdicao();
            marcarFuncionalidade("Pequeno");
            view.getBtnSalvar().doClick();

            view.getBtnFechar().doClick();

            assertTrue(perguntasDeConfirmacao.isEmpty());
            assertTrue(view.isClosed());
        });
    }

    @Test
    @DisplayName("Renomear conta como alteração não salva")
    void renomearContaComoAlteracao() throws Exception {
        naThreadDoSwing(() -> {
            criarEAbrirEmEdicao();
            view.getTxtNome().setText("Outro nome");
            view.getBtnFechar().doClick();
            assertEquals(1, perguntasDeConfirmacao.size());
        });
    }

    @Test
    @DisplayName("Cancelar não pergunta")
    void cancelarNaoPergunta() throws Exception {
        naThreadDoSwing(() -> {
            criarEAbrirEmEdicao();
            marcarFuncionalidade("Pequeno");
            view.getBtnCancelar().doClick();
            assertTrue(perguntasDeConfirmacao.isEmpty());
            view.getBtnFechar().doClick();
            assertTrue(perguntasDeConfirmacao.isEmpty());
            assertTrue(view.isClosed());
        });
    }

    @Test
    @DisplayName("Projeto recém-criado e fechado sem salvar continua não calculado")
    void recemCriadoContinuaNaoCalculado() throws Exception {
        naThreadDoSwing(() -> {
            view = new ProjetoView();
            presenter = new ManterProjetoPresenter(view, projetos, perfis, exportacao, ana);
            presenter.iniciarInclusao();
            view.getTxtNome().setText("Recente");
            view.getBtnSalvar().doClick();
            assertTrue(view.getPainelEstimativa().isVisible(), "foi para a edição");
            UUID id = presenter.getProjeto().getId();

            view.getBtnFechar().doClick();

            assertTrue(perguntasDeConfirmacao.isEmpty());
            assertFalse(projetos.buscarParaVisualizacao(id, ana).isCalculado());
        });
    }
}
