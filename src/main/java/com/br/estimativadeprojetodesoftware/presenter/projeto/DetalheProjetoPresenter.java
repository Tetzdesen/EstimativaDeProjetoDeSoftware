package com.br.estimativadeprojetodesoftware.presenter.projeto;

import com.br.estimativadeprojetodesoftware.command.projeto.CarregarDetalhesProjetoCommand;
import com.br.estimativadeprojetodesoftware.model.Projeto;
import com.br.estimativadeprojetodesoftware.observer.Observer;
import com.br.estimativadeprojetodesoftware.service.DataHoraService;
import com.br.estimativadeprojetodesoftware.service.EstimaProjetoService;
import com.br.estimativadeprojetodesoftware.service.ProjetoService;
import com.br.estimativadeprojetodesoftware.singleton.UsuarioLogadoSingleton;
import com.br.estimativadeprojetodesoftware.state.projeto.DetalheProjetoPresenterState;
import com.br.estimativadeprojetodesoftware.state.projeto.EstimadoState;
import com.br.estimativadeprojetodesoftware.state.projeto.NaoEstimadoState;
import com.br.estimativadeprojetodesoftware.view.projeto.DetalheProjetoView;
import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

public final class DetalheProjetoPresenter implements Observer {

    private final DetalheProjetoView view;
    private final ProjetoService projetoService;
    private final EstimaProjetoService estimaService;
    private Projeto projeto;
    private DetalheProjetoPresenterState estado;

    public DetalheProjetoPresenter(DetalheProjetoView view, ProjetoService projetoService, String projetoNome) {
        this.view = view;
        this.projetoService = projetoService;
        this.projeto = projetoService.buscarProjetoPorNome(projetoNome).get();
        this.estimaService = new EstimaProjetoService();
        if (!isProjetoEstimado()) {
            this.estado = new NaoEstimadoState(this);
        } else {
            this.estado = new EstimadoState(this);
        }
        verificarBotoesDeProjetoCompartilhado();
        configurarPresenter();
        update();
    }

    public void configurarPresenter() {
        this.projetoService.addObserver(this);
        configurarListeners();
    }

    private void carregarCabecalho(Projeto projeto) {
        String status = projeto.getStatus();
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
          //  projeto.setStatus("Estimado");
            // update();
            JOptionPane.showMessageDialog(view, "Projeto estimado com sucesso!", "Estimativa", JOptionPane.INFORMATION_MESSAGE);

        });

        view.getBtnCancelar().addActionListener((ActionEvent e) -> {

            estado.cancelarEstimativa();
          //  projeto.setStatus("Não estimado");
            // update();
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

    public ProjetoService getProjetoService() {
        return projetoService;
    }

    public Projeto getProjeto() {
        return projeto;
    }

    public DetalheProjetoPresenterState getEstado() {
        return estado;
    }

    public EstimaProjetoService getEstimaService() {
        return estimaService;
    }
 
    public void setEstado(DetalheProjetoPresenterState estado) {
        if (estado == null) {
            throw new IllegalArgumentException("Estado nulo");
        }
        this.estado = estado;
    }

    public void setProjeto(Projeto projeto) {
        if (projeto == null) {
            throw new IllegalArgumentException("Projeto nulo");
        }
        this.projeto = projeto;
    }

    public void verificarBotoesDeProjetoCompartilhado() {
        if (!(projeto.getCriador().equalsIgnoreCase(UsuarioLogadoSingleton.getInstancia().getUsuario().getNome()))) {
            view.getBtnEstimar().setEnabled(false);
            view.getBtnCancelar().setEnabled(false);
            view.getBtnCompartilhar().setEnabled(false);
            view.getBtnExportar().setEnabled(false);
        }
    }

    private void recarregarProjeto() {
        this.projeto = projetoService.buscarProjetoPorNome(projeto.getNome()).orElse(projeto);
        carregarCabecalho(projeto);
        new CarregarDetalhesProjetoCommand(view, projeto, isProjetoEstimado()).execute();
        view.revalidate();
        view.repaint();
    }

    @Override
    public void update() {
        recarregarProjeto();
    }
}
