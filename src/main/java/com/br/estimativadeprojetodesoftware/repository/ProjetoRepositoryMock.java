package com.br.estimativadeprojetodesoftware.repository;

import com.br.estimativadeprojetodesoftware.model.Campo;
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

        // Criação do perfil 1
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
        List<Campo> camposProjeto1 = new ArrayList<>();
        camposProjeto1.add(new Campo(UUID.randomUUID(), "Tamanho do App: Médio", "", 30.00));
        camposProjeto1.add(new Campo(UUID.randomUUID(), "Cadastro por E-mail e Senha", "", 1.00));
        camposProjeto1.add(new Campo(UUID.randomUUID(), "Painel (Dashboard)", "", 5.00));
        camposProjeto1.add(new Campo(UUID.randomUUID(), "Contas Multi-tenant", "", 3.00));
        camposProjeto1.add(new Campo(UUID.randomUUID(), "Subdomínios", "", 4.00));
        camposProjeto1.add(new Campo(UUID.randomUUID(), "E-mails Transacionais", "", 2.00));
        camposProjeto1.add(new Campo(UUID.randomUUID(), "Gerente de Projeto", "", 10.00));
        camposProjeto1.add(new Campo(UUID.randomUUID(), "Nível de UI: Básico", "", 50.00));
        camposProjeto1.add(new Campo(UUID.randomUUID(), "Integração com CMS", "", 7.00));
        camposProjeto1.add(new Campo(UUID.randomUUID(), "Monitoramento de Performance", "", 1.00));
        camposProjeto1.add(new Campo(UUID.randomUUID(), "Relatórios de Erros", "", 1.00));

        Projeto projeto1 = new Projeto("Gerenciamento Corporativo", "Usuario 1", "Web/Back-End", camposProjeto1);
        projeto1.adicionarPerfil(perfil1);
        projetos.add(projeto1);

        // Projeto 2: iOS
        List<Campo> camposProjeto2 = new ArrayList<>();
        camposProjeto2.add(new Campo(UUID.randomUUID(), "Tamanho do App: Pequeno", "", 10.00));
        camposProjeto2.add(new Campo(UUID.randomUUID(), "Cadastro pelo Facebook", "", 2.00));
        camposProjeto2.add(new Campo(UUID.randomUUID(), "Feed de Atividades", "", 4.00));
        camposProjeto2.add(new Campo(UUID.randomUUID(), "Upload de Mídia", "", 4.00));
        camposProjeto2.add(new Campo(UUID.randomUUID(), "Perfis de Usuário", "", 2.00));
        camposProjeto2.add(new Campo(UUID.randomUUID(), "Notificações Push", "", 3.00));
        camposProjeto2.add(new Campo(UUID.randomUUID(), "Design de Ícone do App", "", 7.00));
        camposProjeto2.add(new Campo(UUID.randomUUID(), "Nível de UI: Profissional", "", 70.00));
        camposProjeto2.add(new Campo(UUID.randomUUID(), "Apple Watch", "", 7.00));
        camposProjeto2.add(new Campo(UUID.randomUUID(), "Sincronização em Nuvem", "", 5.00));

        Projeto projeto2 = new Projeto("Aplicativo Social iOS", "Usuario 2", "iOS", camposProjeto2);
        projeto2.adicionarPerfil(perfil1);
        projetos.add(projeto2);

        // Projeto 3: Android
        List<Campo> camposProjeto3 = new ArrayList<>();
        camposProjeto3.add(new Campo(UUID.randomUUID(), "Tamanho do App: Médio", "", 30.00));
        camposProjeto3.add(new Campo(UUID.randomUUID(), "Cadastro pelo Google", "", 2.00));
        camposProjeto3.add(new Campo(UUID.randomUUID(), "Feed de Atividades", "", 4.00));
        camposProjeto3.add(new Campo(UUID.randomUUID(), "Compartilhamento Social", "", 1.00));
        camposProjeto3.add(new Campo(UUID.randomUUID(), "Pesquisa", "", 3.00));
        camposProjeto3.add(new Campo(UUID.randomUUID(), "Mensagens", "", 5.00));
        camposProjeto3.add(new Campo(UUID.randomUUID(), "Nível de UI: Básico", "", 50.00));
        camposProjeto3.add(new Campo(UUID.randomUUID(), "Dados de Sensores do Dispositivo", "", 5.00));
        camposProjeto3.add(new Campo(UUID.randomUUID(), "Códigos de Barras ou QR Codes", "", 2.00));

        Projeto projeto3 = new Projeto("Aplicativo de Marketplace Android", "Usuario 3", "Android", camposProjeto3);
        projeto3.adicionarPerfil(perfil1);
        projetos.add(projeto3);

        // Projeto 4: Web/Back-end e iOS
        List<Campo> camposProjeto4 = new ArrayList<>();
        camposProjeto4.add(new Campo(UUID.randomUUID(), "Tamanho do App: Grande", "", 50.00));
        camposProjeto4.add(new Campo(UUID.randomUUID(), "Cadastro por E-mail e Senha", "", 1.00));
        camposProjeto4.add(new Campo(UUID.randomUUID(), "Feed de Atividades", "", 4.00));
        camposProjeto4.add(new Campo(UUID.randomUUID(), "Painel (Dashboard)", "", 5.00));
        camposProjeto4.add(new Campo(UUID.randomUUID(), "Notificações Push", "", 3.00));
        camposProjeto4.add(new Campo(UUID.randomUUID(), "Upload de Mídia", "", 4.00));
        camposProjeto4.add(new Campo(UUID.randomUUID(), "Gerente de Projeto", "", 10.00));
        camposProjeto4.add(new Campo(UUID.randomUUID(), "Nível de UI: Profissional", "", 70.00));
        camposProjeto4.add(new Campo(UUID.randomUUID(), "Planos de Assinatura", "", 8.00));
        camposProjeto4.add(new Campo(UUID.randomUUID(), "Envio de SMS", "", 4.00));

        Projeto projeto4 = new Projeto("Aplicativo Financeiro Completo", "Usuario 4", "Web/Back-end e iOS", camposProjeto4);
        projeto4.adicionarPerfil(perfil1);
        projetos.add(projeto4);

        // Projeto 5: Android com Web/Back-end
        List<Campo> camposProjeto5 = new ArrayList<>();
        camposProjeto5.add(new Campo(UUID.randomUUID(), "Tamanho do App: Médio", "", 30.00));
        camposProjeto5.add(new Campo(UUID.randomUUID(), "Cadastro pelo Facebook", "", 2.00));
        camposProjeto5.add(new Campo(UUID.randomUUID(), "Feed de Atividades", "", 4.00));
        camposProjeto5.add(new Campo(UUID.randomUUID(), "Mensagens", "", 5.00));
        camposProjeto5.add(new Campo(UUID.randomUUID(), "Compartilhamento Social", "", 1.00));
        camposProjeto5.add(new Campo(UUID.randomUUID(), "Pesquisa", "", 3.00));
        camposProjeto5.add(new Campo(UUID.randomUUID(), "Painel (Dashboard)", "", 5.00));
        camposProjeto5.add(new Campo(UUID.randomUUID(), "Nível de UI: Básico", "", 50.00));
        camposProjeto5.add(new Campo(UUID.randomUUID(), "Processamento de Pagamentos", "", 5.00));
        camposProjeto5.add(new Campo(UUID.randomUUID(), "Suporte Multilíngue", "", 4.00));

        Projeto projeto5 = new Projeto("Plataforma de Compras Android", "Usuario 5", "Android e Web/Back-end", camposProjeto5);
        projeto5.adicionarPerfil(perfil1);
        projetos.add(projeto5);

        // Projeto 6: Web/Back-end com funcionalidades extras
        List<Campo> camposProjeto6 = new ArrayList<>();
        camposProjeto6.add(new Campo(UUID.randomUUID(), "Tamanho do App: Pequeno", "", 10.00));
        camposProjeto6.add(new Campo(UUID.randomUUID(), "Painel (Dashboard)", "", 5.00));
        camposProjeto6.add(new Campo(UUID.randomUUID(), "E-mails Transacionais", "", 2.00));
        camposProjeto6.add(new Campo(UUID.randomUUID(), "Subdomínios", "", 4.00));
        camposProjeto6.add(new Campo(UUID.randomUUID(), "Contas Multi-tenant", "", 3.00));
        camposProjeto6.add(new Campo(UUID.randomUUID(), "Nível de UI: MVP", "", 30.00));
        camposProjeto6.add(new Campo(UUID.randomUUID(), "Conectar a um ou mais serviços de terceiros", "", 6.00));
        camposProjeto6.add(new Campo(UUID.randomUUID(), "Moderação / Aprovação de Conteúdo", "", 4.00));

        Projeto projeto6 = new Projeto("Sistema de Suporte Web", "Usuario 6", "Web/Back-end", camposProjeto6);
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

    public void adicionarProjeto(Projeto projeto) {
        projetos.add(projeto);
        notifyObservers();
    }

    public void adicionarProjeto(String nome, String criador, List<String> tipos, List<Campo> campos,
                                 String status, boolean compartilhado, String compartilhadoPor) {

        Projeto novoProjeto = new Projeto(nome, criador, tipos.get(0));

        for (String tipo : tipos) {
            novoProjeto.adicionarPerfil(new Perfil(tipo));
        }

        novoProjeto.setCompartilhado(compartilhado);

        if (compartilhado && compartilhadoPor != null && !compartilhadoPor.isEmpty()) {
            novoProjeto.setCompartilhadoPor(compartilhadoPor);
        }

        for (Campo campo : campos) {
            novoProjeto.adicionarCampo(campo);
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
