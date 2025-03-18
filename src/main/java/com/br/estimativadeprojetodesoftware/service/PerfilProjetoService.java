package com.br.estimativadeprojetodesoftware.service;

import com.br.estimativadeprojetodesoftware.abstractfactory.FabricaRepository;
import com.br.estimativadeprojetodesoftware.abstractfactory.SeletorFabricaRepository;
import com.br.estimativadeprojetodesoftware.model.PerfilProjeto;
import com.br.estimativadeprojetodesoftware.observer.Subject;
import com.br.estimativadeprojetodesoftware.observer.Observer;
import com.br.estimativadeprojetodesoftware.repository.IPerfilRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 *
 * @author tetzner
 */
public class PerfilProjetoService implements Subject {

    private final FabricaRepository fabricaDAO;
    private final IPerfilRepository perfilRepository;
    private final List<Observer> observers;

    public PerfilProjetoService() {
        fabricaDAO = SeletorFabricaRepository.obterInstancia();
        perfilRepository = fabricaDAO.criarPerfilRepository();
        this.observers = new ArrayList<>();
    }

    public void salvar(PerfilProjeto perfil){
        perfilRepository.salvar(perfil);
        notifyObservers();
    }

    public void atualizar(PerfilProjeto perfil){
        perfilRepository.atualizar(perfil);
        notifyObservers();
    }

    public void removerPorId(UUID id){
        perfilRepository.removerPorId(id);
        notifyObservers();
    }

    public Optional<PerfilProjeto> buscarPorId(UUID id){
        return perfilRepository.buscarPorId(id);
    }
    
    public List<PerfilProjeto> buscarPerfisPorProjeto(UUID idProjeto){
        return perfilRepository.buscarPerfisPorProjeto(idProjeto);
    }

    public List<PerfilProjeto> buscarTodos(){
      return perfilRepository.buscarTodos();
    }
    
    public List<PerfilProjeto> buscarTodosPerfisPorIdUsuario(UUID id
    ) {
        return perfilRepository.buscarTodosPerfisPorIdUsuario(id);
    }

    public Map<String, Double> buscarTodosCamposPorPerfil(PerfilProjeto perfil) {
        return perfilRepository.buscarTodosCamposPorPerfil(perfil);
    }
    
    public int obterQuantidadePerfisPorUsuario(UUID idUsuario) {
        return perfilRepository.obterQuantidadePerfisPorUsuario(idUsuario);
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
