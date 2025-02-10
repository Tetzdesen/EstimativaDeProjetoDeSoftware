package com.br.estimativadeprojetodesoftware.service;

import com.br.estimativadeprojetodesoftware.command.ProjetoCommand;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Enumeration;

public final class ConstrutorDeArvoreNavegacaoService {

    public ConstrutorDeArvoreNavegacaoService() {
    }

    public NoArvoreComposite criarNo(String texto, String chaveIcone, ProjetoCommand comando) {
        return new NoArvoreComposite(texto, chaveIcone, comando);
    }

    public DefaultMutableTreeNode converterParaNoMutavel(ComponenteNoArvore comp) {
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(comp);
        for (ComponenteNoArvore filho : comp.obterFilhos()) {
            node.add(converterParaNoMutavel(filho));
        }
        return node;
    }

    public JTree criarJTreeDoModelo(DefaultMutableTreeNode raiz) {
        final JTree arvore = new JTree(raiz);
        arvore.setShowsRootHandles(true);
        arvore.setRootVisible(true);
        arvore.setCellRenderer(new DefaultTreeCellRenderer() {
            @Override
            public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
                super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
                ComponenteNoArvore comp = (ComponenteNoArvore) node.getUserObject();
                setIcon(IconService.getIcon(comp.obterChaveIcone()));
                return this;
            }
        });

        arvore.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent evento) {
                if (SwingUtilities.isLeftMouseButton(evento)) {
                    int row = arvore.getRowForLocation(evento.getX(), evento.getY());
                    if (row != -1) {
                        arvore.setSelectionRow(row);
                        TreePath path = arvore.getPathForRow(row);
                        DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
                        ComponenteNoArvore comp = (ComponenteNoArvore) node.getUserObject();
                        ProjetoCommand cmd = comp.obterComando();
                        if (cmd != null) {
                            cmd.execute();
                        }
                    }
                } else if (evento.isPopupTrigger() || SwingUtilities.isRightMouseButton(evento)) {
                    int row = arvore.getRowForLocation(evento.getX(), evento.getY());
                    if (row != -1) {
                        arvore.setSelectionRow(row);
                        TreePath path = arvore.getPathForRow(row);
                        DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
                        Object userObj = node.getUserObject();
                        if (userObj instanceof NoArvoreComposite) {
                            NoArvoreComposite noComposite = (NoArvoreComposite) userObj;
                            if (noComposite.getMenuContextual() != null) {
                                JPopupMenu menu = noComposite.getMenuContextual().criarMenuContextual();
                                menu.show(arvore, evento.getX(), evento.getY());
                            }
                        }
                    }
                }
            }
        });

        expandirTodosOsNos(arvore);
        return arvore;
    }

    private void expandirTodosOsNos(JTree arvore) {
        DefaultMutableTreeNode raiz = (DefaultMutableTreeNode) arvore.getModel().getRoot();
        Enumeration<?> enumeracao = raiz.children();
        while (enumeracao.hasMoreElements()) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) enumeracao.nextElement();
            TreePath path = new TreePath(node.getPath());
            arvore.expandPath(path);
        }
    }
}
