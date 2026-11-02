package com.br.estimativadeprojetodesoftware.suporte;

import com.br.estimativadeprojetodesoftware.service.RegistroOperacoes;
import java.util.ArrayList;
import java.util.List;

/** Registro de operações em memória para os testes. */
public class LogFalso implements RegistroOperacoes {

    public final List<String> registros = new ArrayList<>();

    @Override
    public void sucesso(String operacao, String nomeProjeto, String usuario) {
        registros.add("OK " + operacao + " | " + nomeProjeto + " | " + usuario);
    }

    @Override
    public void falha(String operacao, String nomeProjeto, String usuario, String mensagem) {
        registros.add("FALHA " + operacao + " | " + nomeProjeto + " | " + usuario + " | " + mensagem);
    }

    public String ultimo() {
        return registros.get(registros.size() - 1);
    }
}
