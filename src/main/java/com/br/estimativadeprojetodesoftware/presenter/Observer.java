package com.br.estimativadeprojetodesoftware.presenter;

import br.projeto.model.Projeto;

import java.util.List;

public interface Observer {
    void update(List<Projeto> projetos);
}
