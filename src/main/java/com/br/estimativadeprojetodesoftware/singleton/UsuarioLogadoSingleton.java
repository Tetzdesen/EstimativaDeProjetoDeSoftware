package com.br.estimativadeprojetodesoftware.singleton;

import com.br.estimativadeprojetodesoftware.model.Subject;
import com.br.estimativadeprojetodesoftware.model.Usuario;
import com.br.estimativadeprojetodesoftware.presenter.Observer;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tetzner
 */
public class UsuarioLogadoSingleton implements Subject {

    public static UsuarioLogadoSingleton instancia = null;
    private Usuario usuario;
    private final List<Observer> observers;

    private UsuarioLogadoSingleton() {
        this.usuario = null;
        this.observers = new ArrayList<>();
    }

    public static UsuarioLogadoSingleton getInstancia() {
        if (instancia == null) {
            instancia = new UsuarioLogadoSingleton();
        }
        return instancia;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public boolean usuarioLogado() {
        return usuario != null;
    }

    public void logout() {
        this.usuario = null;
        notifyObservers();
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
