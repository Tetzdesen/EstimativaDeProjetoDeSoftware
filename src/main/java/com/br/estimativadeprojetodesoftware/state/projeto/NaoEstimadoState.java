package com.br.estimativadeprojetodesoftware.state.projeto;

import com.br.estimativadeprojetodesoftware.model.Projeto;

/**
 *
 * @author tetzner
 */
public class NaoEstimadoState extends ProjetoState {

    public NaoEstimadoState(Projeto projeto) {
        super(projeto);
    }

    @Override
    public void estimar() {
        System.out.println("Projeto foi estimado com sucesso!");
        projeto.setStatus("Estimado");
        projeto.setEstado(new EstimadoState(projeto));
    }

    @Override
    public String toString() {
        return "Não estimado";
    }
}
