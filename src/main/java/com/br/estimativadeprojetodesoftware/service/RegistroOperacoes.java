package com.br.estimativadeprojetodesoftware.service;

/** Porta usada pelos services para registrar operações no log. */
public interface RegistroOperacoes {

    void sucesso(String operacao, String nomeProjeto, String usuario);

    void falha(String operacao, String nomeProjeto, String usuario, String mensagem);
}
