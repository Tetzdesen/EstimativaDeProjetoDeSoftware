package com.br.estimativadeprojetodesoftware.builder.painel;

import javax.swing.JPanel;

public abstract class PainelBuilder {
    protected JPanel painel;

    public abstract void addPainel();
    public abstract void addLabel();
    public abstract void addButton();
    
    public final JPanel getPainel() {
        return this.painel;
    }
}
