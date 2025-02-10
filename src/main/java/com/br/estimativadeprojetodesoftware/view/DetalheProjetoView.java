package com.br.estimativadeprojetodesoftware.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.DecimalFormat;

public class DetalheProjetoView extends JInternalFrame {
    private JLabel lblNome, lblCriador, lblData, lblTipoProjeto, lblStatus, lblValorTotal;
    private JTable tabelaDetalhes;
    private DefaultTableModel modeloTabela;

    public DetalheProjetoView() {
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
        lblCriador = new JLabel("Criador: ");
        lblData = new JLabel("Data de Criação: ");
        lblTipoProjeto = new JLabel("Tipo de Projeto: ");
        lblStatus = new JLabel("Status: ");

        painelCabecalho.add(lblNome);
        painelCabecalho.add(lblCriador);
        painelCabecalho.add(lblData);
        painelCabecalho.add(lblTipoProjeto);
        painelCabecalho.add(lblStatus);

        painelPrincipal.add(painelCabecalho, BorderLayout.NORTH);

        JPanel painelTabela = new JPanel(new BorderLayout());
        painelTabela.setBorder(BorderFactory.createTitledBorder("Funcionalidades do Projeto"));

        modeloTabela = new DefaultTableModel(new Object[]{"Nome", "Dias", "Valor"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelaDetalhes = new JTable(modeloTabela);
        tabelaDetalhes.setFillsViewportHeight(true);

        JScrollPane scrollTabela = new JScrollPane(tabelaDetalhes);
        painelTabela.add(scrollTabela, BorderLayout.CENTER);

        JPanel painelValorTotal = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        lblValorTotal = new JLabel("Valor Total: R$ 0,00");
        lblValorTotal.setFont(new Font("Arial", Font.BOLD, 14));
        painelValorTotal.add(lblValorTotal);

        painelTabela.add(painelValorTotal, BorderLayout.SOUTH);
        painelPrincipal.add(painelTabela, BorderLayout.CENTER);
    }

    public void atualizarCabecalho(String nome, String criador, String data, String tipoProjeto, String status) {
        lblNome.setText("Nome: " + nome);
        lblCriador.setText("Criador: " + criador);
        lblData.setText("Data de Criação: " + data);
        lblTipoProjeto.setText("Tipo de Projeto: " + tipoProjeto);
        lblStatus.setText("Status: " + status);
    }

    public void atualizarTabela(Object[][] dados, double valorTotal) {
        modeloTabela.setRowCount(0);
        for (Object[] linha : dados) {
            modeloTabela.addRow(linha);
        }
        DecimalFormat df = new DecimalFormat("R$ #,##0.00");
        lblValorTotal.setText("Valor Total: " + df.format(valorTotal));
    }
}
