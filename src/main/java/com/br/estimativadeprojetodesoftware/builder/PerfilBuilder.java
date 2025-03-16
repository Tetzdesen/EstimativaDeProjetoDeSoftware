package com.br.estimativadeprojetodesoftware.builder;

import com.br.estimativadeprojetodesoftware.model.PerfilProjeto;
import com.br.estimativadeprojetodesoftware.model.Usuario;

public abstract class PerfilBuilder {
    protected PerfilProjeto perfil;

    public PerfilBuilder(String nomePerfil, Usuario usuario) {
        this.perfil = new PerfilProjeto(nomePerfil, usuario);
    }

    public final PerfilProjeto getPerfil() {
        return perfil;
    }

    public abstract void addTamanhoApp();
    public abstract void addNivelUI();
    public abstract void addFuncionalidades();
    public abstract void addTaxasDiarias();
}
