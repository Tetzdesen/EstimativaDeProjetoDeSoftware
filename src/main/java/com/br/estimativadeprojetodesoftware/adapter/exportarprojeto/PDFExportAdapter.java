package com.br.estimativadeprojetodesoftware.adapter.exportarprojeto;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

public class PDFExportAdapter implements FileExportAdapter {
    private PDDocument document;
    private PDPageContentStream contentStream;

    @Override
    public void createDocument() {
        document = new PDDocument();
    }

    @Override
    public void addPage() {
        PDPage page = new PDPage();
        document.addPage(page);
        try {
            contentStream = new PDPageContentStream(document, page);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criar o ContentStream: " + e.getMessage());
        }
    }

    @Override
    public void addText(String text, float x, float y, String fontName, float fontSize) {
        try {
            contentStream.beginText();
            // Mapeamento simples de fonte – pode ser expandido conforme a necessidade
            if ("HELVETICA_BOLD".equals(fontName)) {
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, fontSize);
            } else {
                contentStream.setFont(PDType1Font.HELVETICA, fontSize);
            }
            contentStream.newLineAtOffset(x, y);
            contentStream.showText(text);
            contentStream.endText();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao adicionar texto: " + e.getMessage());
        }
    }

    @Override
    public void save(String filePath) {
        try {
            if (contentStream != null) {
                contentStream.close();
            }
            document.save(filePath);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar o documento: " + e.getMessage());
        }
    }

    @Override
    public void close() {
        try {
            document.close();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao fechar o documento: " + e.getMessage());
        }
    }
}
