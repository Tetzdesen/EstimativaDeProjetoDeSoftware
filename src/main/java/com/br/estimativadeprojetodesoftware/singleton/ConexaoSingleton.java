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

    private ConexaoSingleton() {
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

    private void criaTabelasSQLIte() {
        String sql = """
           -- SQLite Script
           
           -- Tabela usuario
           CREATE TABLE IF NOT EXISTS usuario (
             idUsuario TEXT PRIMARY KEY,
             nomeUsuario VARCHAR(75) NOT NULL,
             email VARCHAR(50) NOT NULL UNIQUE,
             senha VARCHAR(75) NOT NULL,
             created_atUsuario DATETIME NOT NULL,
             updated_atUsuario DATETIME NOT NULL,
             log VARCHAR(45) NOT NULL
           );
           
           -- Tabela estimativa
           CREATE TABLE IF NOT EXISTS estimativa (
             idEstimativa TEXT PRIMARY KEY,
             created_atEstimativa DATETIME NOT NULL,
             updated_atEstimativa DATETIME NOT NULL
           );
           
           -- Tabela projeto
           CREATE TABLE IF NOT EXISTS projeto (
             idProjeto TEXT PRIMARY KEY,
             nomeProjeto VARCHAR(75) NOT NULL,
             tipoProjeto VARCHAR(45) NOT NULL,
             created_atProjeto DATETIME NOT NULL,
             updated_atProjeto DATETIME NOT NULL,
             status VARCHAR(45) NOT NULL,
             estimativa_idEstimativa TEXT NOT NULL,
             FOREIGN KEY (estimativa_idEstimativa) REFERENCES estimativa(idEstimativa)
           );
           
           -- Tabela perfil
           CREATE TABLE IF NOT EXISTS perfil (
             idPerfil TEXT PRIMARY KEY,
             nomePerfil VARCHAR(75) NOT NULL,
             perfilBackend BOOLEAN NOT NULL,
             created_atPerfil DATETIME NOT NULL,
             updated_atPerfil DATETIME NOT NULL,
             usuario_idUsuario TEXT NOT NULL,
             FOREIGN KEY (usuario_idUsuario) REFERENCES usuario(idUsuario)
           );
           
           -- Tabela campo
           CREATE TABLE IF NOT EXISTS campo (
             idCampo INTEGER PRIMARY KEY AUTOINCREMENT,
             tipoCampo VARCHAR(45) NOT NULL,
             nomeCampo VARCHAR(75) NOT NULL UNIQUE
           );
           
           -- Tabela usuario_has_projeto
           CREATE TABLE IF NOT EXISTS usuario_has_projeto (
             usuario_idUsuario TEXT NOT NULL,
             projeto_idProjeto TEXT NOT NULL,
             isCompartilhado BOOLEAN NOT NULL,
             PRIMARY KEY (usuario_idUsuario, projeto_idProjeto),
             FOREIGN KEY (usuario_idUsuario) REFERENCES usuario(idUsuario),
             FOREIGN KEY (projeto_idProjeto) REFERENCES projeto(idProjeto)
           );
           
           -- Tabela estimativa_has_campo
           CREATE TABLE IF NOT EXISTS estimativa_has_campo (
             estimativa_idEstimativa TEXT NOT NULL,
             campo_idCampo INTEGER NOT NULL,
             valorEstimativaCampo DOUBLE NOT NULL,
             PRIMARY KEY (estimativa_idEstimativa, campo_idCampo),
             FOREIGN KEY (estimativa_idEstimativa) REFERENCES estimativa(idEstimativa),
             FOREIGN KEY (campo_idCampo) REFERENCES campo(idCampo)
           );
           
           -- Tabela perfil_has_campo
           CREATE TABLE IF NOT EXISTS perfil_has_campo (
             perfil_idPerfil TEXT NOT NULL,
             campo_idCampo INTEGER NOT NULL,
             valorPerfilCampo DOUBLE NOT NULL,
             PRIMARY KEY (perfil_idPerfil, campo_idCampo),
             FOREIGN KEY (perfil_idPerfil) REFERENCES perfil(idPerfil),
             FOREIGN KEY (campo_idCampo) REFERENCES campo(idCampo)
           );
        """;

        try (var stmt = conexao.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Erro na criação de tabela no banco de dados: " + e.getMessage(), e);
        }
    }

    private void criaTabelasH2() {
        String sql = """
           -- H2 Script
           
           CREATE SCHEMA IF NOT EXISTS mydb;
           USE mydb;
           
           -- Tabela usuario
           CREATE TABLE IF NOT EXISTS usuario (
             idUsuario VARCHAR(255) PRIMARY KEY,
             nomeUsuario VARCHAR(75) NOT NULL,
             email VARCHAR(50) NOT NULL UNIQUE,
             senha VARCHAR(75) NOT NULL,
             created_atUsuario DATETIME NOT NULL,
             updated_atUsuario DATETIME NOT NULL,
             log VARCHAR(45) NOT NULL
           );
           
           -- Tabela estimativa
           CREATE TABLE IF NOT EXISTS estimativa (
             idEstimativa VARCHAR(255) PRIMARY KEY,
             created_atEstimativa DATETIME NOT NULL,
             updated_atEstimativa DATETIME NOT NULL
           );
           
           -- Tabela projeto
           CREATE TABLE IF NOT EXISTS projeto (
             idProjeto VARCHAR(255) PRIMARY KEY,
             nomeProjeto VARCHAR(75) NOT NULL,
             tipoProjeto VARCHAR(45) NOT NULL,
             created_atProjeto DATETIME NOT NULL,
             updated_atProjeto DATETIME NOT NULL,
             status VARCHAR(45) NOT NULL,
             estimativa_idEstimativa VARCHAR(255) NOT NULL,
             FOREIGN KEY (estimativa_idEstimativa) REFERENCES estimativa(idEstimativa)
           );
           
           -- Tabela perfil
           CREATE TABLE IF NOT EXISTS perfil (
             idPerfil VARCHAR(255) PRIMARY KEY,
             nomePerfil VARCHAR(75) NOT NULL,
             perfilBackend BOOLEAN NOT NULL,
             created_atPerfil DATETIME NOT NULL,
             updated_atPerfil DATETIME NOT NULL,
             usuario_idUsuario VARCHAR(255) NOT NULL,
             FOREIGN KEY (usuario_idUsuario) REFERENCES usuario(idUsuario)
           );
           
           -- Tabela campo
           CREATE TABLE IF NOT EXISTS campo (
             idCampo INTEGER PRIMARY KEY AUTO_INCREMENT,
             tipoCampo VARCHAR(45) NOT NULL,
             nomeCampo VARCHAR(75) NOT NULL UNIQUE
           );
           
           -- Tabela usuario_has_projeto
           CREATE TABLE IF NOT EXISTS usuario_has_projeto (
             usuario_idUsuario VARCHAR(255) NOT NULL,
             projeto_idProjeto VARCHAR(255) NOT NULL,
             isCompartilhado BOOLEAN NOT NULL,
             PRIMARY KEY (usuario_idUsuario, projeto_idProjeto),
             FOREIGN KEY (usuario_idUsuario) REFERENCES usuario(idUsuario),
             FOREIGN KEY (projeto_idProjeto) REFERENCES projeto(idProjeto)
           );
           
           -- Tabela estimativa_has_campo
           CREATE TABLE IF NOT EXISTS estimativa_has_campo (
             estimativa_idEstimativa VARCHAR(255) NOT NULL,
             campo_idCampo INTEGER NOT NULL,
             valorEstimativaCampo DOUBLE NOT NULL,
             PRIMARY KEY (estimativa_idEstimativa, campo_idCampo),
             FOREIGN KEY (estimativa_idEstimativa) REFERENCES estimativa(idEstimativa),
             FOREIGN KEY (campo_idCampo) REFERENCES campo(idCampo)
           );
           
           -- Tabela perfil_has_campo
           CREATE TABLE IF NOT EXISTS perfil_has_campo (
             perfil_idPerfil VARCHAR(255) NOT NULL,
             campo_idCampo INTEGER NOT NULL,
             valorPerfilCampo DOUBLE NOT NULL,
             PRIMARY KEY (perfil_idPerfil, campo_idCampo),
             FOREIGN KEY (perfil_idPerfil) REFERENCES perfil(idPerfil),
             FOREIGN KEY (campo_idCampo) REFERENCES campo(idCampo)
           );
        """;

        try (var stmt = conexao.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Erro na criação de tabela no banco de dados: " + e.getMessage(), e);
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
