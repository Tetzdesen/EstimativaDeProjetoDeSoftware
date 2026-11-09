package com.br.estimativadeprojetodesoftware.suporte;

import com.br.estimativadeprojetodesoftware.command.ConfirmarCommand;
import com.br.estimativadeprojetodesoftware.command.ExibirMensagemCommand;
import com.br.estimativadeprojetodesoftware.command.TipoMensagem;
import com.br.estimativadeprojetodesoftware.observer.EventoDominio;
import com.br.estimativadeprojetodesoftware.observer.Observer;
import com.br.estimativadeprojetodesoftware.observer.Subject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import javax.swing.SwingUtilities;
import org.junit.jupiter.api.function.Executable;

/** Mensagens, confirmações e eventos capturados, e execução na thread do Swing. */
public final class AmbienteTeste implements AutoCloseable {

    public final List<String> erros = new ArrayList<>();
    public final List<String> sucessos = new ArrayList<>();
    public final List<String> perguntas = new ArrayList<>();
    public boolean resposta = true;

    /** Subject que só entrega aos observadores registrados nele. */
    public static final class Publicador implements Subject {

        private final List<Observer> observadores = new CopyOnWriteArrayList<>();
        public final List<EventoDominio> eventos = new ArrayList<>();

        @Override
        public void addObserver(Observer o) {
            observadores.add(o);
        }

        @Override
        public void removeObserver(Observer o) {
            observadores.remove(o);
        }

        @Override
        public void notifyObservers(EventoDominio e) {
            eventos.add(e);
            observadores.forEach(o -> o.update(e));
        }
    }

    public AmbienteTeste() {
        ExibirMensagemCommand.setExibidor((tipo, texto) -> (tipo == TipoMensagem.SUCESSO ? sucessos : erros).add(texto));
        ConfirmarCommand.setConfirmador(c -> {
            perguntas.add(c.getMensagem());
            return resposta;
        });
    }

    @Override
    public void close() throws Exception {
        ExibirMensagemCommand.setExibidor(null);
        ConfirmarCommand.setConfirmador(null);
        SwingUtilities.invokeAndWait(() -> {
        });
    }

    public static void naThreadDoSwing(Executable corpo) throws Exception {
        Throwable[] falha = new Throwable[1];
        SwingUtilities.invokeAndWait(() -> {
            try {
                corpo.execute();
            } catch (Throwable t) {
                falha[0] = t;
            }
        });
        if (falha[0] instanceof Error erro) {
            throw erro;
        }
        if (falha[0] instanceof Exception excecao) {
            throw excecao;
        }
    }

    /** Executa a ação e garante que as janelas abertas por ela sejam fechadas. */
    public static void fecharDepois(java.awt.Window janela, Consumer<java.awt.Window> acao) {
        try {
            acao.accept(janela);
        } finally {
            janela.dispose();
        }
    }
}
