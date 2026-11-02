package com.br.estimativadeprojetodesoftware.command.projeto;

import com.br.estimativadeprojetodesoftware.command.Command;
import com.br.estimativadeprojetodesoftware.presenter.projeto.ManterProjetoPresenter;
import com.br.estimativadeprojetodesoftware.templatemethod.FormatoExportacao;
import java.io.File;
import java.nio.file.Path;

/** Exporta o projeto calculado no formato escolhido. */
public class ExportarProjetoCommand implements Command {

    private final ManterProjetoPresenter presenter;
    private final FormatoExportacao formato;

    public ExportarProjetoCommand(ManterProjetoPresenter presenter, FormatoExportacao formato) {
        this.presenter = presenter;
        this.formato = formato;
    }

    @Override
    public void execute() {
        presenter.getExportacaoService().verificarPodeExportar(presenter.getProjeto(), presenter.getUsuario());
        String sugestao = presenter.getProjeto().getNome().replaceAll("[^\\p{L}\\p{N}._-]+", "_") + "." + formato.getExtensao();
        File arquivo = presenter.getView().escolherArquivo(sugestao);
        if (arquivo == null) {
            return;
        }
        Path gerado = presenter.getExportacaoService().exportar(presenter.getProjeto(), formato, arquivo.toPath(), presenter.getUsuario());
        presenter.sucesso("Relatório gerado em:\n" + gerado.toAbsolutePath());
    }
}
