package com.br.estimativadeprojetodesoftware.view.projeto;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.*;

import com.br.estimativadeprojetodesoftware.service.IconService;

public class ExportarProjetoView extends JDialog {
    private JButton btnExportarPdf, btnExportarCsv;
    private JLabel lblPrincipal;

    public ExportarProjetoView() {
        setModal(true);

        // Painel principal com BoxLayout (vertical)
        JPanel painelPrincipal = new JPanel();
        painelPrincipal.setLayout(new BoxLayout(painelPrincipal, BoxLayout.Y_AXIS));
        add(painelPrincipal);

        // Espaço para empurrar o conteúdo para o centro verticalmente
        painelPrincipal.add(Box.createVerticalGlue());

        // Label centralizado
        lblPrincipal = new JLabel("Selecione o formato de exportação:");
        lblPrincipal.setFont(lblPrincipal.getFont().deriveFont(20.0f));
        lblPrincipal.setAlignmentX(CENTER_ALIGNMENT);
        painelPrincipal.add(lblPrincipal);

        // Espaço vertical entre o label e os botões
        painelPrincipal.add(Box.createRigidArea(new Dimension(0, 60)));

        // Painel para os botões, com FlowLayout centralizado
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));

        btnExportarPdf = new JButton();
        btnExportarPdf.setIcon(IconService.getIcon("exportar-pdf"));
        btnExportarPdf.setPreferredSize(new Dimension(120, 100));

        btnExportarCsv = new JButton();
        btnExportarCsv.setPreferredSize(new Dimension(120, 100));
        btnExportarCsv.setIcon(IconService.getIcon("exportar-csv"));

        painelBotoes.add(btnExportarPdf);
        painelBotoes.add(btnExportarCsv);
        // Alinha o painel de botões no centro horizontalmente
        painelBotoes.setAlignmentX(CENTER_ALIGNMENT);
        painelPrincipal.add(painelBotoes);

        // Espaço para empurrar o conteúdo para o centro verticalmente
        painelPrincipal.add(Box.createVerticalGlue());
    }

    public JButton getBtnExportarPdf() {
        return btnExportarPdf;
    }

    public JButton getBtnExportarCsv() {
        return btnExportarCsv;
    }
}
