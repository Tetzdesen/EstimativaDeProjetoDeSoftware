package com.br.estimativadeprojetodesoftware.view.usuario;

import javax.swing.JButton;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 *
 * @author tetzner
 */
public class UsuarioView extends javax.swing.JInternalFrame {

    public UsuarioView() {
        initComponents();
    }

    public JTextField getTxtNome() {
        return txtNome;
    }

    public JTextField getTxtDataCriacao() {
        return txtDataCriacao;
    }

    public JTextField getTxtEmail() {
        return txtEmail;
    }

    public JPasswordField getTxtSenhaAtual() {
        return txtSenhaAtual;
    }

    public JTextField getTxtTotalProjetos() {
        return txtTotalProjetos;
    }

    public JTextField getTxtTotalPerfis() {
        return txtTotalPerfis;
    }

    public JButton getBtnExibirSenha() {
        return btnExibirSenha;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtNome = new javax.swing.JTextField();
        txtDataCriacao = new javax.swing.JTextField();
        txtEmail = new javax.swing.JTextField();
        txtSenhaAtual = new javax.swing.JPasswordField();
        txtTotalProjetos = new javax.swing.JTextField();
        txtTotalPerfis = new javax.swing.JTextField();
        lblNome = new javax.swing.JLabel();
        lblDataCriacao = new javax.swing.JLabel();
        lblEmail = new javax.swing.JLabel();
        lblTotalProjetos = new javax.swing.JLabel();
        lblTotalPerfis = new javax.swing.JLabel();
        lblSenhaAtual = new javax.swing.JLabel();
        btnExibirSenha = new javax.swing.JButton();

        setTitle("Usuário");
        setToolTipText("");

        txtNome.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtNome.setEnabled(false);

        txtDataCriacao.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtDataCriacao.setEnabled(false);

        txtEmail.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtEmail.setEnabled(false);

        txtSenhaAtual.setEnabled(false);

        txtTotalProjetos.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtTotalProjetos.setEnabled(false);

        txtTotalPerfis.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtTotalPerfis.setEnabled(false);

        lblNome.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblNome.setText("Nome:");

        lblDataCriacao.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblDataCriacao.setText("Data de Criação:");

        lblEmail.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblEmail.setText("Email:");

        lblTotalProjetos.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblTotalProjetos.setText("Total de Projetos:");

        lblTotalPerfis.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblTotalPerfis.setText("Total de Perfis: ");

        lblSenhaAtual.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblSenhaAtual.setText("Senha atual:");

        btnExibirSenha.setBackground(new java.awt.Color(242, 242, 242));
        btnExibirSenha.setIcon(new javax.swing.ImageIcon(getClass().getResource("/olho-icon.png"))); // NOI18N
        btnExibirSenha.setBorder(null);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtTotalProjetos, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblTotalProjetos))
                                .addGap(10, 10, 10)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblTotalPerfis)
                                    .addComponent(txtTotalPerfis, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(txtSenhaAtual))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnExibirSenha))
                    .addComponent(lblSenhaAtual)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(lblNome)
                            .addGap(320, 320, 320)
                            .addComponent(lblDataCriacao))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(txtNome, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(40, 40, 40)
                            .addComponent(txtDataCriacao, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(lblEmail)
                        .addComponent(txtEmail)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblNome)
                    .addComponent(lblDataCriacao))
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtNome, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDataCriacao, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9)
                .addComponent(lblEmail)
                .addGap(10, 10, 10)
                .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblSenhaAtual)
                .addGap(8, 8, 8)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnExibirSenha, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSenhaAtual, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTotalProjetos)
                    .addComponent(lblTotalPerfis))
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtTotalProjetos, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTotalPerfis, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(42, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnExibirSenha;
    private javax.swing.JLabel lblDataCriacao;
    private javax.swing.JLabel lblEmail;
    private javax.swing.JLabel lblNome;
    private javax.swing.JLabel lblSenhaAtual;
    private javax.swing.JLabel lblTotalPerfis;
    private javax.swing.JLabel lblTotalProjetos;
    private javax.swing.JTextField txtDataCriacao;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtNome;
    private javax.swing.JPasswordField txtSenhaAtual;
    private javax.swing.JTextField txtTotalPerfis;
    private javax.swing.JTextField txtTotalProjetos;
    // End of variables declaration//GEN-END:variables
}
