package com.br.estimativadeprojetodesoftware.builder;

import com.br.estimativadeprojetodesoftware.model.Usuario;

public class AndroidBuilder extends PerfilBuilder {
    public AndroidBuilder(String nomePerfil, Usuario usuario) {
        super(nomePerfil, usuario);
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
        perfil.adicionarFuncionalidade("Contas Multi-tenant", 5);
        perfil.adicionarFuncionalidade("Subdomínios", 4);
        perfil.adicionarFuncionalidade("Domínios Personalizados", 4);
        perfil.adicionarFuncionalidade("Painel (Dashboard)", 5);
        perfil.adicionarFuncionalidade("Feed de Atividades (Activity Feed)", 4);
        perfil.adicionarFuncionalidade("Upload de Arquivos (File Uploading)", 4);
        perfil.adicionarFuncionalidade("Upload de Mídia (Media Uploading)", 0);
        perfil.adicionarFuncionalidade("Perfis de Usuário (User Profiles)", 2);
        perfil.adicionarFuncionalidade("E-mails Transacionais (Transactional Emails)", 2);
        perfil.adicionarFuncionalidade("Tags", 5);
        perfil.adicionarFuncionalidade("Avaliações ou Comentários (Ratings or Reviews)", 5);
        perfil.adicionarFuncionalidade("Processamento de Áudio/Vídeo", 5);
        perfil.adicionarFuncionalidade("Pesquisa de Texto Livre (Free text searching)", 1);
        perfil.adicionarFuncionalidade("Pesquisa (Searching)", 3);
        perfil.adicionarFuncionalidade("Calendário (Calendaring)", 3);
        perfil.adicionarFuncionalidade("Exibição de Dados de Mapa / Geolocalização", 5);
        perfil.adicionarFuncionalidade("Exibição de Marcadores/Regiões de Mapa Personalizados", 5);
        perfil.adicionarFuncionalidade("Reservas (Bookings)", 5);
        perfil.adicionarFuncionalidade("Mensagens (Messaging)", 3);
        perfil.adicionarFuncionalidade("Fóruns ou Comentários", 3);
        perfil.adicionarFuncionalidade("Compartilhamento Social (Social Sharing)", 5);
        perfil.adicionarFuncionalidade("Integração com Facebook Open Graph (Push to Facebook Open Graph)", 5);
        perfil.adicionarFuncionalidade("Notificações Push (Push Notifications)", 0);
        perfil.adicionarFuncionalidade("Planos de Assinatura (Subscription plans)", 5);
        perfil.adicionarFuncionalidade("Processamento de Pagamentos (Payment processing)", 3);
        perfil.adicionarFuncionalidade("Carrinho de Compras (Shopping Cart)", 0);
        perfil.adicionarFuncionalidade("Marketplace de Usuários (User Marketplace)", 0);
        perfil.adicionarFuncionalidade("Gerenciamento de Produtos (Product Management)", 3);
        perfil.adicionarFuncionalidade("Compras dentro do Aplicativo (In-App Purchasing)", 3);
        perfil.adicionarFuncionalidade("Coleta de Informações de Pagamento", 1);
        perfil.adicionarFuncionalidade("Integração com CMS (CMS Integration)", 4);
        perfil.adicionarFuncionalidade("Páginas de Administração de Usuários (User Admin pages)", 3);
        perfil.adicionarFuncionalidade("Moderação / Aprovação de Conteúdo (Moderation / Content Approval)", 4);
        perfil.adicionarFuncionalidade("Intercom", 4);
        perfil.adicionarFuncionalidade("Análises de Uso (Usage Analytics)", 0);
        perfil.adicionarFuncionalidade("Relatório de Erros (Crash Reporting)", 7);
        perfil.adicionarFuncionalidade("Monitoramento de Performance (Performance Monitoring)", 5);
        perfil.adicionarFuncionalidade("Suporte Multilíngue (Multilingual Support)", 5);
        perfil.adicionarFuncionalidade("Conectar a um ou mais serviços de terceiros", 2);
        perfil.adicionarFuncionalidade("Uma API para que terceiros integrem ao seu app", 4);
        perfil.adicionarFuncionalidade("Envio de SMS (SMS Messaging)", 7);
        perfil.adicionarFuncionalidade("Mascaramento de Número de Telefone (Phone Number Masking)", 10);
    }

    @Override
    public void addTaxasDiarias() {
        perfil.adicionarTaxaDiaria("designer ui/ux", 550);
        perfil.adicionarTaxaDiaria("gerência de projeto", 300);
        perfil.adicionarTaxaDiaria("desenvolvimento", 450);
    }

}
