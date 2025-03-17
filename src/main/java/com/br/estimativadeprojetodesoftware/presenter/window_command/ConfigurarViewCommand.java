package com.br.estimativadeprojetodesoftware.presenter.window_command;

import com.br.estimativadeprojetodesoftware.command.projeto.MostrarMensagemProjetoCommand;
import com.br.estimativadeprojetodesoftware.presenter.projeto.PrincipalProjetoPresenter;
import com.br.estimativadeprojetodesoftware.service.BarraService;

import javax.swing.*;
import com.br.estimativadeprojetodesoftware.command.Command;

public class ConfigurarViewCommand implements Command {

    private final PrincipalProjetoPresenter presenter;

    public ConfigurarViewCommand(PrincipalProjetoPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void execute() {
        BarraService barraService = new BarraService(presenter.getComandos());
        JToolBar barraDeBotoes = barraService.criarBarraPrincipal();
        presenter.getView().setMainComponents(barraDeBotoes);
        presenter.setCriarBarraService(barraService);
        SwingUtilities.invokeLater(() -> {
            Command comandoPrincipal = presenter.getComandos().get("Principal");
            if (comandoPrincipal != null) {
                comandoPrincipal.execute();
            } else {
                new MostrarMensagemProjetoCommand("Comando 'Principal' não encontrado.").execute();
            }
        });
    }
}
