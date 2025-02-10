package com.br.estimativadeprojetodesoftware.presenter;

import java.util.Stack;

public class Zelador {
    private static Zelador instance;
    private final Stack<DesktopMemento> mementoStack;

    private Zelador() {
        mementoStack = new Stack<>();
    }

    public static Zelador getInstance() {
        if (instance == null) {
            instance = new Zelador();
        }
        return instance;
    }

    public void salvarEstado(DesktopMemento memento) {
        mementoStack.push(memento);
    }

    public DesktopMemento restaurarEstado() {
        if (!mementoStack.isEmpty()) {
            return mementoStack.pop();
        }
        return null;
    }
}
