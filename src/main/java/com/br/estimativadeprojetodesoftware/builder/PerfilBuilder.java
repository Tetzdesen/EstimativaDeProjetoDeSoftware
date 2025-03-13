package com.br.estimativadeprojetodesoftware.builder;

import com.br.estimativadeprojetodesoftware.model.Perfil;
import com.br.estimativadeprojetodesoftware.model.Usuario;

public abstract class PerfilBuilder {
    protected Perfil perfil;

    public PerfilBuilder(String nomePerfil, Usuario usuario) {
        this.perfil = new Perfil(nomePerfil, usuario);
    }

    public final Perfil getPerfil() {
        return perfil;
    }

    public abstract void addTamanhoApp();
    public abstract void addNivelUI();
    public abstract void addFuncionalidades();
    public abstract void addTaxasDiarias();
}
