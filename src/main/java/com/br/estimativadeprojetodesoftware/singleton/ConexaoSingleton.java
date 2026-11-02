package com.br.estimativadeprojetodesoftware.singleton;

import com.br.estimativadeprojetodesoftware.repository.RepositorioException;
import com.br.estimativadeprojetodesoftware.repository.jdbc.JdbcPerfilRepository;
import com.br.estimativadeprojetodesoftware.repository.jdbc.JdbcUsuarioRepository;
import com.br.estimativadeprojetodesoftware.seeder.PerfilPadraoSeeder;
import com.br.estimativadeprojetodesoftware.seeder.UsuarioSeeder;
import com.br.estimativadeprojetodesoftware.service.DotenvService;
import com.br.estimativadeprojetodesoftware.strategy.DatabaseInitializer;
import com.br.estimativadeprojetodesoftware.strategy.H2Initializer;
import com.br.estimativadeprojetodesoftware.strategy.SQLiteInitializer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

/**
 * Conexão única com o banco escolhido em TIPO_BANCO (SQLite ou H2). A cada
 * início cria as tabelas que faltam e roda os seeders, que não duplicam dados.
 */
public class ConexaoSingleton {

    private static final Map<String, DatabaseInitializer> INICIALIZADORES = Map.of(
            "sqlite", new SQLiteInitializer(),
            "h2", new H2Initializer()
    );

    private static ConexaoSingleton instancia;

    private final Connection conexao;
    private final String tipo;

    private ConexaoSingleton() {
        this.tipo = DotenvService.getValor("TIPO_BANCO");
        String url = DotenvService.getValor("DB_URL");
        if (tipo == null || url == null) {
            throw new IllegalStateException("Defina TIPO_BANCO e DB_URL no arquivo .env (veja o .env.example)");
        }
        DatabaseInitializer initializer = inicializadorPara(tipo);
        this.conexao = abrir(initializer, url, DotenvService.getValor("DB_USER"), DotenvService.getValor("DB_PASSWORD"));
    }

    public static DatabaseInitializer inicializadorPara(String tipo) {
        DatabaseInitializer initializer = INICIALIZADORES.get(tipo.trim().toLowerCase());
        if (initializer == null) {
            throw new IllegalStateException("Banco de dados não suportado: " + tipo + " (use SQLite ou H2)");
        }
        return initializer;
    }

    /**
     * Abre a conexão, cria as tabelas e roda os seeders. Usado pelo singleton
     * e pelos testes de integração.
     */
    public static Connection abrir(DatabaseInitializer initializer, String url, String usuario, String senha) {
        try {
            Class.forName(initializer.getDriver());
        } catch (ClassNotFoundException e) {
            throw new RepositorioException("Driver JDBC não encontrado: " + initializer.getDriver(), e);
        }
        try {
            initializer.prepararArmazenamento(url);
            Connection conexao = DriverManager.getConnection(url, usuario == null ? "" : usuario, senha == null ? "" : senha);
            initializer.configurarConexao(conexao);
            initializer.criarTabelas(conexao);
            verificarEsquema(conexao, url);
            new UsuarioSeeder(new JdbcUsuarioRepository(conexao)).executar();
            new PerfilPadraoSeeder(new JdbcPerfilRepository(conexao)).executar();
            return conexao;
        } catch (SQLException e) {
            throw new RepositorioException("Erro ao conectar com o banco de dados: " + e.getMessage(), e);
        }
    }

    /** Recusa bancos criados pela versão antiga do sistema, com instrução clara. */
    private static void verificarEsquema(Connection conexao, String url) {
        try (var st = conexao.createStatement()) {
            st.executeQuery("SELECT id, nome, email, senha, criado_em FROM usuario WHERE 1 = 0").close();
            st.executeQuery("SELECT id, dono_id, web_backend, pct_impostos FROM projeto WHERE 1 = 0").close();
        } catch (SQLException e) {
            throw new RepositorioException("O banco " + url + " foi criado por uma versão antiga do sistema e tem outro esquema. "
                    + "Apague a pasta do banco (ex.: db/) e inicie o sistema de novo para recriá-lo.", e);
        }
    }

    public static synchronized ConexaoSingleton getInstancia() {
        if (instancia == null) {
            instancia = new ConexaoSingleton();
        }
        return instancia;
    }

    public Connection getConexao() {
        return conexao;
    }

    public String getTipo() {
        return tipo;
    }

    public void closeConnection() {
        try {
            if (conexao != null && !conexao.isClosed()) {
                conexao.close();
            }
        } catch (SQLException e) {
            throw new RepositorioException("Erro ao fechar a conexão com o banco de dados: " + e.getMessage(), e);
        }
    }
}
