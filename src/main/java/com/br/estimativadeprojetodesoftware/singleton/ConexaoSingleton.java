package com.br.estimativadeprojetodesoftware.singleton;

import com.br.estimativadeprojetodesoftware.service.DotenvService;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

public class ConexaoSingleton {

    private Connection conexao;
    private String url;
    private String user;
    private String senha;
    private String driver;
    private File arquivo;
    private static ConexaoSingleton instancia = null;

    private ConexaoSingleton() {
        this.url = DotenvService.getEnv("DB_URL");
        this.user = DotenvService.getEnv("DB_USER");
        this.senha = DotenvService.getEnv("DB_PASSWORD");
        this.driver = DotenvService.getEnv("DB_DRIVER");

        // Criar banco de dados caso não exista (SQLite)
        if (url.startsWith("jdbc:sqlite:")) {
            boolean arquivoCriado = criarArquivoBDSQLite(url.replace("jdbc:sqlite:", ""));

            try {
                this.conexao = DriverManager.getConnection(url, user, senha);
            } catch (SQLException e) {
                throw new RuntimeException("Erro ao conectar com o banco de dados: " + e.getMessage());
            }

            if (arquivoCriado) {
                configurarBancoSQLite();
            }

        } else {
            //criarTabelasH2();
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

    private boolean criarArquivoBDSQLite(String caminho) {
        arquivo = new File(caminho);
        File pasta = arquivo.getParentFile(); // Obtém o diretório 'db'

        if (pasta != null && !pasta.exists()) {
            pasta.mkdirs(); // Cria a pasta 'db' se não existir
        }

        if (!arquivo.exists()) {
            try {
                if (arquivo.createNewFile()) {
                    return true;
                }
            } catch (Exception e) {
                throw new RuntimeException("Erro ao criar arquivo do banco SQLite: " + e.getMessage());
            }
        }
        return false;
    }

    private void configurarBancoSQLite() {
        criarTabelasSQLite();
        inserirDadosIniciais();
    }

    private void criarTabelasSQLite() {
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
            for (String query : sql.split(";")) {
                if (!query.trim().isEmpty()) {
                    stmt.execute(query);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro na criação de tabelas no banco de dados: " + e.getMessage(), e);
        }
    }

    private void inserirDadosIniciais() {
        try {
            // Inserção do usuário
            String sqlUsuario = """
              INSERT INTO usuario (idUsuario, nomeUsuario, email, senha, log) VALUES (?, ?, ?, ?, ?);
          """;

            try (PreparedStatement stmt = conexao.prepareStatement(sqlUsuario)) {
                stmt.setString(1, UUID.randomUUID().toString());
                stmt.setString(2, "admin");
                stmt.setString(3, "admin@email.com");
                stmt.setString(4, "admin"); // Em produção, usar hash da senha
                stmt.setString(5, "JSON");
                stmt.executeUpdate();
            }

            // Inserção dos campos
            String sqlCampo = """
              INSERT INTO campo(idCampo, tipoCampo, nomeCampo) VALUES (?, ?, ?);
          """;

            try (PreparedStatement stmt = conexao.prepareStatement(sqlCampo)) {
                String[][] campos = {
                    {"tamanho", "pequeno"},
                    {"tamanho", "médio"},
                    {"tamanho", "grande"},
                    {"nivel", "mvp"},
                    {"nivel", "básico"},
                    {"nivel", "profissional"},
                    {"funcionalidade", "Cadastro pelo Google"},
                    {"funcionalidade", "Cadastro pelo LinkedIn"},
                    {"funcionalidade", "Cadastro pelo Github"},
                    {"funcionalidade", "Cadastro pelo Twitter"},
                    {"funcionalidade", "E-mails de Convite de Usuário"},
                    {"funcionalidade", "Contas Multi-tenant"},
                    {"funcionalidade", "Subdomínios"},
                    {"funcionalidade", "Domínios Personalizados"},
                    {"funcionalidade", "Painel (Dashboard)"},
                    {"funcionalidade", "Feed de Atividades (Activity Feed)"},
                    {"funcionalidade", "Upload de Arquivos (File Uploading)"},
                    {"funcionalidade", "Upload de Mídia (Media Uploading)"},
                    {"funcionalidade", "Perfis de Usuário (User Profiles)"},
                    {"funcionalidade", "E-mails Transacionais (Transactional Emails)"},
                    {"funcionalidade", "Tags"},
                    {"funcionalidade", "Avaliações ou Comentários (Ratings or Reviews)"},
                    {"funcionalidade", "Processamento de Áudio/Vídeo"},
                    {"funcionalidade", "Pesquisa de Texto Livre (Free text searching)"},
                    {"funcionalidade", "Pesquisa (Searching)"},
                    {"funcionalidade", "Calendário (Calendaring)"},
                    {"funcionalidade", "Exibição de Dados de Mapa / Geolocalização"},
                    {"funcionalidade", "Exibição de Marcadores/Regiões de Mapa Personalizados"},
                    {"funcionalidade", "Reservas (Bookings)"},
                    {"funcionalidade", "Mensagens (Messaging)"},
                    {"funcionalidade", "Fóruns ou Comentários"},
                    {"funcionalidade", "Compartilhamento Social (Social Sharing)"},
                    {"funcionalidade", "Integração com Facebook Open Graph (Push to Facebook Open Graph)"},
                    {"funcionalidade", "Notificações Push (Push Notifications)"},
                    {"funcionalidade", "Planos de Assinatura (Subscription plans)"},
                    {"funcionalidade", "Processamento de Pagamentos (Payment processing)"},
                    {"funcionalidade", "Carrinho de Compras (Shopping Cart)"},
                    {"funcionalidade", "Marketplace de Usuários (User Marketplace)"},
                    {"funcionalidade", "Gerenciamento de Produtos (Product Management)"},
                    {"funcionalidade", "Compras dentro do Aplicativo (In-App Purchasing)"},
                    {"funcionalidade", "Coleta de Informações de Pagamento"},
                    {"funcionalidade", "Integração com CMS (CMS Integration)"},
                    {"funcionalidade", "Páginas de Administração de Usuários (User Admin pages)"},
                    {"funcionalidade", "Moderação / Aprovação de Conteúdo (Moderation / Content Approval)"},
                    {"funcionalidade", "Intercom"},
                    {"funcionalidade", "Análises de Uso (Usage Analytics)"},
                    {"funcionalidade", "Relatório de Erros (Crash Reporting)"},
                    {"funcionalidade", "Monitoramento de Performance (Performance Monitoring)"},
                    {"funcionalidade", "Suporte Multilíngue (Multilingual Support)"},
                    {"funcionalidade", "Conectar a um ou mais serviços de terceiros"},
                    {"funcionalidade", "Uma API para que terceiros integrem ao seu app"},
                    {"funcionalidade", "Envio de SMS (SMS Messaging)"},
                    {"funcionalidade", "Mascaramento de Número de Telefone (Phone Number Masking)"},
                    {"funcionalidade", "Segurança baseada em Certificado SSL"},
                    {"funcionalidade", "Proteção contra DoS (DoS protection)"},
                    {"funcionalidade", "Autenticação em Duas Etapas (Two Factor Authentication)"},
                    {"funcionalidade", "Desenvolvimento Específico do Aplicativo"},
                    {"funcionalidade", "Design de Ícone do App (App Icon Design)"},
                    {"funcionalidade", "Sincronização em Nuvem (Cloud Syncing)"},
                    {"funcionalidade", "Dados de Sensores do Dispositivo (Device Sensor Data)"},
                    {"funcionalidade", "Códigos de Barras ou QR Codes (Barcodes ou QR Codes)"},
                    {"funcionalidade", "Dados de Saúde (Health Data)"},
                    {"funcionalidade", "Apple Watch"},
                    {"taxa diária", "designer ui/ux"},
                    {"taxa diária", "gerência de projeto"},
                    {"taxa diária", "desenvolvimento"}
                    
                        
                        // lembrar de colocar os campos fixos de projeto 
                };

                for (String[] campo : campos) {
                    stmt.setString(1, UUID.randomUUID().toString());
                    stmt.setString(2, campo[0]);
                    stmt.setString(3, campo[1]);
                    stmt.executeUpdate();
                }        
                
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
