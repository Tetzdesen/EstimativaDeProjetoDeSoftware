/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package com.br.estimativadeprojetodesoftware.view.perfil;

import java.awt.BorderLayout;

import javax.swing.*;

/**
 *
 * @author kauac
 */
public class ManterPerfilView extends javax.swing.JInternalFrame {
    private JLabel lblNome;
    private JDesktopPane desktop;

    public ManterPerfilView(JDesktopPane desktop) {
        setTitle("Manter Perfis");
        setResizable(true);
        setClosable(true);
        setMaximizable(true);
        setIconifiable(false);
        setResizable(true);
        setSize(1000, 700);

        this.desktop = desktop;

        JPanel painelPrincipal = new JPanel(new BorderLayout());
        add(painelPrincipal);

        lblNome = new JLabel("Teste");
        painelPrincipal.add(lblNome, BorderLayout.CENTER);
    }

    public JDesktopPane getDesktop() {
        return desktop;
    }

    
}
