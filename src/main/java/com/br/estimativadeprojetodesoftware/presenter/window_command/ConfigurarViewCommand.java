package com.br.estimativadeprojetodesoftware.presenter.window_command;

import com.br.estimativadeprojetodesoftware.command.MostrarMensagemProjetoCommand;
import com.br.estimativadeprojetodesoftware.command.ProjetoCommand;
import com.br.estimativadeprojetodesoftware.presenter.PrincipalPresenter;
import com.br.estimativadeprojetodesoftware.service.CriarBarraBotaoService;

import javax.swing.*;

public class ConfigurarViewCommand implements WindowCommand {
    private final PrincipalPresenter presenter;

    public ConfigurarViewCommand(PrincipalPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void execute() {
        presenter.configurarArvore();
        JToolBar barraDeBotoes = new CriarBarraBotaoService(presenter.getComandos()).criarBarraDeBotoes();
        presenter.getView().setMainComponents(barraDeBotoes);
        SwingUtilities.invokeLater(() -> {
         //   ProjetoCommand comandoPrincipal = presenter.getComandos().get("Principal");
            ProjetoCommand comandoPrincipal = presenter.getComandos().get("Login");
            if (comandoPrincipal != null) {
                comandoPrincipal.execute();
            } else {
                new MostrarMensagemProjetoCommand("Comando 'Principal' não encontrado.").execute();
            }
        });
    }
}
