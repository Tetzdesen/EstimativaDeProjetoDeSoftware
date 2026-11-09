package com.br.estimativadeprojetodesoftware.view.componentes;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

/**
 * Faixa compacta com os indicadores da estimativa: o primeiro em destaque, os
 * demais lado a lado, e um aviso opcional embaixo. Só exibe o texto recebido.
 */
public class BarraResumo extends JPanel {

    /** Um indicador da barra: título pequeno, valor e detalhe ao lado do valor. */
    public static class Indicador extends JPanel {

        private final JLabel lblValor;
        private final JLabel lblDetalhe;

        Indicador(String titulo, boolean destaque) {
            super(new BorderLayout(0, 0));
            setOpaque(false);
            JPanel linhaValor = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
            linhaValor.setOpaque(false);
            lblValor = Estilos.aplicar(new JLabel("—"), destaque ? Estilos.VALOR_RESUMO_DESTAQUE : Estilos.VALOR_RESUMO);
            lblDetalhe = Estilos.aplicar(new JLabel(" "), Estilos.ROTULO_PEQUENO);
            lblDetalhe.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 0));
            linhaValor.add(lblValor);
            linhaValor.add(lblDetalhe);
            add(Estilos.aplicar(new JLabel(titulo), Estilos.ROTULO_PEQUENO), BorderLayout.NORTH);
            add(linhaValor, BorderLayout.CENTER);
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

    private final Indicador[] indicadores;
    private final JLabel lblAviso = Estilos.aplicar(new JLabel(), Estilos.AVISO_ESTIMATIVA);

    /** @param titulos o primeiro título vira o indicador em destaque */
    public BarraResumo(String... titulos) {
        super(new BorderLayout(0, 6));
        Estilos.aplicar(this, Estilos.BARRA_RESUMO);
        JPanel linha = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        linha.setOpaque(false);
        indicadores = new Indicador[titulos.length];
        for (int i = 0; i < titulos.length; i++) {
            if (i > 0) {
                JSeparator separador = new JSeparator(SwingConstants.VERTICAL);
                separador.setPreferredSize(new java.awt.Dimension(1, 36));
                JPanel espaco = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
                espaco.setOpaque(false);
                espaco.add(separador);
                linha.add(espaco);
            }
            indicadores[i] = new Indicador(titulos[i], i == 0);
            linha.add(indicadores[i]);
        }
        lblAviso.setVisible(false);
        add(linha, BorderLayout.CENTER);
        add(lblAviso, BorderLayout.SOUTH);
    }

    public Indicador getIndicador(int indice) {
        return indicadores[indice];
    }

    /** Texto abaixo dos indicadores; null ou vazio esconde o aviso. */
    public void exibirAviso(String mensagem) {
        boolean visivel = mensagem != null && !mensagem.isBlank();
        lblAviso.setText(visivel ? "<html>" + mensagem + "</html>" : "");
        lblAviso.setVisible(visivel);
    }

    public JLabel getLblAviso() {
        return lblAviso;
    }
}
