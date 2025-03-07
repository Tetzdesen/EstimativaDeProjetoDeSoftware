package com.br.estimativadeprojetodesoftware.view.usuario;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class LoginView extends javax.swing.JFrame {

    private javax.swing.JButton btnEntrar;
    private javax.swing.JButton btnExibirSenha;
    private javax.swing.JLabel lblEmail;
    private javax.swing.JLabel lblLogin;
    private javax.swing.JLabel lblSenha;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JPasswordField txtSenha;

    public LoginView() {
        initComponents();
    }

    public JTextField getTxtEmail() {
        return txtEmail;
    }

    public JPasswordField getTxtSenha() {
        return txtSenha;
    }

    public JButton getBtnEntrar() {
        return btnEntrar;
    }

    public JButton getBtnExibirSenha() {
        return btnExibirSenha;
    }

    private void initComponents() {
        lblLogin = new javax.swing.JLabel();
        lblSenha = new javax.swing.JLabel();
        lblEmail = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        txtSenha = new javax.swing.JPasswordField();
        btnExibirSenha = new javax.swing.JButton();
        btnEntrar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Login");
        setResizable(false);

        lblLogin.setFont(new java.awt.Font("Segoe UI", 1, 24)); 
        lblLogin.setText("Login");
        lblLogin.setForeground(Color.WHITE); 

        lblSenha.setFont(new java.awt.Font("Segoe UI", 0, 14)); 
        lblSenha.setText("Senha:");
        lblSenha.setForeground(Color.WHITE); 

        lblEmail.setFont(new java.awt.Font("Segoe UI", 0, 14)); 
        lblEmail.setText("E-mail:");
        lblEmail.setForeground(Color.WHITE); 

        txtEmail.setFont(new java.awt.Font("Segoe UI", 0, 14));
        txtEmail.setPreferredSize(new java.awt.Dimension(300, 30)); 

        txtSenha.setFont(new java.awt.Font("Segoe UI", 0, 14)); 
        txtSenha.setPreferredSize(new java.awt.Dimension(300, 30)); 

        btnExibirSenha.setBackground(new java.awt.Color(34, 45, 68));
        btnExibirSenha.setIcon(new javax.swing.ImageIcon(getClass().getResource("/olho-icon.png"))); 
        btnExibirSenha.setBorder(null);
        btnExibirSenha.setPreferredSize(new Dimension(30, 30));

        btnEntrar.setFont(new java.awt.Font("Segoe UI", 0, 18)); 
        btnEntrar.setText("Entrar");
        btnEntrar.setBackground(new Color(34, 45, 68));
        btnEntrar.setForeground(Color.WHITE);
        btnEntrar.setFocusPainted(false);
        btnEntrar.setPreferredSize(new Dimension(85, 45)); 
        btnEntrar.setBorder(new CompoundBorder(
            new LineBorder(Color.WHITE, 3, true),
            new EmptyBorder(8, 8, 8, 8)
        ));

        addButtonHoverEffect(btnEntrar);

        setLayout(new BorderLayout());

        JPanel painelFundo = new JPanel(new GridBagLayout());
        painelFundo.setBackground(new Color(34, 45, 68)); 
        painelFundo.setBorder(new LineBorder(Color.WHITE, 2, true));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        gbc.gridwidth = 2; 
        painelFundo.add(lblLogin, gbc);
        gbc.gridwidth = 1;

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
        gbc.gridwidth = 2;
        painelFundo.add(btnEntrar, gbc); 

        add(painelFundo, BorderLayout.CENTER);

        setSize(400, 350); 
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void addButtonHoverEffect(JButton button) {
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(57, 68, 99));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(34, 45, 68));
            }
        });
    }
}
