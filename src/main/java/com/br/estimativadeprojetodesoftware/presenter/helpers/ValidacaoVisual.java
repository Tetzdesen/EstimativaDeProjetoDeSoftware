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
        aoEditar(campo, () -> limpar.accept(campo));
    }

    /** Executa a ação sempre que o texto do campo mudar. */
    public static void aoEditar(JTextComponent campo, Runnable acao) {
        campo.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                acao.run();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                acao.run();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                acao.run();
            }
        });
    }

    public static boolean vazio(JTextComponent campo) {
        return campo.getText() == null || campo.getText().isBlank();
    }
}
