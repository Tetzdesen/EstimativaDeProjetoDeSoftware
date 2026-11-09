package com.br.estimativadeprojetodesoftware.presenter.projeto;

import com.br.estimativadeprojetodesoftware.adapter.senha.ValidadorSenhaAdapter;
import com.br.estimativadeprojetodesoftware.command.ExibirMensagemCommand;
import com.br.estimativadeprojetodesoftware.model.Perfil;
import com.br.estimativadeprojetodesoftware.model.Projeto;
import com.br.estimativadeprojetodesoftware.model.Usuario;
import com.br.estimativadeprojetodesoftware.presenter.dashboard.DashboardPresenter;
import com.br.estimativadeprojetodesoftware.presenter.helpers.FilaBanco;
import com.br.estimativadeprojetodesoftware.presenter.usuario.LoginPresenter;
import com.br.estimativadeprojetodesoftware.repository.jdbc.JdbcCompartilhamentoRepository;
import com.br.estimativadeprojetodesoftware.repository.jdbc.JdbcPerfilRepository;
import com.br.estimativadeprojetodesoftware.repository.jdbc.JdbcProjetoRepository;
import com.br.estimativadeprojetodesoftware.repository.jdbc.JdbcUsuarioRepository;
import com.br.estimativadeprojetodesoftware.service.CalculoEstimativaService;
import com.br.estimativadeprojetodesoftware.service.DashboardService;
import com.br.estimativadeprojetodesoftware.service.ExportacaoService;
import com.br.estimativadeprojetodesoftware.service.PerfilService;
import com.br.estimativadeprojetodesoftware.service.ProjetoService;
import com.br.estimativadeprojetodesoftware.service.RegistroOperacoes;
import com.br.estimativadeprojetodesoftware.service.UsuarioService;
import com.br.estimativadeprojetodesoftware.singleton.ConexaoSingleton;
import com.br.estimativadeprojetodesoftware.singleton.UsuarioLogadoSingleton;
import com.br.estimativadeprojetodesoftware.suporte.AmbienteTeste;
import com.br.estimativadeprojetodesoftware.view.dashboard.DashboardView;
import com.br.estimativadeprojetodesoftware.view.projeto.ProjetoView;
import com.br.estimativadeprojetodesoftware.view.projeto.SecaoProjeto;
import com.br.estimativadeprojetodesoftware.view.tema.TemaClaro;
import com.br.estimativadeprojetodesoftware.view.tema.TemaEscuro;
import com.br.estimativadeprojetodesoftware.view.usuario.LoginView;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import javax.imageio.ImageIO;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.SwingUtilities;

/**
 * Gera as imagens do README com as telas reais e dados de demonstração num H2
 * em memória (nunca o banco configurado no .env). Não é um teste: rode com
 *
 * <pre>mvn -q test-compile && java -cp "target/classes:target/test-classes:$(cat cp.txt)" \
 *   com.br.estimativadeprojetodesoftware.presenter.projeto.GeradorVitrine docs/imagens target/vitrine/quadros</pre>
 *
 * e depois {@code python3 scripts/montar-gif.py} para montar o GIF.
 */
public final class GeradorVitrine {

    private static final int LARGURA = 1160;
    private static final int ALTURA = 720;

    private final Path destino;
    private final Path quadros;
    private ProjetoService projetos;
    private PerfilService perfis;
    private UsuarioService usuarios;
    private Usuario demo;
    private Perfil padrao;

    private GeradorVitrine(Path destino, Path quadros) {
        this.destino = destino;
        this.quadros = quadros;
    }

