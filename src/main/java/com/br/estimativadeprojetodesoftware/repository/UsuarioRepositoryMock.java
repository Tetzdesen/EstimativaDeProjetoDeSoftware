package com.br.estimativadeprojetodesoftware.repository;

import com.br.estimativadeprojetodesoftware.model.Perfil;
import com.br.estimativadeprojetodesoftware.model.Projeto;
import com.br.estimativadeprojetodesoftware.model.Subject;
import com.br.estimativadeprojetodesoftware.model.Usuario;
import com.br.estimativadeprojetodesoftware.presenter.Observer;

import java.util.*;

/**
 *
 * @author tetzner
 */
public class UsuarioRepositoryMock implements Subject {

    private final List<Usuario> usuarios;
    private final List<Observer> observers;

    public UsuarioRepositoryMock() {
        usuarios = new ArrayList<>();
        observers = new ArrayList<>();

        // Usuário 1
        Usuario usuario1 = new Usuario("Alice Silva", "alice@email.com", "senha123");
        usuario1.adicionarPerfil(new Perfil("Desenvolvedor Back-End"));
        usuario1.adicionarProjeto(new Projeto("Sistema ERP", "Alice Silva", "Web/Back-End"));
        usuarios.add(usuario1);

        // Usuário 2
        Usuario usuario2 = new Usuario("Carlos Souza", "carlos@email.com", "senha456");
        usuario2.adicionarPerfil(new Perfil("Desenvolvedor Mobile"));
        usuario2.adicionarPerfil(new PerfilRepositoryMock().getPerfis().get(0));
        usuario2.adicionarPerfil(new PerfilRepositoryMock().getPerfis().get(1));
        usuario2.adicionarProjeto(new Projeto("App Delivery", "Carlos Souza", "Android"));
        usuarios.add(usuario2);
       
    }

    // Retorna a lista completa de usuários
    public List<Usuario> getUsuarios() {
        return new ArrayList<>(usuarios); // Retorna uma cópia para evitar modificações externas
    }

    // Busca um usuário pelo e-mail
    public Usuario getUsuarioPorEmail(String email) {
        return usuarios.stream()
                .filter(usuario -> usuario.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }

    // Adiciona um novo usuário
    public void adicionarUsuario(String nome, String email, String senha) {
        if (getUsuarioPorEmail(email) != null) {
            throw new IllegalArgumentException("Já existe um usuário com este e-mail.");
        }

        Usuario novoUsuario = new Usuario(nome, email, senha);
        usuarios.add(novoUsuario);
        notifyObservers();
    }

    // Remove um usuário pelo e-mail
    public boolean removerUsuarioPorEmail(String email) {
        boolean removido = usuarios.removeIf(usuario -> usuario.getEmail().equals(email));
        if (removido) {
            notifyObservers();
        }
        return removido;
    }

    // Atualiza os dados de um usuário existente
    public boolean atualizarUsuario(Usuario usuario, String novoEmail, String novoNome) {

        if (usuario == null) {
            return false; // Usuário não encontrado
        }

        for (int i = 0; i < usuarios.size(); i++) {
            Usuario usuarioExistente = usuarios.get(i);
            if (usuarioExistente.getId().equals(usuario.getId())) {
                usuarios.remove(i);
                notifyObservers();
                return true;
            }
        }

        notifyObservers();
        return true;
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
