package com.br.estimativadeprojetodesoftware.presenter.helpers;

import javax.swing.Timer;

/**
 * Agrupa alterações seguidas em uma única execução: cada agendamento reinicia
 * a espera, e a ação roda na thread do Swing quando a espera termina.
 */
public final class RecalculoAgendado {

    private final Timer timer;
    private final Runnable acao;

    public RecalculoAgendado(int atrasoEmMilissegundos, Runnable acao) {
        this.acao = acao;
        this.timer = new Timer(atrasoEmMilissegundos, e -> acao.run());
        this.timer.setRepeats(false);
    }

    public void agendar() {
        timer.restart();
    }

    public void cancelar() {
        timer.stop();
    }

    /** Executa já, sem esperar, descartando o agendamento pendente. */
    public void executarAgora() {
        timer.stop();
        acao.run();
    }
}
