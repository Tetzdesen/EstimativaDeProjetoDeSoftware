package com.br.estimativadeprojetodesoftware.view.projeto;

import javax.swing.*;
import java.awt.*;

public class EdicaoProjetoView extends JDialog {

    private JButton btnAdicionarPerfil;
    private JButton btnRemoverPerfil;
    private JButton btnCadastrarProjeto;
    private JButton btnCancelar;
    private JComboBox<String> cbmPerfis;
    private JList<String> jListPerfis;
    private JTextField txtNome;
    private JComboBox<String> cbmTamanhoApp;
    private JComboBox<String> cbmNivelUI;

    public EdicaoProjetoView() {
        setTitle("Edição de Projeto");
        setSize(Toolkit.getDefaultToolkit().getScreenSize()); // Maximiza a janela
        setResizable(false);
        setLayout(new BorderLayout(10, 10));

        initComponents();
    }

    private void initComponents() {
        // Painel do título
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel lblCadastroProjeto = new JLabel("Edição de Projeto");
        lblCadastroProjeto.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titlePanel.add(lblCadastroProjeto);

        // Painel do formulário
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Campo Nome
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Nome:"), gbc);

        gbc.gridx = 1; gbc.gridwidth = 2;
        txtNome = new JTextField(30);
        formPanel.add(txtNome, gbc);

        // Tamanho do APP
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        formPanel.add(new JLabel("Tamanho do APP:"), gbc);

        gbc.gridx = 1; gbc.gridwidth = 2;
        cbmTamanhoApp = new JComboBox<>(new String[]{"Pequeno", "Médio", "Grande"});
        formPanel.add(cbmTamanhoApp, gbc);

        // Nível de UI
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1;
        formPanel.add(new JLabel("Nível de UI:"), gbc);

        gbc.gridx = 1; gbc.gridwidth = 2;
        cbmNivelUI = new JComboBox<>(new String[]{"MVP", "Básico", "Profissional"});
        formPanel.add(cbmNivelUI, gbc);

        // Campo Perfis
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 1;
        formPanel.add(new JLabel("Perfis:"), gbc);

        gbc.gridx = 1;
        cbmPerfis = new JComboBox<>(new String[]{"Web/Back-end", "Mobile", "Full Stack"});
        formPanel.add(cbmPerfis, gbc);

        gbc.gridx = 2;
        btnAdicionarPerfil = new JButton("Adicionar Perfil");
        formPanel.add(btnAdicionarPerfil, gbc);

        // Lista de Perfis
        gbc.gridx = 1; gbc.gridy = 4; gbc.gridwidth = 2;
        jListPerfis = new JList<>();
        JScrollPane spListaPerfis = new JScrollPane(jListPerfis);
        spListaPerfis.setPreferredSize(new Dimension(250, 80));
        formPanel.add(spListaPerfis, gbc);

        // Botão Remover
        gbc.gridx = 3; gbc.gridwidth = 1;
        btnRemoverPerfil = new JButton("Remover Perfil");
        formPanel.add(btnRemoverPerfil, gbc);

        // Painel dos botões de ação
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnCadastrarProjeto = new JButton("Salvar");
        btnCancelar = new JButton("Cancelar");
        buttonPanel.add(btnCadastrarProjeto);
        buttonPanel.add(btnCancelar);

        // Adiciona os painéis na janela principal
        add(titlePanel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public JButton getBtnAdicionarPerfil() { return btnAdicionarPerfil; }
    public JButton getBtnRemoverPerfil() { return btnRemoverPerfil; }
    public JButton getBtnCadastrarProjeto() { return btnCadastrarProjeto; }
    public JButton getBtnCancelar() { return btnCancelar; }
    public JComboBox<String> getCbmPerfis() { return cbmPerfis; }
    public JList<String> getJListPerfis() { return jListPerfis; }
    public JTextField getTxtNome() { return txtNome; }
    public JComboBox<String> getCbmTamanhoApp() { return cbmTamanhoApp; }
    public JComboBox<String> getCbmNivelUI() { return cbmNivelUI; }
}