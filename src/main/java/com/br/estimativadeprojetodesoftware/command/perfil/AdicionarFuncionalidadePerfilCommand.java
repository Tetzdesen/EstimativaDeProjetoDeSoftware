package com.br.estimativadeprojetodesoftware.command.perfil;

import com.br.estimativadeprojetodesoftware.command.Command;
import com.br.estimativadeprojetodesoftware.command.PerguntarCommand;
import com.br.estimativadeprojetodesoftware.model.Funcionalidade;
import com.br.estimativadeprojetodesoftware.model.Plataforma;
import com.br.estimativadeprojetodesoftware.model.TipoSelecao;
import com.br.estimativadeprojetodesoftware.model.TipoValor;
import com.br.estimativadeprojetodesoftware.presenter.perfil.ManterPerfilPresenter;
import java.math.BigDecimal;

/** Acrescenta uma funcionalidade nova (múltipla escolha, 0 dias) para o usuário editar na tabela. */
public class AdicionarFuncionalidadePerfilCommand implements Command {

    private final ManterPerfilPresenter presenter;

    public AdicionarFuncionalidadePerfilCommand(ManterPerfilPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void execute() {
        String categoria = PerguntarCommand.perguntar("Nova funcionalidade", "Categoria da nova funcionalidade:");
        if (categoria == null || categoria.isBlank()) {
            return;
        }
        String nome = PerguntarCommand.perguntar("Nova funcionalidade", "Nome da nova funcionalidade:");
        if (nome == null || nome.isBlank()) {
            return;
        }
        Funcionalidade f = new Funcionalidade(categoria, nome, TipoSelecao.MULTIPLA, TipoValor.DIAS,
                presenter.getPerfil().proximaOrdem());
        for (Plataforma p : Plataforma.values()) {
            f.setValor(p, BigDecimal.ZERO);
        }
        presenter.getPerfil().adicionarFuncionalidade(f);
        presenter.atualizarTabelas();
    }
}
