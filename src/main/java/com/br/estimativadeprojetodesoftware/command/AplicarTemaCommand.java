package com.br.estimativadeprojetodesoftware.command;

import com.br.estimativadeprojetodesoftware.model.Tema;
import com.br.estimativadeprojetodesoftware.observer.EventoDominio;
import com.br.estimativadeprojetodesoftware.observer.PublicadorEventos;
import com.br.estimativadeprojetodesoftware.view.tema.TemaClaro;
import com.br.estimativadeprojetodesoftware.view.tema.TemaEscuro;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.extras.FlatAnimatedLafChange;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Aplica o tema (claro ou escuro) em todas as janelas abertas e avisa os
 * observadores (ex.: gráficos do dashboard). Deve rodar na thread do Swing.
 */
public class AplicarTemaCommand implements Command {

    private final Tema tema;

    public AplicarTemaCommand(Tema tema) {
        this.tema = tema == null ? Tema.CLARO : tema;
    }

    @Override
    public void execute() {
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(this::execute);
            return;
        }
        boolean trocaVisivel = temaAtual() != tema && java.awt.Window.getWindows().length > 0;
        if (trocaVisivel) {
            FlatAnimatedLafChange.showSnapshot();
        }
        if (tema == Tema.ESCURO) {
            TemaEscuro.setup();
        } else {
            TemaClaro.setup();
        }
        FlatLaf.updateUI();
        if (trocaVisivel) {
            FlatAnimatedLafChange.hideSnapshotWithAnimation();
        }
        PublicadorEventos.getInstancia().notifyObservers(
                new EventoDominio(EventoDominio.Tipo.ALTERACAO, EventoDominio.Entidade.TEMA, null));
    }

    /** Tema em uso no momento (claro se o look and feel não for do sistema). */
    public static Tema temaAtual() {
        return UIManager.getLookAndFeel() instanceof TemaEscuro ? Tema.ESCURO : Tema.CLARO;
    }
}
