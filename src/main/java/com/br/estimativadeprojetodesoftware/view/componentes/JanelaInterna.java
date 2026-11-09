package com.br.estimativadeprojetodesoftware.view.componentes;

import com.br.estimativadeprojetodesoftware.service.IconService;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
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
import javax.swing.JProgressBar;
import javax.swing.KeyStroke;

/**
 * Base das janelas internas do MDI: cabeçalho (ícone, título, subtítulo),
 * barra de ações e área de conteúdo com margem padrão. Esc fecha a janela e
 * Ctrl+S aciona o botão Salvar registrado (só se estiver habilitado).
 * Enquanto ocupada, mostra o progresso no cabeçalho e não aceita cliques nem
 * atalhos.
 */
public class JanelaInterna extends JInternalFrame implements Ocupavel {

    private final JLabel lblTitulo = Estilos.aplicar(new JLabel(), Estilos.TITULO);
    private final JLabel lblSubtitulo = Estilos.aplicar(new JLabel(), Estilos.SUBTITULO);
    private final JPanel extrasCabecalho = new JPanel(new BorderLayout());
    private final BarraAcoes barra = new BarraAcoes();
    private final JPanel conteudo = new JPanel(new BorderLayout(0, 10));
    private JButton botaoSalvar;
    private final JProgressBar progresso = new JProgressBar();
    private final JLabel lblProgresso = Estilos.aplicar(new JLabel(), Estilos.ROTULO_PEQUENO);
    private final JPanel painelProgresso = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
    private final JPanel bloqueio = new JPanel();
    private boolean ocupado;

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

        progresso.setIndeterminate(true);
        progresso.setPreferredSize(new java.awt.Dimension(90, 4));
        painelProgresso.setOpaque(false);
        painelProgresso.add(lblProgresso);
        painelProgresso.add(progresso);
        painelProgresso.setVisible(false);
        JPanel direita = new JPanel(new BorderLayout(0, 6));
        direita.setOpaque(false);
        direita.add(extrasCabecalho, BorderLayout.NORTH);
        direita.add(painelProgresso, BorderLayout.SOUTH);
        // camada transparente que absorve cliques enquanto a janela trabalha
        bloqueio.setOpaque(false);
        bloqueio.addMouseListener(new MouseAdapter() {
        });
        bloqueio.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        setGlassPane(bloqueio);

        JPanel cabecalho = new JPanel(new BorderLayout(12, 8));
        cabecalho.setBorder(BorderFactory.createEmptyBorder(10, 16, 4, 16));
        cabecalho.add(textos, BorderLayout.CENTER);
        cabecalho.add(direita, BorderLayout.EAST);
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
                if (!ocupado) {
                    doDefaultCloseAction();
                }
            }
        });
        raiz.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK), "salvar");
        raiz.getActionMap().put("salvar", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!ocupado && botaoSalvar != null && botaoSalvar.isEnabled() && botaoSalvar.isShowing()) {
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

    @Override
    public void setOcupado(boolean ocupado, String mensagem) {
        this.ocupado = ocupado;
        lblProgresso.setText(ocupado && mensagem != null ? mensagem : "");
        painelProgresso.setVisible(ocupado);
        bloqueio.setVisible(ocupado);
        if (ocupado) {
            bloqueio.requestFocusInWindow();
        }
    }

    @Override
    public boolean isOcupado() {
        return ocupado;
    }

    @Override
    public boolean isFechada() {
        return isClosed();
    }

    public JLabel getLblProgresso() {
        return lblProgresso;
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
