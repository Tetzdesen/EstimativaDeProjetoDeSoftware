package com.br.estimativadeprojetodesoftware.command.projeto;

import com.br.estimativadeprojetodesoftware.command.ProjetoCommand;
import com.br.estimativadeprojetodesoftware.model.Projeto;
import com.br.estimativadeprojetodesoftware.service.ProjetoRepositoryService;

/**
 *
 * @author tetzner
 */
public class EditarProjetoProjetoCommand implements ProjetoCommand {
    private final ProjetoRepositoryService projetoService;
    private final Projeto projeto;

    // utilizar um DTO
    public EditarProjetoProjetoCommand(ProjetoRepositoryService projetoService, Projeto projeto) {
        this.projetoService = projetoService;
        this.projeto = projeto;
    }

    @Override
    public void execute() {
        projetoService.atualizar(projeto);
    }
}
