package com.br.estimativadeprojetodesoftware.view.componentes;

import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/** Cartão com um título pequeno e um valor em destaque (dashboard e resultado). */
public class CartaoIndicador extends JPanel {

    private final JLabel lblTitulo;
    private final JLabel lblValor;
    private final JLabel lblDetalhe;

    public CartaoIndicador(String titulo) {
        super(new BorderLayout(0, 4));
        Estilos.aplicar(this, Estilos.CARTAO);
        lblTitulo = Estilos.aplicar(new JLabel(titulo), Estilos.ROTULO_PEQUENO);
        lblValor = Estilos.aplicar(new JLabel("-"), Estilos.VALOR_GRANDE);
        lblDetalhe = Estilos.aplicar(new JLabel(" "), Estilos.ROTULO_PEQUENO);
        lblValor.setHorizontalAlignment(SwingConstants.LEADING);
        add(lblTitulo, BorderLayout.NORTH);
        add(lblValor, BorderLayout.CENTER);
        add(lblDetalhe, BorderLayout.SOUTH);
    }

    /** Dá ao cartão o destaque de indicador principal da tela. */
    public void destacar() {
        Estilos.aplicar(this, Estilos.CARTAO_DESTAQUE);
        Estilos.aplicar(lblValor, Estilos.VALOR_DESTAQUE);
    }

    public void setValor(String valor) {
        lblValor.setText(valor);
    }

    public void setDetalhe(String detalhe) {
        lblDetalhe.setText(detalhe == null || detalhe.isBlank() ? " " : detalhe);
    }

    public JLabel getLblValor() {
        return lblValor;
    }

    public JLabel getLblDetalhe() {
        return lblDetalhe;
    }
}
