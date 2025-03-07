package com.br.estimativadeprojetodesoftware.builder;

import com.br.estimativadeprojetodesoftware.model.Perfil;

public abstract class PerfilBuilder {
    protected Perfil perfil;

    public PerfilBuilder(String nomePerfil) {
        this.perfil = new Perfil(nomePerfil);
    }

    public final Perfil getPerfil() {
        return perfil;
    }

    public abstract void addTamanhoApp();
    public abstract void addNivelUI();
    public abstract void addFuncionalidades();
    public abstract void addTaxasDiarias();
}
