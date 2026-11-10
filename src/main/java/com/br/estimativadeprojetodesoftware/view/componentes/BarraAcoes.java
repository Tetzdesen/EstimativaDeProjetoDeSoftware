package com.br.estimativadeprojetodesoftware.view.componentes;

import com.br.estimativadeprojetodesoftware.service.IconService;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

/** Barra de ações no topo das janelas: botões com ícone e texto, separadores e área à direita. */
public class BarraAcoes extends JPanel {

    public BarraAcoes() {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setOpaque(false);
    }

    public JButton adicionar(String texto, String icone) {
        JButton b = new JButton(texto, icone == null ? null : IconService.getIcon(icone));
        b.setFocusable(true);
        if (icone == null) {
            // sem ícone o botão ficaria mais baixo que os vizinhos: um ícone vazio da mesma altura iguala
            b.setIcon(new javax.swing.ImageIcon(new java.awt.image.BufferedImage(1, IconService.TAMANHO_PADRAO,
                    java.awt.image.BufferedImage.TYPE_INT_ARGB)));
            b.setIconTextGap(0);
        }
        espacar();
        add(b);
        return b;
    }

    /** Botão da ação principal da tela, destacado com a cor do tema. */
    public JButton adicionarPrincipal(String texto, String icone) {
        JButton b = Estilos.aplicar(adicionar(texto, icone), Estilos.BOTAO_PRINCIPAL);
        if (icone != null) {
            b.setIcon(IconService.getIconContraste(icone, IconService.TAMANHO_PADRAO));
        }
        return b;
    }

    /** Botão de ação destrutiva (ex.: Excluir), com texto na cor de erro. */
    public JButton adicionarPerigo(String texto, String icone) {
        return Estilos.aplicar(adicionar(texto, icone), Estilos.BOTAO_PERIGO);
    }

    /**
     * Botão que abre um menu com as ações informadas (ex.: Exportar → PDF, CSV).
     * Cada item continua sendo habilitado e acionado pelo seu próprio estado.
     */
    public JButton adicionarMenu(String texto, String icone, JMenuItem... itens) {
        JButton b = adicionar(texto + "  ▾", icone);
        JPopupMenu menu = new JPopupMenu();
        for (JMenuItem item : itens) {
            menu.add(item);
        }
        b.addActionListener(e -> menu.show(b, 0, b.getHeight()));
        b.putClientProperty("menu", menu);
        // o botão só fica ativo se alguma ação do menu estiver disponível
        Runnable sincronizar = () -> b.setEnabled(java.util.Arrays.stream(itens).anyMatch(JMenuItem::isEnabled));
        for (JMenuItem item : itens) {
            item.addPropertyChangeListener("enabled", e -> sincronizar.run());
        }
        sincronizar.run();
        return b;
    }

    /** Item de menu com ícone, no mesmo padrão dos botões da barra. */
    public static JMenuItem item(String texto, String icone) {
        return new JMenuItem(texto, icone == null ? null : IconService.getIcon(icone));
    }

    public void separador() {
        espacar();
        JSeparator s = new JSeparator(SwingConstants.VERTICAL);
        s.setMaximumSize(new Dimension(8, 26));
        s.setPreferredSize(new Dimension(8, 26));
        add(s);
    }

    /** Tudo o que for adicionado depois fica alinhado à direita. */
    public void empurrarParaDireita() {
        add(Box.createHorizontalGlue());
    }

    public void adicionarComponente(Component c) {
        espacar();
        add(c);
    }

    private void espacar() {
        if (getComponentCount() > 0) {
            add(Box.createHorizontalStrut(6));
        }
    }
}
