package com.br.estimativadeprojetodesoftware.view.componentes;

import com.br.estimativadeprojetodesoftware.service.IconService;
import java.awt.Component;
import java.awt.KeyboardFocusManager;
import javax.swing.JOptionPane;

/**
 * Diálogos padronizados (erro, aviso, confirmação e entrada de texto). É o
 * único ponto do sistema que usa JOptionPane. Esc sempre cancela.
 */
public final class Dialogos {

    private Dialogos() {
    }

    private static Component pai(Component pai) {
        return pai != null ? pai : KeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow();
    }

    public static void erro(Component pai, String mensagem) {
        JOptionPane.showMessageDialog(pai(pai), mensagem, "Não foi possível concluir", JOptionPane.ERROR_MESSAGE,
                IconService.getIcon("erro", 32));
    }

    public static void aviso(Component pai, String mensagem) {
        JOptionPane.showMessageDialog(pai(pai), mensagem, "Atenção", JOptionPane.WARNING_MESSAGE,
                IconService.getIcon("aviso", 32));
    }

    public static void informacao(Component pai, String mensagem) {
        JOptionPane.showMessageDialog(pai(pai), mensagem, "Informação", JOptionPane.INFORMATION_MESSAGE,
                IconService.getIcon("info", 32));
    }

    /**
     * Pergunta de confirmação com textos próprios nos botões. O foco começa em
     * Cancelar; Esc ou fechar a janela contam como cancelar.
     */
    public static boolean confirmar(Component pai, String titulo, String mensagem, String textoConfirmar, String textoCancelar) {
        Object[] opcoes = {textoConfirmar, textoCancelar};
        int r = JOptionPane.showOptionDialog(pai(pai), mensagem, titulo, JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE, IconService.getIcon("aviso", 32), opcoes, textoCancelar);
        return r == 0;
    }

    /** Entrada de texto; null se o usuário cancelar. */
    public static String perguntar(Component pai, String titulo, String mensagem, String valorInicial) {
        Object r = JOptionPane.showInputDialog(pai(pai), mensagem, titulo, JOptionPane.QUESTION_MESSAGE,
                IconService.getIcon("info", 32), null, valorInicial);
        return r == null ? null : r.toString();
    }
}
