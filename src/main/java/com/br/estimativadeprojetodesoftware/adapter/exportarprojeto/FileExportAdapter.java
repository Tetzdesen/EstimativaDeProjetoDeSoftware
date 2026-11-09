package com.br.estimativadeprojetodesoftware.adapter.exportarprojeto;

/**
 * Interface alvo do Adapter de documentos paginados usada pela exportação em
 * PDF. Coordenadas em pontos, com a origem no canto inferior esquerdo da
 * página; cores em RGB de 0 a 255.
 */
public interface FileExportAdapter {

    void createDocument();

    /** Acrescenta uma página ao fim do documento e passa a desenhar nela. */
    void addPage();

    void addText(String text, float x, float y, String fontName, float fontSize);

    /** Texto colorido (ex.: rótulos em cinza, valor em destaque). */
    void addText(String text, float x, float y, String fontName, float fontSize, int r, int g, int b);

    float getPageHeight();

    float getPageWidth();

    /** Largura do texto na fonte e no tamanho informados, para alinhar à direita. */
    float getTextWidth(String text, String fontName, float fontSize);

    void fillRect(float x, float y, float width, float height, int r, int g, int b);

    void drawLine(float x1, float y1, float x2, float y2, float width, int r, int g, int b);

    int getPageCount();

    /** Volta a desenhar numa página já criada (índice a partir de 0), por exemplo para os rodapés. */
    void selectPage(int index);

    void save(String filePath);

    void close();
}
