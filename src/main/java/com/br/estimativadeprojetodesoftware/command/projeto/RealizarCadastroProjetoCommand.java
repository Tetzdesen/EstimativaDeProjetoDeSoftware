package com.br.estimativadeprojetodesoftware.command.projeto;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.swing.ListModel;

import com.br.estimativadeprojetodesoftware.command.Command;
import com.br.estimativadeprojetodesoftware.model.Campo;
import com.br.estimativadeprojetodesoftware.model.PerfilProjeto;
import com.br.estimativadeprojetodesoftware.model.Projeto;
import com.br.estimativadeprojetodesoftware.presenter.projeto.CadastroProjetoPresenter;
import com.br.estimativadeprojetodesoftware.service.CampoService;
import com.br.estimativadeprojetodesoftware.singleton.UsuarioLogadoSingleton;

/**
 *
 * @author tetzner
 */
public class RealizarCadastroProjetoCommand implements Command {

    private final CadastroProjetoPresenter presenter;
    private CampoService campoService;

    public RealizarCadastroProjetoCommand(CadastroProjetoPresenter presenter) {
        this.presenter = presenter;
        this.campoService = new CampoService();
    }

    @Override
    public void execute() {
        String nomeProjeto = presenter.getView().getTxtNome().getText().trim();

        String tamanho = (String) presenter.getView().getCbmTamanhoApp().getSelectedItem();

        String nivel = (String) presenter.getView().getCbmNivelUI().getSelectedItem();

        if (nomeProjeto.isEmpty()) {
            throw new IllegalArgumentException("Nome é um campo obrigatório!");
        }

        if (presenter.getView().getJListPerfis().getModel().getSize() == 0) {
            throw new IllegalArgumentException("Adicione no mínimo um perfil obrigatório!");
        }

        Optional<Projeto> optionalProjeto = criarProjeto(nomeProjeto, tamanho, nivel);

        if (optionalProjeto.isPresent()) {
            new SalvarProjetoCommand(presenter.getProjetoService(), optionalProjeto.get()).execute();
            presenter.exibirMensagem("Projeto criado com sucesso!");
            presenter.getView().dispose();
        } else {
            throw new RuntimeException("Erro ao criar o projeto!");
        }
    }

    private Optional<Projeto> criarProjeto(String nomeProjeto, String tamanho, String nivel) {
        String nome = nomeProjeto;
        String criador = UsuarioLogadoSingleton.getInstancia().getUsuario().getNome();

        Projeto projeto = new Projeto(nome, criador);
        projeto.adicionarUsuario(UsuarioLogadoSingleton.getInstancia().getUsuario());

        ListModel<String> listModel = presenter.getView().getJListPerfis().getModel();
        for (int i = 0; i < listModel.getSize(); i++) {
            String item = listModel.getElementAt(i);

            projeto.adicionarPerfil(presenter.getPerfilService().buscarPorId(presenter.getPerfisSelecionados().get(item).getId()).get());
        }

        String tiposConcatenados = projeto.getPerfis().stream()
                .map(PerfilProjeto::getNome)
                .collect(Collectors.joining(", "));

        projeto.setTipo(tiposConcatenados);

        for (PerfilProjeto perfil : projeto.getPerfis()) {

            List<Campo> tamanhos = campoService.buscarPorIdPerfilTipo(perfil.getId(), "tamanho");
            List<Campo> nivelUI = campoService.buscarPorIdPerfilTipo(perfil.getId(), "nivel");
            List<Campo> funcionalidades = campoService.buscarPorIdPerfilTipo(perfil.getId(), "funcionalidade");
            List<Campo> taxasDiarias = campoService.buscarPorIdPerfilTipo(perfil.getId(), "taxa diária");

            for (Campo campo : tamanhos) {
                if (campo.getNome().equalsIgnoreCase(tamanho)) {
                    projeto.adicionarCampo(campo);
                }
            }

            for (Campo campo : nivelUI) {
                if (campo.getNome().equalsIgnoreCase(nivel)) {
                    projeto.adicionarCampo(campo);
                }
            }

            for (Campo campo : funcionalidades) {
                projeto.adicionarCampo(campo);
            }

            for (Campo campo : taxasDiarias) {
                projeto.adicionarCampo(campo);
            }

            List<Campo> camposFixos = campoService.listarTodosPorTipo("campo fixo");
            for (Campo campo : camposFixos) {
                campo.setDias(campoService.buscarDiasPorPerfilCampo(perfil.getId(), campo.getId()));
                projeto.adicionarCampo(campo);

            }
        }
        return Optional.of(projeto);
    }

}
