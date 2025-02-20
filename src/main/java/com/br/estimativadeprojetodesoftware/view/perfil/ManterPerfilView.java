package com.br.estimativadeprojetodesoftware.view.perfil;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ManterPerfilView extends JDialog {
    private JDesktopPane desktop;
    private JTable tabelaDetalhes;
    private DefaultTableModel modeloTabela;

    public ManterPerfilView(JDesktopPane desktop) {
        setTitle("Manter Perfis");

        setTitle("Manter Perfis");
        setResizable(true);

        this.desktop = desktop;

        JPanel painelPrincipal = new JPanel(new BorderLayout());
        add(painelPrincipal);

        JPanel painelTabela = new JPanel(new BorderLayout());

        modeloTabela = new DefaultTableModel(new Object[]{"Funcionalidade", "Número de Dias"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return true;
            }
        };
        tabelaDetalhes = new JTable(modeloTabela);
        tabelaDetalhes.setFillsViewportHeight(true);

        JScrollPane scrollTabela = new JScrollPane(tabelaDetalhes);
        painelTabela.add(scrollTabela, BorderLayout.CENTER);

        painelPrincipal.add(painelTabela, BorderLayout.CENTER);
    }

    public JDesktopPane getDesktop() {
        return desktop;
    }

    public void atualizarTabela(Object[][] dados) {
        for (Object[] linha : dados) {
            modeloTabela.addRow(linha);
        }
    }
}
