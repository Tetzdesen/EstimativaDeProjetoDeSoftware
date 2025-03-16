package com.br.estimativadeprojetodesoftware.state.projeto;

import com.br.estimativadeprojetodesoftware.model.Projeto;

/**
 *
 * @author tetzner
 */
public abstract class ProjetoState {

    protected Projeto projeto;

    public ProjetoState(Projeto projeto) {
        this.projeto = projeto;
    }

    public void estimar() {
        throw new RuntimeException("Não é possível estimar projeto neste estado: " + toString());
    }

    public void cancelarEstimativa() {
        throw new RuntimeException("Não é possível cancelar estimativa neste estado: " + toString());
    }

    public void compartilharProjeto() {
        throw new RuntimeException("Não é possível compartilhar projeto neste estado: " + toString());
    }

    public void exportarProjeto() {
        throw new RuntimeException("Não é possível exportar projeto neste estado: " + toString());
    }

}
