package com.br.estimativadeprojetodesoftware.adapter.exportarprojeto;

/** Interface alvo do Adapter de documentos paginados usada pela exportação em PDF. */
public interface FileExportAdapter {

    void createDocument();

    void addPage();

    void addText(String text, float x, float y, String fontName, float fontSize);

    float getPageHeight();

    void save(String filePath);

    void close();
}
