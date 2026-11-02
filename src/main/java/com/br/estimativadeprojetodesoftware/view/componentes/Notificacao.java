package com.br.estimativadeprojetodesoftware.view.componentes;

import com.br.estimativadeprojetodesoftware.service.IconService;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.KeyboardFocusManager;
import java.awt.Window;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.RootPaneContainer;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 * Aviso não modal no canto inferior direito da janela ativa, que some sozinho.
 * Usado para mensagens de sucesso, sem bloquear a janela.
 */
public final class Notificacao extends JPanel {

    public static final int DURACAO_MS = 3000;

    public enum Tipo {
        SUCESSO("sucesso", "Estimativa.sucesso"),
        INFO("info", "Estimativa.destaque"),
        AVISO("aviso", "Estimativa.aviso");

        private final String icone;
        private final String cor;

        Tipo(String icone, String cor) {
            this.icone = icone;
            this.cor = cor;
        }
    }

    private final JLabel texto;

    private Notificacao(String mensagem, Tipo tipo) {
        super(new BorderLayout());
        Estilos.aplicar(this, "arc: 14; background: $Estimativa.cartao; border: 10,14,10,16,$" + tipo.cor + ",1,14");
        texto = new JLabel("<html><body style='width:260px'>" + escapar(mensagem) + "</body></html>",
                IconService.getIcon(tipo.icone, 22), JLabel.LEADING);
        texto.setIconTextGap(10);
        add(texto, BorderLayout.CENTER);
    }

    /** Mostra a notificação na janela ativa; devolve o componente (ou null se não houver janela). */
    public static Notificacao mostrar(String mensagem, Tipo tipo) {
        JRootPane raiz = raizAtiva();
        if (raiz == null) {
            return null;
        }
        Notificacao n = new Notificacao(mensagem, tipo);
        JLayeredPane camada = raiz.getLayeredPane();
        Dimension d = n.getPreferredSize();
        int margem = 18;
        int deslocamento = 0;
        for (var c : camada.getComponentsInLayer(JLayeredPane.POPUP_LAYER)) {
            if (c instanceof Notificacao) {
                deslocamento += c.getHeight() + 8;
            }
        }
        n.setBounds(camada.getWidth() - d.width - margem, camada.getHeight() - d.height - margem - deslocamento, d.width, d.height);
        camada.add(n, JLayeredPane.POPUP_LAYER);
        camada.revalidate();
        camada.repaint();
        Timer t = new Timer(DURACAO_MS, e -> {
            camada.remove(n);
            camada.repaint();
        });
        t.setRepeats(false);
        t.start();
        return n;
    }

    public String getTexto() {
        return texto.getText();
    }

    private static JRootPane raizAtiva() {
        Window ativa = KeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow();
        if (ativa instanceof RootPaneContainer r && ativa.isShowing()) {
            return r.getRootPane();
        }
        Window escolhida = null;
        for (Window w : Window.getWindows()) {
            if (w.isShowing() && w instanceof RootPaneContainer) {
                escolhida = w;
            }
        }
        return escolhida == null ? null : SwingUtilities.getRootPane(((RootPaneContainer) escolhida).getContentPane());
    }

    private static String escapar(String s) {
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\n", "<br>");
    }
}
