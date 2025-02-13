package com.br.estimativadeprojetodesoftware.repository;

import com.br.estimativadeprojetodesoftware.model.Perfil;
import com.br.estimativadeprojetodesoftware.model.Projeto;
import com.br.estimativadeprojetodesoftware.model.Subject;
import com.br.estimativadeprojetodesoftware.model.Usuario;
import com.br.estimativadeprojetodesoftware.presenter.Observer;

import java.time.LocalDateTime;
import java.util.*;

/**
 *
 * @author tetzner
 */

public class UsuarioRepositoryMock /* implements Subject */ {
    private final List<Usuario> usuarios;
    private final List<Observer> observers;

    public UsuarioRepositoryMock() {
        usuarios = new ArrayList<>();
        observers = new ArrayList<>();

        // Usuário 1
        Usuario usuario1 = new Usuario("Alice Silva", "alice@email.com", "senha123");
        usuario1.setIsAutorizado(true);
        usuario1.adicionarPerfil(new Perfil("Desenvolvedor Back-End"));
        usuario1.adicionarProjeto(new Projeto("Sistema ERP", "Alice Silva", "Web/Back-End"));
        usuarios.add(usuario1);

        // Usuário 2
        Usuario usuario2 = new Usuario("Carlos Souza", "carlos@email.com", "senha456");
        usuario2.adicionarPerfil(new Perfil("Desenvolvedor Mobile"));
        usuario2.adicionarProjeto(new Projeto("App Delivery", "Carlos Souza", "Android"));
        usuarios.add(usuario2);
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public Usuario getUsuarioPorEmail(String email) {
        return usuarios.stream()
                .filter(usuario -> usuario.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }

    public void adicionarUsuario(String nome, String email, String senha) {
        Usuario novoUsuario = new Usuario(nome, email, senha);
       // novoUsuario.setIsAutorizado(isAutorizado);

       // for (String tipo : perfis) {
           // novoUsuario.adicionarPerfil(new Perfil(tipo));
        //}

        usuarios.add(novoUsuario);
        //notifyObservers();
    }

    public boolean removerUsuarioPorEmail(String email) {
        boolean removido = usuarios.removeIf(usuario -> usuario.getEmail().equals(email));
        if (removido) {
            //notifyObservers();
        }
        return removido;
    }

   // @Override
   // public void addObserver(Observer observer) {
       // observers.add(observer);
   // }

   // @Override
   // public void removeObserver(Observer observer) {
        //observers.remove(observer);
   // }

    /*
    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update(usuarios);
        }
    } */
}
