package com.br.estimativadeprojetodesoftware.view.projeto;

import javax.swing.*;
import java.awt.*;

public final class PrincipalProjetoView extends JFrame {
    private JDesktopPane desktop;
    private JToolBar barraPrincipal;
    private JTree tree;
    private JScrollPane treeScrollPane;

    public PrincipalProjetoView() {
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
        barraPrincipal = toolBar;
        add(toolBar, BorderLayout.NORTH);
    }

    public JDesktopPane getDesktop() {
        return desktop;
    }

    public JToolBar getBarraPrincipal() {
        return barraPrincipal;
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
