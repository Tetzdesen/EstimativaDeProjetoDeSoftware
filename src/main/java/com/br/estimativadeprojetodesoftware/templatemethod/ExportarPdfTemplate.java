package com.br.estimativadeprojetodesoftware.templatemethod;

import java.io.File;

import javax.swing.JFileChooser;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

public class ExportarPdfTemplate extends ExportadorProjeto {

    @Override
    protected String carregarCaminhoArquivo() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Selecione o diretório para salvar o PDF");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setAcceptAllFileFilterUsed(false);
        
        int userSelection = fileChooser.showSaveDialog(null);
        
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File diretorioSelecionado = fileChooser.getSelectedFile();
            return diretorioSelecionado.getAbsolutePath() 
                    + File.separator 
                    + projeto.getNome() 
                    + ".pdf";
        }
        return null;
    }

    @Override
    protected void gerarArquivo(String filePath) {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = 
                     new PDPageContentStream(document, page)) {

                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 18);
                contentStream.newLineAtOffset(50, 750);
                contentStream.showText("Projeto: " + projeto.getNome());
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.newLineAtOffset(50, 720);
                contentStream.showText("Criador: " + projeto.getCriador());
                contentStream.newLineAtOffset(0, -15);
                contentStream.showText("Tipo: " + projeto.getTipo());
                contentStream.newLineAtOffset(0, -15);
                contentStream.showText("Status: " + projeto.getStatus());

                contentStream.endText();
            }

            document.save(filePath);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao exportar o projeto: " + e.getMessage());
        }
    }
}
