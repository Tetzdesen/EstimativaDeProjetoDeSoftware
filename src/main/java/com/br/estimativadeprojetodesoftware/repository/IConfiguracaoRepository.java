package com.br.estimativadeprojetodesoftware.repository;

import java.util.Optional;

public interface IConfiguracaoRepository {

    String CHAVE_FORMATO_LOG = "log.formato";

    Optional<String> buscar(String chave);

    void salvar(String chave, String valor);
}
