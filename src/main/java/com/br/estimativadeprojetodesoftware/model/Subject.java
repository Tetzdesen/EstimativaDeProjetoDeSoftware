package com.br.estimativadeprojetodesoftware.model;

import com.br.estimativadeprojetodesoftware.presenter.Observer;

public interface Subject {
    void addObserver(Observer observer);

    void removeObserver(Observer observer);

    void notifyObservers();
}
