package com.br.estimativadeprojetodesoftware.view.perfil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 *
 * @author kauac
 */
public class PerfilProjetoView extends JInternalFrame {
    private JTable tabelaDetalhes;
    private JButton btnNovoPerfil, btnVisualizar;
    private DefaultTableModel modeloTabela;
    private JDesktopPane desktop;

    public PerfilProjetoView(JDesktopPane desktop) {
        setTitle("Visualizar Perfis");
        setClosable(true);
        setMaximizable(true);
        setIconifiable(false);
        setResizable(true);
        setSize(1000, 700);

        this.desktop = desktop;

        JPanel painelPrincipal = new JPanel(new BorderLayout());
        add(painelPrincipal);

        JPanel painelNorte = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnNovoPerfil = new JButton("Novo Perfil");
        btnNovoPerfil.setSize(120, 20);
        painelNorte.add(btnNovoPerfil);

        btnVisualizar = new JButton("Visualizar");
        btnVisualizar.setSize(120, 20);
        painelNorte.add(btnVisualizar);

        painelPrincipal.add(painelNorte, BorderLayout.NORTH);

        JPanel painelTabela = new JPanel(new BorderLayout());

        modeloTabela = new DefaultTableModel(new Object[]{"id", "Nome"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelaDetalhes = new JTable(modeloTabela);
        tabelaDetalhes.setFillsViewportHeight(true);

        JScrollPane scrollTabela = new JScrollPane(tabelaDetalhes);
        painelTabela.add(scrollTabela, BorderLayout.CENTER);

        painelPrincipal.add(painelTabela, BorderLayout.CENTER);

    }

    public void atualizarTabela(Object[] dados) {
        modeloTabela.addRow(dados);
    }

    public JTable getTablePerfis() {
        return tabelaDetalhes;
    }

    public DefaultTableModel getModeloTabela() {
        return modeloTabela;
    }

    public JButton getBtnVisualizar() {
        return btnVisualizar;
    }

    public JButton getBtnNovoPerfil() {
        return btnNovoPerfil;
    }

    public JDesktopPane getDesktop() {
        return desktop;
    }
}
