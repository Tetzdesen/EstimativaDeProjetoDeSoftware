/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package com.br.estimativadeprojetodesoftware.view.perfil;

import java.awt.BorderLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.*;

/**
 *
 * @author kauac
 */
public class ManterPerfilView extends javax.swing.JInternalFrame {
    private Map<JLabel, JTextField> campos = new HashMap<>();
    private JLabel lblNome;
    private JDesktopPane desktop;

    public ManterPerfilView(JDesktopPane desktop, Map<JLabel, JTextField> campos) {
        setTitle("Manter Perfis");
        setResizable(true);
        setClosable(true);
        setMaximizable(true);
        setIconifiable(false);
        setResizable(true);
        setSize(1000, 700);

        this.desktop = desktop;
        this.campos = campos;

        JPanel painelPrincipal = new JPanel(new BorderLayout());
        add(painelPrincipal);

        lblNome = new JLabel("Teste");
        painelPrincipal.add(lblNome, BorderLayout.CENTER);
    }

    public JDesktopPane getDesktop() {
        return desktop;
    }

    public void adicionarCampoView(Map<JLabel, JTextField> campos) {
        this.campos = campos;
    }

    
}
