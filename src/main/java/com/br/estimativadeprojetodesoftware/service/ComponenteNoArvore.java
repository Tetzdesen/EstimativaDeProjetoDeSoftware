package com.br.estimativadeprojetodesoftware.service;

import com.br.estimativadeprojetodesoftware.command.ProjetoCommand;

import java.util.List;

public interface ComponenteNoArvore {
    String obterTexto();

    String obterChaveIcone();

    ProjetoCommand obterComando();

    List<ComponenteNoArvore> obterFilhos();
}
