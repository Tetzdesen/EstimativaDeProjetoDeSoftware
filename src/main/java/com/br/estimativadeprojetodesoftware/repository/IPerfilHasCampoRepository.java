package com.br.estimativadeprojetodesoftware.repository;

import java.util.List;

/**
 *
 * @author tetzner
 */
public interface IPerfilHasCampoRepository {

    void salvar(String perfilId, int campoId, double valorPerfilCampo);

    void removerPorIds(String perfilId, int campoId);

    List<Double> buscarValoresPorPerfil(String perfilId);
}