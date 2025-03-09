package com.br.estimativadeprojetodesoftware.builder.painel;

import java.awt.FlowLayout;

import javax.swing.JPanel;

public class TamanhoAppBuilder extends PainelBuilder {

    public void addPainel() {
        JPanel painelPequeno = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 15));
        painel.add(painelPequeno);
        painel.add(new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 15)));
        painel.add(new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 15)));
    }

    public void addLabel() {
        
    }

    public void addButton() {

    }
}
