package com.github.tetzdesen.estimativalog;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Adapter que grava cada registro como uma linha CSV com campos separados por ";".
 */
public class CsvLoggerAdapter implements Logger {

    public static final String SEPARADOR = ";";
    public static final String CABECALHO = String.join(SEPARADOR,
            "data", "hora", "usuario", "operacao", "projeto", "status", "falha", "mensagem");

    private final Path arquivo;

    public CsvLoggerAdapter(Path arquivo) {
        this.arquivo = arquivo;
    }

    @Override
    public synchronized void registrar(LogRegistro registro) {
        try {
            criarDiretorio();
            boolean novo = Files.notExists(arquivo) || Files.size(arquivo) == 0;
            StringBuilder conteudo = new StringBuilder();
            if (novo) {
                conteudo.append(CABECALHO).append(System.lineSeparator());
            }
            conteudo.append(paraLinha(registro)).append(System.lineSeparator());
            Files.writeString(arquivo, conteudo, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException | UncheckedIOException e) {
            throw new LogException("Não foi possível gravar o log CSV em " + arquivo, e);
        }
    }

    static String paraLinha(LogRegistro r) {
        List<String> campos = List.of(
                r.getData(), r.getHora(), r.getUsuario(), r.getOperacao(), r.getNomeProjeto(),
                r.isFalha() ? "FALHA" : "SUCESSO",
                r.getMensagemFalha() == null ? "" : r.getMensagemFalha(),
                r.getMensagem());
        return campos.stream().map(CsvLoggerAdapter::escapar).collect(Collectors.joining(SEPARADOR));
    }

    private static String escapar(String campo) {
        if (campo.contains(SEPARADOR) || campo.contains("\"") || campo.contains("\n") || campo.contains("\r")) {
            return "\"" + campo.replace("\"", "\"\"") + "\"";
        }
        return campo;
    }

    private void criarDiretorio() throws IOException {
        Path pai = arquivo.toAbsolutePath().getParent();
        if (pai != null) {
            Files.createDirectories(pai);
        }
    }

    @Override
    public FormatoLog getFormato() {
        return FormatoLog.CSV;
    }

    public Path getArquivo() {
        return arquivo;
    }
}
