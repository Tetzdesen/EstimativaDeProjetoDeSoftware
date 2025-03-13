package com.br.estimativadeprojetodesoftware.view.usuario;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class CadastroView extends javax.swing.JFrame {

    private javax.swing.JButton btnSalvar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnExibirSenha;
    private javax.swing.JButton btnExibirConfirmarSenha;
    private javax.swing.JLabel lblCadastro;
    private javax.swing.JLabel lblNomeUsuario;
    private javax.swing.JLabel lblEmail;
    private javax.swing.JLabel lblSenha;
    private javax.swing.JLabel lblConfirmarSenha;
    private javax.swing.JTextField txtNome;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JPasswordField txtSenha;
    private javax.swing.JPasswordField txtConfirmarSenha;

    public CadastroView() {
        initComponents();
    }

    public JTextField getTxtNomeUsuario() {
        return txtNome;
    }

    public JTextField getTxtEmail() {
        return txtEmail;
    }

    public JPasswordField getTxtSenha() {
        return txtSenha;
    }

    public JPasswordField getTxtConfirmarSenha() {
        return txtConfirmarSenha;
    }

    public JButton getBtnSalvar() {
        return btnSalvar;
    }

    public JButton getBtnCancelar() {
        return btnCancelar;
    }

    public JButton getBtnExibirSenha() {
        return btnExibirSenha;
    }

    public JButton getBtnExibirConfirmarSenha() {
        return btnExibirConfirmarSenha;
    }

    private void initComponents() {

        lblCadastro = new javax.swing.JLabel();
        lblNomeUsuario = new javax.swing.JLabel();
        lblEmail = new javax.swing.JLabel();
        lblSenha = new javax.swing.JLabel();
        lblConfirmarSenha = new javax.swing.JLabel();
        txtNome = new javax.swing.JTextField();
        txtEmail = new javax.swing.JTextField();
        txtSenha = new javax.swing.JPasswordField();
        txtConfirmarSenha = new javax.swing.JPasswordField();
        btnExibirSenha = new javax.swing.JButton();
        btnExibirConfirmarSenha = new javax.swing.JButton();
        btnSalvar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Cadastro");
        setResizable(true);

        lblCadastro.setFont(new java.awt.Font("Segoe UI", 1, 24));
        lblCadastro.setText("Cadastro");
        lblCadastro.setForeground(Color.BLACK);
        lblCadastro.setHorizontalAlignment(SwingConstants.CENTER);

        lblNomeUsuario.setFont(new java.awt.Font("Segoe UI", 0, 14));
        lblNomeUsuario.setText("Nome:");
        lblNomeUsuario.setForeground(Color.BLACK);

        lblEmail.setFont(new java.awt.Font("Segoe UI", 0, 14));
        lblEmail.setText("E-mail:");
        lblEmail.setForeground(Color.BLACK);

        lblSenha.setFont(new java.awt.Font("Segoe UI", 0, 14));
        lblSenha.setText("Senha:");
        lblSenha.setForeground(Color.BLACK);

        lblConfirmarSenha.setFont(new java.awt.Font("Segoe UI", 0, 14));
        lblConfirmarSenha.setText("Confirmar Senha:");
        lblConfirmarSenha.setForeground(Color.BLACK);

        txtNome.setFont(new java.awt.Font("Segoe UI", 0, 14));
        txtNome.setPreferredSize(new java.awt.Dimension(400, 30));

        txtEmail.setFont(new java.awt.Font("Segoe UI", 0, 14));
        txtEmail.setPreferredSize(new java.awt.Dimension(400, 30));

        txtSenha.setFont(new java.awt.Font("Segoe UI", 0, 14));
        txtSenha.setPreferredSize(new java.awt.Dimension(400, 30));

        txtConfirmarSenha.setFont(new java.awt.Font("Segoe UI", 0, 14));
        txtConfirmarSenha.setPreferredSize(new java.awt.Dimension(400, 30));

        btnExibirSenha.setBackground(new java.awt.Color(255, 255, 255));
        btnExibirSenha.setIcon(new javax.swing.ImageIcon(getClass().getResource("/olho-icon.png")));
        btnExibirSenha.setBorder(null);
        btnExibirSenha.setPreferredSize(new Dimension(30, 30));

        btnExibirConfirmarSenha.setBackground(new java.awt.Color(255, 255, 255));
        btnExibirConfirmarSenha.setIcon(new javax.swing.ImageIcon(getClass().getResource("/olho-icon.png")));
        btnExibirConfirmarSenha.setBorder(null);
        btnExibirConfirmarSenha.setPreferredSize(new Dimension(30, 30));

        btnSalvar.setFont(new java.awt.Font("Segoe UI", 0, 18));
        btnSalvar.setText("Cadastrar");
        btnSalvar.setBackground(new Color(255, 255, 255));
        btnSalvar.setForeground(Color.BLACK);
        btnSalvar.setFocusPainted(false);
        btnSalvar.setPreferredSize(new Dimension(120, 45));

        btnSalvar.setBorder(new CompoundBorder(
                new LineBorder(Color.BLACK, 3, true),
                new EmptyBorder(8, 8, 8, 8)
        ));

        btnCancelar.setFont(new java.awt.Font("Segoe UI", 0, 18));
        btnCancelar.setText("Cancelar");
        btnCancelar.setBackground(new Color(255, 255, 255));
        btnCancelar.setForeground(Color.BLACK);
        btnCancelar.setFocusPainted(false);
        btnCancelar.setPreferredSize(new Dimension(120, 45));
        btnCancelar.setBorder(new CompoundBorder(
                new LineBorder(Color.BLACK, 3, true),
                new EmptyBorder(8, 8, 8, 8)
        ));

        setLayout(new BorderLayout());

        JPanel painelFundo = new JPanel(new GridBagLayout());
        painelFundo.setBackground(new Color(255, 255, 255));
        painelFundo.setBorder(new LineBorder(Color.BLACK, 2, true));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridwidth = 2;

        gbc.gridy = 0;
        painelFundo.add(lblCadastro, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        painelFundo.add(lblNomeUsuario, gbc);
        gbc.gridx = 1;
        painelFundo.add(txtNome, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        painelFundo.add(lblEmail, gbc);
        gbc.gridx = 1;
        painelFundo.add(txtEmail, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        painelFundo.add(lblSenha, gbc);
        gbc.gridx = 1;
        painelFundo.add(txtSenha, gbc);
        gbc.gridx = 2;
        painelFundo.add(btnExibirSenha, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        painelFundo.add(lblConfirmarSenha, gbc);
        gbc.gridx = 1;
        painelFundo.add(txtConfirmarSenha, gbc);
        gbc.gridx = 2;
        painelFundo.add(btnExibirConfirmarSenha, gbc);

        // Painel para os botões alinhados (Cadastrar e Cancelar)
        JPanel painelBotoes = new JPanel(new GridLayout(1, 2, 20, 0));
        painelBotoes.setBackground(new Color(255, 255, 255));
        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnCancelar);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        painelFundo.add(painelBotoes, gbc);

        add(painelFundo, BorderLayout.CENTER);

        setSize(1500, 700);
        setLocationRelativeTo(null);
        setVisible(true);
    }

}
