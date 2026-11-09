package com.br.estimativadeprojetodesoftware.view.componentes;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

/**
 * Exibe um {@link ItemComContador}: rótulo à esquerda e quantidade à direita,
 * em negrito quando maior que zero. Itens principais ficam em negrito e
 * subitens, recuados.
 */
public class RenderizadorComContador implements ListCellRenderer<ItemComContador> {

    private static final int RECUO_SUBITEM = 18;

    private final DefaultListCellRenderer base = new DefaultListCellRenderer();
    private final JPanel painel = new JPanel(new BorderLayout(8, 0));
    private final JLabel lblRotulo = new JLabel();
    private final JLabel lblQuantidade = new JLabel();

    public RenderizadorComContador() {
        painel.add(lblRotulo, BorderLayout.CENTER);
        painel.add(lblQuantidade, BorderLayout.EAST);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends ItemComContador> lista, ItemComContador item,
            int indice, boolean selecionado, boolean comFoco) {
        Component ref = base.getListCellRendererComponent(lista, "", indice, selecionado, comFoco);
        boolean subitem = item != null && item.subitem();
        boolean temSubitens = item != null && !subitem && indice + 1 < lista.getModel().getSize()
                && lista.getModel().getElementAt(indice + 1).subitem();
        painel.setBorder(BorderFactory.createEmptyBorder(subitem ? 3 : 5, 8 + (subitem ? RECUO_SUBITEM : 0), subitem ? 3 : 5, 8));
        painel.setBackground(ref.getBackground());
        painel.setOpaque(selecionado);
        lblRotulo.setForeground(ref.getForeground());
        lblQuantidade.setForeground(ref.getForeground());
        Font fonte = lista.getFont();
        lblRotulo.setFont(subitem ? fonte : fonte.deriveFont(Font.BOLD));
        Integer quantidade = item == null ? null : item.quantidade();
        lblQuantidade.setFont(fonte.deriveFont(quantidade != null && quantidade > 0 ? Font.BOLD : Font.PLAIN));
        lblRotulo.setText(item == null ? "" : item.rotulo());
        lblQuantidade.setText(quantidade == null ? "" : String.valueOf(quantidade));
        painel.setToolTipText(item == null ? null : item.rotulo());
        if (temSubitens) {
            // o item com subitens (Funcionalidades) não fica colado no próximo grupo
            painel.setBorder(BorderFactory.createEmptyBorder(5, 8, 3, 8));
        }
        return painel;
    }
}
