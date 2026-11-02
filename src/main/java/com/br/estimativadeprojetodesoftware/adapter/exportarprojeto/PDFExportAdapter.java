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
    public void addText(String text, float x, float y, String fontName, float fontSize) {
        PDFont font = "HELVETICA_BOLD".equals(fontName) ? PDType1Font.HELVETICA_BOLD : PDType1Font.HELVETICA;
        try {
            contentStream.beginText();
            contentStream.setFont(font, fontSize);
            contentStream.newLineAtOffset(x, y);
            contentStream.showText(compatibilizar(text, font));
            contentStream.endText();
        } catch (IOException e) {
            throw new IllegalStateException("Erro ao escrever no PDF: " + e.getMessage(), e);
        }
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
