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
    private final JLabel lblValorTotal;
    private final JTable tabelaDetalhes;
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

        JPanel painelValores = new JPanel(new GridLayout(1, 10, 15, 5)); // Alinhamento na mesma linha

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
                String categoria = (String) getValueAt(row, 0);

                // Permite edição SOMENTE da coluna "Valor Total (R$)" nos "Adicionais"
                if (categoria != null && categoria.equalsIgnoreCase("Adicionais")) {
                    return column == colunas.length - 1 && !isEstimado;
                }

                // Mantém a lógica de edição das colunas de dias/perfis apenas se o projeto NÃO estiver estimado
                return !isEstimado && column > 0 && column <= perfis.size();
            }
        };

        tabelaDetalhes.setModel(modeloTabela);
        tabelaDetalhes.setDefaultRenderer(Object.class, new CustomTableRenderer());

        DecimalFormat df = new DecimalFormat("R$ #,##0.00");
        lblValorTotal.setText("Valor Total: " + df.format(valorTotal));
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

    public JTable getTabelaDetalhes() {
        return tabelaDetalhes;
    }

}
