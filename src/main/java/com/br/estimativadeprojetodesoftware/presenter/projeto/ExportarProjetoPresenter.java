package com.br.estimativadeprojetodesoftware.presenter.projeto;

import javax.swing.JOptionPane;

import com.br.estimativadeprojetodesoftware.command.projeto.MostrarMensagemProjetoCommand;
import com.br.estimativadeprojetodesoftware.model.RegistroOperacao;
import com.br.estimativadeprojetodesoftware.service.LogService;
import com.br.estimativadeprojetodesoftware.singleton.UsuarioLogadoSingleton;
import com.br.estimativadeprojetodesoftware.templatemethod.ExportarCSVTemplate;
import com.br.estimativadeprojetodesoftware.templatemethod.ExportarPDFTemplate;
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
                exportarPdf();
            } catch (Exception ex) {
                 RegistroOperacao registro = new RegistroOperacao("Exportação de projeto em PDF", UsuarioLogadoSingleton.getInstancia().getUsuario().getNome(), ex.getMessage());
                LogService.getInstancia().escreverMensagem(registro.formatarLog());
                new MostrarMensagemProjetoCommand(ex.getMessage()).execute();
            }
        });

        view.getBtnExportarCsv().addActionListener(e -> {
            try {
                exportarCsv();
            } catch (Exception ex) {
                RegistroOperacao registro = new RegistroOperacao("Exportação de projeto em CSV", UsuarioLogadoSingleton.getInstancia().getUsuario().getNome(), ex.getMessage());
                LogService.getInstancia().escreverMensagem(registro.formatarLog());
                new MostrarMensagemProjetoCommand(ex.getMessage()).execute();
            }
        });
    }

    private void exportarPdf() {
        new ExportarPDFTemplate().exportar(projetoNome);
        JOptionPane.showMessageDialog(null, "Arquivo gerado com sucesso!");
        RegistroOperacao registro = new RegistroOperacao("Exportação de projeto em PDF", UsuarioLogadoSingleton.getInstancia().getUsuario().getNome());
        LogService.getInstancia().escreverMensagem(registro.formatarLog());
        view.dispose();
    }

    private void exportarCsv() {
        new ExportarCSVTemplate().exportar(projetoNome);
        JOptionPane.showMessageDialog(null, "Arquivo gerado com sucesso!");
        RegistroOperacao registro = new RegistroOperacao("Exportação de projeto em CSV", UsuarioLogadoSingleton.getInstancia().getUsuario().getNome());
        LogService.getInstancia().escreverMensagem(registro.formatarLog());
        view.dispose();
    }
}
