package com.br.estimativadeprojetodesoftware.singleton;

import com.br.estimativadeprojetodesoftware.service.DotenvService;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoSingleton {

    private Connection conexao;
    private String url;
    private String user;
    private String senha;
    private String driver;
    private static ConexaoSingleton instancia = null;

    private ConexaoSingleton() {
        this.url = DotenvService.getEnv("DB_URL");
        this.user = DotenvService.getEnv("DB_USER");
        this.senha = DotenvService.getEnv("DB_PASSWORD");
        this.driver = DotenvService.getEnv("DB_DRIVER");

        try {
            this.conexao = DriverManager.getConnection(url, user, senha);
            criarTabelasSQLite();
            //criarTabelasH2();
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

    private void criarTabelasSQLite() {
        String sql = """
        -- Tabela usuario
        CREATE TABLE IF NOT EXISTS usuario (
          idUsuario TEXT PRIMARY KEY,
          nomeUsuario VARCHAR(75) NOT NULL,
          email VARCHAR(50) NOT NULL UNIQUE,
          senha VARCHAR(75) NOT NULL,
          created_atUsuario DATETIME NOT NULL,
          updated_atUsuario DATETIME,
          log VARCHAR(255) NOT NULL
        );

        -- Tabela projeto
        CREATE TABLE IF NOT EXISTS projeto (
          idProjeto TEXT PRIMARY KEY,
          nomeProjeto VARCHAR(75) NOT NULL,
          tipoProjeto VARCHAR(45) NOT NULL,
          created_atProjeto DATETIME NOT NULL,
          updated_atProjeto DATETIME,
          status VARCHAR(45) NOT NULL
        );

        -- Tabela perfil
        CREATE TABLE IF NOT EXISTS perfil (
          idPerfil TEXT PRIMARY KEY,
          nomePerfil VARCHAR(75) NOT NULL,
          perfilBackend BOOLEAN NOT NULL,
          created_atPerfil DATETIME NOT NULL,
          updated_atPerfil DATETIME,
          usuario_idUsuario TEXT NOT NULL,
          FOREIGN KEY (usuario_idUsuario) REFERENCES usuario(idUsuario) ON DELETE CASCADE
        );

        -- Tabela campo
        CREATE TABLE IF NOT EXISTS campo (
          idCampo TEXT PRIMARY KEY,
          tipoCampo VARCHAR(45) NOT NULL,
          nomeCampo VARCHAR(75) NOT NULL UNIQUE
        );

        -- Tabela usuario_has_projeto
        CREATE TABLE IF NOT EXISTS usuario_has_projeto (
          usuario_idUsuario TEXT NOT NULL,
          projeto_idProjeto TEXT NOT NULL,
          isCompartilhado BOOLEAN NOT NULL,
          PRIMARY KEY (usuario_idUsuario, projeto_idProjeto),
          FOREIGN KEY (usuario_idUsuario) REFERENCES usuario(idUsuario) ON DELETE CASCADE,
          FOREIGN KEY (projeto_idProjeto) REFERENCES projeto(idProjeto) ON DELETE CASCADE
        );

        -- Tabela perfil_has_campo
        CREATE TABLE IF NOT EXISTS perfil_has_campo (
          perfil_idPerfil TEXT NOT NULL,
          campo_idCampo TEXT NOT NULL,
          diasPerfil DOUBLE NOT NULL,
          PRIMARY KEY (perfil_idPerfil, campo_idCampo),
          FOREIGN KEY (perfil_idPerfil) REFERENCES perfil(idPerfil) ON DELETE CASCADE,
          FOREIGN KEY (campo_idCampo) REFERENCES campo(idCampo) ON DELETE CASCADE
        );

        -- Tabela projeto_has_perfil
        CREATE TABLE IF NOT EXISTS projeto_has_perfil (
          projeto_idProjeto TEXT NOT NULL,
          perfil_idPerfil TEXT NOT NULL,
          PRIMARY KEY (projeto_idProjeto, perfil_idPerfil),
          FOREIGN KEY (projeto_idProjeto) REFERENCES projeto(idProjeto) ON DELETE CASCADE,
          FOREIGN KEY (perfil_idPerfil) REFERENCES perfil(idPerfil) ON DELETE CASCADE
        );

        -- Tabela projeto_has_campo
        CREATE TABLE IF NOT EXISTS projeto_has_campo (
          projeto_idProjeto TEXT NOT NULL,
          campo_idCampo TEXT NOT NULL,
          diasProjeto INTEGER NOT NULL,
          PRIMARY KEY (projeto_idProjeto, campo_idCampo),
          FOREIGN KEY (projeto_idProjeto) REFERENCES projeto(idProjeto) ON DELETE CASCADE,
          FOREIGN KEY (campo_idCampo) REFERENCES campo(idCampo) ON DELETE CASCADE
        );
    """;

        try (var stmt = conexao.createStatement()) {
            // Divide o script em múltiplas instruções e executa cada uma separadamente
            for (String query : sql.split(";")) {
                if (!query.trim().isEmpty()) {
                    stmt.execute(query);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro na criação de tabelas no banco de dados: " + e.getMessage(), e);
        }
    }

    private void criarTabelasH2() {
        String sql = """
       -- H2 Script
       
       CREATE DATABASE IF NOT EXISTS sistemaProjetoEstimativa;
       USE sistemaProjetoEstimativa;
       
       -- Tabela usuario
       CREATE TABLE IF NOT EXISTS usuario (
         idUsuario VARCHAR(255) PRIMARY KEY,
         nomeUsuario VARCHAR(75) NOT NULL,
         email VARCHAR(50) NOT NULL UNIQUE,
         senha VARCHAR(75) NOT NULL,
         created_atUsuario TIMESTAMP NOT NULL,
         updated_atUsuario TIMESTAMP,
         log VARCHAR(255) NOT NULL
       );
       
       -- Tabela projeto
       CREATE TABLE IF NOT EXISTS projeto (
         idProjeto VARCHAR(255) PRIMARY KEY,
         nomeProjeto VARCHAR(75) NOT NULL,
         tipoProjeto VARCHAR(45) NOT NULL,
         created_atProjeto TIMESTAMP NOT NULL,
         updated_atProjeto TIMESTAMP,
         status VARCHAR(45) NOT NULL
       );
       
       -- Tabela perfil
       CREATE TABLE IF NOT EXISTS perfil (
         idPerfil VARCHAR(255) PRIMARY KEY,
         nomePerfil VARCHAR(75) NOT NULL,
         perfilBackend BOOLEAN NOT NULL,
         created_atPerfil TIMESTAMP NOT NULL,
         updated_atPerfil TIMESTAMP,
         usuario_idUsuario VARCHAR(255) NOT NULL,
         FOREIGN KEY (usuario_idUsuario) REFERENCES usuario(idUsuario) ON DELETE CASCADE
       );
       
       -- Tabela campo
       CREATE TABLE IF NOT EXISTS campo (
         idCampo VARCHAR(255) PRIMARY KEY,
         tipoCampo VARCHAR(45) NOT NULL,
         nomeCampo VARCHAR(75) NOT NULL UNIQUE
       );
       
       -- Tabela usuario_has_projeto
       CREATE TABLE IF NOT EXISTS usuario_has_projeto (
         usuario_idUsuario VARCHAR(255) NOT NULL,
         projeto_idProjeto VARCHAR(255) NOT NULL,
         isCompartilhado BOOLEAN NOT NULL,
         PRIMARY KEY (usuario_idUsuario, projeto_idProjeto),
         FOREIGN KEY (usuario_idUsuario) REFERENCES usuario(idUsuario) ON DELETE CASCADE,
         FOREIGN KEY (projeto_idProjeto) REFERENCES projeto(idProjeto) ON DELETE CASCADE
       );
       
       -- Tabela perfil_has_campo
       CREATE TABLE IF NOT EXISTS perfil_has_campo (
         perfil_idPerfil VARCHAR(255) NOT NULL,
         campo_idCampo VARCHAR(255) NOT NULL,
         diasPerfil DOUBLE NOT NULL,
         PRIMARY KEY (perfil_idPerfil, campo_idCampo),
         FOREIGN KEY (perfil_idPerfil) REFERENCES perfil(idPerfil) ON DELETE CASCADE,
         FOREIGN KEY (campo_idCampo) REFERENCES campo(idCampo) ON DELETE CASCADE
       );
       
       -- Tabela projeto_has_perfil
       CREATE TABLE IF NOT EXISTS projeto_has_perfil (
         projeto_idProjeto VARCHAR(255) NOT NULL,
         perfil_idPerfil VARCHAR(255) NOT NULL,
         PRIMARY KEY (projeto_idProjeto, perfil_idPerfil),
         FOREIGN KEY (projeto_idProjeto) REFERENCES projeto(idProjeto) ON DELETE CASCADE,
         FOREIGN KEY (perfil_idPerfil) REFERENCES perfil(idPerfil) ON DELETE CASCADE
       );
       
       -- Tabela projeto_has_campo
       CREATE TABLE IF NOT EXISTS projeto_has_campo (
         projeto_idProjeto VARCHAR(255) NOT NULL,
         campo_idCampo VARCHAR(255) NOT NULL,
         diasProjeto INT NOT NULL,
         PRIMARY KEY (projeto_idProjeto, campo_idCampo),
         FOREIGN KEY (projeto_idProjeto) REFERENCES projeto(idProjeto) ON DELETE CASCADE,
         FOREIGN KEY (campo_idCampo) REFERENCES campo(idCampo) ON DELETE CASCADE
       );
    """;

        try (var stmt = conexao.createStatement()) {
            // Divide o script em múltiplas instruções e executa cada uma separadamente
            for (String query : sql.split(";")) {
                if (!query.trim().isEmpty()) {
                    stmt.execute(query);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro na criação de tabelas no banco de dados: " + e.getMessage(), e);
        }
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
