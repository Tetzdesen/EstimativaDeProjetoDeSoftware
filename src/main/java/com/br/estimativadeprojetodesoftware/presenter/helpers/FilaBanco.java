package com.br.estimativadeprojetodesoftware.presenter.helpers;

import com.br.estimativadeprojetodesoftware.view.componentes.Ocupavel;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import javax.swing.SwingUtilities;

/**
 * Tira o acesso ao banco da thread do Swing. As tarefas rodam numa única
 * thread, em ordem, porque o sistema usa uma só conexão: duas operações em
 * paralelo poderiam misturar transações. O resultado volta na thread do Swing
 * e é descartado se a janela já tiver sido fechada.
 *
 * <p>Nos testes a fila roda em modo imediato (propriedade de sistema
 * {@value #PROPRIEDADE_IMEDIATA}), executando tudo na própria thread.</p>
 */
public final class FilaBanco {

    public static final String PROPRIEDADE_IMEDIATA = "estimativa.fila.imediata";

    private static final ExecutorService FILA = Executors.newSingleThreadExecutor(r -> {
        Thread t = new Thread(r, "fila-banco");
        t.setDaemon(true);
        return t;
    });

    private static volatile boolean imediata = Boolean.getBoolean(PROPRIEDADE_IMEDIATA);

    private FilaBanco() {
    }

    public static void usarExecucaoImediata(boolean valor) {
        imediata = valor;
    }

    public static boolean isImediata() {
        return imediata;
    }

    /**
     * Executa o trabalho fora da thread do Swing, com a janela marcada como
     * ocupada até o resultado chegar.
     *
     * @param janela janela que espera o resultado; pode ser {@code null}
     * @param aoFalhar recebe a exceção do trabalho (RuntimeException na prática)
     */
    public static <T> void executar(Ocupavel janela, String mensagem, Callable<T> trabalho,
            Consumer<T> aoConcluir, Consumer<RuntimeException> aoFalhar) {
        if (janela != null) {
            janela.setOcupado(true, mensagem);
        }
        if (imediata) {
            entregar(janela, executarCapturando(trabalho), aoConcluir, aoFalhar);
            return;
        }
        FILA.execute(() -> {
            Resultado<T> resultado = executarCapturando(trabalho);
            SwingUtilities.invokeLater(() -> entregar(janela, resultado, aoConcluir, aoFalhar));
        });
    }

    private record Resultado<T>(T valor, RuntimeException erro) {
    }

    private static <T> Resultado<T> executarCapturando(Callable<T> trabalho) {
        try {
            return new Resultado<>(trabalho.call(), null);
        } catch (RuntimeException e) {
            return new Resultado<>(null, e);
        } catch (Exception e) {
            return new Resultado<>(null, new IllegalStateException(e.getMessage(), e));
        }
    }

    private static <T> void entregar(Ocupavel janela, Resultado<T> resultado, Consumer<T> aoConcluir,
            Consumer<RuntimeException> aoFalhar) {
        if (janela != null) {
            if (janela.isFechada()) {
                return;
            }
            janela.setOcupado(false, null);
        }
        if (resultado.erro() != null) {
            aoFalhar.accept(resultado.erro());
        } else {
            aoConcluir.accept(resultado.valor());
        }
    }
}
