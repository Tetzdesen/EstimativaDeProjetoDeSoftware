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
    private final Projeto projeto;
    private DetalheProjetoPresenterState estado;

    public DetalheProjetoPresenter(DetalheProjetoView view, String projetoNome) {
        this.view = view;
        this.projetoService = new ProjetoRepositoryService();
        this.projeto = projetoService.buscarProjetoPorNome(projetoNome).get();
        configurarPresenter();

        if (!isProjetoEstimado()) {
            this.estado = new NaoEstimadoState(this);
        } else {
            this.estado = new EstimadoState(this);
        }
        carregarDetalhesProjeto();
    }

    public void configurarPresenter() {
        this.projetoService.addObserver(this);
        configurarListeners();
    }

    private void carregarDetalhesProjeto() {
        // verificar se projeto é != null
        carregarCabecalho(projeto);
        new CarregarDetalhesProjetoProjetoCommand(view, projeto, isProjetoEstimado()).execute();

        view.revalidate();
        view.repaint();
    }

    private void carregarCabecalho(Projeto projeto) {
        view.atualizarCabecalho(
                projeto.getNome(),
                projeto.getCriador(),
                DataHoraService.formatarData(projeto.getCreated_at().toLocalDate()),
                projeto.getTipo(),
                projeto.getStatus()
        );
    }

    private void configurarListeners() {

        view.getBtnEstimar().addActionListener((ActionEvent e) -> {
            estado.estimar();
            carregarDetalhesProjeto();
            JOptionPane.showMessageDialog(view, "Projeto estimado com sucesso!", "Estimativa", JOptionPane.INFORMATION_MESSAGE);

        });

        view.getBtnCancelar().addActionListener((ActionEvent e) -> {

            // CancelarEstimativaProjetoProjetoCommand
            // projeto.cancelarEstimativa();
            estado.cancelarEstimativa();
            carregarDetalhesProjeto();
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

    @Override
    public void update() {
        carregarDetalhesProjeto();
    }

}
