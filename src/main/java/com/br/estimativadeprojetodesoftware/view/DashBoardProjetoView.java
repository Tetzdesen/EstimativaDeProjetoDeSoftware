package com.br.estimativadeprojetodesoftware.view;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;

public class DashBoardProjetoView extends JInternalFrame {
    private JLabel lblTotalProjetosValor;
    private JLabel lblDiasTotaisValor;
    private JLabel lblCustoTotalValor;
    private JPanel painelGraficos;

    public DashBoardProjetoView() {
        setTitle("Dashboard de Projetos");
        setClosable(true);
        setMaximizable(true);
        setIconifiable(false);
        setResizable(true);
        setSize(1000, 700);

        JPanel painelPrincipal = new JPanel(new BorderLayout());
        add(painelPrincipal);

        JPanel painelMetricas = new JPanel(new GridLayout(1, 3, 20, 10));
        painelMetricas.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        painelMetricas.add(criarPainelMetrica("Total de Projetos", "0"));
        painelMetricas.add(criarPainelMetrica("Dias Totais", "0"));
        painelMetricas.add(criarPainelMetrica("Custo Total", "R$ 0,00"));

        painelPrincipal.add(painelMetricas, BorderLayout.NORTH);

        painelGraficos = new JPanel(new GridLayout(1, 2, 20, 10));
        painelGraficos.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        painelPrincipal.add(painelGraficos, BorderLayout.CENTER);
    }

    private JPanel criarPainelMetrica(String titulo, String valor) {
        JPanel painel = new JPanel(new BorderLayout());
        JLabel lblTitulo = new JLabel(titulo, JLabel.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        JLabel lblValor = new JLabel(valor, JLabel.CENTER);
        lblValor.setFont(new Font("Arial", Font.BOLD, 20));

        painel.add(lblTitulo, BorderLayout.NORTH);
        painel.add(lblValor, BorderLayout.CENTER);
        painel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        if (titulo.equals("Total de Projetos")) {
            lblTotalProjetosValor = lblValor;
        } else if (titulo.equals("Dias Totais")) {
            lblDiasTotaisValor = lblValor;
        } else if (titulo.equals("Custo Total")) {
            lblCustoTotalValor = lblValor;
        }

        return painel;
    }

    public void exibirDadosConsolidados(int totalProjetos, int diasTotais, double custoTotal) {
        lblTotalProjetosValor.setText(String.valueOf(totalProjetos));
        lblDiasTotaisValor.setText(String.valueOf(diasTotais));
        lblCustoTotalValor.setText("R$ " + new DecimalFormat("#,##0.00").format(custoTotal));
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
