package com.br.estimativadeprojetodesoftware.presenter.window_command;

import com.br.estimativadeprojetodesoftware.command.Command;
import com.br.estimativadeprojetodesoftware.presenter.projeto.PrincipalProjetoPresenter;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/** Monta a barra de menus da janela principal. */
public class ConfigurarMenuJanelaCommand implements Command {

    private final PrincipalProjetoPresenter presenter;

    public ConfigurarMenuJanelaCommand(PrincipalProjetoPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void execute() {
        JMenuBar menuBar = new JMenuBar();

        JMenu menuSistema = new JMenu("Sistema");
        menuSistema.add(item("Dashboard", "Dashboard"));
        menuSistema.add(item("Projetos", "Projetos"));
        menuSistema.add(item("Novo projeto", "Novo projeto"));
        menuSistema.add(item("Perfis de estimativa", "Perfis"));
        menuSistema.addSeparator();
        menuSistema.add(item("Configuração", "Configuração"));
        menuSistema.add(item("Usuário", "Usuário"));
        menuSistema.addSeparator();
        menuSistema.add(item("Sair (logout)", "Logout"));

        JMenu menuJanela = new JMenu("Janela");
        JMenuItem itemLadoALado = new JMenuItem("Lado a Lado");
        itemLadoALado.addActionListener(e -> new OrganizarLadoALadoCommand(presenter.getView().getDesktop()).execute());
        JMenuItem itemRestaurar = new JMenuItem("Restaurar Janelas");
        itemRestaurar.addActionListener(e -> presenter.restaurarJanelas());
        menuJanela.add(itemLadoALado);
        menuJanela.add(itemRestaurar);

        menuBar.add(menuSistema);
        menuBar.add(menuJanela);
        presenter.getView().setJMenuBar(menuBar);
    }

    private JMenuItem item(String texto, String chaveComando) {
        JMenuItem item = new JMenuItem(texto);
        item.addActionListener(e -> presenter.executarComando(chaveComando));
        return item;
    }
}
