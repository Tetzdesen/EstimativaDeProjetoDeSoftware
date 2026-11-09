package com.br.estimativadeprojetodesoftware.presenter.helpers;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;

class RecalculoAgendadoTest {

    private static final int ATRASO = 40;

    @Test
    void agendamentosSeguidosGeramUmaExecucao() throws Exception {
        AtomicInteger execucoes = new AtomicInteger();
        CountDownLatch executou = new CountDownLatch(1);
        RecalculoAgendado recalculo = new RecalculoAgendado(ATRASO, () -> {
            execucoes.incrementAndGet();
            executou.countDown();
        });

        recalculo.agendar();
        recalculo.agendar();
        recalculo.agendar();

        assertTrue(executou.await(5, TimeUnit.SECONDS));
        Thread.sleep(ATRASO * 3L);
        assertEquals(1, execucoes.get());
    }

    @Test
    void cancelarImpedeAExecucaoPendente() throws Exception {
        AtomicInteger execucoes = new AtomicInteger();
        RecalculoAgendado recalculo = new RecalculoAgendado(ATRASO, execucoes::incrementAndGet);

        recalculo.agendar();
        recalculo.cancelar();

        Thread.sleep(ATRASO * 3L);
        assertEquals(0, execucoes.get());
    }

    @Test
    void executarAgoraRodaUmaVezEDescartaOPendente() throws Exception {
        AtomicInteger execucoes = new AtomicInteger();
        RecalculoAgendado recalculo = new RecalculoAgendado(ATRASO, execucoes::incrementAndGet);

        recalculo.agendar();
        recalculo.executarAgora();

        assertEquals(1, execucoes.get());
        Thread.sleep(ATRASO * 3L);
        assertEquals(1, execucoes.get());
    }
}
