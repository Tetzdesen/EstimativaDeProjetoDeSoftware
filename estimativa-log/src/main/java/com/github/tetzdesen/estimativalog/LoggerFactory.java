package com.github.tetzdesen.estimativalog;

import java.nio.file.Path;

/**
 * Cria o adaptador do formato escolhido. O arquivo recebe a extensão do formato
 * (por exemplo, "logs/estimativa" vira "logs/estimativa.csv").
 */
public final class LoggerFactory {

    private LoggerFactory() {
    }

    public static Logger criar(FormatoLog formato, Path arquivoSemExtensao) {
        Path arquivo = Path.of(arquivoSemExtensao.toString() + "." + formato.getExtensao());
        return switch (formato) {
            case CSV -> new CsvLoggerAdapter(arquivo);
            case JSON -> new JsonLoggerAdapter(arquivo);
        };
    }
}
