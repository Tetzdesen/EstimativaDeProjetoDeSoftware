package com.br.estimativadeprojetodesoftware.templatemethod;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import javax.swing.JFileChooser;

import com.br.estimativadeprojetodesoftware.model.Campo;
import com.br.estimativadeprojetodesoftware.model.PerfilProjeto;
import com.br.estimativadeprojetodesoftware.model.Usuario;

public class ExportarCsvTemplate extends ExportadorProjeto {

    @Override
    protected String carregarCaminhoArquivo() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Selecione o arquivo CSV para salvar");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setAcceptAllFileFilterUsed(false);

        int userSelection = fileChooser.showSaveDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File diretorioSelecionado = fileChooser.getSelectedFile();
            String nomeArquivo = projeto.getNome().replaceAll("\\s+", "_") + ".csv";

            File novoArquivo = new File(diretorioSelecionado, nomeArquivo);
            return novoArquivo.getAbsolutePath();
        }
        return null;
    }

    @Override
    protected void gerarArquivo(String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // Cabeçalho do CSV para os dados principais do projeto
            writer.write("Projeto;Criador;Tipo;Status;Data de Criação");
            writer.newLine();
            writer.write(
                projeto.getNome() + ";" +
                projeto.getCriador() + ";" +
                projeto.getTipo() + ";" +
                projeto.getStatus() + ";" +
                projeto.getCreated_at()
            );
            writer.newLine();
            writer.newLine();

            // Exportando Perfis (caso existam)
            List<PerfilProjeto> perfis = projeto.getPerfis();
            if (!perfis.isEmpty()) {
                writer.write("Perfis:");
                writer.newLine();
                writer.write("Nome;É BackEnd");
                writer.newLine();
                for (PerfilProjeto p : perfis) {
                    // Ajuste as colunas conforme os atributos de Perfil que deseja exportar
                    writer.write(p.getNome() + ";" + p.isPerfilBackEnd());
                    writer.newLine();
                }
                writer.newLine();
            }

            // Exportando Usuários (caso existam)
            List<Usuario> usuarios = projeto.getUsuarios();
            if (!usuarios.isEmpty()) {
                writer.write("Usuários:");
                writer.newLine();
                writer.write("Nome;Email;Outros Dados");
                writer.newLine();
                for (Usuario u : usuarios) {
                    // Ajuste as colunas conforme os atributos de Usuario
                    writer.write(u.getNome() + ";" + u.getEmail());
                    writer.newLine();
                }
                writer.newLine();
            }

            // Exportando Campos (caso existam)
            List<Campo> campos = projeto.getCampos();
            if (!campos.isEmpty()) {
                writer.write("Campos:");
                writer.newLine();
                writer.write("Nome;Tipo;Valor");
                writer.newLine();
                for (Campo c : campos) {
                    // Ajuste as colunas conforme os atributos de Campo
                    writer.write(c.getNome() + ";" + c.getTipo() + ";");
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro ao exportar CSV: " + e.getMessage(), e);
        }
    }
}
