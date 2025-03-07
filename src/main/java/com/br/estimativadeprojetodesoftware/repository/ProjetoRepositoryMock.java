package com.br.estimativadeprojetodesoftware.repository;

import com.br.estimativadeprojetodesoftware.model.Estimativa;
import com.br.estimativadeprojetodesoftware.model.Perfil;
import com.br.estimativadeprojetodesoftware.model.Projeto;
import com.br.estimativadeprojetodesoftware.model.Subject;
import com.br.estimativadeprojetodesoftware.presenter.Observer;

import java.util.*;

public class ProjetoRepositoryMock implements Subject {

    private final List<Projeto> projetos;
    private final List<Observer> observers;

    public ProjetoRepositoryMock() {
        projetos = new ArrayList<>();
        observers = new ArrayList<>();

        //criação do perfil 1
        Perfil perfil1 = new Perfil("Web/Back-end");

        perfil1.adicionarFuncionalidade("Cadastro por E-mail e Senha", 1);
        perfil1.adicionarFuncionalidade("Painel (Dashboard)", 5);
        perfil1.adicionarFuncionalidade("Contas Multi-tenant", 3);
        perfil1.adicionarFuncionalidade("Subdomínios", 4);
        perfil1.adicionarFuncionalidade("E-mails Transacionais", 2);
        perfil1.adicionarFuncionalidade("Gerente de Projeto", 10);
        perfil1.adicionarFuncionalidade("Integração com CMS", 50);
        perfil1.adicionarFuncionalidade("Monitoramento de Performance", 7);
        perfil1.adicionarFuncionalidade("Relatórios de Erros", 1);

        // Projeto 1: Web/Back-end
        Estimativa estimativa1 = new Estimativa();

        estimativa1.adicionarCampo("Tamanho do App: Médio", 30);
        estimativa1.adicionarCampo("Cadastro por E-mail e Senha", 1);
        estimativa1.adicionarCampo("Painel (Dashboard)", 5);
        estimativa1.adicionarCampo("Contas Multi-tenant", 3);
        estimativa1.adicionarCampo("Subdomínios", 4);
        estimativa1.adicionarCampo("E-mails Transacionais", 2);
        estimativa1.adicionarCampo("Gerente de Projeto", 10);
        estimativa1.adicionarCampo("Nível de UI: Básico", 50);
        estimativa1.adicionarCampo("Integração com CMS", 7);
        estimativa1.adicionarCampo("Monitoramento de Performance", 1);
        estimativa1.adicionarCampo("Relatórios de Erros", 1);

        Projeto projeto1 = new Projeto("Gerenciamento Corporativo", "Usuario 1", "Web/Back-End");
        projeto1.setEstimativa(estimativa1);
        projeto1.adicionarPerfil(perfil1);
        projetos.add(projeto1);

        // Projeto 2: iOS
        Estimativa estimativa2 = new Estimativa();

        estimativa2.adicionarCampo("Tamanho do App: Pequeno", 10);
        estimativa2.adicionarCampo("Cadastro pelo Facebook", 2);
        estimativa2.adicionarCampo("Feed de Atividades", 4);
        estimativa2.adicionarCampo("Upload de Mídia", 4);
        estimativa2.adicionarCampo("Perfis de Usuário", 2);
        estimativa2.adicionarCampo("Notificações Push", 3);
        estimativa2.adicionarCampo("Design de Ícone do App", 7);
        estimativa2.adicionarCampo("Nível de UI: Profissional", 70);
        estimativa2.adicionarCampo("Apple Watch", 7);
        estimativa2.adicionarCampo("Sincronização em Nuvem", 5);

        Projeto projeto2 = new Projeto("Aplicativo Social iOS", "Usuario 2", "iOS");
        projeto2.setEstimativa(estimativa2);
        projeto2.adicionarPerfil(perfil1);
        projetos.add(projeto2);

        // Projeto 3: Android
        Estimativa estimativa3 = new Estimativa();

        estimativa3.adicionarCampo("Tamanho do App: Médio", 30);
        estimativa3.adicionarCampo("Cadastro pelo Google", 2);
        estimativa3.adicionarCampo("Feed de Atividades", 4);
        estimativa3.adicionarCampo("Compartilhamento Social", 1);
        estimativa3.adicionarCampo("Pesquisa", 3);
        estimativa3.adicionarCampo("Mensagens", 5);
        estimativa3.adicionarCampo("Nível de UI: Básico", 50);
        estimativa3.adicionarCampo("Dados de Sensores do Dispositivo", 5);
        estimativa3.adicionarCampo("Códigos de Barras ou QR Codes", 2);

        Projeto projeto3 = new Projeto("Aplicativo de Marketplace Android", "Usuario 3", "Android");
        projeto3.setEstimativa(estimativa3);
        projeto3.adicionarPerfil(perfil1);
        projetos.add(projeto3);

        // Projeto 4: Web/Back-end e iOS
        Estimativa estimativa4 = new Estimativa();

        estimativa4.adicionarCampo("Tamanho do App: Grande", 50);
        estimativa4.adicionarCampo("Cadastro por E-mail e Senha", 1);
        estimativa4.adicionarCampo("Feed de Atividades", 4);
        estimativa4.adicionarCampo("Painel (Dashboard)", 5);
        estimativa4.adicionarCampo("Notificações Push", 3);
        estimativa4.adicionarCampo("Upload de Mídia", 4);
        estimativa4.adicionarCampo("Gerente de Projeto", 10);
        estimativa4.adicionarCampo("Nível de UI: Profissional", 70);
        estimativa4.adicionarCampo("Planos de Assinatura", 8);
        estimativa4.adicionarCampo("Envio de SMS", 4);

        Projeto projeto4 = new Projeto("Aplicativo Financeiro Completo", "Usuario 4", "Web/Back-end e iOS");
        projeto4.setEstimativa(estimativa4);
        projeto4.adicionarPerfil(perfil1);
        projetos.add(projeto4);

        // Projeto 5: Android com Web/Back-end
        Estimativa estimativa5 = new Estimativa();

        estimativa5.adicionarCampo("Tamanho do App: Médio", 30);
        estimativa5.adicionarCampo("Cadastro pelo Facebook", 2);
        estimativa5.adicionarCampo("Feed de Atividades", 4);
        estimativa5.adicionarCampo("Mensagens", 5);
        estimativa5.adicionarCampo("Compartilhamento Social", 1);
        estimativa5.adicionarCampo("Pesquisa", 3);
        estimativa5.adicionarCampo("Painel (Dashboard)", 5);
        estimativa5.adicionarCampo("Nível de UI: Básico", 50);
        estimativa5.adicionarCampo("Processamento de Pagamentos", 5);
        estimativa5.adicionarCampo("Suporte Multilíngue", 4);

        Projeto projeto5 = new Projeto("Plataforma de Compras Android", "Usuario 5", "Android e Web/Back-end");
        projeto5.setEstimativa(estimativa5);
        projeto5.adicionarPerfil(perfil1);
        projetos.add(projeto5);

        // Projeto 6: Web/Back-end com funcionalidades extras
        Estimativa estimativa6 = new Estimativa();

        estimativa6.adicionarCampo("Tamanho do App: Pequeno", 10);
        estimativa6.adicionarCampo("Painel (Dashboard)", 5);
        estimativa6.adicionarCampo("E-mails Transacionais", 2);
        estimativa6.adicionarCampo("Subdomínios", 4);
        estimativa6.adicionarCampo("Contas Multi-tenant", 3);
        estimativa6.adicionarCampo("Nível de UI: MVP", 30);
        estimativa6.adicionarCampo("Conectar a um ou mais serviços de terceiros", 6);
        estimativa6.adicionarCampo("Moderação / Aprovação de Conteúdo", 4);

        Projeto projeto6 = new Projeto("Sistema de Suporte Web", "Usuario 6", "Web/Back-end");
        projeto6.setEstimativa(estimativa6);
        projeto6.adicionarPerfil(perfil1);
        projetos.add(projeto6);
    }

