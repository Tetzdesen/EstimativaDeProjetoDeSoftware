package com.br.estimativadeprojetodesoftware.command;

import com.br.estimativadeprojetodesoftware.model.Projeto;
import com.br.estimativadeprojetodesoftware.repository.ProjetoRepositoryMock;
import com.br.estimativadeprojetodesoftware.service.CriarProjetoMock;

import javax.swing.*;
import java.util.Optional;

public class CriarProjetoProjetoCommand implements ProjetoCommand {
    private final ProjetoRepositoryMock repository;
    private final JDesktopPane desktop;
    private final CriarProjetoMock criarProjetoMock;

    public CriarProjetoProjetoCommand(ProjetoRepositoryMock repository, JDesktopPane desktop) {
        this.repository = repository;
        this.desktop = desktop;
        this.criarProjetoMock = new CriarProjetoMock(repository);
    }

    @Override
    public void execute() {
        Optional<Projeto> projetoCriado = criarProjetoMock.criarProjetoAleatorio();

        projetoCriado.ifPresentOrElse(
                projeto -> {
                    repository.adicionarProjeto(
                            projeto.getNome(),
                            projeto.getCriador(),
                            projeto.getCreated_at().toString(),
                            projeto.getStatus(),
                            projeto.getCompartilhado(),
                            projeto.getCompartilhadoPor(),
                            projeto.getPerfis(),
                            projeto.getEstimativa().getCampos()
                    );
                    new MostrarMensagemProjetoCommand("Projeto \"" + projeto.getNome() + "\" criado com sucesso!").execute();
                },
                () -> new MostrarMensagemProjetoCommand("Falha ao criar o projeto.").execute());
    }
}
