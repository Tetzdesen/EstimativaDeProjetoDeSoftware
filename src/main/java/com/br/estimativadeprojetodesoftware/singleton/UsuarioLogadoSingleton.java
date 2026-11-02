package com.br.estimativadeprojetodesoftware.singleton;

import com.br.estimativadeprojetodesoftware.observer.Subject;
import com.br.estimativadeprojetodesoftware.model.Usuario;
import com.br.estimativadeprojetodesoftware.observer.EventoDominio;
import com.br.estimativadeprojetodesoftware.observer.Observer;
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
        if (usuario != null) {
            notifyObservers(new EventoDominio(EventoDominio.Tipo.ALTERACAO, EventoDominio.Entidade.SESSAO, usuario.getId()));
        }
    }

    public boolean usuarioLogado() {
        return usuario != null;
    }

    public void logout() {
        this.usuario = null;
        notifyObservers(new EventoDominio(EventoDominio.Tipo.EXCLUSAO, EventoDominio.Entidade.SESSAO, null));
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
    public void notifyObservers(EventoDominio evento) {
        for (Observer observer : new ArrayList<>(observers)) {
            observer.update(evento);
        }
    }

}
