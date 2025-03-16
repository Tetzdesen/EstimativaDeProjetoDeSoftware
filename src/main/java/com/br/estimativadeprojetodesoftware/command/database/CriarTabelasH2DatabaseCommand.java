package com.br.estimativadeprojetodesoftware.command.database;

import com.br.estimativadeprojetodesoftware.singleton.ConexaoSingleton;
import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author tetzner
 */
public class CriarTabelasH2DatabaseCommand implements DatabaseCommand {

    @Override
    public void execute() {
        Connection conexao = ConexaoSingleton.getInstancia().getConexao();

        String sql = """
                    CREATE TABLE IF NOT EXISTS usuario (
                          idUsuario VARCHAR(36) NOT NULL PRIMARY KEY,
                          nomeUsuario VARCHAR(75) NOT NULL,
                          email VARCHAR(50) NOT NULL UNIQUE,
                          senha VARCHAR(75) NOT NULL,
                          created_atUsuario TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                          updated_atUsuario TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                          log VARCHAR(255) NOT NULL
                      );
                      
                      CREATE TABLE IF NOT EXISTS projeto (
                          idProjeto VARCHAR(36) NOT NULL PRIMARY KEY,
                          nomeProjeto VARCHAR(75) NOT NULL,
                          tipoProjeto VARCHAR(45) NOT NULL,
                          created_atProjeto TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                          updated_atProjeto TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                          status VARCHAR(45) NOT NULL
                      );
                      
                      CREATE TABLE IF NOT EXISTS perfil (
                          idPerfil VARCHAR(36) NOT NULL PRIMARY KEY,
                          nomePerfil VARCHAR(75) NOT NULL,
                          perfilBackend BOOLEAN NOT NULL,
                          created_atPerfil TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                          updated_atPerfil TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                          usuario_idUsuario VARCHAR(36) NOT NULL,
                          FOREIGN KEY (usuario_idUsuario) REFERENCES usuario(idUsuario) ON UPDATE CASCADE ON DELETE CASCADE
                      );
                      
                      CREATE TABLE IF NOT EXISTS campo (
                          idCampo VARCHAR(36) NOT NULL PRIMARY KEY,
                          tipoCampo VARCHAR(45) NOT NULL,
                          nomeCampo VARCHAR(75) NOT NULL UNIQUE
                      );
                      
                      CREATE TABLE IF NOT EXISTS usuario_has_projeto (
                          usuario_idUsuario VARCHAR(36) NOT NULL,
                          projeto_idProjeto VARCHAR(36) NOT NULL,
                          isCompartilhado BOOLEAN NOT NULL,
                          PRIMARY KEY (usuario_idUsuario, projeto_idProjeto),
                          FOREIGN KEY (usuario_idUsuario) REFERENCES usuario(idUsuario) ON UPDATE CASCADE ON DELETE CASCADE,
                          FOREIGN KEY (projeto_idProjeto) REFERENCES projeto(idProjeto) ON UPDATE CASCADE ON DELETE CASCADE
                      );
                      
                      CREATE TABLE IF NOT EXISTS perfil_has_campo (
                          perfil_idPerfil VARCHAR(36) NOT NULL,
                          campo_idCampo VARCHAR(36) NOT NULL,
                          diasPerfil DOUBLE NOT NULL,
                          PRIMARY KEY (perfil_idPerfil, campo_idCampo),
                          FOREIGN KEY (perfil_idPerfil) REFERENCES perfil(idPerfil) ON UPDATE CASCADE ON DELETE CASCADE,
                          FOREIGN KEY (campo_idCampo) REFERENCES campo(idCampo) ON UPDATE CASCADE ON DELETE CASCADE
                      );
                      
                      CREATE TABLE IF NOT EXISTS projeto_has_perfil (
                          projeto_idProjeto VARCHAR(36) NOT NULL,
                          perfil_idPerfil VARCHAR(36) NOT NULL,
                          PRIMARY KEY (projeto_idProjeto, perfil_idPerfil),
                          FOREIGN KEY (projeto_idProjeto) REFERENCES projeto(idProjeto) ON UPDATE CASCADE ON DELETE CASCADE,
                          FOREIGN KEY (perfil_idPerfil) REFERENCES perfil(idPerfil) ON UPDATE CASCADE ON DELETE CASCADE
                      );
                      
                      CREATE TABLE IF NOT EXISTS projeto_has_campo (
                          projeto_idProjeto VARCHAR(36) NOT NULL,
                          campo_idCampo VARCHAR(36) NOT NULL,
                          diasProjeto INTEGER NOT NULL,
                          PRIMARY KEY (projeto_idProjeto, campo_idCampo),
                          FOREIGN KEY (projeto_idProjeto) REFERENCES projeto(idProjeto) ON UPDATE CASCADE ON DELETE CASCADE,
                          FOREIGN KEY (campo_idCampo) REFERENCES campo(idCampo) ON UPDATE CASCADE ON DELETE CASCADE
                      );     
        """;

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
