package com.br.estimativadeprojetodesoftware.command.projeto;

import com.br.estimativadeprojetodesoftware.command.ProjetoCommand;
import com.br.estimativadeprojetodesoftware.model.Campo;
import com.br.estimativadeprojetodesoftware.model.PerfilProjeto;
import com.br.estimativadeprojetodesoftware.model.Projeto;
import com.br.estimativadeprojetodesoftware.presenter.projeto.CadastroProjetoPresenter;
import com.br.estimativadeprojetodesoftware.presenter.projeto.EdicaoProjetoPresenter;
import com.br.estimativadeprojetodesoftware.service.CampoRepositoryService;
import com.br.estimativadeprojetodesoftware.singleton.UsuarioLogadoSingleton;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.swing.ListModel;

/**
 *
 * @author tetzner
 */
public class RealizarEdicaoProjetoProjetoCommand implements ProjetoCommand {
    
    private final EdicaoProjetoPresenter presenter;

    public RealizarEdicaoProjetoProjetoCommand(EdicaoProjetoPresenter presenter) {
        this.presenter = presenter;
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

        // pegar do DTO
        Optional<Projeto> optionalProjeto = criarProjeto(nomeProjeto, tamanho, nivel);

        if (optionalProjeto.isPresent()) {
            new EditarProjetoProjetoCommand(presenter.getProjetoService(), optionalProjeto.get()).execute();
            presenter.exibirMensagem("Projeto editado com sucesso!");
            presenter.getView().dispose();
        } else {
            throw new RuntimeException("Erro ao editar o projeto!");
        }
    }

    private Optional<Projeto> criarProjeto(String nomeProjeto, String tamanho, String nivel) {
        String nome = nomeProjeto;
        String criador = UsuarioLogadoSingleton.getInstancia().getUsuario().getNome();

        Projeto projeto = new Projeto(presenter.getProjetoAtual().getId(), nome, presenter.getProjetoAtual().getStatus(), criador);
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
            // Criando campos para estimativa

            List<Campo> tamanhos = new CampoRepositoryService().buscarPorIdPerfilTipo(perfil.getId(), "tamanho");
            List<Campo> nivelUI = new CampoRepositoryService().buscarPorIdPerfilTipo(perfil.getId(), "nivel");
            List<Campo> funcionalidades = new CampoRepositoryService().buscarPorIdPerfilTipo(perfil.getId(), "funcionalidade");
            List<Campo> taxasDiarias = new CampoRepositoryService().buscarPorIdPerfilTipo(perfil.getId(), "taxa diária");

            for (Campo campo : tamanhos) {
                if (campo.getNome().equalsIgnoreCase(tamanho)) {
                    //  campo.setDias(new CampoRepositoryService().buscarDiasPorPerfilCampo(perfil.getId(), campo.getId()));
                    projeto.adicionarCampo(campo);
                }
            }

            for (Campo campo : nivelUI) {
                if (campo.getNome().equalsIgnoreCase(nivel)) {
                    //   campo.setDias(new CampoRepositoryService().buscarDiasPorPerfilCampo(perfil.getId(), campo.getId()));
                    projeto.adicionarCampo(campo);
                }
            }

            for (Campo campo : funcionalidades) {
                //campo.setDias(new CampoRepositoryService().buscarDiasPorPerfilCampo(perfil.getId(), campo.getId()));
                projeto.adicionarCampo(campo);
            }

            for (Campo campo : taxasDiarias) {
                // campo.setDias(new CampoRepositoryService().buscarDiasPorPerfilCampo(perfil.getId(), campo.getId()));
                projeto.adicionarCampo(campo);
            }

            List<Campo> camposFixos = new CampoRepositoryService().listarTodosPorTipo("campo fixo");
            for (Campo campo : camposFixos) {
                campo.setDias(new CampoRepositoryService().buscarDiasPorPerfilCampo(perfil.getId(), campo.getId()));
                projeto.adicionarCampo(campo);

            }
        }
        //  System.out.println(projeto.getCampos());
        // System.out.println(projeto.getCampos());
        return Optional.of(projeto);
    }
}
