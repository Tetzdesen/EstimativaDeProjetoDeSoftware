package com.br.estimativadeprojetodesoftware.view.perfil;

import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ManterPerfilView extends JDialog {
    private boolean isCellEditable;
    private JDesktopPane desktop;
    private JTable tabelaDetalhes;
    private DefaultTableModel modeloTabela;
    private JTextField txtNome;
    private JTextField txtMvp, txtBasico, txtProfissional;
    private JTextField txtDesignerUI, txtGerenciaProjeto, txtDesenvolvimento;
    private Map<JLabel, JComponent> camposObrigatorios;
    private JLabel lblNome;
    private JCheckBox tglBackEnd;
    private JButton btnSalvar, btnEditar, btnExcluir, btnCancelar, btnRemoverCampo, btnAdicionarCampo;
    private JSpinner jspPequeno, jspMedio, jspGrande;

    public ManterPerfilView(JDesktopPane desktop) {
        setTitle("Manter Perfis");
        setResizable(true);
        // Inicializa o map de campos obrigatórios
        setCamposObrigatorios();
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

    /**
     * Inicializa o map de campos obrigatórios, onde cada entrada associa um JLabel a um componente de entrada.
     */
    private void setCamposObrigatorios() {
        camposObrigatorios = new LinkedHashMap<>();
        // Campos para "Tamanho do App"
        jspPequeno = new JSpinner(new SpinnerNumberModel(0, 0, 999, 1));
        jspMedio   = new JSpinner(new SpinnerNumberModel(0, 0, 999, 1));
        jspGrande  = new JSpinner(new SpinnerNumberModel(0, 0, 999, 1));
        camposObrigatorios.put(new JLabel("Pequeno: "), jspPequeno);
        camposObrigatorios.put(new JLabel("Médio: "), jspMedio);
        camposObrigatorios.put(new JLabel("Grande: "), jspGrande);

        // Campos para "Nível de UI"
        txtMvp          = new JFormattedTextField();
        txtBasico       = new JFormattedTextField();
        txtProfissional = new JFormattedTextField();
        camposObrigatorios.put(new JLabel("MVP: "), txtMvp);
        camposObrigatorios.put(new JLabel("Básico: "), txtBasico);
        camposObrigatorios.put(new JLabel("Profissional: "), txtProfissional);

        // Campos para "Taxa Diária"
        txtDesignerUI      = new JFormattedTextField();
        txtGerenciaProjeto = new JFormattedTextField();
        txtDesenvolvimento = new JFormattedTextField();
        camposObrigatorios.put(new JLabel("Designer UI/UX: "), txtDesignerUI);
        camposObrigatorios.put(new JLabel("Gerência de Projeto: "), txtGerenciaProjeto);
        camposObrigatorios.put(new JLabel("Desenvolvimento: "), txtDesenvolvimento);
    }

    /**
     * Cria o painel para o "Tamanho do App" utilizando os componentes do map.
     */
    private JPanel criarPainelTamanhoApp() {
        JPanel painelTamanhoApp = new JPanel(new GridLayout(1, 3, 3, 15));
        painelTamanhoApp.setBorder(BorderFactory.createTitledBorder("Tamanho do App (número de dias):"));
        for (Map.Entry<JLabel, JComponent> entry : camposObrigatorios.entrySet()) {
            String labelText = entry.getKey().getText();
            if (labelText.contains("Pequeno") || labelText.contains("Médio") || labelText.contains("Grande")) {
                JPanel painel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 15));
                painel.add(entry.getKey());
                painel.add(entry.getValue());
                painelTamanhoApp.add(painel);
            }
        }
        return painelTamanhoApp;
    }

    /**
     * Cria o painel para o "Nível de UI" utilizando os componentes do map.
     */
    private JPanel criarPainelNivelUI() {
        JPanel painelNivelUI = new JPanel(new GridLayout(1, 3, 3, 15));
        painelNivelUI.setBorder(BorderFactory.createTitledBorder("Nível de UI (%):"));
        for (Map.Entry<JLabel, JComponent> entry : camposObrigatorios.entrySet()) {
            String labelText = entry.getKey().getText();
            if (labelText.contains("MVP") || labelText.contains("Básico") || labelText.contains("Profissional")) {
                JPanel painel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 15));
                painel.add(entry.getKey());
                painel.add(entry.getValue());
                painel.add(new JLabel("%"));
                painelNivelUI.add(painel);
            }
        }
        return painelNivelUI;
    }

    /**
     * Cria o painel para a "Taxa Diária" utilizando os componentes do map.
     */
    private JPanel criarPainelTaxaDiaria() {
        JPanel painelTaxaDiaria = new JPanel(new GridLayout(1, 3, 3, 15));
        painelTaxaDiaria.setBorder(BorderFactory.createTitledBorder("Taxa Diária (R$):"));
        for (Map.Entry<JLabel, JComponent> entry : camposObrigatorios.entrySet()) {
            String labelText = entry.getKey().getText();
            if (labelText.contains("Designer UI/UX") || labelText.contains("Gerência de Projeto") || labelText.contains("Desenvolvimento")) {
                JPanel painel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 15));
                painel.add(entry.getKey());
                painel.add(new JLabel("R$"));
                painel.add(entry.getValue());
                painelTaxaDiaria.add(painel);
            }
        }
        return painelTaxaDiaria;
    }

    // Métodos getters e atualizadores

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
