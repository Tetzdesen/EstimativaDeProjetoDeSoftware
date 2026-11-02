package com.br.estimativadeprojetodesoftware.repository;

/**
 * Falha de acesso a dados com mensagem pronta para ser exibida ao usuário.
 * Os repositórios convertem {@link java.sql.SQLException} nesta exceção.
 */
public class RepositorioException extends RuntimeException {

    public RepositorioException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }

    public RepositorioException(String mensagem) {
        super(mensagem);
    }
}
