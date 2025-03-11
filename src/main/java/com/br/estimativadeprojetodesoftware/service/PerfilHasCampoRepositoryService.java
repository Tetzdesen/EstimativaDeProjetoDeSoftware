package com.br.estimativadeprojetodesoftware.service;

import com.br.estimativadeprojetodesoftware.abstractfactory.FabricaRepository;
import com.br.estimativadeprojetodesoftware.abstractfactory.SeletorFabricaRepository;
import com.br.estimativadeprojetodesoftware.repository.IPerfilHasCampoRepository;
import java.util.List;

/**
 *
 * @author tetzner
 */
public class PerfilHasCampoRepositoryService {

    private static PerfilHasCampoRepositoryService INSTANCIA = null;
    private final FabricaRepository fabricaDAO;
    private final IPerfilHasCampoRepository perfilCampoRepository;

    private PerfilHasCampoRepositoryService() {
        fabricaDAO = SeletorFabricaRepository.obterInstancia();
        perfilCampoRepository = fabricaDAO.criarPerfilCampoRepository();
    }

    public static PerfilHasCampoRepositoryService getInstancia() {
        if (INSTANCIA == null) {
            INSTANCIA = new PerfilHasCampoRepositoryService();
        }

        return INSTANCIA;
    }

    public void salvar(String perfilId, int campoId, double valorPerfilCampo){
        perfilCampoRepository.salvar(perfilId, campoId, valorPerfilCampo);
    }

    public void removerPorIds(String perfilId, int campoId){
        perfilCampoRepository.removerPorIds(perfilId, campoId);
    }

    public List<Double> buscarValoresPorPerfil(String perfilId){
        return perfilCampoRepository.buscarValoresPorPerfil(perfilId);
    }
}
