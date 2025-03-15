package com.br.estimativadeprojetodesoftware.service;

import com.br.estimativadeprojetodesoftware.abstractfactory.FabricaRepository;
import com.br.estimativadeprojetodesoftware.abstractfactory.SeletorFabricaRepository;
import com.br.estimativadeprojetodesoftware.model.Campo;
import com.br.estimativadeprojetodesoftware.model.Perfil;
import com.br.estimativadeprojetodesoftware.model.Projeto;
import com.br.estimativadeprojetodesoftware.repository.ICampoRepository;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author tetzner
 */
public class CampoRepositoryService {

    private final FabricaRepository fabricaDAO;
    private final ICampoRepository campoRepository;

    public CampoRepositoryService() {
        fabricaDAO = SeletorFabricaRepository.obterInstancia();
        campoRepository = fabricaDAO.criarCampoRepository();
    }

    public void salvar(Campo campo) {
        campoRepository.salvar(campo);
    }

    public void salvarProjetoCampo(Projeto projeto, Campo campo) {
        campoRepository.salvarProjetoCampo(projeto, campo);
    }

    public void salvarPerfilCampo(Perfil perfil, Campo campo) {
        campoRepository.salvarPerfilCampo(perfil, campo);
    }

    public void atualizar(Campo campo) {
        campoRepository.atualizar(campo);
    }

    public void atualizarDiasProjetoCampo(Projeto projeto, Campo campo) {
        campoRepository.atualizarDiasProjetoCampo(projeto, campo);
    }

    public void atualizarDiasPerfilCampo(Perfil perfil, Campo campo) {
        campoRepository.atualizarDiasPerfilCampo(perfil, campo);
    }

    public void removerPorID(UUID id) {
        campoRepository.removerPorID(id);
    }

    public Campo buscarPorId(UUID id) {
        return campoRepository.buscarPorId(id);
    }

    public List<Campo> listarTodos() {
        return campoRepository.listarTodos();
    }

    public List<Campo> listarTodosPorTipo(String tipo) {
        return campoRepository.listarTodosPorTipo(tipo);
    }

    public Integer buscarDiasPorProjetoCampo(UUID idProjeto, UUID idCampo) {
        return campoRepository.buscarDiasPorProjetoCampo(idProjeto, idCampo);
    }

    public Double buscarDiasPorPerfilCampo(UUID idPerfil, UUID idCampo) {
        return campoRepository.buscarDiasPorPerfilCampo(idPerfil, idCampo);
    }

    public Integer buscarDiasPorProjeto(UUID idProjeto) {
        return campoRepository.buscarDiasPorProjeto(idProjeto);
    }

    public Double buscarDiasPorPerfil(UUID idPerfil) {
        return campoRepository.buscarDiasPorPerfil(idPerfil);
    }

    public List<Campo> listarTodosPorIdProjeto(UUID idProjeto) {
        return campoRepository.listarTodosPorIdProjeto(idProjeto);
    }

    public List<Campo> listarTodosPorIdPerfil(UUID idPerfil) {
        return campoRepository.listarTodosPorIdPerfil(idPerfil);
    }
    
    public Campo buscarPorIdProjeto(UUID idProjeto) {
        return campoRepository.buscarPorIdProjeto(idProjeto);
    }

    public Campo buscarPorIdPerfil(UUID idPerfil) {
        return campoRepository.buscarPorIdPerfil(idPerfil);
    }

    public List<Campo> buscarPorIdProjetoTipo(UUID idProjeto, String tipo) {
        return campoRepository.buscarPorIdProjetoTipo(idProjeto, tipo);
    }

    public List<Campo> buscarPorIdPerfilTipo(UUID idPerfil, String tipo) {
        return campoRepository.buscarPorIdPerfilTipo(idPerfil, tipo);
    }

    public double buscarValorPorNomeProjetoCampo(UUID idProjeto, String nome) {
        return campoRepository.buscarValorPorNomeProjetoCampo(idProjeto, nome);
    }

}
