package com.br.estimativadeprojetodesoftware.service;


import java.util.ArrayList;
import java.util.List;
import com.br.estimativadeprojetodesoftware.command.Command;

public class NoArvoreComposite implements ComponenteNoArvore {
    private final String texto;
    private final String chaveIcone;
    private final Command comando;
    private final List<ComponenteNoArvore> filhos = new ArrayList<>();
    private ProvedorMenuContextual menuContextual;

    public NoArvoreComposite(String texto, String chaveIcone, Command comando) {
        this.texto = texto;
        this.chaveIcone = chaveIcone;
        this.comando = comando;
    }

    @Override
    public String obterTexto() {
        return texto;
    }

    @Override
    public String obterChaveIcone() {
        return chaveIcone;
    }

    @Override
    public Command obterComando() {
        return comando;
    }

    @Override
    public List<ComponenteNoArvore> obterFilhos() {
        return filhos;
    }

    public void adicionarFilho(ComponenteNoArvore filho) {
        filhos.add(filho);
    }

    public ProvedorMenuContextual getMenuContextual() {
        return menuContextual;
    }

    public void setMenuContextual(ProvedorMenuContextual menuContextual) {
        this.menuContextual = menuContextual;
    }

    @Override
    public String toString() {
        return obterTexto();
    }
}
