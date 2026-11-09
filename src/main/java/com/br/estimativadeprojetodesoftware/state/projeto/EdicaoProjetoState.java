package com.br.estimativadeprojetodesoftware.state.projeto;

import com.br.estimativadeprojetodesoftware.command.projeto.AbrirCompartilhamentoProjetoCommand;
import com.br.estimativadeprojetodesoftware.command.projeto.AtualizarEstimativaCommand;
import com.br.estimativadeprojetodesoftware.command.projeto.DescartarAlteracoesCommand;
import com.br.estimativadeprojetodesoftware.command.projeto.ExcluirProjetoCommand;
import com.br.estimativadeprojetodesoftware.command.projeto.ExportarProjetoCommand;
import com.br.estimativadeprojetodesoftware.command.projeto.SalvarProjetoCommand;
import com.br.estimativadeprojetodesoftware.presenter.projeto.ManterProjetoPresenter;
import com.br.estimativadeprojetodesoftware.templatemethod.FormatoExportacao;
import com.br.estimativadeprojetodesoftware.view.componentes.Selo;
import com.br.estimativadeprojetodesoftware.view.projeto.ProjetoView;

/**
 * Dono com o projeto aberto: tudo é editável desde a abertura. Salvar e
 * Descartar alterações só ficam habilitados com alteração não salva; exportar
 * vale para o projeto salvo, então fica indisponível enquanto houver alteração.
 */
public class EdicaoProjetoState extends ManterProjetoPresenterState {

    public EdicaoProjetoState(ManterProjetoPresenter presenter) {
        super(presenter);
    }

    @Override
    public void entrar() {
        ProjetoView v = presenter.getView();
        v.getBtnDescartar().setText("Descartar alterações");
        v.getPainelPerfis().setVisible(false);
        v.getPainelEstimativa().setVisible(true);
        v.getPainelSecoes().setVisible(true);
        presenter.setEditavel(true);
        atualizarAcoesDeGravacao(presenter.isAlterado());
        atualizarEstimativa();
    }

    @Override
    public void atualizarAcoesDeGravacao(boolean alterado) {
        ProjetoView v = presenter.getView();
        boolean exporta = !alterado && presenter.getProjeto().isCalculado();
        if (alterado) {
            habilitar(v.getBtnSalvar(), v.getBtnDescartar(), v.getBtnExcluir(), v.getBtnCompartilhar());
            v.exibirSituacao("Não salvo", Selo.Tipo.AVISO, "A estimativa já mostra as alterações. Salve (Ctrl+S) para guardar.");
        } else if (exporta) {
            habilitar(v.getBtnExcluir(), v.getBtnCompartilhar(), v.getBtnExportarPdf(), v.getBtnExportarCsv());
            v.exibirSituacao("Salvo", Selo.Tipo.SUCESSO, "Tudo salvo. A estimativa é atualizada a cada alteração.");
        } else {
            habilitar(v.getBtnExcluir(), v.getBtnCompartilhar());
            v.exibirSituacao("Salvo", Selo.Tipo.NEUTRO, "Marque plataformas e funcionalidades: a estimativa é atualizada a cada alteração.");
        }
    }

    @Override
    public String getNome() {
        return "edição";
    }

    @Override
    public void salvar() {
        new SalvarProjetoCommand(presenter).execute();
    }

    @Override
    public void descartar() {
        new DescartarAlteracoesCommand(presenter).execute();
    }

    @Override
    public void atualizarEstimativa() {
        new AtualizarEstimativaCommand(presenter).execute();
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
