package com.br.estimativadeprojetodesoftware.view.projeto;

import com.br.estimativadeprojetodesoftware.service.IconService;
import com.br.estimativadeprojetodesoftware.view.componentes.Estilos;
import com.br.estimativadeprojetodesoftware.view.componentes.JanelaInterna;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

/** Compartilhamento de projeto com usuários registrados. Passive View. */
public class CompartilharProjetoView extends JanelaInterna {

    private final JList<String> listaCandidatos = new JList<>();
    private final JList<String> listaDestinatarios = new JList<>();
    private final JButton btnCompartilhar;
    private final JButton btnFechar;

    public CompartilharProjetoView() {
        super("Compartilhar projeto", "Quem recebe o projeto só pode visualizá-lo (sem editar, excluir ou exportar).", "compartilhar");
        btnCompartilhar = getBarra().adicionarPrincipal("Compartilhar com o selecionado", "compartilhar");
        getBarra().empurrarParaDireita();
        btnFechar = getBarra().adicionar("Fechar", "fechar");
        listaCandidatos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        for (JList<String> l : java.util.List.of(listaCandidatos, listaDestinatarios)) {
            l.setCellRenderer(new RendererUsuario());
            l.setFixedCellHeight(30);
        }
        JPanel listas = new JPanel(new GridLayout(1, 2, 12, 0));
        listas.setOpaque(false);
        listas.add(cartao("Usuários registrados", listaCandidatos));
        listas.add(cartao("Já compartilhado com", listaDestinatarios));
        getConteudo().add(listas, BorderLayout.CENTER);
        setSize(700, 440);
    }

    private static JPanel cartao(String titulo, JList<String> lista) {
        JPanel p = new JPanel(new BorderLayout(0, 8));
        Estilos.aplicar(p, Estilos.CARTAO);
        p.add(Estilos.aplicar(new JLabel(titulo), Estilos.SECAO), BorderLayout.NORTH);
        JScrollPane rolagem = new JScrollPane(lista);
        rolagem.setBorder(BorderFactory.createEmptyBorder());
        p.add(rolagem, BorderLayout.CENTER);
        return p;
    }

    /** Linha da lista com ícone de usuário. */
    private static class RendererUsuario extends DefaultListCellRenderer {

        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean selected, boolean focus) {
            JLabel l = (JLabel) super.getListCellRendererComponent(list, value, index, selected, focus);
            l.setIcon(IconService.getIcon("usuario", 16));
            l.setIconTextGap(8);
            l.setBorder(BorderFactory.createEmptyBorder(0, 6, 0, 6));
            return l;
        }
    }

    public JList<String> getListaCandidatos() {
        return listaCandidatos;
    }

    public JList<String> getListaDestinatarios() {
        return listaDestinatarios;
    }

    public JButton getBtnCompartilhar() {
        return btnCompartilhar;
    }

    public JButton getBtnFechar() {
        return btnFechar;
    }
}
