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
    /** Nos testes tudo roda de forma síncrona (mesma propriedade da fila do banco). */
    private static final boolean ENTREGA_IMEDIATA = Boolean.getBoolean("estimativa.fila.imediata");
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

    /**
     * Entrega o evento na thread do Swing, porque os observadores atualizam
     * telas e os services podem publicar a partir da fila do banco.
     */
    @Override
    public void notifyObservers(EventoDominio evento) {
        if (ENTREGA_IMEDIATA || javax.swing.SwingUtilities.isEventDispatchThread()) {
            entregar(evento);
        } else {
            javax.swing.SwingUtilities.invokeLater(() -> entregar(evento));
        }
    }

    private void entregar(EventoDominio evento) {
        for (Observer observer : observers) {
            observer.update(evento);
        }
    }

    public int quantidadeObservadores() {
        return observers.size();
    }
}
