package com.br.estimativadeprojetodesoftware.view;

import javax.swing.*;
import java.awt.*;

public final class PrincipalView extends JFrame {
    private JDesktopPane desktop;
    private JTree tree;
    private JScrollPane treeScrollPane;

    public PrincipalView() {
        setTitle("Sistema de Estimativa de Projetos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);

        treeScrollPane = new JScrollPane();
        desktop = new JDesktopPane();

        JSplitPane divisoriaPainel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, getTreeScrollPane(), getDesktop());
        divisoriaPainel.setDividerLocation(300);
        divisoriaPainel.setResizeWeight(0.0);
        divisoriaPainel.setContinuousLayout(true);
        divisoriaPainel.setOneTouchExpandable(true);

        add(divisoriaPainel, BorderLayout.CENTER);
    }

    public void setMainComponents(JToolBar toolBar) {
        add(toolBar, BorderLayout.NORTH);
    }

    public JDesktopPane getDesktop() {
        return desktop;
    }

    public JTree getTree() {
        return tree;
    }

    public void setTree(JTree tree) {
        this.tree = tree;
        treeScrollPane.setViewportView(tree);
    }

    public JScrollPane getTreeScrollPane() {
        return treeScrollPane;
    }

}