    public static void main(String[] args) throws Exception {
        FilaBanco.usarExecucaoImediata(true);
        ExibirMensagemCommand.setExibidor((tipo, texto) -> {
        });
        GeradorVitrine g = new GeradorVitrine(Path.of(args[0]), Path.of(args[1]));
        Files.createDirectories(g.destino);
        Files.createDirectories(g.quadros);
        SwingUtilities.invokeAndWait(() -> {
            try {
                g.prepararDados();
                for (String tema : List.of("claro", "escuro")) {
                    if (tema.equals("claro")) {
                        TemaClaro.setup();
                    } else {
                        TemaEscuro.setup();
                    }
                    g.telasDoProjeto(tema);
                    g.dashboard(tema);
                    g.login(tema);
                }
                TemaClaro.setup();
                g.quadrosDoGif();
                g.relatorioPdf();
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        });
        System.exit(0);
    }

    private void prepararDados() {
        Connection c = ConexaoSingleton.abrir(ConexaoSingleton.inicializadorPara("H2"),
                "jdbc:h2:mem:vitrine;DB_CLOSE_DELAY=-1", null, null);
        RegistroOperacoes semLog = new RegistroOperacoes() {
            @Override
            public void sucesso(String operacao, String nomeProjeto, String usuario) {
            }

            @Override
            public void falha(String operacao, String nomeProjeto, String usuario, String mensagem) {
            }
        };
        var publicador = new AmbienteTeste.Publicador();
        var perfilRepo = new JdbcPerfilRepository(c);
        perfis = new PerfilService(perfilRepo, publicador);
        projetos = new ProjetoService(new JdbcProjetoRepository(c), perfilRepo, new JdbcCompartilhamentoRepository(c), semLog,
                new CalculoEstimativaService(), publicador);
        usuarios = new UsuarioService(new JdbcUsuarioRepository(c), new ValidadorSenhaAdapter(), publicador);
        demo = new Usuario(UUID.randomUUID(), "Equipe Demo", "demo@exemplo.com", "hash", LocalDateTime.now());
        new JdbcUsuarioRepository(c).salvar(demo);
        UsuarioLogadoSingleton.getInstancia().setUsuario(demo);
        padrao = perfis.listarVisiveis(demo).stream().filter(Perfil::isPadrao).findFirst().orElseThrow();

        Projeto loja = projetos.criar("Loja virtual", List.of(padrao.getId()), demo);
        Cenarios.montarLoja(loja);
        projetos.salvarComEstimativa(loja, demo);
        Projeto agenda = projetos.criar("App de agendamento", List.of(padrao.getId()), demo);
        Cenarios.montarAgenda(agenda);
        projetos.salvarComEstimativa(agenda, demo);
    }

    /** Monta os dados de demonstração a partir dos nomes do perfil padrão. */
    private static final class Cenarios {

        static void marcar(Projeto p, String categoria, String nome) {
            p.selecionar(p.buscarItem(categoria, nome).orElseThrow().getFuncionalidade().getId(), true);
        }

        static void montarLoja(Projeto p) {
            p.setPlataformaMarcada(com.br.estimativadeprojetodesoftware.model.Plataforma.IOS, false);
            marcar(p, "Tamanho do App", "Médio");
            marcar(p, "Nível de UI", "Profissional");
            marcar(p, "Gerente de Projeto", "Gerente de Projeto");
            String u = "Usuários e Contas";
            marcar(p, u, "Cadastro por E-mail e Senha");
            marcar(p, u, "Cadastro pelo Google");
            marcar(p, "Conteúdo Gerado pelo Usuário", "Painel (Dashboard)");
            marcar(p, "Conteúdo Gerado pelo Usuário", "Pesquisa de Texto Livre");
            marcar(p, "Faturamento e e-Commerce", "Processamento de Pagamentos");
            marcar(p, "Faturamento e e-Commerce", "Carrinho de Compras");
            marcar(p, "Segurança", "Autenticação em Duas Etapas");
        }

        static void montarAgenda(Projeto p) {
            p.setPlataformaMarcada(com.br.estimativadeprojetodesoftware.model.Plataforma.WEB_BACKEND, false);
            marcar(p, "Tamanho do App", "Pequeno");
            marcar(p, "Nível de UI", "Básico");
            marcar(p, "Usuários e Contas", "Cadastro por E-mail e Senha");
            marcar(p, "Datas e Localizações", "Calendário");
        }
    }

    private ManterProjetoPresenter abrirProjeto(String nome) {
        Projeto p = projetos.listarProprios(demo).stream().filter(x -> x.getNome().equals(nome)).findFirst().orElseThrow();
        ProjetoView view = new ProjetoView();
        ManterProjetoPresenter presenter = new ManterProjetoPresenter(view, projetos, perfis,
                new ExportacaoService(new CalculoEstimativaService(), null), demo);
        presenter.abrirProjeto(p.getId());
        return presenter;
    }

    private void telasDoProjeto(String tema) throws IOException {
        ManterProjetoPresenter p = abrirProjeto("Loja virtual");
        p.getView().getTxtBuscaFuncionalidades().setText("");
        salvar(p.getView(), "projeto-funcionalidades-" + tema);
        p.escolherSecao(SecaoProjeto.TAMANHO_E_UI);
        salvar(p.getView(), "projeto-tamanho-e-ui-" + tema);
        p.escolherSecao(SecaoProjeto.DETALHAMENTO);
        salvar(p.getView(), "projeto-detalhamento-" + tema);
        p.getView().dispose();
    }

    private void dashboard(String tema) throws IOException {
        DashboardView view = new DashboardView();
        new DashboardPresenter(view, new DashboardService(projetos, new CalculoEstimativaService()));
        salvar(view, "dashboard-" + tema);
        view.dispose();
    }

    private void login(String tema) throws IOException {
        LoginView view = new LoginView();
        new LoginPresenter(view, null, () -> usuarios, () -> {
        });
        view.getTxtEmail().setText("demo@exemplo.com");
        view.pack();
        BufferedImage img = renderizar(view.getContentPane(), view.getContentPane().getPreferredSize().width,
                view.getContentPane().getPreferredSize().height);
        ImageIO.write(img, "png", destino.resolve("login-" + tema + ".png").toFile());
        view.dispose();
    }

    /** Quadros do GIF: a estimativa mudando a cada marcação, sem nenhum clique em calcular. */
    private void quadrosDoGif() throws IOException {
        ManterProjetoPresenter p = abrirProjeto("App de agendamento");
        ProjetoView v = p.getView();
        p.escolherSecao(SecaoProjeto.FUNCIONALIDADES);
        int n = 0;
        quadro(v, n++);
        for (String nome : List.of("Cadastro pelo Google", "Cadastro pelo LinkedIn", "Painel (Dashboard)", "Feed de Atividades")) {
            marcarNaTabela(v, nome);
            p.executarRecalculoPendente();
            quadro(v, n++);
        }
        v.getChkWeb().doClick();
        p.executarRecalculoPendente();
        quadro(v, n++);
        p.escolherSecao(SecaoProjeto.TAMANHO_E_UI);
        quadro(v, n++);
        var tamanho = v.getTabelasConfiguracao().get(0);
        tamanho.setValueAt(true, 1, 0); // Médio
        p.executarRecalculoPendente();
        quadro(v, n++);
        p.escolherSecao(SecaoProjeto.DETALHAMENTO);
        quadro(v, n++);
        v.dispose();
    }

    /** Primeira página do relatório em PDF da "Loja virtual", como imagem para o README. */
    private void relatorioPdf() throws IOException {
        Projeto loja = projetos.listarProprios(demo).stream().filter(x -> x.getNome().equals("Loja virtual")).findFirst().orElseThrow();
        loja.setDonoNome(demo.getNome());
        Path pdf = quadros.resolve("loja-virtual.pdf");
        new com.br.estimativadeprojetodesoftware.templatemethod.ExportarPDFTemplate()
                .exportar(loja, new CalculoEstimativaService().calcular(loja), pdf);
        try (var doc = org.apache.pdfbox.pdmodel.PDDocument.load(pdf.toFile())) {
            BufferedImage pagina = new org.apache.pdfbox.rendering.PDFRenderer(doc).renderImageWithDPI(0, 110);
            ImageIO.write(pagina, "png", destino.resolve("relatorio-pdf.png").toFile());
        }
    }

    private static void marcarNaTabela(ProjetoView v, String nome) {
        var t = v.getTabelaItens();
        for (int i = 0; i < t.getRowCount(); i++) {
            if (nome.equals(t.getValueAt(i, 1))) {
                t.setValueAt(true, i, 0);
                t.setRowSelectionInterval(i, i);
                return;
            }
        }
    }

    private void quadro(JInternalFrame janela, int numero) throws IOException {
        ImageIO.write(renderizar(janela), "png", quadros.resolve(String.format("quadro-%02d.png", numero)).toFile());
    }

    private void salvar(JInternalFrame janela, String nome) throws IOException {
        ImageIO.write(renderizar(janela), "png", destino.resolve(nome + ".png").toFile());
    }

    /** Desenha a janela interna como se estivesse maximizada numa área MDI de 1160 × 720. */
    private static BufferedImage renderizar(JInternalFrame janela) {
        JDesktopPane area = new JDesktopPane();
        JFrame frame = new JFrame();
        frame.setContentPane(area);
        area.add(janela);
        janela.setVisible(true);
        janela.setBounds(0, 0, LARGURA, ALTURA);
        frame.setSize(LARGURA, ALTURA);
        frame.addNotify();
        frame.validate();
        janela.validate();
        BufferedImage img = renderizar(janela, LARGURA, ALTURA);
        area.remove(janela);
        frame.dispose();
        return img;
    }

    private static BufferedImage renderizar(Component c, int largura, int altura) {
        c.setSize(largura, altura);
        c.doLayout();
        if (c instanceof java.awt.Container container) {
            container.validate();
        }
        BufferedImage img = new BufferedImage(largura, altura, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        c.printAll(g);
        g.dispose();
        return img;
    }
}
