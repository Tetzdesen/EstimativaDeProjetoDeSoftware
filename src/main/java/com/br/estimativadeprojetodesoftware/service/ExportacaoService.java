package com.br.estimativadeprojetodesoftware.service;

import com.br.estimativadeprojetodesoftware.model.Projeto;
import com.br.estimativadeprojetodesoftware.model.ResultadoEstimativa;
import com.br.estimativadeprojetodesoftware.model.Usuario;
import com.br.estimativadeprojetodesoftware.templatemethod.ExportadorFactory;
import com.br.estimativadeprojetodesoftware.templatemethod.FormatoExportacao;
import java.nio.file.Path;

/**
 * Exportação do projeto calculado (BR06, BR10). Recusa projetos sem cálculo e
 * projetos de outro dono (compartilhados). Registra a operação no log.
 */
public class ExportacaoService {

    private final CalculoEstimativaService calculo;
    private final RegistroOperacoes log;

    public ExportacaoService(CalculoEstimativaService calculo, RegistroOperacoes log) {
        this.calculo = calculo;
        this.log = log;
    }

    public ExportacaoService() {
        this(new CalculoEstimativaService(), LogService.getInstancia());
    }

    public void verificarPodeExportar(Projeto projeto, Usuario usuario) {
        if (usuario == null || !projeto.isDono(usuario.getId())) {
            throw new IllegalStateException("A exportação não está disponível para projetos compartilhados com você.");
        }
        if (!projeto.isCalculado()) {
            throw new IllegalStateException("A estimativa precisa ser calculada antes de exportar o projeto.");
        }
    }

    public Path exportar(Projeto projeto, FormatoExportacao formato, Path destino, Usuario usuario) {
        String operacao = OperacaoLog.EXPORTACAO + " em " + formato.name();
        String nomeUsuario = usuario == null ? "desconhecido" : usuario.getNome();
        try {
            verificarPodeExportar(projeto, usuario);
            ResultadoEstimativa resultado = calculo.calcular(projeto);
            Path arquivo = ExportadorFactory.criar(formato).exportar(projeto, resultado, destino);
            log.sucesso(operacao, projeto.getNome(), nomeUsuario);
            return arquivo;
        } catch (RuntimeException e) {
            log.falha(operacao, projeto.getNome(), nomeUsuario, e.getMessage());
            throw e;
        }
    }
}
