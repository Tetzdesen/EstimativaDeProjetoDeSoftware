package com.br.estimativadeprojetodesoftware.composite;

import java.util.ArrayList;
import java.util.List;

public class Painel extends Componente {
    private List<Componente> itens;

    public Painel() {
        itens = new ArrayList<>();
    }

    public void adicionar(Componente item) {
        itens.add(item);
    }

    public void remover(Componente item) {
        itens.remove(item);
    }
}
