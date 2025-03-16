package com.br.estimativadeprojetodesoftware.command.perfil;

import java.util.ArrayList;
import java.util.List;

import com.br.estimativadeprojetodesoftware.model.Campo;
import com.br.estimativadeprojetodesoftware.model.Perfil;
import com.br.estimativadeprojetodesoftware.service.CampoRepositoryService;

public class CarregarCamposPerfilProjetoCommand implements PerfilProjetoCommand {

    @Override
    public void execute() {
        List<Perfil> perfisNovos = new ArrayList<>();

        for (Perfil perfil : perfis) {

            // buscar nome do campo pelo id do Perfil
            List<Campo> camposTamanhoApp = new CampoRepositoryService().buscarPorIdPerfilTipo(perfil.getId(),
                    "tamanho");

            for (Campo campo : camposTamanhoApp) {
                Double dias = new CampoRepositoryService().buscarDiasPorPerfilCampo(perfil.getId(), campo.getId());
                perfil.adicionarTamanhoApp(campo.getNome(), dias.intValue());

            }

            List<Campo> camposNivelUI = new CampoRepositoryService().buscarPorIdPerfilTipo(perfil.getId(), "nivel");

            for (Campo campo : camposNivelUI) {
                Double dias = new CampoRepositoryService().buscarDiasPorPerfilCampo(perfil.getId(), campo.getId());
                perfil.adicionarNivelUI(campo.getNome(), dias.doubleValue());
            }

            List<Campo> camposFuncionalidades = new CampoRepositoryService().buscarPorIdPerfilTipo(perfil.getId(),
                    "funcionalidade");

            for (Campo campo : camposFuncionalidades) {
                Double dias = new CampoRepositoryService().buscarDiasPorPerfilCampo(perfil.getId(), campo.getId());
                perfil.adicionarFuncionalidade(campo.getNome(), dias.intValue());
            }

            List<Campo> taxasDiarias = new CampoRepositoryService().buscarPorIdPerfilTipo(perfil.getId(),
                    "taxa diária");

            for (Campo campo : taxasDiarias) {
                Double dias = new CampoRepositoryService().buscarDiasPorPerfilCampo(perfil.getId(), campo.getId());
                perfil.adicionarTaxaDiaria(campo.getNome(), dias.doubleValue());
            }

            perfisNovos.add(perfil);
        }

        return perfisNovos;

    }
    
}
