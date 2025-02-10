package com.br.estimativadeprojetodesoftware.command;

import com.br.estimativadeprojetodesoftware.model.Perfil;
import com.br.estimativadeprojetodesoftware.model.Projeto;
import com.br.estimativadeprojetodesoftware.repository.ProjetoRepositoryMock;
import com.br.estimativadeprojetodesoftware.service.CriarProjetoMock;
import java.util.HashMap;
import java.util.List;

import javax.swing.*;
import java.util.Optional;
import java.util.stream.Collectors;

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
                    
                    List<String> nomesPerfis = projeto.getPerfis().stream()
                            .map(Perfil::getNome)
                            .collect(Collectors.toList());

                    repository.adicionarProjeto(
                            projeto.getNome(),
                            projeto.getCriador(),
                            nomesPerfis, 
                            projeto.getEstimativa() != null ? projeto.getEstimativa().getCampos() : new HashMap<>(),
                            projeto.getStatus(),
                            projeto.getCompartilhado(),
                            projeto.getCompartilhadoPor()
                    );

                    new MostrarMensagemProjetoCommand("Projeto \"" + projeto.getNome() + "\" criado com sucesso!").execute();
                },
                () -> new MostrarMensagemProjetoCommand("Falha ao criar o projeto.").execute()
        );
    }
}
