package com.br.estimativadeprojetodesoftware.view.components;

import javax.swing.*;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.util.UUID;
import java.awt.event.MouseAdapter;

public class PerfilPanel extends JPanel {
    private final String nome;
    private final UUID id;
    private boolean status;

    public PerfilPanel(String nome, UUID id, LocalDateTime created_at, boolean status) {
        this.nome = nome;
        this.id = id;
        this.status = status;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY, 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Título
        JLabel lblTitulo = new JLabel(nome, JLabel.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 14));
        
        // Detalhes
        JPanel painelDetalhes = new JPanel(new GridLayout(0, 1, 5, 5));
        painelDetalhes.add(new JLabel("ID: " + id));
        painelDetalhes.add(new JLabel("Data de Criação: " + created_at));
        
        // Status (exemplo com cor)
        JLabel lblStatus = new JLabel("Status: " + status, JLabel.RIGHT);
        lblStatus.setForeground(status ? Color.GREEN : Color.RED);

        // Adiciona componentes
        add(lblTitulo, BorderLayout.NORTH);
        add(painelDetalhes, BorderLayout.CENTER);
        add(lblStatus, BorderLayout.SOUTH);

        // Evento de clique
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                abrirDetalhesPerfil(id);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
            }
        });
    }

    private void abrirDetalhesPerfil(UUID id) {
        // Lógica para abrir detalhes (ex: dialog ou nova janela)
        JOptionPane.showMessageDialog(this, "Detalhes do Perfil " + id);
    }
}