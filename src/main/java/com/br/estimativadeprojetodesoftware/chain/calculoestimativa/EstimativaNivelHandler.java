package com.br.estimativadeprojetodesoftware.chain.calculoestimativa;

import com.br.estimativadeprojetodesoftware.model.Campo;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author tetzner
 */
public class EstimativaNivelHandler implements EstimativaHandler {
    
    @Override
    public void calcularEstimativa(UUID idProjeto, Campo campo, List<EstimativaFuncionalidade> estimativas) {
        if (campo.getTipo().equalsIgnoreCase("nivel")) {
            EstimativaFuncionalidade estimativa = new EstimativaFuncionalidade(
                    idProjeto,
                    campo.getNome(),
                    campo.getDias().intValue(),
                    "designer"
            );
            estimativas.add(estimativa);
        } 
    }

}
