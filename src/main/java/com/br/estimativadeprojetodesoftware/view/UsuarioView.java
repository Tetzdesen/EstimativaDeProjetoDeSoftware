package com.br.estimativadeprojetodesoftware.view;

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

    public JTextField getTxtSenhaAtual() {
        return txtSenhaAtual;
    }

    public JTextField getTxtTotalProjetos() {
        return txtTotalProjetos;
    }

    public JTextField getTxtTotalPerfis() {
        return txtTotalPerfis;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtNome = new javax.swing.JTextField();
        txtDataCriacao = new javax.swing.JTextField();
        txtEmail = new javax.swing.JTextField();
        txtTotalProjetos = new javax.swing.JTextField();
        txtTotalPerfis = new javax.swing.JTextField();
        lblNome = new javax.swing.JLabel();
        lblDataCriacao = new javax.swing.JLabel();
        lblEmail = new javax.swing.JLabel();
        lblTotalProjetos = new javax.swing.JLabel();
        lblTotalPerfis = new javax.swing.JLabel();
        txtSenhaAtual = new javax.swing.JTextField();
        lblSenhaAtual = new javax.swing.JLabel();

        setTitle("Usuário");
        setToolTipText("");

        txtNome.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtNome.setEnabled(false);

        txtDataCriacao.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtDataCriacao.setEnabled(false);

        txtEmail.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtEmail.setEnabled(false);

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

        txtSenhaAtual.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtSenhaAtual.setEnabled(false);

        lblSenhaAtual.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblSenhaAtual.setText("Senha atual:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtTotalProjetos, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTotalProjetos))
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTotalPerfis)
                    .addComponent(txtTotalPerfis, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(txtSenhaAtual)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
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
                            .addComponent(txtEmail))))
                .addContainerGap(30, Short.MAX_VALUE))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                .addComponent(lblSenhaAtual)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtSenhaAtual, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTotalProjetos)
                    .addComponent(lblTotalPerfis))
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtTotalProjetos, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTotalPerfis, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lblDataCriacao;
    private javax.swing.JLabel lblEmail;
    private javax.swing.JLabel lblNome;
    private javax.swing.JLabel lblSenhaAtual;
    private javax.swing.JLabel lblTotalPerfis;
    private javax.swing.JLabel lblTotalProjetos;
    private javax.swing.JTextField txtDataCriacao;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtNome;
    private javax.swing.JTextField txtSenhaAtual;
    private javax.swing.JTextField txtTotalPerfis;
    private javax.swing.JTextField txtTotalProjetos;
    // End of variables declaration//GEN-END:variables
}
