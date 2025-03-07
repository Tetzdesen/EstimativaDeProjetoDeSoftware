package com.br.estimativadeprojetodesoftware.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import com.br.estimativadeprojetodesoftware.model.Perfil;
import com.br.estimativadeprojetodesoftware.model.Subject;
import com.br.estimativadeprojetodesoftware.presenter.Observer;

public class PerfilRepositoryMock implements Subject {
    private final List<Perfil> perfis;
    private final List<Observer> observers;
    
    public PerfilRepositoryMock () {
        perfis = new ArrayList<>();
        observers = new ArrayList<>();

        //perfil 1
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
        perfis.add(perfil1);

        //perfil 2
        Perfil perfil2 = new Perfil("Android");

        perfil2.adicionarFuncionalidade("Cadastro por E-mail e Senha", 1);
        perfil2.adicionarFuncionalidade("Painel (Dashboard)", 5);
        perfil2.adicionarFuncionalidade("Contas Multi-tenant", 3);
        perfil2.adicionarFuncionalidade("Subdomínios", 4);
        perfil2.adicionarFuncionalidade("E-mails Transacionais", 2);
        perfil2.adicionarFuncionalidade("Gerente de Projeto", 10);
        perfil2.adicionarFuncionalidade("Integração com CMS", 50);
        perfil2.adicionarFuncionalidade("Monitoramento de Performance", 7);
        perfil2.adicionarFuncionalidade("Relatórios de Erros", 1);
        perfis.add(perfil2);
    }

    public List<Perfil> getPerfis() {
        return Collections.unmodifiableList(perfis);
    }

    public void setPerfil(Perfil perfil) {
        perfis.add(perfil);
    }

    public Perfil buscarPerfilPorId(UUID id) {
        for (Perfil perfil : perfis) {
            if (perfil.getId().equals(id)) {
                return perfil;
            }
        }
        throw new RuntimeException("Nenhum perfil encontrado");
    }

    public void removerPerfil(Perfil perfil) {
        perfis.remove(perfil);
    }

    public Perfil removerPerfil(UUID id) {
        Perfil perfil = buscarPerfilPorId(id);
        perfis.remove(perfil);
        return perfil;
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
