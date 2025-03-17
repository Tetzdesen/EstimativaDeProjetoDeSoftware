package com.br.estimativadeprojetodesoftware.presenter.projeto;

import com.br.estimativadeprojetodesoftware.command.projeto.CarregarDetalhesProjetoProjetoCommand;
import com.br.estimativadeprojetodesoftware.model.Projeto;
import com.br.estimativadeprojetodesoftware.presenter.Observer;
import com.br.estimativadeprojetodesoftware.service.DataHoraService;
import com.br.estimativadeprojetodesoftware.service.ProjetoRepositoryService;
import com.br.estimativadeprojetodesoftware.state.projeto.DetalheProjetoPresenterState;
import com.br.estimativadeprojetodesoftware.state.projeto.EstimadoState;
import com.br.estimativadeprojetodesoftware.state.projeto.NaoEstimadoState;
import com.br.estimativadeprojetodesoftware.view.projeto.DetalheProjetoView;
import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

public final class DetalheProjetoPresenter implements Observer {

    private final DetalheProjetoView view;
    private final ProjetoRepositoryService projetoService;
    private Projeto projeto;
    private DetalheProjetoPresenterState estado;

    public DetalheProjetoPresenter(DetalheProjetoView view, ProjetoRepositoryService projetoService, String projetoNome) {
        this.view = view;
        this.projetoService = projetoService;
        this.projeto = projetoService.buscarProjetoPorNome(projetoNome).get();

        if (!isProjetoEstimado()) {
            this.estado = new NaoEstimadoState(this);
        } else {
            this.estado = new EstimadoState(this);
        }
        configurarPresenter();
        carregarDetalhesProjeto();
    }

    public void configurarPresenter() {
       // this.projetoService.addObserver(this);
        configurarListeners();
    }

    private void carregarDetalhesProjeto() {
        carregarCabecalho(projeto);
        new CarregarDetalhesProjetoProjetoCommand(view, projeto, isProjetoEstimado()).execute();

        view.revalidate();
        view.repaint();
    }

    private void carregarCabecalho(Projeto projeto) {
        String status = estado.toString();
        view.atualizarCabecalho(
                projeto.getNome(),
                projeto.getCriador(),
                DataHoraService.formatarData(projeto.getCreated_at().toLocalDate()),
                projeto.getTipo(),
                status
        );
    }

    private void configurarListeners() {

        view.getBtnEstimar().addActionListener((ActionEvent e) -> {
            estado.estimar();
            JOptionPane.showMessageDialog(view, "Projeto estimado com sucesso!", "Estimativa", JOptionPane.INFORMATION_MESSAGE);

        });

        view.getBtnCancelar().addActionListener((ActionEvent e) -> {

            estado.cancelarEstimativa();
            update();
            JOptionPane.showMessageDialog(view, "Estimativa cancelada!", "Cancelamento", JOptionPane.WARNING_MESSAGE);

        });

        view.getBtnCompartilhar().addActionListener((ActionEvent e) -> {
            estado.compartilharProjeto();
        });

        view.getBtnExportar().addActionListener((ActionEvent e) -> {

            estado.exportarProjeto();
        });

    }

    public boolean isProjetoEstimado() {
        return projeto.getStatus().equalsIgnoreCase("Estimado");
    }

    public DetalheProjetoView getView() {
        return view;
    }

    public ProjetoRepositoryService getProjetoService() {
        return projetoService;
    }

    public Projeto getProjeto() {
        return projeto;
    }

    public DetalheProjetoPresenterState getEstado() {
        return estado;
    }

    public void setEstado(DetalheProjetoPresenterState estado) {
        this.estado = estado;
    }

    public void setProjeto(Projeto projeto) {
        this.projeto = projeto;
    }

    @Override
    public void update() {
        carregarDetalhesProjeto();
    }

}
