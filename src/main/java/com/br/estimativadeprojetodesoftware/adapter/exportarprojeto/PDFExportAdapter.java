package com.br.estimativadeprojetodesoftware.adapter.exportarprojeto;

import java.io.IOException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

/** Adapter do Apache PDFBox para a interface {@link FileExportAdapter}. */
public class PDFExportAdapter implements FileExportAdapter {

    private PDDocument document;
    private PDPageContentStream contentStream;

    @Override
    public void createDocument() {
        document = new PDDocument();
    }

    @Override
    public void addPage() {
        try {
            fecharPaginaAtual();
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);
            contentStream = new PDPageContentStream(document, page);
        } catch (IOException e) {
            throw new IllegalStateException("Erro ao criar a página do PDF: " + e.getMessage(), e);
        }
    }

    @Override
    public void selectPage(int index) {
        try {
            fecharPaginaAtual();
            contentStream = new PDPageContentStream(document, document.getPage(index),
                    PDPageContentStream.AppendMode.APPEND, true, true);
        } catch (IOException e) {
            throw new IllegalStateException("Erro ao voltar à página do PDF: " + e.getMessage(), e);
        }
    }

    @Override
    public int getPageCount() {
        return document.getNumberOfPages();
    }

    @Override
    public void addText(String text, float x, float y, String fontName, float fontSize) {
        addText(text, x, y, fontName, fontSize, 0, 0, 0);
    }

    @Override
    public void addText(String text, float x, float y, String fontName, float fontSize, int r, int g, int b) {
        PDFont font = fonte(fontName);
        try {
            contentStream.beginText();
            contentStream.setNonStrokingColor(r / 255f, g / 255f, b / 255f);
            contentStream.setFont(font, fontSize);
            contentStream.newLineAtOffset(x, y);
            contentStream.showText(compatibilizar(text, font));
            contentStream.endText();
        } catch (IOException e) {
            throw new IllegalStateException("Erro ao escrever no PDF: " + e.getMessage(), e);
        }
    }

    @Override
    public float getTextWidth(String text, String fontName, float fontSize) {
        PDFont font = fonte(fontName);
        try {
            return font.getStringWidth(compatibilizar(text, font)) / 1000f * fontSize;
        } catch (IOException e) {
            throw new IllegalStateException("Erro ao medir texto do PDF: " + e.getMessage(), e);
        }
    }

    @Override
    public void fillRect(float x, float y, float width, float height, int r, int g, int b) {
        try {
            contentStream.setNonStrokingColor(r / 255f, g / 255f, b / 255f);
            contentStream.addRect(x, y, width, height);
            contentStream.fill();
        } catch (IOException e) {
            throw new IllegalStateException("Erro ao desenhar no PDF: " + e.getMessage(), e);
        }
    }

    @Override
    public void drawLine(float x1, float y1, float x2, float y2, float width, int r, int g, int b) {
        try {
            contentStream.setStrokingColor(r / 255f, g / 255f, b / 255f);
            contentStream.setLineWidth(width);
            contentStream.moveTo(x1, y1);
            contentStream.lineTo(x2, y2);
            contentStream.stroke();
        } catch (IOException e) {
            throw new IllegalStateException("Erro ao desenhar no PDF: " + e.getMessage(), e);
        }
    }

    private static PDFont fonte(String nome) {
        return "HELVETICA_BOLD".equals(nome) ? PDType1Font.HELVETICA_BOLD : PDType1Font.HELVETICA;
    }

    /** Troca caracteres que a fonte padrão não codifica por "?". */
    private static String compatibilizar(String texto, PDFont font) {
        StringBuilder sb = new StringBuilder();
        texto.codePoints().forEach(cp -> {
            String c = new String(Character.toChars(cp));
            try {
                font.encode(c);
                sb.append(c);
            } catch (IOException | IllegalArgumentException e) {
                sb.append('?');
            }
        });
        return sb.toString();
    }

    @Override
    public float getPageHeight() {
        return PDRectangle.A4.getHeight();
    }

    @Override
    public float getPageWidth() {
        return PDRectangle.A4.getWidth();
    }

    @Override
    public void save(String filePath) {
        try {
            fecharPaginaAtual();
            document.save(filePath);
        } catch (IOException e) {
            throw new IllegalStateException("Erro ao salvar o PDF: " + e.getMessage(), e);
        }
    }

    private void fecharPaginaAtual() throws IOException {
        if (contentStream != null) {
            contentStream.close();
            contentStream = null;
        }
    }

    @Override
    public void close() {
        try {
            fecharPaginaAtual();
            if (document != null) {
                document.close();
            }
        } catch (IOException e) {
            throw new IllegalStateException("Erro ao fechar o PDF: " + e.getMessage(), e);
        }
    }
}
