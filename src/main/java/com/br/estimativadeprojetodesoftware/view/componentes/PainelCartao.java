package com.br.estimativadeprojetodesoftware.view.componentes;

import com.br.estimativadeprojetodesoftware.service.IconService;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * Fundo com um cartão centralizado (telas de entrada: boas-vindas, login e
 * cadastro): logo, título, subtítulo e o conteúdo abaixo.
 */
public class PainelCartao extends JPanel {

    private final int largura;
    private final JPanel cartao = new JPanel(new BorderLayout(0, 18)) {
        @Override
        public Dimension getPreferredSize() {
            return new Dimension(largura, super.getPreferredSize().height);
        }

        @Override
        public Dimension getMinimumSize() {
            return getPreferredSize();
        }
    };
    private final JPanel corpo = new JPanel();
    private final JLabel lblTitulo = Estilos.aplicar(new JLabel("", SwingConstants.CENTER), "font: +8 bold; foreground: $Estimativa.titulo");
    private final JLabel lblSubtitulo = Estilos.aplicar(new JLabel("", SwingConstants.CENTER), Estilos.SUBTITULO);

    public PainelCartao(String titulo, String subtitulo, int largura) {
        super(new GridBagLayout());
        this.largura = largura;
        Estilos.aplicar(this, "background: $Desktop.background");
        Estilos.aplicar(cartao, "arc: 16; background: $Estimativa.cartao; border: 28,32,28,32,$Estimativa.cartaoBorda,1,16");

        JLabel logo = new JLabel(IconService.getIcon("logo", 44), SwingConstants.CENTER);
        lblTitulo.setText(titulo);
        lblSubtitulo.setText("<html><div style='text-align:center'>" + subtitulo + "</div></html>");
        JPanel cabecalho = new JPanel(new BorderLayout(0, 6));
        cabecalho.setOpaque(false);
        cabecalho.add(logo, BorderLayout.NORTH);
        cabecalho.add(lblTitulo, BorderLayout.CENTER);
        cabecalho.add(lblSubtitulo, BorderLayout.SOUTH);

        corpo.setLayout(new BoxLayout(corpo, BoxLayout.Y_AXIS));
        corpo.setOpaque(false);
        cartao.add(cabecalho, BorderLayout.NORTH);
        cartao.add(corpo, BorderLayout.CENTER);
        add(cartao);
        setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
    }

    /** Acrescenta um componente ao corpo do cartão, ocupando toda a largura. */
    public <T extends JComponent> T adicionar(T componente) {
        componente.setAlignmentX(LEFT_ALIGNMENT);
        if (corpo.getComponentCount() > 0) {
            JPanel espaco = new JPanel();
            espaco.setOpaque(false);
            espaco.setMaximumSize(new Dimension(Integer.MAX_VALUE, 12));
            espaco.setAlignmentX(LEFT_ALIGNMENT);
            corpo.add(espaco);
        }
        corpo.add(componente);
        return componente;
    }

    /** Botão largo, ocupando toda a largura do cartão. */
    public static JButton botaoLargo(JButton botao) {
        botao.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        botao.setPreferredSize(new Dimension(10, 40));
        return botao;
    }

    /** Botão com aparência de link. */
    public static JButton link(String texto) {
        JButton b = new JButton(texto);
        b.putClientProperty("JButton.buttonType", "borderless");
        Estilos.aplicar(b, "foreground: $Estimativa.destaque; font: bold");
        return b;
    }
}
