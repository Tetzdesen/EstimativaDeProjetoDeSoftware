package com.br.estimativadeprojetodesoftware.builder;

import com.br.estimativadeprojetodesoftware.model.Usuario;

public class IosBuilder extends PerfilBuilder {
    public IosBuilder(String nomePerfil, Usuario usuario) {
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
        perfil.adicionarFuncionalidade("Cadastro por E-mail e Senha", 1);
        perfil.adicionarFuncionalidade("Cadastro pelo Facebook", 2);
        perfil.adicionarFuncionalidade("Cadastro pelo Twitter", 2);
        perfil.adicionarFuncionalidade("Cadastro pelo Google", 2);
        perfil.adicionarFuncionalidade("Cadastro pelo LinkedIn", 2);
        perfil.adicionarFuncionalidade("Cadastro pelo Github", 2);
        perfil.adicionarFuncionalidade("E-mails de Convite de Usuário", 0);
        perfil.adicionarFuncionalidade("Contas Multi-tenant", 0);
        perfil.adicionarFuncionalidade("Subdomínios", 0);
        perfil.adicionarFuncionalidade("Domínios Personalizados", 0);
        perfil.adicionarFuncionalidade("Painel (Dashboard)", 5);
        perfil.adicionarFuncionalidade("Feed de Atividades (Activity Feed)", 4);
        perfil.adicionarFuncionalidade("Upload de Arquivos (File Uploading)", 0);
        perfil.adicionarFuncionalidade("Upload de Mídia (Media Uploading)", 4);
        perfil.adicionarFuncionalidade("Perfis de Usuário (User Profiles)", 2);
        perfil.adicionarFuncionalidade("E-mails Transacionais (Transactional Emails)", 0);
        perfil.adicionarFuncionalidade("Tags", 2);
        perfil.adicionarFuncionalidade("Avaliações ou Comentários (Ratings or Reviews)", 5);
        perfil.adicionarFuncionalidade("Processamento de Áudio/Vídeo", 0);
        perfil.adicionarFuncionalidade("Pesquisa de Texto Livre (Free text searching)", 0);
        perfil.adicionarFuncionalidade("Pesquisa (Searching)", 3);
        perfil.adicionarFuncionalidade("Calendário (Calendaring)", 6);
        perfil.adicionarFuncionalidade("Exibição de Dados de Mapa / Geolocalização", 3);
        perfil.adicionarFuncionalidade("Exibição de Marcadores/Regiões de Mapa Personalizados", 3);
        perfil.adicionarFuncionalidade("Reservas (Bookings)", 5);
        perfil.adicionarFuncionalidade("Mensagens (Messaging)", 5);
        perfil.adicionarFuncionalidade("Fóruns ou Comentários", 5);
        perfil.adicionarFuncionalidade("Compartilhamento Social (Social Sharing)", 1);
        perfil.adicionarFuncionalidade("Integração com Facebook Open Graph (Push to Facebook Open Graph)", 3);
        perfil.adicionarFuncionalidade("Notificações Push (Push Notifications)", 3);
        perfil.adicionarFuncionalidade("Planos de Assinatura (Subscription plans)", 0);
        perfil.adicionarFuncionalidade("Processamento de Pagamentos (Payment processing)", 5);
        perfil.adicionarFuncionalidade("Carrinho de Compras (Shopping Cart)", 5);
        perfil.adicionarFuncionalidade("Marketplace de Usuários (User Marketplace)", 0);
        perfil.adicionarFuncionalidade("Gerenciamento de Produtos (Product Management)", 0);
        perfil.adicionarFuncionalidade("Compras dentro do Aplicativo (In-App Purchasing)", 5);
        perfil.adicionarFuncionalidade("Coleta de Informações de Pagamento", 3);
        perfil.adicionarFuncionalidade("Integração com CMS (CMS Integration)", 0);
        perfil.adicionarFuncionalidade("Páginas de Administração de Usuários (User Admin pages)", 0);
        perfil.adicionarFuncionalidade("Moderação / Aprovação de Conteúdo (Moderation / Content Approval)", 0);
        perfil.adicionarFuncionalidade("Intercom", 3);
        perfil.adicionarFuncionalidade("Análises de Uso (Usage Analytics)", 3);
        perfil.adicionarFuncionalidade("Relatório de Erros (Crash Reporting)", 1);
        perfil.adicionarFuncionalidade("Monitoramento de Performance (Performance Monitoring)", 0);
        perfil.adicionarFuncionalidade("Suporte Multilíngue (Multilingual Support)", 4);
        perfil.adicionarFuncionalidade("Conectar a um ou mais serviços de terceiros", 3);
        perfil.adicionarFuncionalidade("Uma API para que terceiros integrem ao seu app", 0);
        perfil.adicionarFuncionalidade("Envio de SMS (SMS Messaging)", 4);
        perfil.adicionarFuncionalidade("Mascaramento de Número de Telefone (Phone Number Masking)", 4);
        perfil.adicionarFuncionalidade("Segurança baseada em Certificado SSL", 0);
        perfil.adicionarFuncionalidade("Proteção contra DoS (DoS protection)", 0);
        perfil.adicionarFuncionalidade("Autenticação em Duas Etapas (Two Factor Authentication)", 5);
        perfil.adicionarFuncionalidade("Desenvolvimento Específico do Aplicativo", 0);
        perfil.adicionarFuncionalidade("Design de Ícone do App (App Icon Design)", 7);
        perfil.adicionarFuncionalidade("Sincronização em Nuvem (Cloud Syncing)", 5);
        perfil.adicionarFuncionalidade("Dados de Sensores do Dispositivo (Device Sensor Data)", 5);
        perfil.adicionarFuncionalidade("Códigos de Barras ou QR Codes (Barcodes ou QR Codes)", 2);
        perfil.adicionarFuncionalidade("Dados de Saúde (Health Data)", 4);
        perfil.adicionarFuncionalidade("Apple Watch", 7);
        perfil.adicionarFuncionalidade("Design de Ícone do App (App Icon Design)", 0);
        perfil.adicionarFuncionalidade("Sincronização em Nuvem (Cloud Syncing)", 0);
        perfil.adicionarFuncionalidade("Dados de Sensores do Dispositivo (Device Sensor Data)", 0);
        perfil.adicionarFuncionalidade("Códigos de Barras ou QR Codes", 0);
        perfil.adicionarFuncionalidade("Dados de Saúde (Health Data)", 0);
        perfil.adicionarFuncionalidade("Gerente de Projeto (Project Manager)", 10);
    }


    @Override
    public void addTaxasDiarias() {
        perfil.adicionarTaxaDiaria("designer ui/ux", 550);
        perfil.adicionarTaxaDiaria("gerência de projeto", 300);
        perfil.adicionarTaxaDiaria("desenvolvimento", 450);
    }

}
