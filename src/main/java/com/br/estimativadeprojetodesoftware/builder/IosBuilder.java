package com.br.estimativadeprojetodesoftware.builder;

public class IosBuilder extends PerfilBuilder {
    public IosBuilder(String nomePerfil) {
        super(nomePerfil);
    }

    @Override
    public void addTamanhoApp() {
        perfil.adicionarTamanhoApp("pequeno", 10);
        perfil.adicionarTamanhoApp("médio", 30);
        perfil.adicionarTamanhoApp("grande", 50);
    }

    @Override
    public void addNivelUI() {
        perfil.adicionarNivelUI("mvp", 30);
        perfil.adicionarNivelUI("básico", 50);
        perfil.adicionarNivelUI("profissional", 70);
    }

    @Override
    public void addFuncionalidades() {
        perfil.adicionarFuncionalidade("Cadastro pelo Twitter", 2);
        perfil.adicionarFuncionalidade("Cadastro pelo Google", 2);
        perfil.adicionarFuncionalidade("Cadastro pelo LinkedIn", 2);
        perfil.adicionarFuncionalidade("Cadastro pelo Github", 2);
        perfil.adicionarFuncionalidade("E-mails de Convite de Usuário", 2);
        perfil.adicionarFuncionalidade("Contas Multi-tenant", 3);
        perfil.adicionarFuncionalidade("Subdomínios", 6);
        perfil.adicionarFuncionalidade("Domínios Personalizados", 3);
        perfil.adicionarFuncionalidade("Painel (Dashboard)", 3);
        perfil.adicionarFuncionalidade("Feed de Atividades (Activity Feed)", 5);
        perfil.adicionarFuncionalidade("Upload de Arquivos (File Uploading)", 4);
        perfil.adicionarFuncionalidade("Upload de Mídia (Media Uploading)", 0);
        perfil.adicionarFuncionalidade("Perfis de Usuário (User Profiles)", 4);
        perfil.adicionarFuncionalidade("E-mails Transacionais (Transactional Emails)", 2);
        perfil.adicionarFuncionalidade("Tags", 0);
        perfil.adicionarFuncionalidade("Avaliações ou Comentários (Ratings or Reviews)", 2);
        perfil.adicionarFuncionalidade("Processamento de Áudio/Vídeo", 5);
        perfil.adicionarFuncionalidade("Pesquisa de Texto Livre (Free text searching)", 0);
        perfil.adicionarFuncionalidade("Pesquisa (Searching)", 0);
        perfil.adicionarFuncionalidade("Calendário (Calendaring)", 3);
        perfil.adicionarFuncionalidade("Exibição de Dados de Mapa / Geolocalização", 6);
        perfil.adicionarFuncionalidade("Exibição de Marcadores/Regiões de Mapa Personalizados", 3);
        perfil.adicionarFuncionalidade("Reservas (Bookings)", 3);
        perfil.adicionarFuncionalidade("Mensagens (Messaging)", 5);
        perfil.adicionarFuncionalidade("Fóruns ou Comentários", 5);
        perfil.adicionarFuncionalidade("Compartilhamento Social (Social Sharing)", 5);
        perfil.adicionarFuncionalidade("Integração com Facebook Open Graph (Push to Facebook Open Graph)", 1);
        perfil.adicionarFuncionalidade("Notificações Push (Push Notifications)", 3);
        perfil.adicionarFuncionalidade("Planos de Assinatura (Subscription plans)", 3);
        perfil.adicionarFuncionalidade("Processamento de Pagamentos (Payment processing)", 0);
        perfil.adicionarFuncionalidade("Carrinho de Compras (Shopping Cart)", 5);
        perfil.adicionarFuncionalidade("Marketplace de Usuários (User Marketplace)", 5);
        perfil.adicionarFuncionalidade("Gerenciamento de Produtos (Product Management)", 0);
        perfil.adicionarFuncionalidade("Compras dentro do Aplicativo (In-App Purchasing)", 5);
        perfil.adicionarFuncionalidade("Coleta de Informações de Pagamento", 3);
        perfil.adicionarFuncionalidade("Integração com CMS (CMS Integration)", 0);
        perfil.adicionarFuncionalidade("Páginas de Administração de Usuários (User Admin pages)", 4);
        perfil.adicionarFuncionalidade("Moderação / Aprovação de Conteúdo (Moderation / Content Approval)", 4);
        perfil.adicionarFuncionalidade("Intercom", 0);
        perfil.adicionarFuncionalidade("Análises de Uso (Usage Analytics)", 4);
        perfil.adicionarFuncionalidade("Relatório de Erros (Crash Reporting)", 4);
        perfil.adicionarFuncionalidade("Monitoramento de Performance (Performance Monitoring)", 0);
        perfil.adicionarFuncionalidade("Suporte Multilíngue (Multilingual Support)", 5);
        perfil.adicionarFuncionalidade("Conectar a um ou mais serviços de terceiros", 0);
        perfil.adicionarFuncionalidade("Uma API para que terceiros integrem ao seu app", 7);
        perfil.adicionarFuncionalidade("Envio de SMS (SMS Messaging)", 5);
        perfil.adicionarFuncionalidade("Mascaramento de Número de Telefone (Phone Number Masking)", 5);
        perfil.adicionarFuncionalidade("Segurança baseada em Certificado SSL", 2);
        perfil.adicionarFuncionalidade("Proteção contra DoS (DoS protection)", 4);
        perfil.adicionarFuncionalidade("Autenticação em Duas Etapas (Two Factor Authentication)", 7);
        perfil.adicionarFuncionalidade("Desenvolvimento Específico do Aplicativo", 10);
    }

    @Override
    public void addTaxasDiarias() {
        perfil.adicionarTaxaDiaria("designer ui/ux", 550);
        perfil.adicionarTaxaDiaria("gerência de projeto", 300);
        perfil.adicionarTaxaDiaria("desenvolvimento", 450);
    }

}
