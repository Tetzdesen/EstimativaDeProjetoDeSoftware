package com.br.estimativadeprojetodesoftware.command.seeder;

import com.br.estimativadeprojetodesoftware.command.Command;
import com.br.estimativadeprojetodesoftware.singleton.ConexaoSingleton;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

/**
 *
 * @author tetzner
 */
public class CriarCamposSeederCommand implements Command {

    private final Connection conexao = ConexaoSingleton.getInstancia().getConexao();

    @Override
    public void execute() {
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
                {"taxa diária", "desenvolvimento"},
                {"campo fixo", "Custo com hardware e instalações físicas"},
                {"campo fixo", "Custo com softwares"},
                {"campo fixo", "Custo com riscos"},
                {"campo fixo", "Custo com garantia"},
                {"campo fixo", "Fundo de reserva"},
                {"campo fixo", "Outros custos"},
                {"campo fixo", "Impostos (R$)"},
                {"campo fixo", "Lucro desejado (R$)"}
            };

            for (String[] campo : campos) {
                stmt.setString(1, UUID.randomUUID().toString());
                stmt.setString(2, campo[0]);
                stmt.setString(3, campo[1]);
                stmt.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
