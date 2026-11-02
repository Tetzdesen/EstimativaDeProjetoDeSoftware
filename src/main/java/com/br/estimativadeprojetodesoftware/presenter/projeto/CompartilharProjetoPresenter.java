package com.br.estimativadeprojetodesoftware.presenter.projeto;

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

    private void carregar() {
        candidatos = service.candidatos(projeto.getId(), usuario);
        view.getListaCandidatos().setListData(candidatos.stream().map(u -> u.getNome() + " <" + u.getEmail() + ">").toArray(String[]::new));
        view.getListaDestinatarios().setListData(service.destinatarios(projeto.getId()).stream()
                .map(u -> u.getNome() + " <" + u.getEmail() + ">").toArray(String[]::new));
        view.getBtnCompartilhar().setEnabled(!candidatos.isEmpty());
    }

    private void compartilhar() {
        int i = view.getListaCandidatos().getSelectedIndex();
        try {
            if (i < 0) {
                throw new IllegalArgumentException("Selecione o usuário que vai receber o projeto.");
            }
            Usuario destinatario = candidatos.get(i);
            service.compartilhar(projeto.getId(), destinatario.getId(), usuario);
            ExibirMensagemCommand.sucesso("Projeto compartilhado com " + destinatario.getNome() + " (somente leitura).");
            carregar();
        } catch (RuntimeException ex) {
            ExibirMensagemCommand.erro(ex.getMessage());
        }
    }

    public CompartilharProjetoView getView() {
        return view;
    }
}
