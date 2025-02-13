package com.br.estimativadeprojetodesoftware.view;

import com.br.estimativadeprojetodesoftware.view.components.PerfilPanel;

import javax.swing.*;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 *
 * @author kauac
 */
public class PerfilProjetoView extends JInternalFrame {
    private JLabel lblTotalPerfis;
    private JButton btnNovoPerfil;
    private JPanel painelPerfis;

    public PerfilProjetoView() {
        setTitle("Visualizar Perfil");
        setClosable(true);
        setMaximizable(true);
        setIconifiable(false);
        setResizable(true);
        setSize(1000, 700);

        JPanel painelPrincipal = new JPanel(new BorderLayout());
        add(painelPrincipal);

        //painel do cabeçalho
        JPanel painelCabecalho = new JPanel();
        painelCabecalho.setLayout(new GridLayout(1, 2, 10, 15));
        painelCabecalho.setBorder(BorderFactory.createTitledBorder("Informações dos Perfis"));

        lblTotalPerfis = new JLabel("Total de Perfis: ");

        btnNovoPerfil = new JButton("Criar Perfil");
        btnNovoPerfil.setSize(5, 20);

        painelCabecalho.add(lblTotalPerfis);
        painelCabecalho.add(btnNovoPerfil);

        painelPrincipal.add(painelCabecalho, BorderLayout.NORTH);

        //painel dos perfis
        painelPerfis = new JPanel(new GridLayout(0, 3, 15, 15));
        JScrollPane scrollPerfis = new JScrollPane(painelPerfis);
        scrollPerfis.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        painelPrincipal.add(scrollPerfis, BorderLayout.CENTER);
    }

    public void atualizarCabecalho(int quantidade) {
        lblTotalPerfis.setText("Total de Perfis: " + quantidade);
    }

    public void adicionarPerfis(String nome, UUID id, LocalDateTime created_at, boolean status) {
        for (int i=0; i<30; i++) {
            PerfilPanel painelPerfil = new PerfilPanel(nome, id, created_at, status);
            painelPerfis.add(painelPerfil);
        }
        
        painelPerfis.revalidate();
    }
}
