package com.br.estimativadeprojetodesoftware.command;

/** Tipo da mensagem mostrada ao usuário. */
public enum TipoMensagem {
    /** Aviso não modal que some sozinho. */
    SUCESSO,
    /** Diálogo de atenção. */
    AVISO,
    /** Diálogo de erro, que fica até o usuário fechar. */
    ERRO
}
