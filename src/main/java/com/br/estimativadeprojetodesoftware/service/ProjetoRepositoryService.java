package com.br.estimativadeprojetodesoftware.service;

import com.br.estimativadeprojetodesoftware.abstractfactory.FabricaRepository;
import com.br.estimativadeprojetodesoftware.abstractfactory.SeletorFabricaRepository;
import com.br.estimativadeprojetodesoftware.model.Projeto;
import com.br.estimativadeprojetodesoftware.repository.IProjetoRepository;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 *
 * @author tetzner
 */
public class ProjetoRepositoryService {

    private static ProjetoRepositoryService INSTANCIA = null;
    private final FabricaRepository fabricaDAO;
    private final IProjetoRepository projetoRepository;

    private ProjetoRepositoryService() {
        fabricaDAO = SeletorFabricaRepository.obterInstancia();
        projetoRepository = fabricaDAO.criarProjetoRepository();
    }

    public static ProjetoRepositoryService getInstancia() {
        if (INSTANCIA == null) {
            INSTANCIA = new ProjetoRepositoryService();
        }

        return INSTANCIA;
    }

    public void salvar(Projeto projeto) {
        projetoRepository.salvar(projeto);
    }

    public void atualizar(Projeto projeto) {
        projetoRepository.atualizar(projeto);
    }

    public void removerPorId(UUID id) {
        projetoRepository.removerPorId(id);
    }

    public Optional<Projeto> buscarPorId(UUID id) {
        return projetoRepository.buscarPorId(id);
    }

    public List<Projeto> buscarTodos() {
        return projetoRepository.buscarTodos();
    }

    public boolean buscarIsCompartilhadoPorId(UUID idUsuario, UUID idProjeto) {
        return projetoRepository.buscarIsCompartilhadoPorId(idUsuario, idProjeto);
    }

    public List<String> buscarProjetosPorUsuario(UUID idUsuario) {
        return projetoRepository.buscarProjetosPorUsuario(idUsuario);
    }
}
