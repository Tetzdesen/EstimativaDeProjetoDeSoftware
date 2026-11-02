package com.br.estimativadeprojetodesoftware.strategy;

import java.util.List;

/**
 * DDL do sistema, escrito uma única vez com tipos aceitos pelo SQLite e pelo H2.
 * Todas as tabelas usam CREATE TABLE IF NOT EXISTS, então o script roda a cada
 * início sem efeito colateral. Valores decimais ficam em VARCHAR (texto exato do
 * BigDecimal) e datas em VARCHAR ISO-8601.
 */
public final class EsquemaBanco {

    private EsquemaBanco() {
    }

    public static final List<String> TABELAS = List.of(
            """
            CREATE TABLE IF NOT EXISTS usuario (
              id VARCHAR(36) NOT NULL PRIMARY KEY,
              nome VARCHAR(100) NOT NULL,
              email VARCHAR(150) NOT NULL UNIQUE,
              senha VARCHAR(255) NOT NULL,
              criado_em VARCHAR(30) NOT NULL
            )""",
            """
            CREATE TABLE IF NOT EXISTS perfil (
              id VARCHAR(36) NOT NULL PRIMARY KEY,
              nome VARCHAR(100) NOT NULL,
              padrao BOOLEAN NOT NULL,
              dono_id VARCHAR(36),
              criado_em VARCHAR(30) NOT NULL,
              FOREIGN KEY (dono_id) REFERENCES usuario(id) ON DELETE CASCADE
            )""",
            """
            CREATE TABLE IF NOT EXISTS perfil_funcionalidade (
              id VARCHAR(36) NOT NULL PRIMARY KEY,
              perfil_id VARCHAR(36) NOT NULL,
              categoria VARCHAR(100) NOT NULL,
              nome VARCHAR(150) NOT NULL,
              tipo_selecao VARCHAR(20) NOT NULL,
              tipo_valor VARCHAR(20) NOT NULL,
              ordem INTEGER NOT NULL,
              FOREIGN KEY (perfil_id) REFERENCES perfil(id) ON DELETE CASCADE
            )""",
            """
            CREATE TABLE IF NOT EXISTS perfil_valor (
              funcionalidade_id VARCHAR(36) NOT NULL,
              plataforma VARCHAR(20) NOT NULL,
              valor VARCHAR(40),
              PRIMARY KEY (funcionalidade_id, plataforma),
              FOREIGN KEY (funcionalidade_id) REFERENCES perfil_funcionalidade(id) ON DELETE CASCADE
            )""",
            """
            CREATE TABLE IF NOT EXISTS perfil_taxa (
              perfil_id VARCHAR(36) NOT NULL,
              plataforma VARCHAR(20) NOT NULL,
              papel VARCHAR(20) NOT NULL,
              valor VARCHAR(40) NOT NULL,
              PRIMARY KEY (perfil_id, plataforma, papel),
              FOREIGN KEY (perfil_id) REFERENCES perfil(id) ON DELETE CASCADE
            )""",
            """
            CREATE TABLE IF NOT EXISTS projeto (
              id VARCHAR(36) NOT NULL PRIMARY KEY,
              nome VARCHAR(100) NOT NULL,
              dono_id VARCHAR(36) NOT NULL,
              web_backend BOOLEAN NOT NULL,
              ios BOOLEAN NOT NULL,
              android BOOLEAN NOT NULL,
              pct_impostos VARCHAR(40) NOT NULL,
              pct_lucro VARCHAR(40) NOT NULL,
              calculado BOOLEAN NOT NULL,
              criado_em VARCHAR(30) NOT NULL,
              atualizado_em VARCHAR(30),
              UNIQUE (dono_id, nome),
              FOREIGN KEY (dono_id) REFERENCES usuario(id) ON DELETE CASCADE
            )""",
            """
            CREATE TABLE IF NOT EXISTS projeto_funcionalidade (
              id VARCHAR(36) NOT NULL PRIMARY KEY,
              projeto_id VARCHAR(36) NOT NULL,
              categoria VARCHAR(100) NOT NULL,
              nome VARCHAR(150) NOT NULL,
              tipo_selecao VARCHAR(20) NOT NULL,
              tipo_valor VARCHAR(20) NOT NULL,
              ordem INTEGER NOT NULL,
              selecionado BOOLEAN NOT NULL,
              FOREIGN KEY (projeto_id) REFERENCES projeto(id) ON DELETE CASCADE
            )""",
            """
            CREATE TABLE IF NOT EXISTS projeto_valor (
              funcionalidade_id VARCHAR(36) NOT NULL,
              plataforma VARCHAR(20) NOT NULL,
              valor VARCHAR(40),
              PRIMARY KEY (funcionalidade_id, plataforma),
              FOREIGN KEY (funcionalidade_id) REFERENCES projeto_funcionalidade(id) ON DELETE CASCADE
            )""",
            """
            CREATE TABLE IF NOT EXISTS projeto_taxa (
              projeto_id VARCHAR(36) NOT NULL,
              plataforma VARCHAR(20) NOT NULL,
              papel VARCHAR(20) NOT NULL,
              valor VARCHAR(40) NOT NULL,
              PRIMARY KEY (projeto_id, plataforma, papel),
              FOREIGN KEY (projeto_id) REFERENCES projeto(id) ON DELETE CASCADE
            )""",
            """
            CREATE TABLE IF NOT EXISTS projeto_custo_adicional (
              id VARCHAR(36) NOT NULL PRIMARY KEY,
              projeto_id VARCHAR(36) NOT NULL,
              descricao VARCHAR(150) NOT NULL,
              valor VARCHAR(40) NOT NULL,
              ordem INTEGER NOT NULL,
              FOREIGN KEY (projeto_id) REFERENCES projeto(id) ON DELETE CASCADE
            )""",
            """
            CREATE TABLE IF NOT EXISTS compartilhamento (
              projeto_id VARCHAR(36) NOT NULL,
              usuario_id VARCHAR(36) NOT NULL,
              compartilhado_em VARCHAR(30) NOT NULL,
              PRIMARY KEY (projeto_id, usuario_id),
              FOREIGN KEY (projeto_id) REFERENCES projeto(id) ON DELETE CASCADE,
              FOREIGN KEY (usuario_id) REFERENCES usuario(id) ON DELETE CASCADE
            )""",
            """
            CREATE TABLE IF NOT EXISTS configuracao (
              chave VARCHAR(100) NOT NULL PRIMARY KEY,
              valor VARCHAR(255)
            )"""
    );
}
