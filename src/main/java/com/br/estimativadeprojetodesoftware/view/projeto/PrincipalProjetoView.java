package com.br.estimativadeprojetodesoftware.view.projeto;

import com.br.estimativadeprojetodesoftware.service.IconService;
import com.br.estimativadeprojetodesoftware.view.componentes.Estilos;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.JTree;

/**
 * Janela principal MDI: barra de ferramentas, árvore de navegação, área de
 * janelas internas e rodapé (usuário, banco e formato do log). Passive View.
 */
public final class PrincipalProjetoView extends JFrame {

    private final JDesktopPane desktop = new JDesktopPane();
    private JToolBar barraPrincipal;
    private JTree tree;
    private final JScrollPane treeScrollPane = new JScrollPane();
    private final JLabel lblRodape = new JLabel(" ", IconService.getIcon("usuario", 16), JLabel.LEADING);
    private final JLabel lblBanco = Estilos.aplicar(new JLabel(" "), Estilos.SUBTITULO);
    private final JLabel lblLog = Estilos.aplicar(new JLabel(" ", IconService.getIcon("log", 16), JLabel.LEADING), Estilos.SUBTITULO);

    public PrincipalProjetoView() {
        setTitle("Estimativa de Projetos de Software");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setIconImages(java.util.List.of(IconService.getIcon("logo", 32).getImage(), IconService.getIcon("logo", 16).getImage()));
        setSize(1280, 820);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);

        JLabel tituloNavegacao = Estilos.aplicar(new JLabel("NAVEGAÇÃO"), "font: -2 bold; foreground: $Estimativa.subtitulo");
        tituloNavegacao.setBorder(BorderFactory.createEmptyBorder(12, 14, 6, 10));
        treeScrollPane.setBorder(BorderFactory.createEmptyBorder());
        JPanel navegacao = new JPanel(new BorderLayout());
        navegacao.add(tituloNavegacao, BorderLayout.NORTH);
        navegacao.add(treeScrollPane, BorderLayout.CENTER);

        JSplitPane divisao = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, navegacao, desktop);
        divisao.setDividerLocation(270);
        divisao.setResizeWeight(0.0);
        divisao.setContinuousLayout(true);
        divisao.setBorder(BorderFactory.createEmptyBorder());
        add(divisao, BorderLayout.CENTER);

        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 4));
        rodape.add(lblRodape);
        rodape.add(lblBanco);
        rodape.add(lblLog);
        JPanel sul = new JPanel(new BorderLayout());
        sul.add(new JSeparator(), BorderLayout.NORTH);
        sul.add(rodape, BorderLayout.CENTER);
        add(sul, BorderLayout.SOUTH);
    }

    public void setMainComponents(JToolBar toolBar) {
        barraPrincipal = toolBar;
        JPanel norte = new JPanel(new BorderLayout());
        norte.add(toolBar, BorderLayout.CENTER);
        norte.add(new JSeparator(), BorderLayout.SOUTH);
        add(norte, BorderLayout.NORTH);
    }

    /** Exibe usuário, banco e formato do log no rodapé. */
    public void exibirRodape(String usuario, String banco, String formatoLog) {
        lblRodape.setText(usuario);
        lblBanco.setText(banco);
        lblLog.setText(formatoLog);
    }

    /** Rodapé com o nome do usuário logado. */
    public JLabel getLblRodape() {
        return lblRodape;
    }

    public JLabel getLblBanco() {
        return lblBanco;
    }

    public JLabel getLblLog() {
        return lblLog;
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
        tree.setBorder(BorderFactory.createEmptyBorder(0, 8, 8, 8));
        treeScrollPane.setViewportView(tree);
    }

    public JScrollPane getTreeScrollPane() {
        return treeScrollPane;
    }
}
