package com.br.estimativadeprojetodesoftware.presenter.projeto;

import com.br.estimativadeprojetodesoftware.command.projeto.MostrarMensagemProjetoCommand;
import com.br.estimativadeprojetodesoftware.templatemethod.ExportarCsvTemplate;
import com.br.estimativadeprojetodesoftware.templatemethod.ExportarPdfTemplate;
import com.br.estimativadeprojetodesoftware.view.projeto.ExportarProjetoView;

public class ExportarProjetoPresenter {
    private final ExportarProjetoView view;
    private final String projetoNome;

    public ExportarProjetoPresenter(ExportarProjetoView view, String projetoNome) {
        this.view = view;
        this.projetoNome = projetoNome;

        configuraListeners();
    }

    private void configuraListeners() {
        view.getBtnExportarPdf().addActionListener(e -> {
            try {
                new ExportarPdfTemplate().exportar(projetoNome);
            } catch (Exception ex) {
                new MostrarMensagemProjetoCommand(ex.getMessage()).execute();
            }
        });

        view.getBtnExportarCsv().addActionListener(e -> {
            try {
                new ExportarCsvTemplate().exportar(projetoNome);
            } catch (Exception ex) {
                new MostrarMensagemProjetoCommand(ex.getMessage()).execute();
            }
        });
        //view.getBtnExportarCsv().addActionListener(e -> exportarCsv());
    }
}
