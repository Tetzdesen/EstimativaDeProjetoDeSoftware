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

        private final JLabel lblTitulo;
        private final JLabel lblValor;
        private final JLabel lblDetalhe;
        private final JPanel linhaValor = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        private boolean compacto;

        Indicador(String titulo, boolean destaque) {
            super(new BorderLayout(0, 0));
            setOpaque(false);
            linhaValor.setOpaque(false);
            lblValor = Estilos.aplicar(new JLabel("—"), destaque ? Estilos.VALOR_RESUMO_DESTAQUE : Estilos.VALOR_RESUMO);
            lblDetalhe = Estilos.aplicar(new JLabel(" "), Estilos.ROTULO_PEQUENO);
            lblDetalhe.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 0));
            linhaValor.add(lblValor);
            linhaValor.add(lblDetalhe);
            lblTitulo = Estilos.aplicar(new JLabel(titulo), Estilos.ROTULO_PEQUENO);
            add(lblTitulo, BorderLayout.NORTH);
            add(linhaValor, BorderLayout.CENTER);
        }

        /** Largura com o detalhe ao lado do valor (o modo normal), seja qual for o modo atual. */
        int larguraNormal() {
            int valorEDetalhe = lblValor.getPreferredSize().width + lblDetalhe.getPreferredSize().width
                    + (compacto ? 8 : 0); // no modo compacto o detalhe fica sem a margem esquerda
            return Math.max(lblTitulo.getPreferredSize().width, valorEDetalhe);
        }

        /** Compacto: o detalhe desce para baixo do valor, e o indicador fica mais estreito. */
        void setCompacto(boolean compacto) {
            if (this.compacto == compacto) {
                return;
            }
            this.compacto = compacto;
            if (compacto) {
                linhaValor.remove(lblDetalhe);
                lblDetalhe.setBorder(BorderFactory.createEmptyBorder());
                add(lblDetalhe, BorderLayout.SOUTH);
            } else {
                remove(lblDetalhe);
                lblDetalhe.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 0));
                linhaValor.add(lblDetalhe);
            }
        }

        public boolean isCompacto() {
            return compacto;
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

    private static final int ESPACO_SEPARADOR = 41; // 20 + separador de 1 px + 20
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

    /**
     * Numa janela estreita os indicadores não cabem lado a lado com o detalhe
     * ao lado do valor, e o último ficava escondido: aí todos passam para o
     * modo compacto, com o detalhe embaixo (a barra fica um pouco mais alta).
     */
    private void ajustarModo(int largura) {
        if (largura <= 0) {
            return;
        }
        int necessaria = getInsets().left + getInsets().right + ESPACO_SEPARADOR * (indicadores.length - 1);
        for (Indicador i : indicadores) {
            necessaria += i.larguraNormal();
        }
        boolean compacto = necessaria > largura;
        if (compacto != indicadores[0].isCompacto()) {
            for (Indicador i : indicadores) {
                i.setCompacto(compacto);
            }
        }
    }

    /**
     * A altura depende da largura: o pai pergunta o tamanho antes de dispor a
     * barra, então o modo é decidido pela largura que ele vai dar (a do pai,
     * já que a barra ocupa a faixa inteira).
     */
    @Override
    public java.awt.Dimension getPreferredSize() {
        java.awt.Container pai = getParent();
        if (pai != null && pai.getWidth() > 0) {
            java.awt.Insets in = pai.getInsets();
            ajustarModo(pai.getWidth() - in.left - in.right);
        }
        return super.getPreferredSize();
    }

    @Override
    public void doLayout() {
        boolean antes = indicadores[0].isCompacto();
        ajustarModo(getWidth());
        if (antes != indicadores[0].isCompacto()) {
            javax.swing.SwingUtilities.invokeLater(this::revalidate); // mudou a altura depois de o pai decidir
        }
        super.doLayout();
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