    public List<Projeto> getProjetos() {
        return projetos;
    }

    public Projeto getProjetoPorNome(String nome) {
        return projetos.stream()
                .filter(projeto -> projeto.getNome().equals(nome))
                .findFirst()
                .orElse(null);
    }

    public void adicionarProjeto(String nome, String criador, List<String> tipos, Map<String, Integer> funcionalidadesEscolhidas,
            String status, boolean compartilhado, String compartilhadoPor) {

        Projeto novoProjeto = new Projeto(nome, criador, tipos.get(0)); 

        for (String tipo : tipos) {
            novoProjeto.adicionarPerfil(new Perfil(tipo));
        }

        novoProjeto.setCompartilhado(compartilhado);

        if (compartilhado && compartilhadoPor != null && !compartilhadoPor.isEmpty()) {
            novoProjeto.setCompartilhadoPor(compartilhadoPor);
        }
 
        if (funcionalidadesEscolhidas != null && !funcionalidadesEscolhidas.isEmpty()) {
            Estimativa estimativa = new Estimativa();
            for (Map.Entry<String, Integer> entry : funcionalidadesEscolhidas.entrySet()) {
                estimativa.adicionarCampo(entry.getKey(), entry.getValue());
            }
            novoProjeto.setEstimativa(estimativa);
        }

        projetos.add(novoProjeto);
        notifyObservers();
    }

    public boolean removerProjetoPorNome(String nome) {
        boolean removido = projetos.removeIf(projeto -> projeto.getNome().equals(nome));
        if (removido) {
            notifyObservers();
        }
        return removido;
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update();
        }
    }

}
