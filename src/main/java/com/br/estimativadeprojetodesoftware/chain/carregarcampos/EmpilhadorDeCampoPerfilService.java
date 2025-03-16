package com.br.estimativadeprojetodesoftware.chain.carregarcampos;

import java.util.List;

import com.br.estimativadeprojetodesoftware.model.PerfilProjeto;

public class EmpilhadorDeCampoPerfilService {
    List<ITipoCampoPerfil> tiposCampoPerfil;

    public EmpilhadorDeCampoPerfilService() {
        this.tiposCampoPerfil = List.of(
            new TipoCampoTamanhoApp(),
            new TipoCampoNivelUI(),
            new TipoCampoFuncionalidade(), 
            new TipoCampoTaxaDiaria()
        );
    }

    public void carregarCampos(PerfilProjeto perfil) {
        tiposCampoPerfil.forEach(tipo -> tipo.carregarCampos(perfil));
    }
}
