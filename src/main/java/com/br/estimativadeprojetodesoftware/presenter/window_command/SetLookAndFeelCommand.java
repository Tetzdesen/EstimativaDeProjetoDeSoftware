package com.br.estimativadeprojetodesoftware.presenter.window_command;

import com.br.estimativadeprojetodesoftware.command.Command;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;

public class SetLookAndFeelCommand implements Command {

    @Override
    public void execute() {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            System.err.println("Erro ao definir o FlatLaf. Usando Look and Feel padrão.");
        }
    }
}
