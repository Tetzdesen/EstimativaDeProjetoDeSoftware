package com.br.estimativadeprojetodesoftware.service;

import com.br.estimativadeprojetodesoftware.abstractfactory.FabricaRepository;
import com.br.estimativadeprojetodesoftware.abstractfactory.SeletorFabricaRepository;
import com.br.estimativadeprojetodesoftware.model.Projeto;
import com.br.estimativadeprojetodesoftware.model.Subject;
import com.br.estimativadeprojetodesoftware.presenter.Observer;
import com.br.estimativadeprojetodesoftware.repository.IProjetoRepository;
import java.util.ArrayList;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 *
 * @author tetzner
 */
public class ProjetoRepositoryService implements Subject {

    private final FabricaRepository fabricaDAO;
    private final IProjetoRepository projetoRepository;
    private final List<Observer> observers;

    public ProjetoRepositoryService() {
        fabricaDAO = SeletorFabricaRepository.obterInstancia();
        projetoRepository = fabricaDAO.criarProjetoRepository();
        this.observers = new ArrayList<>();
    }

    public void salvar(Projeto projeto) {
        projetoRepository.salvar(projeto);
        notifyObservers();
    }

    public void atualizar(Projeto projeto) {
        projetoRepository.atualizar(projeto);
        notifyObservers();
    }

    public void removerPorId(UUID id) {
        projetoRepository.removerPorId(id);
        notifyObservers();
    }

    public Optional<Projeto> buscarPorId(UUID id) {
        return projetoRepository.buscarPorId(id);
    }
    
    public Optional<Projeto> buscarProjetoPorNome(String nomeProjeto) {
        return projetoRepository.buscarProjetoPorNome(nomeProjeto);
    }

    public List<String> buscarNomesDeProjetosPorUsuario(UUID idUsuario) {
        return projetoRepository.buscarNomesDeProjetosPorUsuario(idUsuario);
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

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update();
        }
    }
}
