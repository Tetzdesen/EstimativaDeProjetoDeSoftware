package com.br.estimativadeprojetodesoftware.presenter.window_command;

import java.awt.Frame;
import com.br.estimativadeprojetodesoftware.command.Command;

/**
 *
 * @author tetzner
 */
public class FecharJanelaCommand implements Command {

    private final Frame frame;
    
    public FecharJanelaCommand(Frame frame) {
        this.frame = frame;
    }
    
    @Override
    public void execute() {
        frame.dispose();
    }
    
}
