package com.br.estimativadeprojetodesoftware.view.componentes;

import com.br.estimativadeprojetodesoftware.service.IconService;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
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
        espacar();
        add(b);
        return b;
    }

    /** Botão da ação principal da tela, destacado com a cor do tema. */
    public JButton adicionarPrincipal(String texto, String icone) {
        JButton b = Estilos.aplicar(adicionar(texto, null), Estilos.BOTAO_PRINCIPAL);
        if (icone != null) {
            b.setIcon(IconService.getIconContraste(icone, IconService.TAMANHO_PADRAO));
        }
        return b;
    }

    /** Botão de ação destrutiva (ex.: Excluir), com texto na cor de erro. */
    public JButton adicionarPerigo(String texto, String icone) {
        return Estilos.aplicar(adicionar(texto, icone), Estilos.BOTAO_PERIGO);
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
