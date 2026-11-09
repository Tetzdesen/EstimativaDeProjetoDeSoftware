package com.br.estimativadeprojetodesoftware.state.projeto;

import com.br.estimativadeprojetodesoftware.command.projeto.AbrirCompartilhamentoProjetoCommand;
import com.br.estimativadeprojetodesoftware.command.projeto.EditarProjetoCommand;
import com.br.estimativadeprojetodesoftware.command.projeto.ExcluirProjetoCommand;
import com.br.estimativadeprojetodesoftware.command.projeto.ExportarProjetoCommand;
import com.br.estimativadeprojetodesoftware.presenter.projeto.ManterProjetoPresenter;
import com.br.estimativadeprojetodesoftware.view.componentes.Selo;
import com.br.estimativadeprojetodesoftware.templatemethod.FormatoExportacao;
import com.br.estimativadeprojetodesoftware.view.projeto.ProjetoView;

/** Dono vendo o projeto: editar, excluir, compartilhar e (se calculado) exportar. */
public class VisualizacaoProjetoState extends ManterProjetoPresenterState {

    public VisualizacaoProjetoState(ManterProjetoPresenter presenter) {
        super(presenter);
    }

    @Override
    public void entrar() {
        ProjetoView v = presenter.getView();
        if (presenter.getProjeto().isCalculado()) {
            habilitar(v.getBtnEditar(), v.getBtnExcluir(), v.getBtnCompartilhar(),
                    v.getBtnExportarPdf(), v.getBtnExportarCsv());
        } else {
            habilitar(v.getBtnEditar(), v.getBtnExcluir(), v.getBtnCompartilhar());
        }
        v.getPainelPerfis().setVisible(false);
        v.getPainelEstimativa().setVisible(true);
        v.getPainelSecoes().setVisible(true);
        presenter.setEditavel(false);
        if (presenter.getProjeto().isCalculado()) {
            v.exibirSituacao("Calculado", Selo.Tipo.SUCESSO, "Estimativa calculada. Veja a aba Detalhamento ou exporte o relatório.");
        } else {
            v.exibirSituacao("Não calculado", Selo.Tipo.NEUTRO, "Clique em Editar para marcar as funcionalidades; a estimativa é atualizada a cada alteração.");
        }
    }

    @Override
    public String getNome() {
        return "visualização";
    }

    @Override
    public void editar() {
        new EditarProjetoCommand(presenter).execute();
    }

    @Override
    public void excluir() {
        new ExcluirProjetoCommand(presenter).execute();
    }

    @Override
    public void compartilhar() {
        new AbrirCompartilhamentoProjetoCommand(presenter).execute();
    }

    @Override
    public void exportar(FormatoExportacao formato) {
        new ExportarProjetoCommand(presenter, formato).execute();
    }
}
