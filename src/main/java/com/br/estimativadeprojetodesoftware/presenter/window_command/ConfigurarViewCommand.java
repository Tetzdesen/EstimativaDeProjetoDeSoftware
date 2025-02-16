package com.br.estimativadeprojetodesoftware.presenter.window_command;

import com.br.estimativadeprojetodesoftware.command.MostrarMensagemProjetoCommand;
import com.br.estimativadeprojetodesoftware.command.ProjetoCommand;
import com.br.estimativadeprojetodesoftware.presenter.PrincipalPresenter;
import com.br.estimativadeprojetodesoftware.service.CriarBarraService;

import javax.swing.*;

public class ConfigurarViewCommand implements WindowCommand {

    private final PrincipalPresenter presenter;

    public ConfigurarViewCommand(PrincipalPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void execute() {
          presenter.configurarArvore();
          JToolBar barraDeBotoes = new CriarBarraService(presenter.getComandos()).criarBarra();
          presenter.getView().setMainComponents(barraDeBotoes);
          SwingUtilities.invokeLater(() -> {
            ProjetoCommand comandoPrincipal = presenter.getComandos().get("Principal");
            if (comandoPrincipal != null) {
                comandoPrincipal.execute();
            } else {
                new MostrarMensagemProjetoCommand("Comando 'Principal' não encontrado.").execute();
            }
        });
    }
}
