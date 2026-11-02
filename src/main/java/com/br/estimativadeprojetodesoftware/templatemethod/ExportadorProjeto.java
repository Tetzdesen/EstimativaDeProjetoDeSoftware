package com.br.estimativadeprojetodesoftware.templatemethod;

import com.br.estimativadeprojetodesoftware.model.ItemProjeto;
import com.br.estimativadeprojetodesoftware.model.Projeto;
import com.br.estimativadeprojetodesoftware.model.ResultadoEstimativa;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Template Method da exportação: valida, gera o arquivo no formato da subclasse
 * e confere se o arquivo foi gravado. Novos formatos são novas subclasses
 * registradas no {@link ExportadorFactory}, sem alterar as existentes.
 */
public abstract class ExportadorProjeto {

    public final Path exportar(Projeto projeto, ResultadoEstimativa resultado, Path destino) {
        if (projeto == null || resultado == null || destino == null) {
            throw new IllegalArgumentException("Projeto, resultado e arquivo de destino são obrigatórios.");
        }
        Path arquivo = ajustarExtensao(destino);
        try {
            Path pai = arquivo.toAbsolutePath().getParent();
            if (pai != null) {
                Files.createDirectories(pai);
            }
            gerarArquivo(projeto, resultado, arquivo);
            if (!Files.exists(arquivo) || Files.size(arquivo) == 0) {
                throw new IllegalStateException("O arquivo não foi gerado corretamente.");
            }
        } catch (IOException e) {
            throw new IllegalStateException("Erro ao gravar " + arquivo + ": " + e.getMessage(), e);
        }
        return arquivo;
    }

    private Path ajustarExtensao(Path destino) {
        String ext = "." + getFormato().getExtensao();
        String nome = destino.getFileName().toString();
        return nome.toLowerCase().endsWith(ext) ? destino : destino.resolveSibling(nome + ext);
    }

    protected static List<ItemProjeto> selecionados(Projeto projeto) {
        return projeto.getItens().stream().filter(ItemProjeto::isSelecionado).toList();
    }

    public abstract FormatoExportacao getFormato();

    protected abstract void gerarArquivo(Projeto projeto, ResultadoEstimativa resultado, Path arquivo) throws IOException;
}
