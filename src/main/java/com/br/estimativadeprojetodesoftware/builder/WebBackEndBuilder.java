package com.br.estimativadeprojetodesoftware.builder;

public class WebBackEndBuilder extends PerfilBuilder {
    public WebBackEndBuilder(String nomePerfil) {
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
        perfil.adicionarFuncionalidade("Cadastro por E-mail e Senha", 1);
        perfil.adicionarFuncionalidade("Cadastro pelo Facebook", 2);
        perfil.adicionarFuncionalidade("Cadastro pelo Twitter", 2);
        perfil.adicionarFuncionalidade("Cadastro pelo Google", 2);
        perfil.adicionarFuncionalidade("Cadastro pelo LinkedIn", 2);
        perfil.adicionarFuncionalidade("Cadastro pelo Github", 2);
        perfil.adicionarFuncionalidade("E-mails de Convite de Usuário", 2);
        perfil.adicionarFuncionalidade("Contas Multi-tenant", 3);
        perfil.adicionarFuncionalidade("Subdomínios", 4);
        perfil.adicionarFuncionalidade("Domínios Personalizados", 4);
        perfil.adicionarFuncionalidade("Painel (Dashboard)", 5);
        perfil.adicionarFuncionalidade("Feed de Atividades (Activity Feed)", 4);
        perfil.adicionarFuncionalidade("Upload de Arquivos (File Uploading)", 4);
        perfil.adicionarFuncionalidade("Perfis de Usuário (User Profiles)", 2);
        perfil.adicionarFuncionalidade("E-mails Transacionais (Transactional Emails)", 2);
        perfil.adicionarFuncionalidade("Tags", 2);
        perfil.adicionarFuncionalidade("Avaliações ou Comentários (Ratings or Reviews)", 5);
        perfil.adicionarFuncionalidade("Processamento de Áudio/Vídeo", 5);
        perfil.adicionarFuncionalidade("Pesquisa de Texto Livre (Free text searching)", 5);
        perfil.adicionarFuncionalidade("Calendário (Calendaring)", 7);
        perfil.adicionarFuncionalidade("Exibição de Dados de Mapa / Geolocalização", 3);
        perfil.adicionarFuncionalidade("Exibição de Marcadores/Regiões de Mapa Personalizados", 3);
        perfil.adicionarFuncionalidade("Reservas (Bookings)", 8);
        perfil.adicionarFuncionalidade("Mensagens (Messaging)", 6);
        perfil.adicionarFuncionalidade("Fóruns ou Comentários", 5);
        perfil.adicionarFuncionalidade("Compartilhamento Social (Social Sharing)", 2);
        perfil.adicionarFuncionalidade("Integração com Facebook Open Graph (Push to Facebook Open Graph)", 5);
        perfil.adicionarFuncionalidade("Planos de Assinatura (Subscription plans)", 8);
        perfil.adicionarFuncionalidade("Processamento de Pagamentos (Payment processing)", 5);
        perfil.adicionarFuncionalidade("Carrinho de Compras (Shopping Cart)", 8);
        perfil.adicionarFuncionalidade("Marketplace de Usuários (User Marketplace)", 12);
        perfil.adicionarFuncionalidade("Gerenciamento de Produtos (Product Management)", 4);
        perfil.adicionarFuncionalidade("Integração com CMS (CMS Integration)", 7);
        perfil.adicionarFuncionalidade("Páginas de Administração de Usuários (User Admin pages)", 3);
        perfil.adicionarFuncionalidade("Moderação / Aprovação de Conteúdo (Moderation / Content Approval)", 4);
        perfil.adicionarFuncionalidade("Intercom", 3);
        perfil.adicionarFuncionalidade("Análises de Uso (Usage Analytics)", 3);
        perfil.adicionarFuncionalidade("Relatório de Erros (Crash Reporting)", 0);
        perfil.adicionarFuncionalidade("Monitoramento de Performance (Performance Monitoring)", 1);
        perfil.adicionarFuncionalidade("Suporte Multilíngue (Multilingual Support)", 4);
        perfil.adicionarFuncionalidade("Conectar a um ou mais serviços de terceiros", 6);
        perfil.adicionarFuncionalidade("Uma API para que terceiros integrem ao seu app", 8);
        perfil.adicionarFuncionalidade("Envio de SMS (SMS Messaging)", 4);
        perfil.adicionarFuncionalidade("Mascaramento de Número de Telefone (Phone Number Masking)", 4);
        perfil.adicionarFuncionalidade("Segurança baseada em Certificado SSL", 3);
        perfil.adicionarFuncionalidade("Proteção contra DoS (DoS protection)", 5);
        perfil.adicionarFuncionalidade("Autenticação em Duas Etapas (Two Factor Authentication)", 5);
        perfil.adicionarFuncionalidade("Desenvolvimento Específico do Aplicativo", 0);
    }

    @Override
    public void addTaxasDiarias() {
        perfil.adicionarTaxaDiaria("designer ui/ux", 0);
        perfil.adicionarTaxaDiaria("gerência de projeto", 300);
        perfil.adicionarTaxaDiaria("desenvolvimento", 450);
    }

}
