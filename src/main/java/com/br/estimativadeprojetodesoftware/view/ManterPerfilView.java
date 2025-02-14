/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package com.br.estimativadeprojetodesoftware.view;

import java.awt.BorderLayout;

import javax.swing.*;

/**
 *
 * @author kauac
 */
public class ManterPerfilView extends javax.swing.JFrame {
    private JLabel lblNome;

    public ManterPerfilView() {
        setTitle("Manter Perfis");
        setResizable(true);
        setSize(1000, 700);

        JPanel painelPrincipal = new JPanel(new BorderLayout());
        add(painelPrincipal);

        lblNome = new JLabel("Teste");
        painelPrincipal.add(lblNome, BorderLayout.CENTER);
    }

    
}
