package com.br.estimativadeprojetodesoftware.command.database;

import com.br.estimativadeprojetodesoftware.singleton.ConexaoSingleton;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author tetzner
 */
public class CriarTabelasSQLiteDatabaseCommand implements DatabaseCommand {

    public void execute() {
        Connection conexao = ConexaoSingleton.getInstancia().getConexao();

        String sql = """
            -- Tabela usuario
              -- Tabela usuario
                       CREATE TABLE IF NOT EXISTS usuario (
                         idUsuario TEXT NOT NULL PRIMARY KEY,
                         nomeUsuario VARCHAR(75) NOT NULL,
                         email VARCHAR(50) NOT NULL UNIQUE,
                         senha VARCHAR(75) NOT NULL,
                         created_atUsuario DEFAULT CURRENT_TIMESTAMP NOT NULL,
                         updated_atUsuario DATETIME,
                         log VARCHAR(255) NOT NULL
                       );
           
                       -- Tabela projeto
                       CREATE TABLE IF NOT EXISTS projeto (
                         idProjeto TEXT NOT NULL PRIMARY KEY,
                         nomeProjeto VARCHAR(75) NOT NULL,
                         tipoProjeto VARCHAR(45) NOT NULL,
                         created_atProjeto DEFAULT CURRENT_TIMESTAMP NOT NULL,
                         updated_atProjeto DATETIME,
                         status VARCHAR(45) NOT NULL
                       );
           
                       -- Tabela perfil
                       CREATE TABLE IF NOT EXISTS perfil (
                         idPerfil TEXT NOT NULL PRIMARY KEY,
                         nomePerfil VARCHAR(75) NOT NULL,
                         perfilBackend BOOLEAN NOT NULL,
                         created_atPerfil DEFAULT CURRENT_TIMESTAMP NOT NULL,
                         updated_atPerfil DATETIME,
                         usuario_idUsuario TEXT NOT NULL,
                         FOREIGN KEY (usuario_idUsuario) REFERENCES usuario(idUsuario) ON UPDATE CASCADE ON DELETE CASCADE
                       );
           
                       -- Tabela campo
                       CREATE TABLE IF NOT EXISTS campo (
                         idCampo TEXT NOT NULL PRIMARY KEY,
                         tipoCampo VARCHAR(45) NOT NULL,
                         nomeCampo VARCHAR(75) NOT NULL UNIQUE
                       );
           
                       -- Tabela usuario_has_projeto
                       CREATE TABLE IF NOT EXISTS usuario_has_projeto (
                         usuario_idUsuario TEXT NOT NULL,
                         projeto_idProjeto TEXT NOT NULL,
                         isCompartilhado BOOLEAN NOT NULL,
                         PRIMARY KEY (usuario_idUsuario, projeto_idProjeto),
                         FOREIGN KEY (usuario_idUsuario) REFERENCES usuario(idUsuario) ON UPDATE CASCADE ON DELETE CASCADE,
                         FOREIGN KEY (projeto_idProjeto) REFERENCES projeto(idProjeto) ON UPDATE CASCADE ON DELETE CASCADE
                       );
           
                       -- Tabela perfil_has_campo
                       CREATE TABLE IF NOT EXISTS perfil_has_campo (
                         perfil_idPerfil TEXT NOT NULL,
                         campo_idCampo TEXT NOT NULL,
                         diasPerfil DOUBLE NOT NULL,
                         PRIMARY KEY (perfil_idPerfil, campo_idCampo),
                         FOREIGN KEY (perfil_idPerfil) REFERENCES perfil(idPerfil) ON UPDATE CASCADE ON DELETE CASCADE,
                         FOREIGN KEY (campo_idCampo) REFERENCES campo(idCampo) ON UPDATE CASCADE ON DELETE CASCADE
                       );
           
                       -- Tabela projeto_has_perfil
                       CREATE TABLE IF NOT EXISTS projeto_has_perfil (
                         projeto_idProjeto TEXT NOT NULL,
                         perfil_idPerfil TEXT NOT NULL,
                         PRIMARY KEY (projeto_idProjeto, perfil_idPerfil),
                         FOREIGN KEY (projeto_idProjeto) REFERENCES projeto(idProjeto) ON UPDATE CASCADE ON DELETE CASCADE,
                         FOREIGN KEY (perfil_idPerfil) REFERENCES perfil(idPerfil) ON UPDATE CASCADE ON DELETE CASCADE
                       );
           
                       -- Tabela projeto_has_campo
                       CREATE TABLE IF NOT EXISTS projeto_has_campo (
                         projeto_idProjeto TEXT NOT NULL,
                         campo_idCampo TEXT NOT NULL,
                         diasProjeto INTEGER NOT NULL,
                         PRIMARY KEY (projeto_idProjeto, campo_idCampo),
                         FOREIGN KEY (projeto_idProjeto) REFERENCES projeto(idProjeto) ON UPDATE CASCADE ON DELETE CASCADE,
                         FOREIGN KEY (campo_idCampo) REFERENCES campo(idCampo) ON UPDATE CASCADE ON DELETE CASCADE
                       );
        """;

        /*
        String sql = """
            -- Tabela usuario
            CREATE TABLE IF NOT EXISTS usuario (
              idUsuario TEXT PRIMARY KEY,
              nomeUsuario VARCHAR(75) NOT NULL,
              email VARCHAR(50) NOT NULL UNIQUE,
              senha VARCHAR(75) NOT NULL,
              created_atUsuario DEFAULT CURRENT_TIMESTAMP NOT NULL,
              updated_atUsuario DATETIME,
              log VARCHAR(255) NOT NULL
            );

            -- Tabela projeto
            CREATE TABLE IF NOT EXISTS projeto (
              idProjeto TEXT PRIMARY KEY,
              nomeProjeto VARCHAR(75) NOT NULL,
              tipoProjeto VARCHAR(45) NOT NULL,
              created_atProjeto DEFAULT CURRENT_TIMESTAMP NOT NULL,
              updated_atProjeto DATETIME,
              status VARCHAR(45) NOT NULL
            );

            -- Tabela perfil
            CREATE TABLE IF NOT EXISTS perfil (
              idPerfil TEXT PRIMARY KEY,
              nomePerfil VARCHAR(75) NOT NULL,
              perfilBackend BOOLEAN NOT NULL,
              created_atPerfil DEFAULT CURRENT_TIMESTAMP NOT NULL,
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
        """;*/
        try (var stmt = conexao.createStatement()) {
            for (String query : sql.split(";")) {
                if (!query.trim().isEmpty()) {
                    stmt.execute(query);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro na criação de tabelas no banco de dados: " + e.getMessage(), e);
        }
    }
}
