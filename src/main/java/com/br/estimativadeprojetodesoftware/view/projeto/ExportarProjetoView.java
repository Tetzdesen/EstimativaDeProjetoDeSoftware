package com.br.estimativadeprojetodesoftware.view.projeto;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.*;

import com.br.estimativadeprojetodesoftware.service.IconService;

public class ExportarProjetoView extends JDialog {
    private final JButton btnExportarPdf;
    private final JButton btnExportarCsv;
    private final JLabel lblPrincipal;

    public ExportarProjetoView() {
        setModal(true);

        JPanel painelPrincipal = new JPanel();
        painelPrincipal.setLayout(new BoxLayout(painelPrincipal, BoxLayout.Y_AXIS));
        add(painelPrincipal);

        painelPrincipal.add(Box.createVerticalGlue());

        lblPrincipal = new JLabel("Selecione o formato de exportação:");
        lblPrincipal.setFont(lblPrincipal.getFont().deriveFont(20.0f));
        lblPrincipal.setAlignmentX(CENTER_ALIGNMENT);
        painelPrincipal.add(lblPrincipal);

        painelPrincipal.add(Box.createRigidArea(new Dimension(0, 60)));

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));

        btnExportarPdf = new JButton();
        btnExportarPdf.setIcon(IconService.getIcon("exportar-pdf"));
        btnExportarPdf.setPreferredSize(new Dimension(120, 100));

        btnExportarCsv = new JButton();
        btnExportarCsv.setPreferredSize(new Dimension(120, 100));
        btnExportarCsv.setIcon(IconService.getIcon("exportar-csv"));

        painelBotoes.add(btnExportarPdf);
        painelBotoes.add(btnExportarCsv);
        painelBotoes.setAlignmentX(CENTER_ALIGNMENT);
        painelPrincipal.add(painelBotoes);

        painelPrincipal.add(Box.createVerticalGlue());
    }

    public JButton getBtnExportarPdf() {
        return btnExportarPdf;
    }

    public JButton getBtnExportarCsv() {
        return btnExportarCsv;
    }
}
