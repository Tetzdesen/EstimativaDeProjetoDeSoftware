package com.br.estimativadeprojetodesoftware.command;

import com.br.estimativadeprojetodesoftware.view.componentes.Dialogos;
import java.util.function.Predicate;

/**
 * Pede confirmação antes de uma ação destrutiva. Os botões são o verbo da ação
 * (ex.: "Excluir") e "Cancelar"; Esc cancela. Pode ser trocado em testes.
 */
public class ConfirmarCommand implements Command {

    private static final Predicate<ConfirmarCommand> PADRAO =
            c -> Dialogos.confirmar(null, c.titulo, c.mensagem, c.textoConfirmar, "Cancelar");
    private static Predicate<ConfirmarCommand> confirmador = PADRAO;

    private final String titulo;
    private final String mensagem;
    private final String textoConfirmar;
    private boolean confirmado;

    public ConfirmarCommand(String titulo, String mensagem, String textoConfirmar) {
        this.titulo = titulo;
        this.mensagem = mensagem;
        this.textoConfirmar = textoConfirmar;
    }

    /** Atalho: pergunta e devolve true se o usuário confirmou. */
    public static boolean perguntar(String titulo, String mensagem, String textoConfirmar) {
        ConfirmarCommand c = new ConfirmarCommand(titulo, mensagem, textoConfirmar);
        c.execute();
        return c.isConfirmado();
    }

    public static void setConfirmador(Predicate<ConfirmarCommand> novo) {
        confirmador = novo == null ? PADRAO : novo;
    }

    @Override
    public void execute() {
        confirmado = confirmador.test(this);
    }

    public boolean isConfirmado() {
        return confirmado;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getMensagem() {
        return mensagem;
    }

    public String getTextoConfirmar() {
        return textoConfirmar;
    }
}
