package com.br.estimativadeprojetodesoftware.command;

import com.br.estimativadeprojetodesoftware.view.componentes.Dialogos;
import java.util.function.Function;

/** Pede um texto ao usuário (null se cancelar). Pode ser trocado em testes. */
public class PerguntarCommand implements Command {

    private static final Function<PerguntarCommand, String> PADRAO =
            p -> Dialogos.perguntar(null, p.titulo, p.mensagem, p.valorInicial);
    private static Function<PerguntarCommand, String> perguntador = PADRAO;

    private final String titulo;
    private final String mensagem;
    private final String valorInicial;
    private String resposta;

    public PerguntarCommand(String titulo, String mensagem, String valorInicial) {
        this.titulo = titulo;
        this.mensagem = mensagem;
        this.valorInicial = valorInicial;
    }

    public static String perguntar(String titulo, String mensagem) {
        PerguntarCommand p = new PerguntarCommand(titulo, mensagem, "");
        p.execute();
        return p.getResposta();
    }

    public static void setPerguntador(Function<PerguntarCommand, String> novo) {
        perguntador = novo == null ? PADRAO : novo;
    }

    @Override
    public void execute() {
        resposta = perguntador.apply(this);
    }

    public String getResposta() {
        return resposta;
    }

    public String getMensagem() {
        return mensagem;
    }
}
