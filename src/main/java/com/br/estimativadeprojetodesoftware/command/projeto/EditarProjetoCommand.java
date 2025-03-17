package com.br.estimativadeprojetodesoftware.command.projeto;

import com.br.estimativadeprojetodesoftware.model.Projeto;
import com.br.estimativadeprojetodesoftware.service.ProjetoService;
import com.br.estimativadeprojetodesoftware.command.Command;
import com.br.estimativadeprojetodesoftware.model.RegistroOperacao;
import com.br.estimativadeprojetodesoftware.service.LogService;
import com.br.estimativadeprojetodesoftware.singleton.UsuarioLogadoSingleton;

/**
 *
 * @author tetzner
 */
public class EditarProjetoCommand implements Command {

    private final ProjetoService projetoService;
    private final Projeto projeto;

    public EditarProjetoCommand(ProjetoService projetoService, Projeto projeto) {
        this.projetoService = projetoService;
        this.projeto = projeto;
    }

    @Override
    public void execute() {
        projetoService.atualizar(projeto);
        RegistroOperacao registro = new RegistroOperacao("Edição de projeto", UsuarioLogadoSingleton.getInstancia().getUsuario().getNome());
        LogService.getInstancia().escreverMensagem(registro.formatarLog());
    }
}
