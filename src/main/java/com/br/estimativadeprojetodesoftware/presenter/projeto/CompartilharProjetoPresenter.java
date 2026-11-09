package com.br.estimativadeprojetodesoftware.presenter.projeto;

import com.br.estimativadeprojetodesoftware.presenter.helpers.FilaBanco;
import com.br.estimativadeprojetodesoftware.command.ExibirMensagemCommand;
import com.br.estimativadeprojetodesoftware.model.Projeto;
import com.br.estimativadeprojetodesoftware.model.Usuario;
import com.br.estimativadeprojetodesoftware.service.CompartilhamentoService;
import com.br.estimativadeprojetodesoftware.singleton.UsuarioLogadoSingleton;
import com.br.estimativadeprojetodesoftware.view.projeto.CompartilharProjetoView;
import java.util.List;

/** Compartilha o projeto com usuários registrados (BR05). */
public class CompartilharProjetoPresenter {

    private final CompartilharProjetoView view;
    private final CompartilhamentoService service;
    private final Projeto projeto;
    private final Usuario usuario;
    private List<Usuario> candidatos = List.of();

    public CompartilharProjetoPresenter(Projeto projeto) {
        this.view = new CompartilharProjetoView();
        this.service = new CompartilhamentoService();
        this.projeto = projeto;
        this.usuario = UsuarioLogadoSingleton.getInstancia().getUsuario();
        view.setTituloCabecalho("Compartilhar \"" + projeto.getNome() + "\"");
        view.getBtnCompartilhar().addActionListener(e -> compartilhar());
        view.getBtnFechar().addActionListener(e -> view.dispose());
        carregar();
    }

    private record Listas(List<Usuario> candidatos, List<Usuario> destinatarios) {
    }

    private void carregar() {
        FilaBanco.executar(view, "Carregando…",
                () -> new Listas(service.candidatos(projeto.getId(), usuario), service.destinatarios(projeto.getId())),
                listas -> {
                    candidatos = listas.candidatos();
                    view.getListaCandidatos().setListData(candidatos.stream().map(u -> u.getNome() + " <" + u.getEmail() + ">").toArray(String[]::new));
                    view.getListaDestinatarios().setListData(listas.destinatarios().stream()
                            .map(u -> u.getNome() + " <" + u.getEmail() + ">").toArray(String[]::new));
                    view.getBtnCompartilhar().setEnabled(!candidatos.isEmpty());
                }, e -> ExibirMensagemCommand.erro(e.getMessage()));
    }

    private void compartilhar() {
        int i = view.getListaCandidatos().getSelectedIndex();
        if (i < 0) {
            ExibirMensagemCommand.erro("Selecione o usuário que vai receber o projeto.");
            return;
        }
        Usuario destinatario = candidatos.get(i);
        FilaBanco.executar(view, "Compartilhando…", () -> {
            service.compartilhar(projeto.getId(), destinatario.getId(), usuario);
            return null;
        }, ok -> {
            ExibirMensagemCommand.sucesso("Projeto compartilhado com " + destinatario.getNome() + " (somente leitura).");
            carregar();
        }, e -> ExibirMensagemCommand.erro(e.getMessage()));
    }

    public CompartilharProjetoView getView() {
        return view;
    }
}
