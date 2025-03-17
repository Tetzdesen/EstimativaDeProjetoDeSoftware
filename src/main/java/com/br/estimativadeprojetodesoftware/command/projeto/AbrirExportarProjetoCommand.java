package com.br.estimativadeprojetodesoftware.command.projeto;

import com.br.estimativadeprojetodesoftware.presenter.helpers.WindowManager;
import com.br.estimativadeprojetodesoftware.presenter.projeto.ExportarProjetoPresenter;
import com.br.estimativadeprojetodesoftware.view.projeto.ExportarProjetoView;
import com.br.estimativadeprojetodesoftware.command.Command;

public class AbrirExportarProjetoCommand implements Command {
    private final String projetoNome;

    public AbrirExportarProjetoCommand(String projetoNome) {
        this.projetoNome = projetoNome;
    }

    @Override
    public void execute() {
        String tituloJanela = "Exportar Projeto";
        
        WindowManager windowManager = WindowManager.getInstance();

        if (windowManager.isFrameAberto(tituloJanela)) {
            windowManager.bringToFront(tituloJanela);
        } else {
            ExportarProjetoView view = new ExportarProjetoView();
            new ExportarProjetoPresenter(view, projetoNome);
            view.setTitle(tituloJanela);

            windowManager.registrarDialog(tituloJanela, view);

            view.setSize(500, 300);
            view.setVisible(true);

            view.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent e) {
                    windowManager.fechar(tituloJanela);
                }
            });
            
        }
    }
    
}
