/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.br.estimativadeprojetodesoftware.command.projeto;

import com.br.estimativadeprojetodesoftware.command.MostrarMensagemProjetoCommand;
import com.br.estimativadeprojetodesoftware.command.ProjetoCommand;
import com.br.estimativadeprojetodesoftware.repository.ProjetoRepositoryMock;
import javax.swing.JOptionPane;

/**
 *
 * @author tetzner
 */
public class ExcluirProjetoCommand implements ProjetoCommand {

    private final ProjetoRepositoryMock repository;
    private String projetoNome;

    public ExcluirProjetoCommand(ProjetoRepositoryMock repository) {
        this.repository = repository;
    }

    public ExcluirProjetoCommand(ProjetoRepositoryMock repository, String projetoNome) {
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
