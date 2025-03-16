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
    public void calcularEstimativa(UUID idProjeto, String perfilNome, Campo campo, List<EstimativaFuncionalidade> estimativas) {
        if (isCampoFixo(campo.getTipo())) {
            estimativas.add(new EstimativaFuncionalidade(
                    idProjeto,
                    campo.getNome(),
                    campo.getDias().intValue(),
                    "campo fixo",
                    campo.getTipo(),
                    perfilNome
            ));
        }
    }

    private boolean isCampoFixo(String tipo) {
        return !tipo.equalsIgnoreCase("tamanho")
                && !tipo.equalsIgnoreCase("nivel")
                && !tipo.equalsIgnoreCase("funcionalidade");
    }
}
