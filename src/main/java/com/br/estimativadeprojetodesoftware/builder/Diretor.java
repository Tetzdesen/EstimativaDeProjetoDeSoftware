package com.br.estimativadeprojetodesoftware.builder;

import com.br.estimativadeprojetodesoftware.model.PerfilProjeto;

public class Diretor {
    public static PerfilProjeto build(PerfilBuilder perfilBuilder) {
        perfilBuilder.addTamanhoApp();
        perfilBuilder.addNivelUI();
        perfilBuilder.addFuncionalidades();
        perfilBuilder.addTaxasDiarias();
        
        return perfilBuilder.getPerfil();
    }
}
