package com.br.estimativadeprojetodesoftware.observer;

/** Observer: janelas abertas que reagem a mudanças de dados. */
public interface Observer {

    void update(EventoDominio evento);
}
