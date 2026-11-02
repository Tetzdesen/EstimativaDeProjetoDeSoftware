package com.br.estimativadeprojetodesoftware.singleton;

import com.br.estimativadeprojetodesoftware.service.DotenvService;
import com.br.estimativadeprojetodesoftware.strategy.DatabaseInitializer;
import com.br.estimativadeprojetodesoftware.strategy.SQLiteInitializer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

public class ConexaoSingleton {

    private static ConexaoSingleton instancia = null;

    private Connection conexao;
    private String url;
    private String user;
    private String senha;
    private String driver;
    private String tipo;

    private DatabaseInitializer initializer;
    private boolean bancoRecemCriado;

    private final Map<String, DatabaseInitializer> inicializadores = Map.of(
            "SQLite", new SQLiteInitializer()
    );

    private ConexaoSingleton() {
        this.url = DotenvService.getEnv("DB_URL");
        this.user = DotenvService.getEnv("DB_USER");
        this.senha = DotenvService.getEnv("DB_PASSWORD");
        this.driver = DotenvService.getEnv("DB_DRIVER");

        this.tipo = DotenvService.getEnv("TIPO_BANCO");

        if (this.tipo == null) {
            throw new RuntimeException("Variável TIPO_BANCO não definida no .env");
        }

        this.initializer = inicializadores.entrySet().stream()
                .filter(entry -> tipo.toLowerCase().contains(entry.getKey().toLowerCase()))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Banco de dados não suportado: " + tipo));

        this.bancoRecemCriado = initializer.prepararArmazenamento(url, user, senha);

        try {
            this.conexao = DriverManager.getConnection(url, user, senha);

            try (var stmt = this.conexao.createStatement()) {
                stmt.execute("PRAGMA foreign_keys = ON;");
            }
         
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao conectar com o banco de dados: " + e.getMessage());
        }

    }

    public static synchronized ConexaoSingleton getInstancia() {
        if (instancia == null) {
            ConexaoSingleton novaInstancia = new ConexaoSingleton();

            // A instância é publicada antes de configurar o banco porque os
            // comandos de criação de tabelas e os seeders voltam a chamar
            // getInstancia() para obter a conexão.
            instancia = novaInstancia;

            try {
                novaInstancia.configurarBancoSeNecessario();
            } catch (RuntimeException e) {
                instancia = null;
                novaInstancia.closeConnection();
                throw e;
            }
        }
        return instancia;
    }

    private void configurarBancoSeNecessario() {
        if (bancoRecemCriado) {
            bancoRecemCriado = false;
            initializer.configurarBanco();
        }
    }

    public Connection getConexao() {
        return this.conexao;
    }

    public void closeConnection() {
        try {
            if (conexao != null && !conexao.isClosed()) {
                conexao.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error ao fechar conexão com o banco de dados: " + e.getMessage());
        }
    }
}
