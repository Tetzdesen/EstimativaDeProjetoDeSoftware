package com.br.estimativadeprojetodesoftware.view;

import javax.swing.JButton;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 * @author tetzner
 */

public class LoginView extends javax.swing.JInternalFrame {

    public LoginView() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblLogin = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        lblSenha = new javax.swing.JLabel();
        lblEmail = new javax.swing.JLabel();
        btnCadastrar = new javax.swing.JButton();
        lblNPConta = new javax.swing.JLabel();
        btnEntrar = new javax.swing.JButton();
        txtSenha = new javax.swing.JPasswordField();

        setTitle("Login desenvolvedor");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setMaximumSize(new java.awt.Dimension(310, 418));
        setMinimumSize(new java.awt.Dimension(310, 418));
        setOpaque(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblLogin.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblLogin.setText("Login");
        getContentPane().add(lblLogin, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 30, 90, 40));

        txtEmail.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtEmail.setMaximumSize(new java.awt.Dimension(64, 26));
        getContentPane().add(txtEmail, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 120, 180, 30));

        lblSenha.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblSenha.setText("Senha:");
        getContentPane().add(lblSenha, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 170, -1, 20));

        lblEmail.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblEmail.setText("E-mail:");
        getContentPane().add(lblEmail, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 90, -1, -1));

        btnCadastrar.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnCadastrar.setText("Cadastrar");
        btnCadastrar.setToolTipText("");
        getContentPane().add(btnCadastrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 350, 90, -1));

        lblNPConta.setText("Não tem uma conta?");
        getContentPane().add(lblNPConta, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 310, -1, 20));

        btnEntrar.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnEntrar.setText("Entrar");
        getContentPane().add(btnEntrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 260, 90, -1));

        txtSenha.setMaximumSize(new java.awt.Dimension(64, 26));
        txtSenha.setMinimumSize(new java.awt.Dimension(64, 26));
        getContentPane().add(txtSenha, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 200, 180, 30));

        setBounds(570, 150, 495, 442);
    }// </editor-fold>//GEN-END:initComponents

    public JTextField getTxtEmail() {
        return txtEmail;
    }

    public JPasswordField getTxtSenha() {
        return txtSenha;
    }
    
    public JButton getBtnCadastrar() {
        return btnCadastrar;
    }

    public JButton getBtnEntrar() {
        return btnEntrar;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCadastrar;
    private javax.swing.JButton btnEntrar;
    private javax.swing.JLabel lblEmail;
    private javax.swing.JLabel lblLogin;
    private javax.swing.JLabel lblNPConta;
    private javax.swing.JLabel lblSenha;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JPasswordField txtSenha;
    // End of variables declaration//GEN-END:variables
}
