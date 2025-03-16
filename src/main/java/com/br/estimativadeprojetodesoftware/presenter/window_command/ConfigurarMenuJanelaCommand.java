package com.br.estimativadeprojetodesoftware.presenter.window_command;


import com.br.estimativadeprojetodesoftware.presenter.projeto.PrincipalProjetoPresenter;

import javax.swing.*;

public class ConfigurarMenuJanelaCommand implements WindowCommand {
    private final PrincipalProjetoPresenter presenter;

    public ConfigurarMenuJanelaCommand(PrincipalProjetoPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void execute() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menuJanela = new JMenu("Janela");

        JMenuItem itemLadoALado = new JMenuItem("Lado a Lado");
        itemLadoALado.addActionListener(e -> new OrganizarLadoALadoCommand(presenter
                .getView()
                .getDesktop()).execute());

        JMenuItem itemRestaurar = new JMenuItem("Restaurar Janelas");
        itemRestaurar.addActionListener(e -> presenter.restaurarJanelas());

        menuJanela.add(itemLadoALado);
        menuJanela.add(itemRestaurar);
        
        menuBar.add(menuJanela);

        presenter.getView().setJMenuBar(menuBar);
    }
}
