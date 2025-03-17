package com.br.estimativadeprojetodesoftware.command;

import com.br.estimativadeprojetodesoftware.view.mensagem.MensagemModalView;
import java.awt.Frame;


/**
 *
 * @author tetzner
 */
public class ExibirMensagemCommand implements Command {

    private final Frame frame;
    private final String mensagem;

    public ExibirMensagemCommand(Frame frame, String mensagem) {
        this.frame = frame;
        this.mensagem = mensagem;
    }

    @Override
    public void execute() {
        MensagemModalView view = new MensagemModalView(frame, mensagem, true);  // true para erro
        view.exibirModal();
    }
}