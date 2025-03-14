package com.br.estimativadeprojetodesoftware.presenter.window_command;

import com.br.estimativadeprojetodesoftware.command.projeto.MostrarMensagemProjetoCommand;
import com.br.estimativadeprojetodesoftware.command.ProjetoCommand;
import com.br.estimativadeprojetodesoftware.command.projeto.ConfigurarArvoreProjetoCommand;
import com.br.estimativadeprojetodesoftware.presenter.projeto.PrincipalProjetoPresenter;
import com.br.estimativadeprojetodesoftware.service.BarraService;
import com.br.estimativadeprojetodesoftware.service.ConstrutorDeArvoreNavegacaoService;

import javax.swing.*;

public class ConfigurarViewCommand implements WindowCommand {

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
            ProjetoCommand comandoPrincipal = presenter.getComandos().get("Principal");
            if (comandoPrincipal != null) {
                comandoPrincipal.execute();
            } else {
                new MostrarMensagemProjetoCommand("Comando 'Principal' não encontrado.").execute();
            }
        });
    }
}
