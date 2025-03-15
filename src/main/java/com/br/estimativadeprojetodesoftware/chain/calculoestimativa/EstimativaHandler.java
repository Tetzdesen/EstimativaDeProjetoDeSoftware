package com.br.estimativadeprojetodesoftware.chain.calculoestimativa;

import com.br.estimativadeprojetodesoftware.model.Campo;
import com.br.estimativadeprojetodesoftware.model.Perfil;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author tetzner
 */
public interface EstimativaHandler {
    void calcularEstimativa(UUID idProjeto, Campo campo, List<EstimativaFuncionalidade> estimativas);
}