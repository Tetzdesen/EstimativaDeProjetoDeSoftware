package com.br.estimativadeprojetodesoftware.command;

import com.br.estimativadeprojetodesoftware.repository.ProjetoRepositoryMock;

import javax.swing.*;

public class ExcluirProjetoProjetoCommand implements ProjetoCommand {
    private final ProjetoRepositoryMock repository;
    private String projetoNome;

    public ExcluirProjetoProjetoCommand(ProjetoRepositoryMock repository) {
        this.repository = repository;
    }

    public ExcluirProjetoProjetoCommand(ProjetoRepositoryMock repository, String projetoNome) {
        this.repository = repository;
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
            boolean removido = repository.removerProjetoPorNome(projetoNome);
            if (removido) {
                new MostrarMensagemProjetoCommand("Projeto \"" + projetoNome + "\" removido com sucesso!").execute();
            } else {
                new MostrarMensagemProjetoCommand("Erro ao remover o projeto \"" + projetoNome + "\".").execute();
            }
        }
    }
}
