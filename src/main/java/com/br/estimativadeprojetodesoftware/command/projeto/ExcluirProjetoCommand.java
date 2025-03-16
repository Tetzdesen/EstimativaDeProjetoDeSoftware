package com.br.estimativadeprojetodesoftware.command.projeto;

import com.br.estimativadeprojetodesoftware.command.ProjetoCommand;
import com.br.estimativadeprojetodesoftware.service.ProjetoRepositoryService;

import javax.swing.*;

public class ExcluirProjetoCommand implements ProjetoCommand {

    private final ProjetoRepositoryService projetoService;
    private String projetoNome;

    public ExcluirProjetoCommand(ProjetoRepositoryService projetoService, String projetoNome) {
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
