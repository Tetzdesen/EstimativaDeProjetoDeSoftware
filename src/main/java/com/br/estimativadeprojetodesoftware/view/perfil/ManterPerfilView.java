package com.br.estimativadeprojetodesoftware.view.perfil;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class ManterPerfilView extends javax.swing.JInternalFrame {
    private JDesktopPane desktop;
    private JPanel painelPrincipal;
    private JPanel painelCampos;
    private JScrollPane scrollPane;

    public ManterPerfilView(JDesktopPane desktop) {
        setTitle("Manter Perfis");
        //setSize(600, 300);

        this.desktop = desktop;

        painelPrincipal = new JPanel(new BorderLayout());
        painelCampos = new JPanel();
        painelCampos.setLayout(new BoxLayout(painelCampos, BoxLayout.Y_AXIS));
        painelCampos.setBorder(new EmptyBorder(20, 20, 20, 20));

        scrollPane = new JScrollPane(painelCampos);
        painelPrincipal.add(scrollPane, BorderLayout.CENTER);

        add(painelPrincipal);
    }

    public JDesktopPane getDesktop() {
        return desktop;
    }

    public void adicionarCampoView(JLabel label, JSpinner spinner) {
        JPanel linha = new JPanel();
        linha.setLayout(new BorderLayout(5, 5));

        label.setPreferredSize(new Dimension(150, 25));
        spinner.setPreferredSize(new Dimension(80, 25));

        linha.add(label, BorderLayout.WEST);
        linha.add(spinner, BorderLayout.CENTER);

        painelCampos.add(linha);

        painelCampos.revalidate();
        painelCampos.repaint();
    }
}
