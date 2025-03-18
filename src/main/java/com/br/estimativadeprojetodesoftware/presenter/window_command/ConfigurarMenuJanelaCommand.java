package com.br.estimativadeprojetodesoftware.presenter.window_command;

import com.br.estimativadeprojetodesoftware.command.Command;
import com.br.estimativadeprojetodesoftware.model.Usuario;
import com.br.estimativadeprojetodesoftware.presenter.projeto.PrincipalProjetoPresenter;
import com.br.estimativadeprojetodesoftware.service.LogService;
import com.br.estimativadeprojetodesoftware.singleton.UsuarioLogadoSingleton;
import com.br.log.CSVLog;
import com.br.log.JSONLog;
import javax.swing.*;

public class ConfigurarMenuJanelaCommand implements Command {

    private final PrincipalProjetoPresenter presenter;
    private String logFormatoAtual = "JSON";

    public ConfigurarMenuJanelaCommand(PrincipalProjetoPresenter presenter) {
        this.presenter = presenter;
        this.logFormatoAtual = carregarFormatoLog();
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

        JMenu menuLog = new JMenu("Log");

        JRadioButtonMenuItem jsonOption = new JRadioButtonMenuItem("JSON");
        JRadioButtonMenuItem csvOption = new JRadioButtonMenuItem("CSV");

        ButtonGroup logGroup = new ButtonGroup();
        logGroup.add(jsonOption);
        logGroup.add(csvOption);

        if ("JSON".equals(logFormatoAtual)) {
            jsonOption.setSelected(true);
            LogService.getInstancia().setLog(new JSONLog());
        } else {
            csvOption.setSelected(true);
            LogService.getInstancia().setLog(new CSVLog());
        }

        jsonOption.addActionListener(e -> {
            LogService.getInstancia().setLog(new JSONLog());
            salvarFormatoLog("JSON");
        });

        csvOption.addActionListener(e -> {
            LogService.getInstancia().setLog(new CSVLog());
            salvarFormatoLog("CSV");
        });

        menuLog.add(jsonOption);
        menuLog.add(csvOption);
        menuBar.add(menuJanela);
        menuBar.add(menuLog);

        presenter.getView().setJMenuBar(menuBar);
    }

    private String carregarFormatoLog() {
        return UsuarioLogadoSingleton.getInstancia().getUsuario().getLog();
    }

    private void salvarFormatoLog(String formato) {
        Usuario usuario = UsuarioLogadoSingleton.getInstancia().getUsuario();
        usuario.setLog(formato);
        presenter.getUsuarioService().atualizar(usuario);
    }
}
