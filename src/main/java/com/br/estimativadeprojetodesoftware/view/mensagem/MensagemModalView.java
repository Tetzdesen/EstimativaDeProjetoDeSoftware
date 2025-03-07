
package com.br.estimativadeprojetodesoftware.view.mensagem;

import javax.swing.*;
import java.awt.*;

public class MensagemModalView extends JDialog {

    private JLabel lblMensagem;
    private JButton btnOk;
    private String mensagem;

    public MensagemModalView(Frame parent, String mensagem, boolean isErro) {
        super(parent, "Mensagem", true);
        this.mensagem = mensagem;

        setLayout(new BorderLayout());
        setSize(400, 200);
        setLocationRelativeTo(parent);

        lblMensagem = new JLabel(mensagem, SwingConstants.CENTER);
        lblMensagem.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblMensagem.setForeground(isErro ? Color.RED : Color.GREEN);
        add(lblMensagem, BorderLayout.CENTER);

        btnOk = new JButton("OK");
        btnOk.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnOk.addActionListener(e -> dispose());
        JPanel panel = new JPanel();
        panel.add(btnOk);
        add(panel, BorderLayout.SOUTH);
    }

    public void exibirModal() {
        setVisible(true);
    }
}
