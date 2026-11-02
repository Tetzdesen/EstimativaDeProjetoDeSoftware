package com.br.estimativadeprojetodesoftware.presenter.helpers;

import java.util.function.Consumer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

/** Apoio dos presenters para o retorno visual dos formulários. */
public final class ValidacaoVisual {

    private ValidacaoVisual() {
    }

    /** Executa a limpeza do erro sempre que o texto do campo mudar. */
    public static void limparAoEditar(JTextComponent campo, Consumer<JTextComponent> limpar) {
        campo.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                limpar.accept(campo);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                limpar.accept(campo);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                limpar.accept(campo);
            }
        });
    }

    public static boolean vazio(JTextComponent campo) {
        return campo.getText() == null || campo.getText().isBlank();
    }
}
