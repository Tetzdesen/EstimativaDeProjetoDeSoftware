package com.br.estimativadeprojetodesoftware.command.projeto;

import javax.swing.*;
import com.br.estimativadeprojetodesoftware.command.Command;

public class MostrarMensagemProjetoCommand implements Command {
    private final String mensagem;

    public MostrarMensagemProjetoCommand(String mensagem) {
        this.mensagem = mensagem;
    }

    @Override
    public void execute() {
        JOptionPane.showMessageDialog(null, mensagem);
    }
}
