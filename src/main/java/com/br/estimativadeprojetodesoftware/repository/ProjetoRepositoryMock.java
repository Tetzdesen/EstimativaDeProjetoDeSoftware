package com.br.estimativadeprojetodesoftware.repository;

import com.br.estimativadeprojetodesoftware.model.Estimativa;
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

        // Projeto 1: Web/Back-end
        Estimativa estimativa1 = new Estimativa();
        
        Map<String, Integer> funcionalidades1 = new HashMap<>();
        
        estimativa1.adicionarCampo"Tamanho do App: Médio", 30);
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
        
        Projeto projeto1 = new Projeto("Gerenciamento Corporativo", "Usuario 1");
        projeto1.setEstimativa(estimativa1);
        projetos.add(projeto1);

        // Projeto 2: iOS
        Map<String, Integer> funcionalidades2 = new HashMap<>();
        funcionalidades2.put("Tamanho do App: Pequeno", 10);
        funcionalidades2.put("Cadastro pelo Facebook", 2);
        funcionalidades2.put("Feed de Atividades", 4);
        funcionalidades2.put("Upload de Mídia", 4);
        funcionalidades2.put("Perfis de Usuário", 2);
        funcionalidades2.put("Notificações Push", 3);
        funcionalidades2.put("Design de Ícone do App", 7);
        funcionalidades2.put("Nível de UI: Profissional", 70);
        funcionalidades2.put("Apple Watch", 7);
        funcionalidades2.put("Sincronização em Nuvem", 5);

        projetos.add(new Projeto(
                "Aplicativo Social iOS",
                "Usuario 2",
                "02/02/2023",
                "Em andamento",
                true,
                "Usuario 1",
                Arrays.asList("iOS"),
                funcionalidades2
        ));

        // Projeto 3: Android
        Map<String, Integer> funcionalidades3 = new HashMap<>();
        funcionalidades3.put("Tamanho do App: Médio", 30);
        funcionalidades3.put("Cadastro pelo Google", 2);
        funcionalidades3.put("Feed de Atividades", 4);
        funcionalidades3.put("Compartilhamento Social", 1);
        funcionalidades3.put("Pesquisa", 3);
        funcionalidades3.put("Mensagens", 5);
        funcionalidades3.put("Nível de UI: Básico", 50);
        funcionalidades3.put("Dados de Sensores do Dispositivo", 5);
        funcionalidades3.put("Códigos de Barras ou QR Codes", 2);

        projetos.add(new Projeto(
                "Aplicativo de Marketplace Android",
                "Usuario 3",
                "03/03/2023",
                "Estimado",
                false,
                null,
                Arrays.asList("Android"),
                funcionalidades3
        ));

        // Projeto 4: Web/Back-end e iOS
        Map<String, Integer> funcionalidades4 = new HashMap<>();
        funcionalidades4.put("Tamanho do App: Grande", 50);
        funcionalidades4.put("Cadastro por E-mail e Senha", 1);
        funcionalidades4.put("Feed de Atividades", 4);
        funcionalidades4.put("Painel (Dashboard)", 5);
        funcionalidades4.put("Notificações Push", 3);
        funcionalidades4.put("Upload de Mídia", 4);
        funcionalidades4.put("Gerente de Projeto", 10);
        funcionalidades4.put("Nível de UI: Profissional", 70);
        funcionalidades4.put("Planos de Assinatura", 8);
        funcionalidades4.put("Envio de SMS", 4);

        projetos.add(new Projeto(
                "Aplicativo Financeiro Completo",
                "Usuario 4",
                "04/04/2023",
                "Em andamento",
                true,
                "Usuario 2",
                Arrays.asList("Web/Back-end", "iOS"),
                funcionalidades4
        ));

        // Projeto 5: Android com Web/Back-end
        Map<String, Integer> funcionalidades5 = new HashMap<>();
        funcionalidades5.put("Tamanho do App: Médio", 30);
        funcionalidades5.put("Cadastro pelo Facebook", 2);
        funcionalidades5.put("Feed de Atividades", 4);
        funcionalidades5.put("Mensagens", 5);
        funcionalidades5.put("Compartilhamento Social", 1);
        funcionalidades5.put("Pesquisa", 3);
        funcionalidades5.put("Painel (Dashboard)", 5);
        funcionalidades5.put("Nível de UI: Básico", 50);
        funcionalidades5.put("Processamento de Pagamentos", 5);
        funcionalidades5.put("Suporte Multilíngue", 4);

        projetos.add(new Projeto(
                "Plataforma de Compras Android",
                "Usuario 5",
                "05/05/2023",
                "Estimado",
                false,
                null,
                Arrays.asList("Android", "Web/Back-end"),
                funcionalidades5
        ));

        // Projeto 6: Web/Back-end com funcionalidades extras
        Map<String, Integer> funcionalidades6 = new HashMap<>();
        funcionalidades6.put("Tamanho do App: Pequeno", 10);
        funcionalidades6.put("Painel (Dashboard)", 5);
        funcionalidades6.put("E-mails Transacionais", 2);
        funcionalidades6.put("Subdomínios", 4);
        funcionalidades6.put("Contas Multi-tenant", 3);
        funcionalidades6.put("Nível de UI: MVP", 30);
        funcionalidades6.put("Conectar a um ou mais serviços de terceiros", 6);
        funcionalidades6.put("Moderação / Aprovação de Conteúdo", 4);

        projetos.add(new Projeto(
                "Sistema de Suporte Web",
                "Usuario 6",
                "06/06/2023",
                "Em andamento",
                true,
                "Usuario 4",
                Arrays.asList("Web/Back-end"),
                funcionalidades6
        ));
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

    public void adicionarProjeto(String nome, String criador, String dataCriacao, String status,
                                 boolean compartilhado, String compartilhadoPor,
                                 List<String> tipos, Map<String, Integer> funcionalidadesEscolhidas) {
        Projeto novoProjeto = new Projeto(nome, criador, dataCriacao, status, compartilhado,
                compartilhadoPor, tipos, funcionalidadesEscolhidas);
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
            observer.update(projetos);
        }
    }

}
