package com.br.estimativadeprojetodesoftware.service;

import com.br.estimativadeprojetodesoftware.abstractfactory.FabricaRepository;
import com.br.estimativadeprojetodesoftware.abstractfactory.SeletorFabricaRepository;
import com.br.estimativadeprojetodesoftware.builder.AndroidBuilder;
import com.br.estimativadeprojetodesoftware.builder.Diretor;
import com.br.estimativadeprojetodesoftware.builder.IosBuilder;
import com.br.estimativadeprojetodesoftware.builder.WebBackEndBuilder;
import com.br.estimativadeprojetodesoftware.model.Perfil;
import com.br.estimativadeprojetodesoftware.model.Subject;
import com.br.estimativadeprojetodesoftware.presenter.Observer;
import com.br.estimativadeprojetodesoftware.repository.IPerfilRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 *
 * @author tetzner
 */
public class PerfilRepositoryService implements Subject {

    private static PerfilRepositoryService INSTANCIA = null;
    private final FabricaRepository fabricaDAO;
    private final IPerfilRepository perfilRepository;
    private final List<Observer> observers;

    private PerfilRepositoryService() {
        fabricaDAO = SeletorFabricaRepository.obterInstancia();
        perfilRepository = fabricaDAO.criarPerfilRepository();
        this.observers = new ArrayList<>();
    }

    public static PerfilRepositoryService getInstancia() {
        if (INSTANCIA == null) {
            INSTANCIA = new PerfilRepositoryService();
        }

        return INSTANCIA;
    }

    public void salvar(Perfil perfil){
        perfilRepository.salvar(perfil);
        notifyObservers();
    }

    public void atualizar(Perfil perfil){
        perfilRepository.atualizar(perfil);
        notifyObservers();
    }

    public void removerPorId(UUID id){
        perfilRepository.removerPorId(id);
        notifyObservers();
    }

    public Optional<Perfil> buscarPorId(UUID id){
        return perfilRepository.buscarPorId(id);
    }
    
    public List<Perfil> buscarPerfisPorProjeto(UUID idProjeto){
        return perfilRepository.buscarPerfisPorProjeto(idProjeto);
    }

    public List<Perfil> buscarTodos(){
      return perfilRepository.buscarTodos();
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
