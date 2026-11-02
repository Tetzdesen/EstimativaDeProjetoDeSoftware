package com.br.estimativadeprojetodesoftware.service;

import com.br.estimativadeprojetodesoftware.observer.EventoDominio;
import com.br.estimativadeprojetodesoftware.observer.Observer;
import com.br.estimativadeprojetodesoftware.observer.PublicadorEventos;
import com.br.estimativadeprojetodesoftware.observer.Subject;
import java.util.UUID;

/** Base dos services que são Subject: delegam ao publicador compartilhado. */
public abstract class ServicoObservavel implements Subject {

    private final Subject publicador;

    protected ServicoObservavel(Subject publicador) {
        this.publicador = publicador == null ? PublicadorEventos.getInstancia() : publicador;
    }

    protected ServicoObservavel() {
        this(PublicadorEventos.getInstancia());
    }

    @Override
    public void addObserver(Observer observer) {
        publicador.addObserver(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        publicador.removeObserver(observer);
    }

    @Override
    public void notifyObservers(EventoDominio evento) {
        publicador.notifyObservers(evento);
    }

    protected void publicar(EventoDominio.Tipo tipo, EventoDominio.Entidade entidade, UUID id) {
        notifyObservers(new EventoDominio(tipo, entidade, id));
    }
}
