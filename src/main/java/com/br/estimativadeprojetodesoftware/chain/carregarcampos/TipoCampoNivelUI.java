package com.br.estimativadeprojetodesoftware.chain.carregarcampos;

import java.util.List;

import com.br.estimativadeprojetodesoftware.model.Campo;
import com.br.estimativadeprojetodesoftware.model.PerfilProjeto;
import com.br.estimativadeprojetodesoftware.service.CampoService;

public class TipoCampoNivelUI implements ITipoCampoPerfil {

    @Override
    public void carregarCampos(PerfilProjeto perfil) {
        CampoService campoService = new CampoService();
        List<Campo> campos = campoService.buscarPorIdPerfilTipo(perfil.getId(), "nivel");
        if (campos != null) {
            campos.forEach(campo -> {
                Double dias = campoService.buscarDiasPorPerfilCampo(perfil.getId(), campo.getId());
                perfil.adicionarNivelUI(campo.getNome(), dias.doubleValue());
            });
        
        }

    }
    
}
