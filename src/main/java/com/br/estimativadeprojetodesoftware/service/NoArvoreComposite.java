package com.br.estimativadeprojetodesoftware.service;

import com.br.estimativadeprojetodesoftware.command.ProjetoCommand;

import java.util.ArrayList;
import java.util.List;

public class NoArvoreComposite implements ComponenteNoArvore {
    private final String texto;
    private final String chaveIcone;
    private final ProjetoCommand comando;
    private final List<ComponenteNoArvore> filhos = new ArrayList<>();
    private ProvedorMenuContextual menuContextual;

    public NoArvoreComposite(String texto, String chaveIcone, ProjetoCommand comando) {
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
    public ProjetoCommand obterComando() {
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
