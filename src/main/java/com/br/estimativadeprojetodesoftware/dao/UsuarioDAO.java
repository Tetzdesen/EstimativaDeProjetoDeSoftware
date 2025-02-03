package com.br.estimativadeprojetodesoftware.dao;

import java.util.List;

import com.br.estimativadeprojetodesoftware.model.Usuario;

public abstract class UsuarioDAO {
    public abstract void inserir (Usuario usuario);
    public abstract Usuario buscarPorId(int id);
    public abstract List<Usuario> listarTodos();
    public abstract void atualizar(Usuario usuario);
    public abstract void remover(int id);
}
