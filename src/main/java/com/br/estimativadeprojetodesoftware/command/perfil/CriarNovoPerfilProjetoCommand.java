package com.br.estimativadeprojetodesoftware.command.perfil;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JDesktopPane;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.br.estimativadeprojetodesoftware.command.ProjetoCommand;
import com.br.estimativadeprojetodesoftware.presenter.helpers.WindowManager;
import com.br.estimativadeprojetodesoftware.presenter.perfil.ManterPerfilPresenter;
import com.br.estimativadeprojetodesoftware.repository.PerfilRepositoryMock;
import com.br.estimativadeprojetodesoftware.view.perfil.ManterPerfilView;

public class CriarNovoPerfilProjetoCommand implements ProjetoCommand {
    private final JDesktopPane desktop;
    private final PerfilRepositoryMock repository;
    private final Map<JLabel, JTextField> campos;

    public CriarNovoPerfilProjetoCommand(JDesktopPane desktop, PerfilRepositoryMock repository) {
        this.desktop = desktop;
        this.repository = repository;
        campos = new HashMap<>();
    }

    @Override
    public void execute() {
        String tituloJanela = "Criar Novo Perfil";
        WindowManager windowManager = WindowManager.getInstance();

        if (windowManager.isFrameAberto(tituloJanela)) {
            windowManager.bringToFront(tituloJanela);
        } else {
            configuraCampos();
            ManterPerfilView manterPerfilView = new ManterPerfilView(desktop, campos);
            new ManterPerfilPresenter(manterPerfilView, repository);
            manterPerfilView.setTitle(tituloJanela);
            desktop.add(manterPerfilView);
            desktop.revalidate();
            desktop.repaint();
            manterPerfilView.setVisible(true);
            try {
                manterPerfilView.setMaximum(true);
            } catch (Exception ignored) {
            }
        }
    }

    private void configuraCampos() {
        var campos = perfilRepositoryMock.getPerfis();
        for ()
    }
}
