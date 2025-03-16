package com.br.estimativadeprojetodesoftware.templatemethod;

import java.io.File;
import javax.swing.JFileChooser;

import com.br.estimativadeprojetodesoftware.adapter.exportarprojeto.FileExportAdapter;
import com.br.estimativadeprojetodesoftware.adapter.exportarprojeto.PDFExportAdapter;

public class ExportarPdfTemplate extends ExportadorProjeto {
    
    private FileExportAdapter adapter;

    public ExportarPdfTemplate() {
        adapter = new PDFExportAdapter();
    }

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
                    + projeto.getNome().replaceAll("\\s+", "_")
                    + ".pdf";
        }
        return null;
    }

    @Override
    protected void gerarArquivo(String filePath) {
        try {
            adapter.createDocument();
            adapter.addPage();
            adapter.addText("Projeto: " + projeto.getNome(), 50, 750, "HELVETICA_BOLD", 18);
            adapter.addText("Criador: " + projeto.getCriador(), 50, 720, "HELVETICA", 12);
            adapter.addText("Tipo: " + projeto.getTipo(), 50, 700, "HELVETICA", 12);
            adapter.addText("Status: " + projeto.getStatus(), 50, 680, "HELVETICA", 12);
            adapter.save(filePath);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao exportar o projeto: " + e.getMessage(), e);
        } finally {
            try {
                adapter.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
