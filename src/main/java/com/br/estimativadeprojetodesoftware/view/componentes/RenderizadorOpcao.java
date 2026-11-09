package com.br.estimativadeprojetodesoftware.view.componentes;

import java.awt.Component;
import java.awt.Graphics;
import javax.swing.DefaultCellEditor;
import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;

/**
 * Coluna booleana desenhada como botão de opção (escolha única). Por baixo
 * continua sendo uma caixa de marcação: clicar na opção marcada a desmarca,
 * como na tabela de funcionalidades.
 */
public final class RenderizadorOpcao implements TableCellRenderer {

    /** Ícone de botão de opção do tema atual, lido a cada pintura para acompanhar a troca de tema. */
    private static final Icon ICONE_OPCAO = new Icon() {
        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            UIManager.getIcon("RadioButton.icon").paintIcon(c, g, x, y);
        }

        @Override
        public int getIconWidth() {
            return UIManager.getIcon("RadioButton.icon").getIconWidth();
        }

        @Override
        public int getIconHeight() {
            return UIManager.getIcon("RadioButton.icon").getIconHeight();
        }
    };

    private final JCheckBox caixa = opcao();

    private static JCheckBox opcao() {
        JCheckBox c = new JCheckBox();
        c.setIcon(ICONE_OPCAO);
        c.setHorizontalAlignment(SwingConstants.CENTER);
        c.setBorderPainted(false);
        return c;
    }

    /** Usa o botão de opção como renderizador e editor da coluna (índice da view). */
    public static void aplicar(JTable tabela, int colunaView) {
        tabela.getColumnModel().getColumn(colunaView).setCellRenderer(new RenderizadorOpcao());
        tabela.getColumnModel().getColumn(colunaView).setCellEditor(new DefaultCellEditor(opcao()));
    }

    @Override
    public Component getTableCellRendererComponent(JTable tabela, Object valor, boolean selecionado, boolean foco,
            int linha, int coluna) {
        caixa.setSelected(Boolean.TRUE.equals(valor));
        caixa.setEnabled(tabela.isEnabled());
        caixa.setOpaque(true);
        caixa.setBackground(selecionado ? tabela.getSelectionBackground() : tabela.getBackground());
        caixa.setForeground(selecionado ? tabela.getSelectionForeground() : tabela.getForeground());
        return caixa;
    }
}
