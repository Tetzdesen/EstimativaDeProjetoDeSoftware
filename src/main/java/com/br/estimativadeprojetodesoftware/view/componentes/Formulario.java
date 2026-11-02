package com.br.estimativadeprojetodesoftware.view.componentes;

import com.formdev.flatlaf.FlatClientProperties;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Formulário em grade: rótulo à esquerda, campo à direita e, abaixo do campo,
 * a mensagem de erro (oculta até {@link #mostrarErro}). Só exibição: quem
 * decide o que é erro é o presenter.
 */
public class Formulario extends JPanel {

    private final Map<JComponent, JLabel> erros = new LinkedHashMap<>();
    private final Map<JComponent, JLabel> rotulos = new LinkedHashMap<>();
    private int linha = 0;

    public Formulario() {
        super(new GridBagLayout());
        setOpaque(false);
    }

    /** Rótulo + campo; devolve o campo para encadear. */
    public <T extends JComponent> T adicionarCampo(String rotulo, T campo) {
        JLabel lbl = new JLabel(rotulo);
        lbl.setLabelFor(campo);
        GridBagConstraints c = base();
        c.gridx = 0;
        c.gridy = linha;
        c.anchor = GridBagConstraints.LINE_END;
        c.insets = new Insets(linha == 0 ? 0 : 10, 0, 0, 12);
        add(lbl, c);

        c = base();
        c.gridx = 1;
        c.gridy = linha;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(linha == 0 ? 0 : 10, 0, 0, 0);
        add(campo, c);

        JLabel erro = Estilos.aplicar(new JLabel(" "), Estilos.ERRO_CAMPO);
        erro.setVisible(false);
        c = base();
        c.gridx = 1;
        c.gridy = linha + 1;
        c.anchor = GridBagConstraints.LINE_START;
        c.insets = new Insets(3, 2, 0, 0);
        add(erro, c);

        erros.put(campo, erro);
        rotulos.put(campo, lbl);
        linha += 2;
        return campo;
    }

    /** Componente ocupando as duas colunas (ex.: botões ou texto de ajuda). */
    public <T extends JComponent> T adicionarLinha(T componente) {
        GridBagConstraints c = base();
        c.gridx = 0;
        c.gridy = linha++;
        c.gridwidth = 2;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(14, 0, 0, 0);
        add(componente, c);
        return componente;
    }

    /** Título de seção dentro do formulário. */
    public JLabel adicionarSecao(String titulo) {
        JLabel l = Estilos.aplicar(new JLabel(titulo), Estilos.SECAO);
        l.setBorder(BorderFactory.createEmptyBorder(linha == 0 ? 0 : 10, 0, 0, 0));
        return adicionarLinha(l);
    }

    /** Destaca o campo como erro e mostra a mensagem abaixo dele. */
    public void mostrarErro(JComponent campo, String mensagem) {
        campo.putClientProperty(FlatClientProperties.OUTLINE, FlatClientProperties.OUTLINE_ERROR);
        JLabel erro = erros.get(campo);
        if (erro != null) {
            erro.putClientProperty("mensagem", mensagem);
            erro.setText(mensagem != null && mensagem.contains("\n")
                    ? "<html><body style='width:" + Math.max(170, (int) (campo.getWidth() * 0.72)) + "px'>"
                    + mensagem.replace("&", "&amp;").replace("<", "&lt;").replace("\n", "<br>") + "</body></html>"
                    : mensagem);
            erro.setVisible(true);
        }
        revalidate();
        repaint();
    }

    public void limparErro(JComponent campo) {
        campo.putClientProperty(FlatClientProperties.OUTLINE, null);
        JLabel erro = erros.get(campo);
        if (erro != null) {
            erro.setText(" ");
            erro.setVisible(false);
        }
        revalidate();
        repaint();
    }

    public void limparErros() {
        for (JComponent campo : erros.keySet()) {
            limparErro(campo);
        }
    }

    public boolean temErro(JComponent campo) {
        JLabel erro = erros.get(campo);
        return erro != null && erro.isVisible();
    }

    public String getMensagemErro(JComponent campo) {
        JLabel erro = erros.get(campo);
        return erro == null || !erro.isVisible() ? null : (String) erro.getClientProperty("mensagem");
    }

    public JLabel getRotulo(JComponent campo) {
        return rotulos.get(campo);
    }

    /** Texto de exemplo exibido no campo vazio. */
    public static <T extends JTextField> T placeholder(T campo, String texto) {
        campo.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, texto);
        return campo;
    }

    private static GridBagConstraints base() {
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.LINE_START;
        return c;
    }
}
