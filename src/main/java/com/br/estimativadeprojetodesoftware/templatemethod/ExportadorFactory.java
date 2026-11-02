package com.br.estimativadeprojetodesoftware.templatemethod;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Supplier;

/** Factory dos exportadores: um novo formato só precisa ser registrado aqui. */
public final class ExportadorFactory {

    private static final Map<FormatoExportacao, Supplier<ExportadorProjeto>> EXPORTADORES = new EnumMap<>(FormatoExportacao.class);

    static {
        EXPORTADORES.put(FormatoExportacao.PDF, ExportarPDFTemplate::new);
        EXPORTADORES.put(FormatoExportacao.CSV, ExportarCSVTemplate::new);
    }

    private ExportadorFactory() {
    }

    public static ExportadorProjeto criar(FormatoExportacao formato) {
        Supplier<ExportadorProjeto> fabrica = EXPORTADORES.get(formato);
        if (fabrica == null) {
            throw new IllegalArgumentException("Formato de exportação não suportado: " + formato);
        }
        return fabrica.get();
    }
}
