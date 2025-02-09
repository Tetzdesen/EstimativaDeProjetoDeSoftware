package com.br.estimativadeprojetodesoftware.singleton;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import io.github.cdimascio.dotenv.Dotenv;

public class ConexaoSingleton {
    private Connection conexao;
    private String url;
    private String user;
    private String senha;
    private String driver;
    private static ConexaoSingleton instancia = null;

    private ConexaoSingleton () {
        Dotenv dotenv = Dotenv.load();
        this.url = dotenv.get("DB_URL");
        this.user = dotenv.get("DB_USER");
        this.senha = dotenv.get("DB_PASSWORD");
        this.driver = dotenv.get("DB_DRIVER");

        try {
            Class.forName(driver);
        } catch (Exception e) {
            throw new RuntimeException("Caminho especificado não encontrado: " + e.getMessage());
        }

        try {
            this.conexao = DriverManager.getConnection(url, user, senha);
            System.out.println("Conexão ok");
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao conectar com o banco de dados: " + e.getMessage());
        }
    }

    public static synchronized ConexaoSingleton getInstancia() {
        if (instancia == null) {
            instancia = new ConexaoSingleton();
        }
        return instancia;
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
