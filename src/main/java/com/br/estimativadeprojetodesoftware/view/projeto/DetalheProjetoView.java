package com.br.estimativadeprojetodesoftware.view.projeto;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.text.DecimalFormat;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class DetalheProjetoView extends JInternalFrame {

    private JLabel lblNome, lblCriador, lblData, lblTipoProjeto, lblStatus;
    private JLabel lblImpostos, lblValorComImpostos, lblCalculoLucro, lblMediaPorMes, lblValorTotal;
    private JTable tabelaDetalhes;
    private DefaultTableModel modeloTabela;
    private JButton btnEstimar, btnCancelar, btnCompartilhar, btnExportar;

    public DetalheProjetoView() {
        setTitle("Detalhes do Projeto");
        setClosable(true);
        setMaximizable(true);
        setIconifiable(false);
        setResizable(true);
        setSize(1000, 750);

        JPanel painelPrincipal = new JPanel(new BorderLayout());
        add(painelPrincipal);

        // Painel superior com informações do projeto
        JPanel painelCabecalho = new JPanel(new BorderLayout());
        painelCabecalho.setBorder(BorderFactory.createTitledBorder("Informações do Projeto"));

        JPanel painelInfo = new JPanel(new GridLayout(3, 2, 10, 15));
        lblNome = new JLabel("Nome: ");
        lblCriador = new JLabel("Criador: ");
        lblData = new JLabel("Data de Criação: ");
        lblTipoProjeto = new JLabel("Tipo de Projeto: ");
        lblStatus = new JLabel("Status: ");

        btnExportar = new JButton("Exportar");

        painelInfo.add(lblNome);
        painelInfo.add(lblCriador);
        painelInfo.add(lblData);
        painelInfo.add(lblTipoProjeto);
        painelInfo.add(lblStatus);

        painelCabecalho.add(painelInfo, BorderLayout.CENTER);

        // Painel de botões no canto direito (organizado em duas colunas)
        JPanel painelBotoes = new JPanel(new GridLayout(2, 2, 10, 10));

        btnEstimar = new JButton("Estimar");
        btnCancelar = new JButton("Cancelar Estimativa");
        btnCompartilhar = new JButton("Compartilhar Projeto");
        btnExportar = new JButton("Exportar Projeto");

        painelBotoes.add(btnEstimar);
        painelBotoes.add(btnCompartilhar);
        painelBotoes.add(btnCancelar);
        painelBotoes.add(btnExportar);

        painelCabecalho.add(painelBotoes, BorderLayout.EAST);
        painelPrincipal.add(painelCabecalho, BorderLayout.NORTH);

        // Painel da tabela
        JPanel painelTabela = new JPanel(new BorderLayout());
        painelTabela.setBorder(BorderFactory.createTitledBorder("Funcionalidades do Projeto"));

        modeloTabela = new DefaultTableModel(new Object[]{"Perfil", "Nome", "Dias", "Valor"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2 || column == 3; // Apenas as colunas "Dias" e "Valor" são editáveis
            }
        };
        tabelaDetalhes = new JTable(modeloTabela);
        tabelaDetalhes.setFillsViewportHeight(true);

        JScrollPane scrollTabela = new JScrollPane(tabelaDetalhes);
        painelTabela.add(scrollTabela, BorderLayout.CENTER);
        painelPrincipal.add(painelTabela, BorderLayout.CENTER);

        // Painel para valores finais (todos no mesmo nível)
        JPanel painelValores = new JPanel(new GridLayout(1, 10, 15, 5)); // Alinhamento na mesma linha

        lblImpostos = new JLabel("Impostos: 0%");
        lblImpostos.setFont(new Font("Arial", Font.BOLD, 14));
        painelValores.add(lblImpostos);

        lblValorComImpostos = new JLabel("Valor c/ Impostos: R$ 0,00");
        lblValorComImpostos.setFont(new Font("Arial", Font.BOLD, 14));
        painelValores.add(lblValorComImpostos);

        lblCalculoLucro = new JLabel("Lucro: R$ 0,00");
        lblCalculoLucro.setFont(new Font("Arial", Font.BOLD, 14));
        painelValores.add(lblCalculoLucro);

        lblMediaPorMes = new JLabel("Média por Mês: R$ 0,00");
        lblMediaPorMes.setFont(new Font("Arial", Font.BOLD, 14));
        painelValores.add(lblMediaPorMes);

        lblValorTotal = new JLabel("Valor Total: R$ 0,00");
        lblValorTotal.setFont(new Font("Arial", Font.BOLD, 14));
        painelValores.add(lblValorTotal);

        painelPrincipal.add(painelValores, BorderLayout.SOUTH);
    }

    public void atualizarCabecalho(String nome, String criador, String data, String tipoProjeto, String status) {
        lblNome.setText("Nome: " + nome);
        lblCriador.setText("Criador: " + criador);
        lblData.setText("Data de Criação: " + data);
        lblTipoProjeto.setText("Tipo de Projeto: " + tipoProjeto);
        lblStatus.setText("Status: " + status);
    }

    public void atualizarTabela(Object[][] dados, List<String> perfis, double valorTotal, boolean isEstimado) {
        String[] colunas = new String[perfis.size() + 3];
        colunas[0] = "Funcionalidade";

        for (int i = 0; i < perfis.size(); i++) {
            colunas[i + 1] = perfis.get(i) + " (Dias ou %)";
        }

        colunas[perfis.size() + 1] = "Total";
        colunas[perfis.size() + 2] = "Valor Total (R$)";

        modeloTabela = new DefaultTableModel(dados, colunas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Permitir edição APENAS quando o projeto não está estimado e a coluna pertence a dias/perfis
                return !isEstimado && column > 0 && column <= perfis.size();
            }
        };

        tabelaDetalhes.setModel(modeloTabela);
        tabelaDetalhes.setDefaultRenderer(Object.class, new CustomTableRenderer());

        DecimalFormat df = new DecimalFormat("R$ #,##0.00");
        lblValorTotal.setText("Valor Total: " + df.format(valorTotal));
    }

    public void setImpostos(double valor) {
        lblImpostos.setText(String.format("Impostos: %.2f%%", valor));
    }

    public void setValorComImpostos(double valor) {
        lblValorComImpostos.setText(String.format("Valor c/ Impostos: R$ %.2f", valor));
    }

    public void setCalculoLucro(double valor) {
        lblCalculoLucro.setText(String.format("Lucro: R$ %.2f", valor));
    }

    public void setMediaPorMes(double valor) {
        lblMediaPorMes.setText(String.format("Média por Mês: R$ %.2f", valor));
    }

    public void setValorTotal(double valor) {
        lblValorTotal.setText(String.format("Valor Total: R$ %.2f", valor));
    }

    public JButton getBtnEstimar() {
        return btnEstimar;
    }

    public JButton getBtnCancelar() {
        return btnCancelar;
    }

    public JButton getBtnCompartilhar() {
        return btnCompartilhar;
    }

    public JButton getBtnExportar() {
        return btnExportar;
    }
}
