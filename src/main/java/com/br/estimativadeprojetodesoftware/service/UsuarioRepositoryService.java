package com.br.estimativadeprojetodesoftware.service;

import com.br.estimativadeprojetodesoftware.abstractfactory.FabricaRepository;
import com.br.estimativadeprojetodesoftware.abstractfactory.SeletorFabricaRepository;
import com.br.estimativadeprojetodesoftware.model.Subject;
import com.br.estimativadeprojetodesoftware.model.Usuario;
import com.br.estimativadeprojetodesoftware.presenter.Observer;
import com.br.estimativadeprojetodesoftware.repository.IUsuarioRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 *
 * @author tetzner
 */
public class UsuarioRepositoryService implements Subject {

    private static UsuarioRepositoryService INSTANCIA = null;
    private final FabricaRepository fabricaDAO;
    private final IUsuarioRepository usuarioRepository;
    private final List<Observer> observers;

    private UsuarioRepositoryService() {
        this.fabricaDAO = SeletorFabricaRepository.obterInstancia();
        this.usuarioRepository = fabricaDAO.criarUsuarioRepository();
        this.observers = new ArrayList<>();
    }

    public static UsuarioRepositoryService getInstancia() {
        if (INSTANCIA == null) {
            INSTANCIA = new UsuarioRepositoryService();
        }

        return INSTANCIA;
    }

    public void salvar(Usuario usuario) {
        usuarioRepository.salvar(usuario);
        notifyObservers();
    }

    public void atualizar(Usuario usuario) {
        usuarioRepository.atualizar(usuario);
        notifyObservers();
    }

    public void removerPorId(UUID id) {
        usuarioRepository.removerPorId(id);
        notifyObservers();
    }

    public Optional<Usuario> buscarPorId(UUID id) {
        return usuarioRepository.buscarPorId(id);
    }
    
    public Optional<Usuario> buscarPorEmail(String email){
        return usuarioRepository.buscarPorEmail(email);
    }

    public List<Usuario> buscarTodos() {
        return usuarioRepository.buscarTodos();
    }

    public List<Usuario> buscarUsuariosPorProjeto(UUID idProjeto) {
        return usuarioRepository.buscarUsuariosPorProjeto(idProjeto);
    }

    // Implementação do padrão Observer
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
