package com.br.estimativadeprojetodesoftware.service;

import com.br.estimativadeprojetodesoftware.abstractfactory.FabricaRepository;
import com.br.estimativadeprojetodesoftware.abstractfactory.SeletorFabricaRepository;
import com.br.estimativadeprojetodesoftware.repository.IEstimativaHasCampoRepository;
import java.util.List;

/**
 *
 * @author tetzner
 */
public class EstimativaHasCampoRepositoryService {

    private static EstimativaHasCampoRepositoryService INSTANCIA = null;
    private final FabricaRepository fabricaDAO;
    private final IEstimativaHasCampoRepository estimativaCampoRepository;

    private EstimativaHasCampoRepositoryService() {
        fabricaDAO = SeletorFabricaRepository.obterInstancia();
        estimativaCampoRepository = fabricaDAO.criarEstimativaCampoRepository();
    }

    public static EstimativaHasCampoRepositoryService getInstancia() {
        if (INSTANCIA == null) {
            INSTANCIA = new EstimativaHasCampoRepositoryService();
        }

        return INSTANCIA;
    }

    public void salvar(String estimativaId, int campoId, double valorEstimativaCampo){
        estimativaCampoRepository.salvar(estimativaId, campoId, valorEstimativaCampo);
    }

    public void removerPorIds(String estimativaId, int campoId){
        estimativaCampoRepository.removerPorIds(estimativaId, campoId);
    }
    
   // public buscarNomesPorEstimativa(String estimativaId){
        
   // }

    public List<Double> buscarValoresPorEstimativa(String estimativaId){
        return estimativaCampoRepository.buscarValoresPorEstimativa(estimativaId);
    }
}
