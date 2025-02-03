package com.br.estimativadeprojetodesoftware.model;

import java.time.LocalDateTime;
import java.util.List;

public class Usuario {
    private int id;
    private String nome;
    private String email;
    private String senha;
    private boolean isAutorizado;
    private LocalDateTime created_at;
    private LocalDateTime deleted_at;
    private List<Projeto> projetos;
    private List<Perfil> perfis;

    public Usuario(int id, String nome, String email, String senha, boolean isAutorizado) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.isAutorizado = isAutorizado;

        this.created_at = LocalDateTime.now();
    }



}
