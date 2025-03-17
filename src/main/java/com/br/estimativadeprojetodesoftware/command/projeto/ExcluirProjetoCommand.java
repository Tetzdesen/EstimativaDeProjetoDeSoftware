package com.br.estimativadeprojetodesoftware.command.projeto;

import com.br.estimativadeprojetodesoftware.service.ProjetoService;

import javax.swing.*;
import com.br.estimativadeprojetodesoftware.command.Command;
import com.br.estimativadeprojetodesoftware.model.RegistroOperacao;
import com.br.estimativadeprojetodesoftware.service.LogService;
import com.br.estimativadeprojetodesoftware.singleton.UsuarioLogadoSingleton;

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
            throw new IllegalArgumentException("Nome do projeto não definido.");
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
                RegistroOperacao registro = new RegistroOperacao("Exclusão de projeto", UsuarioLogadoSingleton.getInstancia().getUsuario().getNome());
                LogService.getInstancia().escreverMensagem(registro.formatarLog());
            } catch (Exception e) {
                RegistroOperacao registro = new RegistroOperacao("Exclusão de projeto", UsuarioLogadoSingleton.getInstancia().getUsuario().getNome(), "Erro ao remover o projeto \"" + projetoNome + "\"." + e);
                LogService.getInstancia().escreverMensagem(registro.formatarLog());
                throw new RuntimeException("Erro ao remover o projeto \"" + projetoNome + "\"." + e);
            }

        }
    }
}
