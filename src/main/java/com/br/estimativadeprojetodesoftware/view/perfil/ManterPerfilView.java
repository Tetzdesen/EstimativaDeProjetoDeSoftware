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
    private JLabel lblNome, lblPequeno, lblMedio, lblGrande, lblMvp, lblBasico, lblProfissional;
    private JSpinner jspPequeno, jspMedio, jspGrande, jspMvp, jspBasico, jspProfissional;
    private JCheckBox tglBackEnd;
    private JButton btnSalvar, btnEditar, btnExcluir, btnCancelar, btnRemoverCampo, btnAdicionarCampo;

    public ManterPerfilView(JDesktopPane desktop) {
        setTitle("Manter Perfis");
        setResizable(true);

        this.desktop = desktop;

        JPanel painelPrincipal = new JPanel(new BorderLayout());
        add(painelPrincipal);

        // --PAINEL CABEÇALHO--
        JPanel painelCabecalho = new JPanel(new BorderLayout());

        //adicionar input e lable nome
        JPanel painelInfo = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 15));
        lblNome = new JLabel("Nome: ");
        txtNome = new JTextField();
        txtNome.setPreferredSize(new Dimension(350, 25));
        tglBackEnd = new JCheckBox("Perfil de back end");

        painelInfo.add(lblNome);
        painelInfo.add(txtNome);
        painelInfo.add(tglBackEnd);

        painelCabecalho.add(painelInfo, BorderLayout.NORTH);


        JPanel painelCamposObrigatorios = new JPanel();
        painelCamposObrigatorios.setLayout(new BoxLayout(painelCamposObrigatorios, BoxLayout.Y_AXIS));

        /********  PAINEL DE TAMANHO DO APP */

        //adicionar campos obrigatórios
        JPanel painelTamanhoApp = new JPanel();
        painelTamanhoApp.setLayout(new GridLayout(1, 3, 3, 15));
        painelTamanhoApp.setBorder(BorderFactory.createTitledBorder("Tamanho do App (número de dias): "));

        //label e input
        JPanel painelPequeno = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 15));
        lblPequeno = new JLabel("Pequeno: ");
        jspPequeno = new JSpinner();

        painelPequeno.add(lblPequeno);
        painelPequeno.add(jspPequeno);

        painelTamanhoApp.add(painelPequeno);

        //painelCabecalho.add(painelTamanhoApp, BorderLayout.CENTER);

        JPanel painelMedio = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 15));
        lblMedio = new JLabel("Médio: ");
        jspMedio = new JSpinner();

        painelMedio.add(lblMedio);
        painelMedio.add(jspMedio);

        painelTamanhoApp.add(painelMedio);

        //painelCabecalho.add(painelTamanhoApp, BorderLayout.CENTER);

        JPanel painelGrande = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 15));
        lblGrande = new JLabel("Grande: ");
        jspGrande = new JSpinner();

        painelGrande.add(lblGrande);
        painelGrande.add(jspGrande);

        painelTamanhoApp.add(painelGrande);

        painelCamposObrigatorios.add(painelTamanhoApp, BorderLayout.CENTER);





        /********  PAINEL DE NIVEL DE UI */

        JPanel painelNivelUI = new JPanel();
        painelNivelUI.setLayout(new GridLayout(1, 3, 3, 15));
        painelNivelUI.setBorder(BorderFactory.createTitledBorder("Nível de UI (Percentual): "));

        //label e input
        JPanel painelMVP = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 15));
        lblMvp = new JLabel("MVP: ");
        jspMvp = new JSpinner();

        painelMVP.add(lblMvp);
        painelMVP.add(jspMvp);
        painelMVP.add(new JLabel("%"));

        painelNivelUI.add(painelMVP);

        //painelCabecalho.add(painelTamanhoApp, BorderLayout.CENTER);

        JPanel painelBasico = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 15));
        lblBasico = new JLabel("Básico: ");
        jspBasico = new JSpinner();

        painelBasico.add(lblBasico);
        painelBasico.add(jspBasico);
        painelBasico.add(new JLabel("%"));

        painelNivelUI.add(painelBasico);

        //painelCabecalho.add(painelTamanhoApp, BorderLayout.CENTER);

        JPanel painelProfissional = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 15));
        lblProfissional = new JLabel("Profissional: ");
        jspProfissional = new JSpinner();

        painelProfissional.add(lblProfissional);
        painelProfissional.add(jspProfissional);
        painelProfissional.add(new JLabel("%"));

        painelNivelUI.add(painelProfissional);

        painelCamposObrigatorios.add(painelNivelUI, BorderLayout.CENTER);





        painelCabecalho.add(painelCamposObrigatorios, BorderLayout.CENTER);



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
        btnAdicionarCampo = new JButton("Adicionar");
        btnRemoverCampo = new JButton("Remover");
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
