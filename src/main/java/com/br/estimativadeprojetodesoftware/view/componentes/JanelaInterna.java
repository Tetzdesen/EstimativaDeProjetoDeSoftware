package com.br.estimativadeprojetodesoftware.view.componentes;

import com.br.estimativadeprojetodesoftware.service.IconService;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

/**
 * Base das janelas internas do MDI: cabeçalho (ícone, título, subtítulo),
 * barra de ações e área de conteúdo com margem padrão. Esc fecha a janela e
 * Ctrl+S aciona o botão Salvar registrado (só se estiver habilitado).
 */
public class JanelaInterna extends JInternalFrame {

    private final JLabel lblTitulo = Estilos.aplicar(new JLabel(), Estilos.TITULO);
    private final JLabel lblSubtitulo = Estilos.aplicar(new JLabel(), Estilos.SUBTITULO);
    private final JPanel extrasCabecalho = new JPanel(new BorderLayout());
    private final BarraAcoes barra = new BarraAcoes();
    private final JPanel conteudo = new JPanel(new BorderLayout(0, 10));
    private JButton botaoSalvar;

    public JanelaInterna(String titulo, String subtitulo, String icone) {
        super(titulo, true, true, true, true);
        setFrameIcon(IconService.getIcon(icone, 16));
        lblTitulo.setText(titulo);
        lblTitulo.setIcon(IconService.getIcon(icone, 22));
        lblTitulo.setIconTextGap(10);
        setSubtitulo(subtitulo);

        JPanel textos = new JPanel(new BorderLayout(0, 2));
        textos.setOpaque(false);
        textos.add(lblTitulo, BorderLayout.NORTH);
        textos.add(lblSubtitulo, BorderLayout.SOUTH);
        extrasCabecalho.setOpaque(false);

        JPanel cabecalho = new JPanel(new BorderLayout(12, 8));
        cabecalho.setBorder(BorderFactory.createEmptyBorder(10, 16, 4, 16));
        cabecalho.add(textos, BorderLayout.CENTER);
        cabecalho.add(extrasCabecalho, BorderLayout.EAST);
        cabecalho.add(barra, BorderLayout.SOUTH);

        conteudo.setBorder(BorderFactory.createEmptyBorder(6, 16, 12, 16));
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(cabecalho, BorderLayout.NORTH);
        getContentPane().add(conteudo, BorderLayout.CENTER);
        registrarAtalhos();
    }

    private void registrarAtalhos() {
        JComponent raiz = getRootPane();
        raiz.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "fechar");
        raiz.getActionMap().put("fechar", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doDefaultCloseAction();
            }
        });
        raiz.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK), "salvar");
        raiz.getActionMap().put("salvar", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (botaoSalvar != null && botaoSalvar.isEnabled() && botaoSalvar.isShowing()) {
                    botaoSalvar.doClick();
                }
            }
        });
    }

    /** Botão acionado por Ctrl+S; o atalho respeita o estado (botão desabilitado não salva). */
    public void setBotaoSalvar(JButton botao) {
        this.botaoSalvar = botao;
    }

    public void setSubtitulo(String subtitulo) {
        lblSubtitulo.setText(subtitulo == null || subtitulo.isBlank() ? " " : subtitulo);
    }

    public void setTituloCabecalho(String titulo) {
        lblTitulo.setText(titulo);
        setTitle(titulo);
    }

    /** Componente exibido à direita do título (ex.: selo de situação). */
    public void setExtraCabecalho(JComponent componente) {
        extrasCabecalho.removeAll();
        if (componente != null) {
            extrasCabecalho.add(componente, BorderLayout.CENTER);
        }
        extrasCabecalho.revalidate();
    }

    public BarraAcoes getBarra() {
        return barra;
    }

    public JPanel getConteudo() {
        return conteudo;
    }

    public JLabel getLblTitulo() {
        return lblTitulo;
    }

    public JLabel getLblSubtitulo() {
        return lblSubtitulo;
    }
}
