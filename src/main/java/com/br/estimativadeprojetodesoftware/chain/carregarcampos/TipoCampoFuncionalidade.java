package com.br.estimativadeprojetodesoftware.chain.carregarcampos;

import java.util.List;

import com.br.estimativadeprojetodesoftware.model.Campo;
import com.br.estimativadeprojetodesoftware.model.PerfilProjeto;
import com.br.estimativadeprojetodesoftware.service.CampoRepositoryService;

public class TipoCampoFuncionalidade implements ITipoCampoPerfil {
    @Override
    public void carregarCampos(PerfilProjeto perfil) {
        CampoRepositoryService campoService = new CampoRepositoryService();
        List<Campo> campos = campoService.buscarPorIdPerfilTipo(perfil.getId(), "funcionalidade");
        if (campos != null) {
            campos.forEach(campo -> {
                Double dias = campoService.buscarDiasPorPerfilCampo(perfil.getId(), campo.getId());
                perfil.adicionarFuncionalidade(campo.getNome(), dias.intValue());
            });
        
        }

    }
}
