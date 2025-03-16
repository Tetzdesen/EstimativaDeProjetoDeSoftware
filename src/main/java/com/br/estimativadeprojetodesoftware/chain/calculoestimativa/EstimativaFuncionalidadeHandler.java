package com.br.estimativadeprojetodesoftware.chain.calculoestimativa;

import com.br.estimativadeprojetodesoftware.model.Campo;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author tetzner
 */
public class EstimativaFuncionalidadeHandler  implements EstimativaHandler {

    @Override
    public void calcularEstimativa(UUID idProjeto, String perfilNome, Campo campo, List<EstimativaFuncionalidade> estimativas) {
        if (campo.getTipo().equalsIgnoreCase("funcionalidade")) {
            EstimativaFuncionalidade estimativa = new EstimativaFuncionalidade(
                    idProjeto,
                    campo.getNome(),
                    campo.getDias().intValue(),
                    "desenvolvimento",
                    campo.getTipo(),
                    perfilNome
            );
            estimativas.add(estimativa);
        } 
    }
}
