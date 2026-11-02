package com.br.estimativadeprojetodesoftware.command;

import com.br.estimativadeprojetodesoftware.view.componentes.Dialogos;
import com.br.estimativadeprojetodesoftware.view.componentes.Notificacao;
import java.util.Objects;
import java.util.function.BiConsumer;

/**
 * Mostra uma mensagem padronizada: sucesso como notificação que some sozinha;
 * aviso e erro como diálogo. A forma de exibição pode ser trocada (testes).
 */
public class ExibirMensagemCommand implements Command {

    private static final BiConsumer<TipoMensagem, String> PADRAO = (tipo, texto) -> {
        switch (tipo) {
            case SUCESSO -> {
                if (Notificacao.mostrar(texto, Notificacao.Tipo.SUCESSO) == null) {
                    Dialogos.informacao(null, texto);
                }
            }
            case AVISO -> Dialogos.aviso(null, texto);
            case ERRO -> Dialogos.erro(null, texto);
        }
    };

    private static BiConsumer<TipoMensagem, String> exibidor = PADRAO;

    private final TipoMensagem tipo;
    private final String texto;

    public ExibirMensagemCommand(TipoMensagem tipo, String texto) {
        this.tipo = Objects.requireNonNull(tipo);
        this.texto = texto == null || texto.isBlank() ? "Ocorreu um erro inesperado." : texto;
    }

    public static void sucesso(String texto) {
        new ExibirMensagemCommand(TipoMensagem.SUCESSO, texto).execute();
    }

    public static void aviso(String texto) {
        new ExibirMensagemCommand(TipoMensagem.AVISO, texto).execute();
    }

    public static void erro(String texto) {
        new ExibirMensagemCommand(TipoMensagem.ERRO, texto).execute();
    }

    /** Troca a forma de exibição (usado em testes automatizados da interface). */
    public static void setExibidor(BiConsumer<TipoMensagem, String> novo) {
        exibidor = novo == null ? PADRAO : novo;
    }

    @Override
    public void execute() {
        exibidor.accept(tipo, texto);
    }
}
