package com.br.estimativadeprojetodesoftware.view.perfil;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ManterPerfilView extends JDialog {
    private boolean isCellEditable;
    private JDesktopPane desktop;
    private JTable tabelaDetalhes;
    private DefaultTableModel modeloTabela;
    private JTextField txtNome;
    private JLabel lblNome;
    private JCheckBox tglBackEnd;
    private JButton btnSalvar, btnEditar, btnExcluir, btnCancelar, btnRemoverCampo, btnAdicionarCampo;

    public ManterPerfilView(JDesktopPane desktop) {
        setTitle("Manter Perfis");
        setResizable(true);

        this.desktop = desktop;

        JPanel painelPrincipal = new JPanel(new BorderLayout());
        add(painelPrincipal);

        //adicionar input e lable nome
        JPanel painelCabecalho = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 15));

        lblNome = new JLabel("Nome: ");
        txtNome = new JTextField();
        txtNome.setPreferredSize(new Dimension(350, 25));
        tglBackEnd = new JCheckBox("Perfil de back end");

        painelCabecalho.add(lblNome);
        painelCabecalho.add(txtNome);
        painelCabecalho.add(tglBackEnd);

        painelPrincipal.add(painelCabecalho, BorderLayout.NORTH);
        
        //adicionar tabela
        JPanel painelTabela = new JPanel(new BorderLayout());
        
        modeloTabela = new DefaultTableModel(new Object[]{"Funcionalidade", "Número de Dias"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return isCellEditable;
            }
        };
        tabelaDetalhes = new JTable(modeloTabela);
        tabelaDetalhes.setFillsViewportHeight(true);
        tabelaDetalhes.getColumnModel().getColumn(0).setPreferredWidth(400);
        
        JScrollPane scrollTabela = new JScrollPane(tabelaDetalhes);
        painelTabela.add(scrollTabela, BorderLayout.CENTER);
        
        painelPrincipal.add(painelTabela, BorderLayout.CENTER);

        //adicionar botão salvar, remover e adicionar
        btnAdicionarCampo = new JButton("Adicionar Campo");
        btnRemoverCampo = new JButton("Remover Campo");
        btnSalvar = new JButton("Salvar Perfil");
        btnEditar = new JButton("Editar Perfil");
        btnExcluir = new JButton("Excluir Perfil");
        btnCancelar = new JButton("Cancelar");

        btnAdicionarCampo.setPreferredSize(new Dimension(160, 25));
        btnRemoverCampo.setPreferredSize(new Dimension(160, 25));
        btnSalvar.setPreferredSize(new Dimension(160, 25));
        btnEditar.setPreferredSize(new Dimension(160, 25));
        btnExcluir.setPreferredSize(new Dimension(160, 25));
        btnCancelar.setPreferredSize(new Dimension(160, 25));

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER));
        painelBotoes.add(btnAdicionarCampo);
        painelBotoes.add(btnRemoverCampo);
        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnEditar);
        painelBotoes.add(btnExcluir);
        painelBotoes.add(btnCancelar);

        painelPrincipal.add(painelBotoes, BorderLayout.SOUTH);
    }

    public JDesktopPane getDesktop() {
        return desktop;
    }

    public void atualizarTabela(Object[][] dados) {
        DefaultTableModel modelo = (DefaultTableModel) modeloTabela;
        modelo.setRowCount(0);

        for (Object[] linha : dados) {
            modeloTabela.addRow(linha);
        }
    }

    public void setCellEditable(boolean isCellEditable) {
        this.isCellEditable = isCellEditable;
    }

    public JButton getBtnSalvar() {
        return btnSalvar;
    }

    public JButton getBtnRemoverCampo() {
        return btnRemoverCampo;
    }

    public JButton getBtnAdicionarCampo() {
        return btnAdicionarCampo;
    }

    public DefaultTableModel getModeloTabela() {
        return modeloTabela;
    }

    public JTable getTabelaDetalhes() {
        return tabelaDetalhes;
    }

    public JTextField getTxtNome() {
        return txtNome;
    }

    public JLabel getLblNome() {
        return lblNome;
    }

    public JCheckBox getTglBackEnd() {
        return tglBackEnd;
    }
    
    public JButton getBtnEditar() {
        return btnEditar;
    }

    public JButton getBtnExcluir() {
        return btnExcluir;
    }

    public JButton getBtnCancelar() {
        return btnCancelar;
    }
}
