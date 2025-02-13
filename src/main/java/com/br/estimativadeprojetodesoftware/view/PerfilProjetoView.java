package com.br.estimativadeprojetodesoftware.view;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.text.DecimalFormat;

/**
 *
 * @author kauac
 */
public class PerfilProjetoView extends JInternalFrame {
    private JLabel lblTotalPerfis;
    private JButton btnNovoPerfil;

    public PerfilProjetoView() {
        setTitle("Detalhes do Projeto");
        setClosable(true);
        setMaximizable(true);
        setIconifiable(false);
        setResizable(true);
        setSize(1000, 700);

        JPanel painelPrincipal = new JPanel(new BorderLayout());
        add(painelPrincipal);

        JPanel painelCabecalho = new JPanel();
        painelCabecalho.setLayout(new GridLayout(1, 2, 10, 15));
        painelCabecalho.setBorder(BorderFactory.createTitledBorder("Informações do Projeto"));

        lblTotalPerfis = new JLabel("Total de Perfis: ");
        btnNovoPerfil = new JButton("Criar Perfil");

        painelCabecalho.add(lblTotalPerfis);
        painelCabecalho.add(btnNovoPerfil);

        painelPrincipal.add(painelCabecalho, BorderLayout.NORTH);
    }

    public void atualizarCabecalho(int quantidade) {
        lblTotalPerfis.setText("Total de Perfis: " + quantidade);
    }
/*
    public void atualizarTabela(Object[][] dados, double valorTotal) {
        modeloTabela.setRowCount(0);
        for (Object[] linha : dados) {
            modeloTabela.addRow(linha);
        }
        DecimalFormat df = new DecimalFormat("R$ #,##0.00");
        lblValorTotal.setText("Valor Total: " + df.format(valorTotal));
    }
    */
}
