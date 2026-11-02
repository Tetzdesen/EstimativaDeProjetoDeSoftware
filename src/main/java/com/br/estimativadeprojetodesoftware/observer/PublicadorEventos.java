package com.br.estimativadeprojetodesoftware.observer;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Subject compartilhado pelos services. Como cada janela cria suas próprias
 * instâncias de service, os observadores ficam registrados aqui para que uma
 * mudança feita em qualquer janela chegue a todas as janelas abertas.
 */
public final class PublicadorEventos implements Subject {

    private static final PublicadorEventos INSTANCIA = new PublicadorEventos();
    private final List<Observer> observers = new CopyOnWriteArrayList<>();

    private PublicadorEventos() {
    }

    public static PublicadorEventos getInstancia() {
        return INSTANCIA;
    }

    @Override
    public void addObserver(Observer observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(EventoDominio evento) {
        for (Observer observer : observers) {
            observer.update(evento);
        }
    }

    public int quantidadeObservadores() {
        return observers.size();
    }
}
