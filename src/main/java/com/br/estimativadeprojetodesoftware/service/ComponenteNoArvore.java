package com.br.estimativadeprojetodesoftware.service;


import java.util.List;
import com.br.estimativadeprojetodesoftware.command.Command;

public interface ComponenteNoArvore {
    String obterTexto();

    String obterChaveIcone();

    Command obterComando();

    List<ComponenteNoArvore> obterFilhos();
}
