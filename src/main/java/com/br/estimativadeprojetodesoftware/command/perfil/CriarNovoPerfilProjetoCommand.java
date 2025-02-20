package com.br.estimativadeprojetodesoftware.command.perfil;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.br.estimativadeprojetodesoftware.command.ProjetoCommand;
import com.br.estimativadeprojetodesoftware.presenter.helpers.WindowManager;
import com.br.estimativadeprojetodesoftware.presenter.perfil.ManterPerfilPresenter;
import com.br.estimativadeprojetodesoftware.presenter.perfil.PerfilProjetoPresenter;
import com.br.estimativadeprojetodesoftware.repository.PerfilRepositoryMock;
import com.br.estimativadeprojetodesoftware.view.perfil.ManterPerfilView;

public class CriarNovoPerfilProjetoCommand implements ProjetoCommand {
    private final JDesktopPane desktop;
    private final PerfilRepositoryMock repository;

    public CriarNovoPerfilProjetoCommand(JDesktopPane desktop, PerfilRepositoryMock repository) {
        this.desktop = desktop;
        this.repository = repository;
    }

    @Override
    public void execute() {
        String tituloJanela = "Criar Novo Perfil";
        WindowManager windowManager = WindowManager.getInstance();

        if (windowManager.isFrameAberto(tituloJanela)) {
            windowManager.bringToFront(tituloJanela);

            try {
                for (JInternalFrame frame : desktop.getAllFrames()) {
                    if (frame.getTitle().equals(tituloJanela) && frame.isMaximum()) {

                        frame.setMaximum(false);
                        frame.setLocation((desktop.getWidth() - frame.getWidth()) / 2,
                                (desktop.getHeight() - frame.getHeight()) / 2);
                    }
                }
            } catch (Exception ignored) {
            }

        } else {
            ManterPerfilView manterPerfilView = new ManterPerfilView(desktop);
            new ManterPerfilPresenter(manterPerfilView, repository);
            manterPerfilView.setTitle(tituloJanela);

            manterPerfilView.setSize(700, 700);
            int x = (desktop.getWidth() - manterPerfilView.getWidth()) / 2;
            int y = (desktop.getHeight() - manterPerfilView.getHeight()) / 2;
            manterPerfilView.setLocation(x, y);

            //desktop.add(manterPerfilView);
            manterPerfilView.setModal(true);
            manterPerfilView.setVisible(true);
            
            try {
                //manterPerfilView.setSelected(true); // Traz o frame para frente e o seleciona
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                //if (manterPerfilView.isMaximum()) {
                  //  manterPerfilView.setMaximum(false);
                //}
            } catch (Exception ignored) {
            }
        }
    }

    private void configuraCampos() {
        var campos = repository.getPerfis();
    }
}
