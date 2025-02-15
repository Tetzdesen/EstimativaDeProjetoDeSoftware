package com.br.estimativadeprojetodesoftware.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.DecimalFormat;

public class CompartilharProjetoView extends JInternalFrame {
    private JLabel lblNome, lblEmail;
    private JTable tabelaDetalhes;
    private DefaultTableModel modeloTabela;

    public CompartilharProjetoView() {
        setTitle("Detalhes do Projeto");
        setClosable(true);
        setMaximizable(true);
        setIconifiable(false);
        setResizable(true);
        setSize(1000, 700);

        JPanel painelPrincipal = new JPanel(new BorderLayout());
        add(painelPrincipal);

        JPanel painelCabecalho = new JPanel();
        painelCabecalho.setLayout(new GridLayout(3, 2, 10, 15));
        painelCabecalho.setBorder(BorderFactory.createTitledBorder("Informações do Projeto"));

        lblNome = new JLabel("Nome: ");
        lblEmail = new JLabel("E-mail: ");

        painelCabecalho.add(lblNome);
        painelCabecalho.add(lblEmail);

        painelPrincipal.add(painelCabecalho, BorderLayout.NORTH);

        JPanel painelTabela = new JPanel(new BorderLayout());
        painelTabela.setBorder(BorderFactory.createTitledBorder("Usuários cadastrados"));

        modeloTabela = new DefaultTableModel(new Object[]{"Nome", "E-mail"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelaDetalhes = new JTable(modeloTabela);
        tabelaDetalhes.setFillsViewportHeight(true);

        JScrollPane scrollTabela = new JScrollPane(tabelaDetalhes);
        painelTabela.add(scrollTabela, BorderLayout.CENTER);
        painelPrincipal.add(painelTabela, BorderLayout.CENTER);
    }

    public void atualizarCabecalho(String nome, String email) {
        lblNome.setText("Nome: " + nome);
        lblEmail.setText("E-mail: " + email);
    }

    public void atualizarTabela(Object[][] dados) {
        modeloTabela.setRowCount(0);
        for (Object[]linha : dados) {
            modeloTabela.addRow(linha);
        }
    }
}
