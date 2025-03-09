package com.br.estimativadeprojetodesoftware.view.perfil;

import java.awt.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ManterPerfilView extends JDialog {
    private boolean isCellEditable;
    private JDesktopPane desktop;
    private JTable tabelaDetalhes;
    private DefaultTableModel modeloTabela;
    private JTextField txtNome, txtMvp, txtBasico, txtProfissional, txtDesignerUI, txtGerenciaProjeto, txtDesenvolvimento;
    private JLabel lblNome;
    private JCheckBox tglBackEnd;
    private JButton btnSalvar, btnEditar, btnExcluir, btnCancelar, btnRemoverCampo, btnAdicionarCampo;
    private JSpinner jspPequeno, jspMedio, jspGrande;

    public ManterPerfilView(JDesktopPane desktop) {
        setTitle("Manter Perfis");
        setResizable(true);
        this.desktop = desktop;
        isCellEditable = false;

        // Painel Principal
        JPanel painelPrincipal = new JPanel(new BorderLayout());
        add(painelPrincipal);

        // --- Painel Cabeçalho ---
        JPanel painelCabecalho = new JPanel(new BorderLayout());

        // Painel de Informações (Nome e Checkbox)
        JPanel painelInfo = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 15));
        lblNome = new JLabel("Nome: ");
        txtNome = new JTextField();
        txtNome.setPreferredSize(new Dimension(350, 25));
        tglBackEnd = new JCheckBox("Perfil de back end");
        painelInfo.add(lblNome);
        painelInfo.add(txtNome);
        painelInfo.add(tglBackEnd);
        painelCabecalho.add(painelInfo, BorderLayout.NORTH);

        // --- Painel de Campos Obrigatórios ---
        JPanel painelCamposObrigatorios = new JPanel();
        painelCamposObrigatorios.setLayout(new BoxLayout(painelCamposObrigatorios, BoxLayout.Y_AXIS));
        painelCamposObrigatorios.add(criarPainelTamanhoApp());
        painelCamposObrigatorios.add(criarPainelNivelUI());
        painelCamposObrigatorios.add(criarPainelTaxaDiaria());
        painelCabecalho.add(painelCamposObrigatorios, BorderLayout.CENTER);

        painelPrincipal.add(painelCabecalho, BorderLayout.NORTH);

        // --- Painel Tabela ---
        JPanel painelTabela = new JPanel(new BorderLayout());
        modeloTabela = new DefaultTableModel(new Object[]{"Funcionalidade", "Número de Dias"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return isCellEditable;
            }
        };
        tabelaDetalhes = new JTable(modeloTabela);
        tabelaDetalhes.setFillsViewportHeight(true);
        tabelaDetalhes.getColumnModel().getColumn(0).setPreferredWidth(400);
        JScrollPane scrollTabela = new JScrollPane(tabelaDetalhes);
        painelTabela.add(scrollTabela, BorderLayout.CENTER);
        painelPrincipal.add(painelTabela, BorderLayout.CENTER);

        // --- Painel de Botões ---
        btnAdicionarCampo = new JButton("Adicionar");
        btnRemoverCampo = new JButton("Remover");
        btnSalvar = new JButton("Salvar Perfil");
        btnEditar = new JButton("Editar Perfil");
        btnExcluir = new JButton("Excluir Perfil");
        btnCancelar = new JButton("Cancelar");

        Dimension btnDim = new Dimension(160, 25);
        btnAdicionarCampo.setPreferredSize(btnDim);
        btnRemoverCampo.setPreferredSize(btnDim);
        btnSalvar.setPreferredSize(btnDim);
        btnEditar.setPreferredSize(btnDim);
        btnExcluir.setPreferredSize(btnDim);
        btnCancelar.setPreferredSize(btnDim);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER));
        painelBotoes.add(btnAdicionarCampo);
        painelBotoes.add(btnRemoverCampo);
        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnEditar);
        painelBotoes.add(btnExcluir);
        painelBotoes.add(btnCancelar);
        painelPrincipal.add(painelBotoes, BorderLayout.SOUTH);
    }

    private JPanel criarPainelComSpinner(String texto, JSpinner spinner, boolean addExtraLabel) {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 15));
        painel.add(new JLabel(texto));
        painel.add(spinner);
        if (addExtraLabel) {
            painel.add(new JLabel("%"));
        }
        return painel;
    }

    private JPanel criarPainelComTextField(String texto, JTextField txtField, boolean addExtraLabel, boolean addPercent) {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 15));
        painel.add(new JLabel(texto));
        if (addExtraLabel) {
            painel.add(new JLabel("R$"));
        }
        painel.add(txtField);
        if (addPercent) {
            painel.add(new JLabel("%"));
        }
        return painel;
    }

    private JPanel criarPainelTamanhoApp() {
        jspPequeno  = new JSpinner(new SpinnerNumberModel(0, 0, 999, 1));
        jspMedio    = new JSpinner(new SpinnerNumberModel(0, 0, 999, 1));
        jspGrande   = new JSpinner(new SpinnerNumberModel(0, 0, 999, 1));

        JPanel painelPequeno    = criarPainelComSpinner("Pequeno: ", jspPequeno, false);
        JPanel painelMedio      = criarPainelComSpinner("Médio: ", jspMedio, false);
        JPanel painelGrande     = criarPainelComSpinner("Grande: ", jspGrande, false);

        JPanel painelTamanhoApp = new JPanel(new GridLayout(1, 3, 3, 15));
        painelTamanhoApp.setBorder(BorderFactory.createTitledBorder("Tamanho do App (número de dias):"));
        painelTamanhoApp.add(painelPequeno);
        painelTamanhoApp.add(painelMedio);
        painelTamanhoApp.add(painelGrande);

        return painelTamanhoApp;
    }

    private JPanel criarPainelNivelUI() {
        txtMvp          = new JFormattedTextField();
        txtBasico       = new JFormattedTextField();
        txtProfissional = new JFormattedTextField();

        JPanel painelMVP            = criarPainelComTextField("MVP: ", txtMvp, false, true);
        JPanel painelBasico         = criarPainelComTextField("Básico: ", txtBasico, false, true);
        JPanel painelProfissional   = criarPainelComTextField("Profissional: ", txtProfissional, false, true);

        JPanel painelNivelUI = new JPanel(new GridLayout(1, 3, 3, 15));
        painelNivelUI.setBorder(BorderFactory.createTitledBorder("Nível de UI (%):"));
        painelNivelUI.add(painelMVP);
        painelNivelUI.add(painelBasico);
        painelNivelUI.add(painelProfissional);

        return painelNivelUI;
    }

    private JPanel criarPainelTaxaDiaria() {
        txtDesignerUI = new JFormattedTextField();
        txtGerenciaProjeto = new JFormattedTextField();
        txtDesenvolvimento = new JFormattedTextField();

        JPanel painelDesignerUI = criarPainelComTextField("Designer UI/UX: ", txtDesignerUI, true, false);
        JPanel painelGerenciaProjeto = criarPainelComTextField("Gerência de Projeto: ", txtGerenciaProjeto, true, false);
        JPanel painelDesenvolvimento = criarPainelComTextField("Desenvolvimento: ", txtDesenvolvimento, true, false);

        JPanel painelTaxaDiaria = new JPanel(new GridLayout(1, 3, 3, 15));
        painelTaxaDiaria.setBorder(BorderFactory.createTitledBorder("Taxa Diária (R$):"));
        painelTaxaDiaria.add(painelDesignerUI);
        painelTaxaDiaria.add(painelGerenciaProjeto);
        painelTaxaDiaria.add(painelDesenvolvimento);

        return painelTaxaDiaria;
    }

    public JDesktopPane getDesktop() {
        return desktop;
    }

    public void atualizarTabela(Object[][] dados) {
        modeloTabela.setRowCount(0);
        for (Object[] linha : dados) {
            modeloTabela.addRow(linha);
        }
    }

    public void setCellEditable(boolean isCellEditable) {
        this.isCellEditable = isCellEditable;
    }

    public JButton getBtnSalvar() {
        return btnSalvar;
    }

    public JButton getBtnRemoverCampo() {
        return btnRemoverCampo;
    }

    public JButton getBtnAdicionarCampo() {
        return btnAdicionarCampo;
    }

    public DefaultTableModel getModeloTabela() {
        return modeloTabela;
    }

    public JTable getTabelaDetalhes() {
        return tabelaDetalhes;
    }

    public JTextField getTxtNome() {
        return txtNome;
    }

    public JLabel getLblNome() {
        return lblNome;
    }

    public JCheckBox getTglBackEnd() {
        return tglBackEnd;
    }

    public JButton getBtnEditar() {
        return btnEditar;
    }

    public JButton getBtnExcluir() {
        return btnExcluir;
    }

    public JButton getBtnCancelar() {
        return btnCancelar;
    }
    
    public boolean isCellEditable() {
        return isCellEditable;
    }

    public JTextField getTxtDesignerUI() {
        return txtDesignerUI;
    }

    public JTextField getTxtGerenciaProjeto() {
        return txtGerenciaProjeto;
    }

    public JTextField getTxtDesenvolvimento() {
        return txtDesenvolvimento;
    }

    public JSpinner getJspPequeno() {
        return jspPequeno;
    }

    public JSpinner getJspMedio() {
        return jspMedio;
    }

    public JSpinner getJspGrande() {
        return jspGrande;
    }

    public JTextField getTxtMvp() {
        return txtMvp;
    }

    public JTextField getTxtBasico() {
        return txtBasico;
    }

    public JTextField getTxtProfissional() {
        return txtProfissional;
    }
}
