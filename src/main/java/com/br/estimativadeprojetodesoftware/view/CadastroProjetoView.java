package com.br.estimativadeprojetodesoftware.view;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

/**
 *
 * @author tetzner
 */
public class CadastroProjetoView extends javax.swing.JFrame {

    public CadastroProjetoView() {
        initComponents();
    }

    public JButton getBtnAdicionarPerfil() {
        return btnAdicionarPerfil;
    }

    public JButton getBtnCadastrarProjeto() {
        return btnCadastrarProjeto;
    }

    public JButton getBtnCancelar() {
        return btnCancelar;
    }

    public JComboBox<String> getCbmPerfis() {
        return cbmPerfis;
    }

    public JComboBox<String> getjComboBox1() {
        return jComboBox1;
    }

    public JList<String> getjList1() {
        return jList1;
    }

    public JScrollBar getjScrollBar1() {
        return jScrollBar1;
    }

    public JScrollPane getSpListaPerfis() {
        return spListaPerfis;
    }

    public JTextField getTxtNome() {
        return txtNome;
    }

    public JTextField getTxtTipoProjeto() {
        return txtTipoProjeto;
    }  

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jComboBox1 = new javax.swing.JComboBox<>();
        jScrollBar1 = new javax.swing.JScrollBar();
        txtNome = new javax.swing.JTextField();
        txtTipoProjeto = new javax.swing.JTextField();
        btnCadastrarProjeto = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        lblCadastroProjeto = new javax.swing.JLabel();
        lblNomeProjeto = new javax.swing.JLabel();
        lblTipoProjeto = new javax.swing.JLabel();
        lblPerfis = new javax.swing.JLabel();
        cbmPerfis = new javax.swing.JComboBox<>();
        btnAdicionarPerfil = new javax.swing.JButton();
        spListaPerfis = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Cadastro de Projeto");
        setIconImages(null);

        txtNome.setMaximumSize(new java.awt.Dimension(64, 22));
        txtNome.setName(""); // NOI18N

        txtTipoProjeto.setMaximumSize(new java.awt.Dimension(64, 22));
        txtTipoProjeto.setName(""); // NOI18N

        btnCadastrarProjeto.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnCadastrarProjeto.setText("Cadastrar Projeto");

        btnCancelar.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        lblCadastroProjeto.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblCadastroProjeto.setText("Cadastro Projeto");

        lblNomeProjeto.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblNomeProjeto.setText("Nome:");

        lblTipoProjeto.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblTipoProjeto.setText("Tipo:");

        lblPerfis.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblPerfis.setText("Perfis:");

        cbmPerfis.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cbmPerfis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbmPerfisActionPerformed(evt);
            }
        });

        btnAdicionarPerfil.setText("Adicionar Perfil");

        jList1.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        spListaPerfis.setViewportView(jList1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(173, 173, 173)
                        .addComponent(lblCadastroProjeto))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(152, 152, 152)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnCadastrarProjeto)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblPerfis)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(spListaPerfis, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(cbmPerfis, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnAdicionarPerfil)))))))
                .addContainerGap(148, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap(82, Short.MAX_VALUE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(lblNomeProjeto)
                        .addComponent(lblTipoProjeto))
                    .addGap(18, 18, 18)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(txtTipoProjeto, javax.swing.GroupLayout.DEFAULT_SIZE, 354, Short.MAX_VALUE)
                        .addComponent(txtNome, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGap(82, 82, 82)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addComponent(lblCadastroProjeto)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 119, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbmPerfis, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAdicionarPerfil)
                    .addComponent(lblPerfis))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(spListaPerfis, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCadastrarProjeto)
                    .addComponent(btnCancelar))
                .addGap(25, 25, 25))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(102, 102, 102)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(txtNome, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGap(19, 19, 19))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(lblNomeProjeto)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtTipoProjeto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblTipoProjeto))
                    .addGap(231, 231, 231)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void cbmPerfisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbmPerfisActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbmPerfisActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdicionarPerfil;
    private javax.swing.JButton btnCadastrarProjeto;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JComboBox<String> cbmPerfis;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JList<String> jList1;
    private javax.swing.JScrollBar jScrollBar1;
    private javax.swing.JLabel lblCadastroProjeto;
    private javax.swing.JLabel lblNomeProjeto;
    private javax.swing.JLabel lblPerfis;
    private javax.swing.JLabel lblTipoProjeto;
    private javax.swing.JScrollPane spListaPerfis;
    private javax.swing.JTextField txtNome;
    private javax.swing.JTextField txtTipoProjeto;
    // End of variables declaration//GEN-END:variables
}
