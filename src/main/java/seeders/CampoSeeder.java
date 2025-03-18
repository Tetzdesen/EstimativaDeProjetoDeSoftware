package seeders;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

public class CampoSeeder implements ITipoSeeder {

    @Override
    public void seed(Connection connection) {

        String sqlCampo = """
                    INSERT INTO campo(idCampo, tipoCampo, nomeCampo) VALUES (
                        'b4a901a7-074b-4ad1-bf0a-7c076849f2db', 'tamanho', 'pequeno',
                        '2f2857c7-4bfc-4f05-aeb9-4bf5900023ed', tamanho', 'médio' ,
                        '149a395a-de3d-44e7-b87d-92c73eccc416', 'tamanho', 'grande' ,
                        '9b909ee6-0329-4e44-bbe6-59231bdf070a', 'nivel', 'mvp' , 
                        '45788a7a-526b-4ee8-bf76-489f6f6eaeb6', 'nivel', 'básico' ,
                        'fb461e7d-daa9-4128-91c3-bbb6cfa84be1', 'nivel', 'profissional' ,
                        '47fe3e3d-35a6-462e-b1ef-ccfe397710d6', 'funcionalidade', 'Cadastro por E-mail e Senha' ,
                        '3ac6996a-0912-4c46-8867-ce2480465858', 'funcionalidade', 'Cadastro pelo Facebook' ,
                        '34fc6bef-5849-4f68-86e6-e4d8b969873e', 'funcionalidade', 'Cadastro pelo Twitter' ,
                        '2687cc9f-3a9a-46ec-9d29-4df4bd096c4a', 'funcionalidade', 'Cadastro pelo Google' ,
                        '8b21d504-b8a7-411c-bf68-960e838df870', 'funcionalidade', 'Cadastro pelo LinkedIn' ,
                        'd9e889d7-d6f5-41dc-a7f2-bdd495a7d1b5', 'funcionalidade', 'Cadastro pelo Github' ,
                        '9ceb34d6-af2e-4e29-ab8d-6e5ee5a071bb', 'funcionalidade', 'E-mails de Convite de Usuário' ,
                        '0245fdfd-5780-44eb-9a88-23fdbd00e038', 'funcionalidade', 'Contas Multi-tenant' ,
                        'bdc6a3f8-3f53-4781-abb3-f5f4994d8420', 'funcionalidade', 'Subdomínios' ,
                        '73ff7cfe-ef3d-401b-9124-0b53ba4c2569', 'funcionalidade', 'Domínios Personalizados' ,
                        'bfbf94d1-206f-49a1-873b-817a60422e49', 'funcionalidade', 'Painel (Dashboard)' ,
                        'bb0b847b-197e-4032-8a96-ef758bbcb76c', 'funcionalidade', 'Feed de Atividades (Activity Feed)' ,
                        '9e61a042-7015-4af2-a132-802489c7ec1b', 'funcionalidade', 'Upload de Arquivos (File Uploading)' ,
                        '992ba03d-5239-4a0a-8443-ba548b6e6431', 'funcionalidade', 'Upload de Mídia (Media Uploading)' ,
                        '12627d49-1ed7-492a-a188-6ef25ac4dbef', 'funcionalidade', 'Perfis de Usuário (User Profiles)' ,
                        '100d947e-2ec2-4b89-ad92-e80603a7bbd3', 'funcionalidade', 'E-mails Transacionais (Transactional Emails)' ,
                        '7f576a1d-4807-4ddd-9f8f-1e22d56e226b', 'funcionalidade', 'Tags' ,
                        'aecfba78-2cd3-4090-8294-de3b4360b9d4', 'funcionalidade', 'Avaliações ou Comentários (Ratings or Reviews)' ,
                        '6adb8628-4591-44b3-ae48-7469e75e314c', 'funcionalidade', 'Processamento de Áudio/Vídeo' ,
                        'db579a4a-c13c-400c-8cf7-0b38ed0d7aeb', 'funcionalidade', 'Pesquisa de Texto Livre (Free text searching)' ,
                        '40f33e98-21c8-4bcc-a976-d7254ff12dbd', 'funcionalidade', 'Pesquisa (Searching)' ,
                        '91e390ea-897d-4378-b14b-0c130b0255b8', 'funcionalidade', 'Calendário (Calendaring)' ,
                        '038002d8-e5cb-4335-9c17-d2a31572a8e1', 'funcionalidade', 'Exibição de Dados de Mapa / Geolocalização' ,
                        '46a35e5d-7756-4730-8476-ee86032f137e', 'funcionalidade', 'Exibição de Marcadores/Regiões de Mapa Personalizados' ,
                        '879fcda7-dff0-488d-9877-ad37de3c43b8', 'funcionalidade', 'Reservas (Bookings)' ,
                        'c7d9ad75-622f-4759-bfaa-2d9e9148d1f3', 'funcionalidade', 'Mensagens (Messaging)' ,
                        '750de3e9-d0a2-4abf-863f-cb6c4ad39e0f', 'funcionalidade', 'Fóruns ou Comentários' ,
                        'c85849f3-4b68-4f00-b905-3d2979d22e80', 'funcionalidade', 'Compartilhamento Social (Social Sharing)' ,
                        '1cd55c47-b212-4589-856f-fdf143f34b88', 'funcionalidade', 'Integração com Facebook Open Graph (Push to Facebook Open Graph)' ,
                        'b93f58f1-67a5-41d6-8100-4234061e5689', 'funcionalidade', 'Notificações Push (Push Notifications)' ,
                        '28873670-b713-4ea9-814c-e3ecdf107cf4', 'funcionalidade', 'Planos de Assinatura (Subscription plans)' ,
                        '94e706b2-d6db-4414-aa78-94cc95117fc9', 'funcionalidade', 'Processamento de Pagamentos (Payment processing)' ,
                        'def9db92-aaf5-4696-bef1-6ed3949d3264', 'funcionalidade', 'Carrinho de Compras (Shopping Cart)' ,
                        '4f6c9f8b-fd04-4176-b618-901b24730a23', 'funcionalidade', 'Marketplace de Usuários (User Marketplace)' ,
                        '280a43a3-6168-46a8-912c-86647bbf08f7', 'funcionalidade', 'Gerenciamento de Produtos (Product Management)' ,
                        '0b4624a3-aaf3-4415-81e2-304db4422645', 'funcionalidade', 'Compras dentro do Aplicativo (In-App Purchasing)' ,
                        '555ada22-a0bd-4ba1-9aa3-cf63a100de38', 'funcionalidade', 'Coleta de Informações de Pagamento' ,
                        'c8170052-4a94-454f-bc92-2471721ec4bf', 'funcionalidade', 'Integração com CMS (CMS Integration)' ,
                        '4f527130-7c02-499b-81d8-81f2c2c49705', 'funcionalidade', 'Páginas de Administração de Usuários (User Admin pages)' ,
                        '028eed0e-5e6c-4bdf-a4f7-fd1070c9de4d', 'funcionalidade', 'Moderação / Aprovação de Conteúdo (Moderation / Content Approval)' ,
                        'c89fd872-a33c-4983-a6b5-172cd6148d96', 'funcionalidade', 'Intercom' ,
                        '04bf538c-b8db-41d8-a1f8-fe175d4cd0ef', 'funcionalidade', 'Análises de Uso (Usage Analytics)' ,
                        'd2bc6f27-1cec-4e2b-a362-6f7f6080a8da', 'funcionalidade', 'Relatório de Erros (Crash Reporting)' ,
                        '26c362cf-bea7-4ead-9fd6-19200956cd63', 'funcionalidade', 'Monitoramento de Performance (Performance Monitoring)' ,
                        'c2111c79-3c27-4b92-b928-50086d0beebe', 'funcionalidade', 'Suporte Multilíngue (Multilingual Support)' ,
                        '812bb368-e39f-4988-9764-8656d9cf59b8', 'funcionalidade', 'Conectar a um ou mais serviços de terceiros' ,
                        'c50c4e4d-7cdb-44b0-b9ec-62ea48c94a41', 'funcionalidade', 'Uma API para que terceiros integrem ao seu app' ,
                        '9013d1b3-a273-404b-8000-d7440a5dfc43', 'funcionalidade', 'Envio de SMS (SMS Messaging)' ,
                        '133fdc79-6981-4481-95fa-218688bf5b32', 'funcionalidade', 'Mascaramento de Número de Telefone (Phone Number Masking)' ,
                        '9abc800f-cc69-46d1-843a-6b5d6226eaf0', 'funcionalidade', 'Segurança baseada em Certificado SSL' ,
                        'f0b42345-650e-4e86-8554-c030f2b9d023', 'funcionalidade', 'Proteção contra DoS (DoS protection)' ,
                        'b0f40f48-283f-4f34-bea9-f8e9b912a6b9', 'funcionalidade', 'Autenticação em Duas Etapas (Two Factor Authentication)' ,
                        '3ce27e7f-abfc-4102-92df-39fbede6c42f', 'funcionalidade', 'Desenvolvimento Específico do Aplicativo' ,
                        '7df55551-db82-46f3-a26e-5f37fa6af3c4', 'funcionalidade', 'Design de Ícone do App (App Icon Design)' ,
                        'b8689954-f746-4f5e-b83e-ecdb0cec5f4a', 'funcionalidade', 'Sincronização em Nuvem (Cloud Syncing)' ,
                        'a57bc71a-cf4c-41d7-9793-bc630794533e', 'funcionalidade', 'Dados de Sensores do Dispositivo (Device Sensor Data)' ,
                        '201ade80-031a-43d5-ba10-dc86312084f6', 'funcionalidade', 'Códigos de Barras ou QR Codes (Barcodes ou QR Codes)' ,
                        '1f9bee54-60e5-4780-9a90-6a0bd565b873', 'funcionalidade', 'Dados de Saúde (Health Data)' ,
                        'a2d4a9a5-a172-49d4-b75c-2bee72194531', 'funcionalidade', 'Apple Watch' ,
                        '592f7684-30e9-4125-b5b0-877e613bde7a', 'funcionalidade', 'Design de Ícone do App (App Icon Design)' ,
                        '3a15e85e-74f3-4ff2-98d2-61c19bab07eb', 'funcionalidade', 'Sincronização em Nuvem (Cloud Syncing)' ,
                        'a8a1af03-e325-453f-83d5-1f53d817b3fb', 'funcionalidade', 'Dados de Sensores do Dispositivo (Device Sensor Data)' ,
                        '9247327a-a523-461e-8f0a-cf7de5bd6ee9', 'funcionalidade', 'Códigos de Barras ou QR Codes' ,
                        '32d0e1a2-466c-4cff-aea5-7828e75b4eef', 'funcionalidade', 'Dados de Saúde (Health Data)' ,
                        'ef8e2c74-b58e-4ae2-9b6a-cb16f26194d9', 'funcionalidade', 'Gerente de Projeto (Project Manager)' ,
                        '0c020c36-40de-4b25-9900-820873be8119', 'taxa diária', 'designer ui/ux' ,
                        'f5097bc7-c9f9-44e8-a292-075f2f9d2047', 'taxa diária', 'gerência de projeto' ,
                        '93606340-6b27-48f8-8109-07d657421834', 'taxa diária', 'desenvolvimento' ,
                        '4dc38b44-06e4-4057-8e50-54449feff971', 'campo fixo', 'Custo com hardware e instalações físicas' ,
                        '030115ce-be5e-420d-9c28-342c8229e400', 'campo fixo', 'Custo com softwares' ,
                        'b1ecfb9a-e3b7-47f0-873e-6bde5d7517ca', 'campo fixo', 'Custo com riscos' ,
                        'bc8e619d-831f-4367-9d34-6464263475de', 'campo fixo', 'Custo com garantia' ,
                        '2a4b827e-6aba-430c-805a-83142999f30b', 'campo fixo', 'Fundo de reserva' ,
                        'f80e1ea8-f369-466d-9249-2756df013392', 'campo fixo', 'Outros custos' ,
                        'a1a7e9aa-1af9-48c0-9814-91e663fb17b4', 'campo fixo', 'Percentual com impostos (%)' ,
                        '298e8fde-f57b-459f-adf6-df175c69d5ba', 'campo fixo', 'Percentual de Lucro desejado (%)'                         
                    );
                """;
      //  try (PreparedStatement stmt = connection.prepareStatement(sqlCampo)) {
            // Inserção dos campos
           

//            for (String[] campo : campos) {
            //    stmt.setString(1, UUID.randomUUID().toString());
             //   stmt.setString(2, campo[0]);
             //   stmt.setString(3, campo[1]);
            //    stmt.execute();
         //   }

       // } catch (SQLException e) {
          //  e.printStackTrace();
       // }

    }
}
