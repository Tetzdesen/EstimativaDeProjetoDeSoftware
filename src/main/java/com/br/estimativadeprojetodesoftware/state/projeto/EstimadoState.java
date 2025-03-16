package com.br.estimativadeprojetodesoftware.state.projeto;

import com.br.estimativadeprojetodesoftware.model.Projeto;

/**
 *
 * @author tetzner
 */
public class EstimadoState extends ProjetoState {

    public EstimadoState(Projeto projeto) {
        super(projeto);
    }

    @Override
    public void cancelarEstimativa() {
        System.out.println("Estimativa cancelada. O projeto volta ao estado 'Não Estimado'.");
        projeto.setStatus("Não estimado");
        projeto.setEstado(new NaoEstimadoState(projeto));
    }

    @Override
    public void compartilharProjeto() {
        System.out.println("Estimativa cancelada. O projeto volta ao estado 'Não Estimado'.");
    }

    @Override
    public void exportarProjeto() {
        System.out.println("Estimativa cancelada. O projeto volta ao estado 'Não Estimado'.");
    }

    @Override
    public String toString() {
        return "Estimado";
    }

}
