package com.br.estimativadeprojetodesoftware.builder;

import com.br.estimativadeprojetodesoftware.model.Perfil;

public class Diretor {
    public static Perfil build(PerfilBuilder perfilBuilder) {
        perfilBuilder.addTamanhoApp();
        perfilBuilder.addNivelUI();
        perfilBuilder.addFuncionalidades();
        
        return perfilBuilder.getPerfil();
    }
}
