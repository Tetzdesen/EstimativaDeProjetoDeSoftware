package com.br.estimativadeprojetodesoftware.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import com.br.estimativadeprojetodesoftware.builder.AndroidBuilder;
import com.br.estimativadeprojetodesoftware.builder.Diretor;
import com.br.estimativadeprojetodesoftware.builder.IosBuilder;
import com.br.estimativadeprojetodesoftware.builder.WebBackEndBuilder;
import com.br.estimativadeprojetodesoftware.model.Perfil;
import com.br.estimativadeprojetodesoftware.model.Subject;
import com.br.estimativadeprojetodesoftware.presenter.Observer;
import com.br.estimativadeprojetodesoftware.singleton.UsuarioLogadoSingleton;

public class PerfilRepositoryMock implements Subject {
    private final List<Perfil> perfis;
    private final List<Observer> observers;
    
    public PerfilRepositoryMock () {
        perfis = new ArrayList<>();
        observers = new ArrayList<>();

        perfis.add(Diretor.build(new WebBackEndBuilder("Web/Back end")));
        perfis.add(Diretor.build(new IosBuilder("iOS")));
        perfis.add(Diretor.build(new AndroidBuilder("Android")));
    }

    public List<Perfil> getPerfis() {
        // List<Perfil> perfisDoUsuario = new ArrayList<>();
        // for (Perfil perfil : perfis) {
        //     if (perfil.getUsuario().equals(UsuarioLogadoSingleton.getInstancia().getUsuario())) {
        //         perfisDoUsuario.add(perfil);
        //     }
        // }
        // return Collections.unmodifiableList(perfisDoUsuario);
        return Collections.unmodifiableList(perfis);
    }

    public void setPerfil(Perfil perfil) {
        perfis.add(perfil);
        UsuarioLogadoSingleton.getInstancia().getUsuario().adicionarPerfil(perfil);
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
