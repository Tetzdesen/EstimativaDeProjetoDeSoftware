package com.br.estimativadeprojetodesoftware.repository;

import java.util.List;

/**
 *
 * @author tetzner
 */
public interface IEstimativaHasCampoRepository {

    void salvar(String estimativaId, int campoId, double valorEstimativaCampo);

    void removerPorIds(String estimativaId, int campoId);

    List<Double> buscarValoresPorEstimativa(String estimativaId);
}
