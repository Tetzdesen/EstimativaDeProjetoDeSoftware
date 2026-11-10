package com.br.estimativadeprojetodesoftware.view.componentes;

import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import javax.swing.JPanel;
import javax.swing.Scrollable;

/** Painel para JScrollPane que acompanha a largura da janela e rola só na vertical. */
public class PainelRolavel extends JPanel implements Scrollable {

    private boolean preencherAltura;

    public PainelRolavel(LayoutManager layout) {
        super(layout);
    }

    /**
     * Com a janela mais alta que o conteúdo, estica o painel até o fim da área
     * visível (o componente do centro do BorderLayout ganha a sobra); com a
     * janela mais baixa, rola normalmente.
     */
    public void setPreencherAltura(boolean preencherAltura) {
        this.preencherAltura = preencherAltura;
    }

    @Override
    public Dimension getPreferredScrollableViewportSize() {
        return getPreferredSize();
    }

    @Override
    public int getScrollableUnitIncrement(Rectangle visivel, int orientacao, int direcao) {
        return 24;
    }

    @Override
    public int getScrollableBlockIncrement(Rectangle visivel, int orientacao, int direcao) {
        return visivel.height - 24;
    }

    @Override
    public boolean getScrollableTracksViewportWidth() {
        return true;
    }

    @Override
    public boolean getScrollableTracksViewportHeight() {
        return preencherAltura && getParent() instanceof javax.swing.JViewport area
                && area.getHeight() > getPreferredSize().height;
    }
}
