package com.br.estimativadeprojetodesoftware.builder;

public class DefaultBuilder extends PerfilBuilder {
    public DefaultBuilder(String nomePerfil) {
        super(nomePerfil);
    }

    @Override
    public void addTamanhoApp() {
        perfil.adicionarFuncionalidade("Tamanho do App: pequeno", 0);
        perfil.adicionarFuncionalidade("Tamanho do App: médio", 0);
        perfil.adicionarFuncionalidade("Tamanho do App: Grande", 0);
    }

    @Override
    public void addNivelUI() {
        perfil.adicionarFuncionalidade("Nível de UI (Percentual) MVP", 0);
        perfil.adicionarFuncionalidade("Nível de UI (Percentual) Básico", 0);
        perfil.adicionarFuncionalidade("Nível de UI (Percentual) Profissional", 0);
    }

    @Override
    public void addFuncionalidades() {
        perfil.adicionarFuncionalidade("Cadastro pelo Twitter", 0);
        perfil.adicionarFuncionalidade("Cadastro pelo Google", 0);
        perfil.adicionarFuncionalidade("Cadastro pelo LinkedIn", 0);
        perfil.adicionarFuncionalidade("Cadastro pelo Github", 0);
        perfil.adicionarFuncionalidade("E-mails de Convite de Usuário", 0);
        perfil.adicionarFuncionalidade("Contas Multi-tenant", 0);
        perfil.adicionarFuncionalidade("Subdomínios", 0);
        perfil.adicionarFuncionalidade("Domínios Personalizados", 0);
        perfil.adicionarFuncionalidade("Painel (Dashboard)", 0);
        perfil.adicionarFuncionalidade("Feed de Atividades (Activity Feed)", 0);
        perfil.adicionarFuncionalidade("Upload de Arquivos (File Uploading)", 0);
        perfil.adicionarFuncionalidade("Upload de Mídia (Media Uploading)", 0);
        perfil.adicionarFuncionalidade("Perfis de Usuário (User Profiles)", 0);
        perfil.adicionarFuncionalidade("E-mails Transacionais (Transactional Emails)", 0);
        perfil.adicionarFuncionalidade("Tags", 0);
        perfil.adicionarFuncionalidade("Avaliações ou Comentários (Ratings or Reviews)", 0);
        perfil.adicionarFuncionalidade("Processamento de Áudio/Vídeo", 0);
        perfil.adicionarFuncionalidade("Pesquisa de Texto Livre (Free text searching)", 0);
        perfil.adicionarFuncionalidade("Pesquisa (Searching)", 0);
        perfil.adicionarFuncionalidade("Calendário (Calendaring)", 0);
        perfil.adicionarFuncionalidade("Exibição de Dados de Mapa / Geolocalização", 0);
        perfil.adicionarFuncionalidade("Exibição de Marcadores/Regiões de Mapa Personalizados", 0);
        perfil.adicionarFuncionalidade("Reservas (Bookings)", 0);
        perfil.adicionarFuncionalidade("Mensagens (Messaging)", 0);
        perfil.adicionarFuncionalidade("Fóruns ou Comentários", 0);
        perfil.adicionarFuncionalidade("Compartilhamento Social (Social Sharing)", 0);
        perfil.adicionarFuncionalidade("Integração com Facebook Open Graph (Push to Facebook Open Graph)", 0);
        perfil.adicionarFuncionalidade("Notificações Push (Push Notifications)", 0);
        perfil.adicionarFuncionalidade("Planos de Assinatura (Subscription plans)", 0);
        perfil.adicionarFuncionalidade("Processamento de Pagamentos (Payment processing)", 0);
        perfil.adicionarFuncionalidade("Carrinho de Compras (Shopping Cart)", 0);
        perfil.adicionarFuncionalidade("Marketplace de Usuários (User Marketplace)", 0);
        perfil.adicionarFuncionalidade("Gerenciamento de Produtos (Product Management)", 0);
        perfil.adicionarFuncionalidade("Compras dentro do Aplicativo (In-App Purchasing)", 0);
        perfil.adicionarFuncionalidade("Coleta de Informações de Pagamento", 0);
        perfil.adicionarFuncionalidade("Integração com CMS (CMS Integration)", 0);
        perfil.adicionarFuncionalidade("Páginas de Administração de Usuários (User Admin pages)", 0);
        perfil.adicionarFuncionalidade("Moderação / Aprovação de Conteúdo (Moderation / Content Approval)", 0);
        perfil.adicionarFuncionalidade("Intercom", 0);
        perfil.adicionarFuncionalidade("Análises de Uso (Usage Analytics)", 0);
        perfil.adicionarFuncionalidade("Relatório de Erros (Crash Reporting)", 0);
        perfil.adicionarFuncionalidade("Monitoramento de Performance (Performance Monitoring)", 0);
        perfil.adicionarFuncionalidade("Suporte Multilíngue (Multilingual Support)", 0);
        perfil.adicionarFuncionalidade("Conectar a um ou mais serviços de terceiros", 0);
        perfil.adicionarFuncionalidade("Uma API para que terceiros integrem ao seu app", 0);
        perfil.adicionarFuncionalidade("Envio de SMS (SMS Messaging)", 0);
        perfil.adicionarFuncionalidade("Mascaramento de Número de Telefone (Phone Number Masking)", 0);
        perfil.adicionarFuncionalidade("Segurança baseada em Certificado SSL", 0);
        perfil.adicionarFuncionalidade("Proteção contra DoS (DoS protection)", 0);
        perfil.adicionarFuncionalidade("Autenticação em Duas Etapas (Two Factor Authentication)", 0);
        perfil.adicionarFuncionalidade("Desenvolvimento Específico do Aplicativo", 0);
        perfil.adicionarFuncionalidade("Design de Ícone do App (App Icon Design)", 0);
        perfil.adicionarFuncionalidade("Sincronização em Nuvem (Cloud Syncing)", 0);
        perfil.adicionarFuncionalidade("Dados de Sensores do Dispositivo (Device Sensor Data)", 0);
        perfil.adicionarFuncionalidade("Códigos de Barras ou QR Codes (Barcodes ou QR Codes)", 0);
        perfil.adicionarFuncionalidade("Dados de Saúde (Health Data)", 0);
        perfil.adicionarFuncionalidade("Apple Watch", 0);
        perfil.adicionarFuncionalidade("Design de Ícone do App (App Icon Design)", 0);
        perfil.adicionarFuncionalidade("Sincronização em Nuvem (Cloud Syncing)", 0);
        perfil.adicionarFuncionalidade("Dados de Sensores do Dispositivo (Device Sensor Data)", 0);
        perfil.adicionarFuncionalidade("Códigos de Barras ou QR Codes", 0);
        perfil.adicionarFuncionalidade("Dados de Saúde (Health Data)", 0);
        perfil.adicionarFuncionalidade("Gerente de Projeto (Project Manager)", 0);
        perfil.adicionarFuncionalidade("Taxa Diária de Design (Design Day Rate)", 0);
        perfil.adicionarFuncionalidade("Taxa Diária de Gerência de Projeto (Developer Day Rate)", 0);
        perfil.adicionarFuncionalidade("Taxa Diária de Desenvolvimento (Developer Day Rate)", 0);
        perfil.adicionarFuncionalidade("Health Data", 0);
        perfil.adicionarFuncionalidade("Apple Watch", 0);
        
    }

    @Override
    public void addTaxasDiarias() {
        
    }
}
