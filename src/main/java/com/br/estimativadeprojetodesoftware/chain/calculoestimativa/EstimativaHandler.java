package com.br.estimativadeprojetodesoftware.chain.calculoestimativa;

import com.br.estimativadeprojetodesoftware.model.Campo;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author tetzner
 */
public interface EstimativaHandler {
    void calcularEstimativa(UUID idProjeto, String perfilNome, Campo campo, List<EstimativaFuncionalidade> estimativas);
}