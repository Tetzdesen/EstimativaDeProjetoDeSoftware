package com.br.estimativadeprojetodesoftware.service;

import com.br.estimativadeprojetodesoftware.abstractfactory.SeletorFabricaRepository;
import com.br.estimativadeprojetodesoftware.repository.IConfiguracaoRepository;
import com.github.tetzdesen.estimativalog.FormatoLog;
import com.github.tetzdesen.estimativalog.LogRegistro;
import com.github.tetzdesen.estimativalog.Logger;
import com.github.tetzdesen.estimativalog.LoggerFactory;
import java.nio.file.Path;

/**
 * Fachada de log usada pelos services. Cada formato (CSV, JSON) é um Adapter do
 * módulo estimativa-log; o formato vem da tabela configuracao e pode ser trocado
 * em tempo de execução. Falhas ao gravar o log não interrompem a operação.
 */
public class LogService implements RegistroOperacoes {

    private static LogService instancia;

    private final IConfiguracaoRepository configuracao;
    private final Path arquivoBase;
    private Logger logger;

    public LogService(IConfiguracaoRepository configuracao, Path arquivoBase) {
        this.configuracao = configuracao;
        this.arquivoBase = arquivoBase;
        String salvo = configuracao.buscar(IConfiguracaoRepository.CHAVE_FORMATO_LOG).orElse(FormatoLog.JSON.name());
        FormatoLog formato;
        try {
            formato = FormatoLog.deTexto(salvo);
        } catch (IllegalArgumentException e) {
            formato = FormatoLog.JSON;
        }
        this.logger = LoggerFactory.criar(formato, arquivoBase);
    }

    public static synchronized LogService getInstancia() {
        if (instancia == null) {
            instancia = new LogService(SeletorFabricaRepository.obterInstancia().criarConfiguracaoRepository(), caminhoArquivo());
        }
        return instancia;
    }

    /** Troca o formato e salva a escolha; a próxima operação já usa o novo formato. */
    public synchronized void setFormato(FormatoLog formato) {
        if (formato == null) {
            throw new IllegalArgumentException("Escolha o formato do log");
        }
        configuracao.salvar(IConfiguracaoRepository.CHAVE_FORMATO_LOG, formato.name());
        this.logger = LoggerFactory.criar(formato, arquivoBase);
    }

    public synchronized FormatoLog getFormato() {
        return logger.getFormato();
    }

    @Override
    public void sucesso(String operacao, String nomeProjeto, String usuario) {
        registrar(LogRegistro.sucesso(operacao, nomeProjeto, usuario));
    }

    @Override
    public void falha(String operacao, String nomeProjeto, String usuario, String mensagem) {
        registrar(LogRegistro.falha(operacao, nomeProjeto, usuario, mensagem));
    }

    private synchronized void registrar(LogRegistro registro) {
        try {
            logger.registrar(registro);
        } catch (RuntimeException e) {
            System.err.println("Não foi possível gravar o log: " + e.getMessage());
        }
    }

    public Path getArquivoAtual() {
        return Path.of(arquivoBase + "." + getFormato().getExtensao());
    }

    private static Path caminhoArquivo() {
        String caminho = DotenvService.getValor("LOG_ARQUIVO");
        return Path.of(caminho == null ? "logs/estimativa" : caminho);
    }
}
