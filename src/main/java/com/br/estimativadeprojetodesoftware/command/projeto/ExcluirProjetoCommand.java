package com.br.estimativadeprojetodesoftware.command.projeto;

import com.br.estimativadeprojetodesoftware.service.ProjetoService;

import javax.swing.*;
import com.br.estimativadeprojetodesoftware.command.Command;

public class ExcluirProjetoCommand implements Command {

    private final ProjetoService projetoService;
    private String projetoNome;

    public ExcluirProjetoCommand(ProjetoService projetoService, String projetoNome) {
        this.projetoService = projetoService;
        this.projetoNome = projetoNome;
    }

    public void setProjetoNome(String projetoNome) {
        this.projetoNome = projetoNome;
    }

    @Override
    public void execute() {
        if (projetoNome == null || projetoNome.isEmpty()) {
            new MostrarMensagemProjetoCommand("Nome do projeto não definido.").execute();
            return;
        }

        int confirmacao = JOptionPane.showConfirmDialog(
                null,
                "Deseja realmente excluir o projeto \"" + projetoNome + "\"?",
                "Confirmar Exclusão",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacao == JOptionPane.YES_OPTION) {
            try {
                projetoService.removerPorId(projetoService.buscarProjetoPorNome(projetoNome).get().getId());
            } catch (Exception e){
                new MostrarMensagemProjetoCommand("Erro ao remover o projeto \"" + projetoNome + "\"." + e).execute();
            }
      
        }
    }
}
