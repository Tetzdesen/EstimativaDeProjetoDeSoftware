package com.br.estimativadeprojetodesoftware.presenter.projeto;

import com.br.estimativadeprojetodesoftware.command.projeto.AbrirCompartilhamentoProjetoCommand;
import com.br.estimativadeprojetodesoftware.command.projeto.AbrirExportarProjetoCommand;
import com.br.estimativadeprojetodesoftware.command.projeto.CarregarDetalhesProjetoProjetoCommand;

import javax.swing.JOptionPane;

import com.br.estimativadeprojetodesoftware.model.Projeto;
import com.br.estimativadeprojetodesoftware.presenter.Observer;
import com.br.estimativadeprojetodesoftware.service.DataHoraService;
import com.br.estimativadeprojetodesoftware.service.EstimaProjetoService;
import com.br.estimativadeprojetodesoftware.service.ProjetoRepositoryService;
import com.br.estimativadeprojetodesoftware.state.projeto.NaoEstimadoState;
import com.br.estimativadeprojetodesoftware.view.projeto.DetalheProjetoView;
import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

public final class DetalheProjetoPresenter implements Observer {

    private final DetalheProjetoView view;
    private final ProjetoRepositoryService projetoService;
    private final Projeto projeto;

    public DetalheProjetoPresenter(DetalheProjetoView view, String projetoNome) {
        this.view = view;
        this.projetoService = new ProjetoRepositoryService();
        this.projeto = projetoService.buscarProjetoPorNome(projetoNome).get();
        projeto.setEstado(new NaoEstimadoState(projeto));
        configurarPresenter();
        carregarDetalhesProjeto();
    }

    public void configurarPresenter() {
        this.projetoService.addObserver(this);
        this.view.getBtnEstimar().setEnabled(true);
        this.view.getBtnCancelar().setEnabled(false);
        this.view.getBtnCompartilhar().setEnabled(false);
        this.view.getBtnExportar().setEnabled(false);
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

    @Override
    public void update() {
        carregarDetalhesProjeto();
    }

    private void configurarListeners() {

        view.getBtnEstimar().addActionListener((ActionEvent e) -> {
            if (projeto != null) {

                //  RealizarEstimativaProjetoProjetoCommand
                projeto.estimarProjeto();
                carregarDetalhesProjeto();
                atualizarEstadoBotoes();
                JOptionPane.showMessageDialog(view, "Projeto estimado com sucesso!", "Estimativa", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        view.getBtnCancelar().addActionListener((ActionEvent e) -> {
            if (projeto != null) {

                // CancelarEstimativaProjetoProjetoCommand
                projeto.cancelarEstimativa();
                carregarDetalhesProjeto();
                atualizarEstadoBotoes();
                JOptionPane.showMessageDialog(view, "Estimativa cancelada!", "Cancelamento", JOptionPane.WARNING_MESSAGE);
            }
        });

        view.getBtnCompartilhar().addActionListener((ActionEvent e) -> {
            if (projeto != null) {
                new AbrirCompartilhamentoProjetoCommand(projetoService, projeto.getNome()).execute();
            }
        });

        view.getBtnExportar().addActionListener((ActionEvent e) -> {
            if (projeto != null) {
                new AbrirExportarProjetoCommand(projeto.getNome()).execute();
            }
        });

    }

    private void atualizarEstadoBotoes() {
        boolean isEstimado = isProjetoEstimado();

        view.getBtnEstimar().setEnabled(!isEstimado);
        view.getBtnCancelar().setEnabled(isEstimado);
        view.getBtnCompartilhar().setEnabled(isEstimado);
        view.getBtnExportar().setEnabled(isEstimado);
    }

    private boolean isProjetoEstimado() {
        return projeto.getStatus().equalsIgnoreCase("Estimado");
    }
}
