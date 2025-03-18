package com.br.estimativadeprojetodesoftware.templatemethod;

import java.io.File;

import com.br.estimativadeprojetodesoftware.model.Projeto;
import com.br.estimativadeprojetodesoftware.service.ProjetoService;

/**
 * Template Method que organiza o fluxo de exportação:
 * 1) Carrega o Projeto (ou recebe como parâmetro, se preferir)
 * 2) Pergunta ao usuário onde deseja salvar
 * 3) Gera o arquivo no formato desejado
 */

public abstract class ExportadorProjeto {
    protected Projeto projeto;

    public final void exportar(String projetoNome) {
        carregarProjeto(projetoNome);

        String filePath = carregarCaminhoArquivo();
        if (filePath == null) {
            throw new IllegalArgumentException("Arquivo nulo");
        }

        try {
            gerarArquivo(filePath);
            File arquivo = new File(filePath);
            if (!arquivo.exists() || arquivo.length() <= 0) {
                throw new RuntimeException("O arquivo não foi gerado corretamente.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao exportar projeto: " + e.getMessage(), e);
        }
    }

    private void carregarProjeto(String projetoNome) {
        this.projeto = new ProjetoService()
                .buscarProjetoPorNome(projetoNome)
                .orElseThrow(() -> new RuntimeException("Projeto não encontrado"));
    }

    protected abstract String carregarCaminhoArquivo();
    protected abstract void gerarArquivo(String filePath);
}
