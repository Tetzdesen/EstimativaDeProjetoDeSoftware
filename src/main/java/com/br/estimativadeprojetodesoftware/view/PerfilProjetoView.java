package com.br.estimativadeprojetodesoftware.view;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;

/**
 *
 * @author kauac
 */
public class PerfilProjetoView extends JInternalFrame {
    private JLabel lblTotalPerfis;
    private JButton btnCriarPerfil;
    private JPanel painelGraficos;

    public PerfilProjetoView() {
        setTitle("Ver Perfis de Projeto");
        setClosable(true);
        setMaximizable(true);
        setIconifiable(false);
        setResizable(true);
        setSize(1000, 700);

        JPanel painelPrincipal = new JPanel(new BorderLayout());
        add(painelPrincipal);

        JPanel painelCabecalho = new JPanel(new BorderLayout());
        add(painelCabecalho);

        painelCabecalho.setLayout(new GridLayout(3, 2, 10, 15));
        //painelCabecalho.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));


        lblTotalPerfis = new JLabel("Total de Perfis: ");
        btnCriarPerfil = new JButton("Criar Perfil");

        painelCabecalho.add(lblTotalPerfis);
        painelCabecalho.add(btnCriarPerfil);

        painelPrincipal.add(painelCabecalho, BorderLayout.NORTH);

        
    }

    

    public void exibirDadosConsolidados() {
        
    }

    public void atualizarGraficos(DefaultPieDataset datasetCustos, DefaultPieDataset datasetProjetos) {
        painelGraficos.removeAll();

        JFreeChart graficoCustos = ChartFactory.createPieChart("Distribuição de Custos", datasetCustos, true, true, false);
        JFreeChart graficoProjetos = ChartFactory.createPieChart("Tipos de Projetos", datasetProjetos, true, true, false);

        painelGraficos.add(new ChartPanel(graficoCustos));
        painelGraficos.add(new ChartPanel(graficoProjetos));

        painelGraficos.revalidate();
        painelGraficos.repaint();
    }
}
