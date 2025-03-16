package com.br.estimativadeprojetodesoftware.view.projeto;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

public class CustomTableRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        Object firstColumnValue = table.getValueAt(row, 0);

        if (firstColumnValue != null && isCategoria(firstColumnValue.toString())) {
            cell.setFont(new Font("Arial", Font.BOLD, 14));
            cell.setBackground(new Color(200, 200, 200)); 
            cell.setForeground(Color.BLACK);
        } else {
            cell.setFont(new Font("Arial", Font.PLAIN, 12));
            cell.setBackground(Color.WHITE);
            cell.setForeground(Color.BLACK);
        }

        return cell;
    }

    private boolean isCategoria(String text) {
        return text.equalsIgnoreCase("Tamanho do Aplicativo") ||
               text.equalsIgnoreCase("Nível da UI") ||
               text.equalsIgnoreCase("Funcionalidades") ||
               text.equalsIgnoreCase("Taxas Diárias") ||
               text.equalsIgnoreCase("Adicionais");
    }
}
