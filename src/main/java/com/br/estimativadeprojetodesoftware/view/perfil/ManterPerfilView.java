package com.br.estimativadeprojetodesoftware.view.perfil;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class ManterPerfilView extends javax.swing.JInternalFrame {
    private Map<JLabel, JSpinner> campos;
    private List<String> funcionalidades;
    private JDesktopPane desktop;
    private JPanel painelPrincipal;

    public ManterPerfilView(JDesktopPane desktop) {
        setTitle("Manter Perfis");
        setResizable(true);
        setClosable(true);
        setMaximizable(true);
        setIconifiable(false);
        setSize(1000, 700);

        this.desktop = desktop;
        this.campos = new LinkedHashMap<>();
        this.funcionalidades = new ArrayList<>();

        setFuncionalidades();
        configuraCampos();

        painelPrincipal = new JPanel(new BorderLayout());
        add(painelPrincipal);

        painelPrincipal.add(configuraView(), BorderLayout.CENTER);

        revalidate();
        repaint();
    }

    private JScrollPane configuraView() {
        JPanel painelCampos = new JPanel();
        painelCampos.setLayout(new BoxLayout(painelCampos, BoxLayout.Y_AXIS));

        painelCampos.setBorder(new EmptyBorder(20, 20, 20, 20));
    
        for (Map.Entry<JLabel, JSpinner> entry : campos.entrySet()) {
            JPanel linha = new JPanel();
            linha.setLayout(new BorderLayout(5, 5));
    
            JLabel label = entry.getKey();
            JSpinner campo = entry.getValue();
    
            label.setPreferredSize(new Dimension(150, 25));
    
            campo.setPreferredSize(new Dimension(80, 25));
            campo.setMinimumSize(new Dimension(80, 25));
            campo.setMaximumSize(new Dimension(80, 25));
    
            linha.add(label, BorderLayout.WEST);
            linha.add(campo, BorderLayout.CENTER);
    
            painelCampos.add(linha);
        }
    
        JScrollPane scrollPane = new JScrollPane(painelCampos);
        return scrollPane;
    }
    

    private void configuraCampos() {
        for (String funcionalidade : funcionalidades) {
            JSpinner spinner = new JSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1)); // Mínimo 0, sem limite máximo
            campos.put(new JLabel(funcionalidade), spinner);
        }
    }

    public JDesktopPane getDesktop() {
        return desktop;
    }

    public void adicionarCampoView(JLabel label, JSpinner spinner) {
        
    }

    private void setFuncionalidades() {
        funcionalidades.add("Pequeno");
        funcionalidades.add("Médio");
        funcionalidades.add("Grande");
        funcionalidades.add("MVP");
        funcionalidades.add("Básico");
        funcionalidades.add("Profissional");
        funcionalidades.add("Cadastro por Email e Senha");
        funcionalidades.add("Cadastro Pelo Facebook");
    }
}
