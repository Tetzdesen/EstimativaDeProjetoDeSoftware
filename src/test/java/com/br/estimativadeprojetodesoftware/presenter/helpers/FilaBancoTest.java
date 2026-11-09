package com.br.estimativadeprojetodesoftware.presenter.helpers;

import static org.junit.jupiter.api.Assertions.*;

import com.br.estimativadeprojetodesoftware.view.componentes.Ocupavel;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import javax.swing.SwingUtilities;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** A fila real: outra thread, uma tarefa por vez, retorno na thread do Swing. */
class FilaBancoTest {

    private static class Janela implements Ocupavel {
        final List<String> historico = new CopyOnWriteArrayList<>();
        volatile boolean ocupado;
        volatile boolean fechada;

        @Override
        public void setOcupado(boolean ocupado, String mensagem) {
            this.ocupado = ocupado;
            historico.add(ocupado ? "ocupado:" + mensagem : "livre");
        }

        @Override
        public boolean isOcupado() {
            return ocupado;
        }

        @Override
        public boolean isFechada() {
            return fechada;
        }
    }

    @BeforeEach
    void filaReal() {
        FilaBanco.usarExecucaoImediata(false);
    }

    @AfterEach
    void voltarModoDosTestes() {
        FilaBanco.usarExecucaoImediata(true);
    }

    @Test
    void trabalhoForaDaEdtERetornoNaEdt() throws Exception {
        Janela janela = new Janela();
        AtomicBoolean trabalhoNaEdt = new AtomicBoolean(true);
        AtomicBoolean retornoNaEdt = new AtomicBoolean(false);
        AtomicReference<String> recebido = new AtomicReference<>();
        CountDownLatch fim = new CountDownLatch(1);

        SwingUtilities.invokeAndWait(() -> FilaBanco.executar(janela, "Salvando…", () -> {
            trabalhoNaEdt.set(SwingUtilities.isEventDispatchThread());
            return "ok";
        }, r -> {
            retornoNaEdt.set(SwingUtilities.isEventDispatchThread());
            recebido.set(r);
            fim.countDown();
        }, e -> fail(e)));

        assertTrue(fim.await(5, TimeUnit.SECONDS));
        assertFalse(trabalhoNaEdt.get(), "o trabalho não pode rodar na EDT");
        assertTrue(retornoNaEdt.get(), "o resultado volta na EDT");
        assertEquals("ok", recebido.get());
        assertEquals(List.of("ocupado:Salvando…", "livre"), janela.historico);
    }

    @Test
    void tarefasRodamUmaDeCadaVezEmOrdem() throws Exception {
        List<Integer> ordem = new CopyOnWriteArrayList<>();
        AtomicBoolean sobreposicao = new AtomicBoolean(false);
        AtomicBoolean rodando = new AtomicBoolean(false);
        CountDownLatch fim = new CountDownLatch(5);
        for (int i = 0; i < 5; i++) {
            int n = i;
            FilaBanco.executar(null, null, () -> {
                if (!rodando.compareAndSet(false, true)) {
                    sobreposicao.set(true);
                }
                Thread.sleep(20);
                ordem.add(n);
                rodando.set(false);
                return n;
            }, r -> fim.countDown(), e -> fail(e));
        }
        assertTrue(fim.await(5, TimeUnit.SECONDS));
        assertFalse(sobreposicao.get(), "duas tarefas usaram o banco ao mesmo tempo");
        assertEquals(List.of(0, 1, 2, 3, 4), ordem);
    }

    @Test
    void janelaFechadaNaoRecebeResultado() throws Exception {
        Janela janela = new Janela();
        CountDownLatch comecou = new CountDownLatch(1);
        CountDownLatch liberar = new CountDownLatch(1);
        AtomicBoolean entregue = new AtomicBoolean(false);

        FilaBanco.executar(janela, "Carregando…", () -> {
            comecou.countDown();
            liberar.await(5, TimeUnit.SECONDS);
            return 1;
        }, r -> entregue.set(true), e -> entregue.set(true));
        assertTrue(comecou.await(5, TimeUnit.SECONDS));
        janela.fechada = true;
        liberar.countDown();

        // espera a fila e a EDT processarem
        CountDownLatch depois = new CountDownLatch(1);
        FilaBanco.executar(null, null, () -> 0, r -> depois.countDown(), e -> depois.countDown());
        assertTrue(depois.await(5, TimeUnit.SECONDS));
        SwingUtilities.invokeAndWait(() -> {
        });
        assertFalse(entregue.get());
    }

    @Test
    void erroChegaAoTratadorELiberaAJanela() throws Exception {
        Janela janela = new Janela();
        AtomicReference<RuntimeException> erro = new AtomicReference<>();
        CountDownLatch fim = new CountDownLatch(1);

        FilaBanco.executar(janela, "Salvando…", () -> {
            throw new IllegalStateException("conexão perdida");
        }, r -> fail("não deveria concluir"), e -> {
            erro.set(e);
            fim.countDown();
        });

        assertTrue(fim.await(5, TimeUnit.SECONDS));
        assertEquals("conexão perdida", erro.get().getMessage());
        assertFalse(janela.ocupado);
    }

    @Test
    void modoImediatoRodaNaPropriaThread() {
        FilaBanco.usarExecucaoImediata(true);
        AtomicReference<Thread> thread = new AtomicReference<>();
        FilaBanco.executar(null, null, () -> {
            thread.set(Thread.currentThread());
            return 1;
        }, r -> {
        }, e -> fail(e));
        assertSame(Thread.currentThread(), thread.get());
    }
}
