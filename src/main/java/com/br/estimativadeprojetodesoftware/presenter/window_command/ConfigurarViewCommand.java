package com.br.estimativadeprojetodesoftware.presenter.window_command;

import com.br.estimativadeprojetodesoftware.command.MostrarMensagemProjetoCommand;
import com.br.estimativadeprojetodesoftware.command.ProjetoCommand;
import com.br.estimativadeprojetodesoftware.presenter.projeto.PrincipalPresenter;
import com.br.estimativadeprojetodesoftware.service.BarraService;

import javax.swing.*;

public class ConfigurarViewCommand implements WindowCommand {

    private final PrincipalPresenter presenter;

    public ConfigurarViewCommand(PrincipalPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void execute() {
          presenter.configurarArvore();
          BarraService barraService = new BarraService(presenter.getComandos());
          JToolBar barraDeBotoes = barraService.criarBarraPrincipal();
          presenter.getView().setMainComponents(barraDeBotoes);
          presenter.setCriarBarraService(barraService);
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
