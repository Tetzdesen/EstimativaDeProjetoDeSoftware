package com.br.estimativadeprojetodesoftware.singleton;

import com.br.estimativadeprojetodesoftware.repository.RepositorioException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.Duration;
import java.util.Set;
import java.util.function.Supplier;

/**
 * Conexão que se reabre sozinha: antes de repassar uma chamada, se a última
 * verificação tiver passado do intervalo e não houver transação aberta, testa
 * a conexão real e, se ela não for mais válida (ex.: o servidor remoto a
 * encerrou), abre outra. Quem guarda a referência continua usando a mesma.
 */
public final class ConexaoReconectavel {

    private static final Set<String> SEM_VERIFICACAO = Set.of("close", "isClosed", "isValid", "unwrap", "isWrapperFor",
            "toString", "hashCode", "equals");

    private final Supplier<Connection> abrir;
    private final long intervaloNanos;
    private Connection real;
    private long ultimaVerificacao;
    /** Visto pelo proxy: setAutoCommit(false) sem um setAutoCommit(true) depois. */
    private boolean emTransacao;

    private ConexaoReconectavel(Supplier<Connection> abrir, Duration intervalo) {
        this.abrir = abrir;
        this.intervaloNanos = intervalo.toNanos();
        this.real = abrir.get();
        this.ultimaVerificacao = System.nanoTime();
    }

    /**
     * @param abrir abre uma conexão nova já configurada (sem criar tabelas nem rodar seeders)
     * @param intervalo tempo mínimo entre duas verificações da conexão
     */
    public static Connection criar(Supplier<Connection> abrir, Duration intervalo) {
        ConexaoReconectavel c = new ConexaoReconectavel(abrir, intervalo);
        return (Connection) Proxy.newProxyInstance(Connection.class.getClassLoader(), new Class<?>[]{Connection.class},
                (proxy, metodo, args) -> {
                    if (!SEM_VERIFICACAO.contains(metodo.getName())) {
                        c.garantirValida();
                    }
                    try {
                        Object resultado = metodo.invoke(c.real, args);
                        if (metodo.getName().equals("setAutoCommit")) {
                            c.emTransacao = !((Boolean) args[0]);
                        }
                        return resultado;
                    } catch (InvocationTargetException e) {
                        throw e.getCause();
                    }
                });
    }

    private synchronized void garantirValida() throws SQLException {
        long agora = System.nanoTime();
        if (agora - ultimaVerificacao < intervaloNanos) {
            return;
        }
        ultimaVerificacao = agora;
        if (emTransacao) {
            // reabrir no meio de uma transação gravaria o resto em autocommit, sem o que já foi feito;
            // melhor deixar a operação falhar
            return;
        }
        boolean valida;
        try {
            valida = real.isValid(5);
        } catch (SQLException e) {
            valida = false;
        }
        if (!valida) {
            try {
                real.close();
            } catch (SQLException ignorada) {
                // a conexão antiga já não serve; o que importa é abrir a nova
            }
            try {
                real = abrir.get();
            } catch (RepositorioException e) {
                throw new SQLException("Conexão com o banco perdida e não foi possível reabri-la: " + e.getMessage(), e);
            }
        }
    }
}
