package com.br.estimativadeprojetodesoftware.service;

import com.br.estimativadeprojetodesoftware.abstractfactory.FabricaRepository;
import com.br.estimativadeprojetodesoftware.abstractfactory.SeletorFabricaRepository;
import com.br.estimativadeprojetodesoftware.observer.Subject;
import com.br.estimativadeprojetodesoftware.model.Usuario;
import com.br.estimativadeprojetodesoftware.observer.Observer;
import com.br.estimativadeprojetodesoftware.repository.IUsuarioRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 *
 * @author tetzner
 */
public class UsuarioService implements Subject {

    private final FabricaRepository fabricaDAO;
    private final IUsuarioRepository usuarioRepository;
    private final List<Observer> observers;

    public UsuarioService() {
        this.fabricaDAO = SeletorFabricaRepository.obterInstancia();
        this.usuarioRepository = fabricaDAO.criarUsuarioRepository();
        this.observers = new ArrayList<>();
    }

    public void salvar(Usuario usuario) {
        usuarioRepository.salvar(usuario);
        notifyObservers();
    }

    public void atualizar(Usuario usuario) {
        usuarioRepository.atualizar(usuario);
        notifyObservers();
    }

    public boolean removerPorId(UUID id) {
        boolean removido = usuarioRepository.removerPorId(id);
        notifyObservers();
        return removido;
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
    
    public Map<String, String> buscarEmailESenhaDeUsuarios() {
        return usuarioRepository.buscarEmailESenhaDeUsuarios();
    }

    public List<Usuario> buscarUsuariosPorProjeto(UUID idProjeto) {
        return usuarioRepository.buscarUsuariosPorProjeto(idProjeto);
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
