package com.br.estimativadeprojetodesoftware.presenter.window_command;

import com.br.estimativadeprojetodesoftware.command.ProjetoCommand;
import java.awt.Frame;

/**
 *
 * @author tetzner
 */
public class FecharJanelaCommand implements ProjetoCommand {

    private final Frame frame;
    
    public FecharJanelaCommand(Frame frame) {
        this.frame = frame;
    }
    
    @Override
    public void execute() {
        frame.dispose();
    }
    
}
