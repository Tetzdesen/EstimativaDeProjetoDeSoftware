package com.br.estimativadeprojetodesoftware.view.projeto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class CompartilharProjetoView extends JDialog {

    private JButton btnCompartilharProjeto;
    private JTable tabelaUsuarios;
    private DefaultTableModel modeloTabela;

    public CompartilharProjetoView() {
        JPanel painelPrincipal = new JPanel(new BorderLayout());
        add(painelPrincipal);

        JPanel painelNorte = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnCompartilharProjeto = new JButton("Compartilhar Projeto");
        btnCompartilharProjeto.setSize(120, 20);
        painelNorte.add(btnCompartilharProjeto);

        painelPrincipal.add(painelNorte, BorderLayout.NORTH);

        JPanel painelTabela = new JPanel(new BorderLayout());
        painelTabela.setBorder(BorderFactory.createTitledBorder("Usuários Cadastrados"));

        modeloTabela = new DefaultTableModel(new Object[]{"Nome", "E-mail"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelaUsuarios = new JTable(modeloTabela);
        tabelaUsuarios.setFillsViewportHeight(true);

        JScrollPane scrollTabela = new JScrollPane(tabelaUsuarios);
        painelTabela.add(scrollTabela, BorderLayout.CENTER);
        painelPrincipal.add(painelTabela, BorderLayout.CENTER);
    }

    public JTable getTabelaUsuarios() {
        return tabelaUsuarios;
    }

    public DefaultTableModel getModeloTabela() {
        return modeloTabela;
    }


    public JButton getBtnCompartilharProjeto() {
        return btnCompartilharProjeto;
    }

    public void atualizarTabela(Object[][] dados) {
        for (Object[] linha : dados) {
            modeloTabela.addRow(linha);
        }
    }

}
