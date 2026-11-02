package com.br.estimativadeprojetodesoftware.repository;

import com.br.estimativadeprojetodesoftware.model.Projeto;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IProjetoRepository {

    /** Insere ou substitui o projeto inteiro (itens, valores, taxas e custos). */
    void salvar(Projeto projeto);

    boolean remover(UUID id);

    Optional<Projeto> buscarPorId(UUID id);

    List<Projeto> buscarPorDono(UUID donoId);

    boolean existeNomeParaDono(String nome, UUID donoId, UUID ignorarProjetoId);
}
