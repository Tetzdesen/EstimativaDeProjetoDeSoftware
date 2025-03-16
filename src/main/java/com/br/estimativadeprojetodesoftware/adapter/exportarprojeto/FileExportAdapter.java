package com.br.estimativadeprojetodesoftware.adapter.exportarprojeto;

public interface FileExportAdapter {
    void createDocument();
    void addPage();
    void addText(String text, float x, float y, String fontName, float fontSize);
    void save(String filePath);
    void close();
}