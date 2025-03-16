/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.br.estimativadeprojetodesoftware.chain.calculoestimativa;

import com.br.estimativadeprojetodesoftware.model.Campo;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author tetzner
 */
public class EstimativaCampoFixoHandler implements EstimativaHandler {

    @Override
    public void calcularEstimativa(UUID idProjeto, Campo campo, List<EstimativaFuncionalidade> estimativas) {
        if (isCampoFixo(campo.getTipo())) {
            estimativas.add(new EstimativaFuncionalidade(
                    idProjeto,
                    campo.getNome(),
                    campo.getDias().intValue(),
                    campo.getTipo()
            ));
        }
    }

    private boolean isCampoFixo(String tipo) {
        return !tipo.equalsIgnoreCase("tamanho")
                && !tipo.equalsIgnoreCase("nivel")
                && !tipo.equalsIgnoreCase("funcionalidade");
    }
}
