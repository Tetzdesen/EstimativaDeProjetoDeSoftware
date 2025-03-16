package com.br.estimativadeprojetodesoftware.presenter.projeto;

import com.br.estimativadeprojetodesoftware.command.projeto.MostrarMensagemProjetoCommand;
import com.br.estimativadeprojetodesoftware.command.projeto.RealizarEdicaoProjetoProjetoCommand;
import com.br.estimativadeprojetodesoftware.model.Campo;
import com.br.estimativadeprojetodesoftware.model.PerfilProjeto;
import com.br.estimativadeprojetodesoftware.model.Projeto;
import com.br.estimativadeprojetodesoftware.model.Usuario;
import com.br.estimativadeprojetodesoftware.service.CampoRepositoryService;
import com.br.estimativadeprojetodesoftware.service.PerfilRepositoryService;
import com.br.estimativadeprojetodesoftware.service.ProjetoRepositoryService;
import com.br.estimativadeprojetodesoftware.singleton.UsuarioLogadoSingleton;
import com.br.estimativadeprojetodesoftware.view.projeto.EdicaoProjetoView;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.ListModel;

/**
 *
 * @author Hiago Lopes
 */
public class EdicaoProjetoPresenter {

    private final ProjetoRepositoryService projetoService;
    private PerfilRepositoryService perfilService;
    private EdicaoProjetoView view;
    private String nomeProjeto;
    private Usuario usuario;
    private Map<String, PerfilProjeto> perfisSelecionados;
    private Projeto projetoAtual;

    public EdicaoProjetoPresenter(ProjetoRepositoryService projetoService, EdicaoProjetoView view, String nomeProjeto) {
        this.projetoService = projetoService;
        this.perfilService = new PerfilRepositoryService();
        this.view = view;
        this.nomeProjeto = nomeProjeto;
        this.usuario = UsuarioLogadoSingleton.getInstancia().getUsuario();
        this.perfisSelecionados = new HashMap<>();
        this.projetoAtual = projetoService.buscarProjetoPorNome(nomeProjeto).get();

        configuraView();
    }

    private void configuraView() {
        view.setModal(true);
        view.setResizable(false);
        view.setLocationRelativeTo(null);

        configuraActionsListerns();
        carregarListaPerfis();
        carregarDadosProjeto();
    }

    private void carregarDadosProjeto() {
        if (projetoAtual == null) {
            return;
        }

        view.getTxtNome().setText(projetoAtual.getNome());

        List<Campo> campos = new CampoRepositoryService().buscarPorIdProjetoTipo(projetoAtual.getId(), "tamanho");

        if (!campos.isEmpty()) {
            Campo campoTamanho = campos.get(0); // Obter o primeiro campo
            ListModel<String> modelTamanho = view.getCbmTamanhoApp().getModel();

            for (int i = 0; i < modelTamanho.getSize(); i++) {
                String item = modelTamanho.getElementAt(i);
                if (item.equalsIgnoreCase(campoTamanho.getNome())) { // Comparando pelo ID, ou outro critério
                    view.getCbmTamanhoApp().setSelectedItem(item); // Setando o item correto
                    break;
                }
            }
        }

        campos = new CampoRepositoryService().buscarPorIdProjetoTipo(projetoAtual.getId(), "nivel");

        if (!campos.isEmpty()) {
            Campo campoNivel = campos.get(0); // Obter o primeiro campo
            ListModel<String> modelNivel = view.getCbmNivelUI().getModel();

            for (int i = 0; i < modelNivel.getSize(); i++) {
                String item = modelNivel.getElementAt(i);
                if (item.equalsIgnoreCase(campoNivel.getNome())) { // Comparando pelo ID, ou outro critério
                    view.getCbmNivelUI().setSelectedItem(item); // Setando o item correto
                    break;
                }
            }

            DefaultListModel<String> listModel = new DefaultListModel<>();

            for (PerfilProjeto perfil : projetoAtual.getPerfis()) {
                listModel.addElement(perfil.getNome());
                perfisSelecionados.put(perfil.getNome(), perfil);
            }

            view.getJListPerfis().setModel(listModel);
        }

    }

    private void configuraActionsListerns() {
        view.getBtnCadastrarProjeto().addActionListener(e -> {
            try {
                salvarAlteracoes();
            } catch (Exception ex) {
                exibirMensagem(ex.getMessage());
            }
        });

        view.getBtnCancelar().addActionListener(e -> {
            try {
                view.dispose();
            } catch (Exception ex) {
                exibirMensagem(ex.getMessage());
            }
        });

        view.getBtnAdicionarPerfil().addActionListener(e -> {
            try {
                adicionarPerfil();
            } catch (Exception ex) {
                exibirMensagem(ex.getMessage());
            }
        });

        view.getBtnRemoverPerfil().addActionListener(e -> {
            try {
                removerPerfil();
            } catch (Exception ex) {
                exibirMensagem(ex.getMessage());
            }
        });

    }

    private void salvarAlteracoes() {
        new RealizarEdicaoProjetoProjetoCommand(this).execute();

        carregarListaPerfis();
    }

    private void carregarListaPerfis() {

        List<PerfilProjeto> perfis = new PerfilRepositoryService().buscarTodosPerfisPorIdUsuario(UsuarioLogadoSingleton.getInstancia().getUsuario().getId());

        DefaultComboBoxModel<String> cmbPerfis = new DefaultComboBoxModel<>();

        for (PerfilProjeto perfil : perfis) {
            cmbPerfis.addElement(perfil.getNome());
            perfisSelecionados.put(perfil.getNome(), perfil);
        }

        view.getCbmPerfis().setModel(cmbPerfis);
    }

    private Map<String, Integer> combinarFuncionalidades(List<PerfilProjeto> perfis) {
        Map<String, Integer> funcionalidadesCombinadas = new HashMap<>();
        for (PerfilProjeto perfil : perfis) {
            perfil.getFuncionalidades().forEach(funcionalidadesCombinadas::putIfAbsent);
        }
        return funcionalidadesCombinadas;
    }

    private void adicionarPerfil() {

        String perfil = (String) view.getCbmPerfis().getSelectedItem();

        if (perfil == null || perfil.isEmpty()) {
            throw new IllegalArgumentException("Selecione um perfil válido!");
        }

        if (!(view.getJListPerfis().getModel() instanceof DefaultListModel)) {
            view.getJListPerfis().setModel(new DefaultListModel<>());
        }

        DefaultListModel<String> model = (DefaultListModel<String>) view.getJListPerfis().getModel();

        if (!model.contains(perfil)) {
            model.addElement(perfil);
        } else {
            throw new RuntimeException("Este perfil já foi adicionado!");
        }

        exibirMensagem("Perfil adicionado com sucesso!");

    }

    private void removerPerfil() {

        String perfil = (String) view.getJListPerfis().getSelectedValue();

        if (perfil == null || perfil.isEmpty()) {
            throw new IllegalArgumentException("Selecione um perfil válido!");
        }

        DefaultListModel<String> model = (DefaultListModel<String>) view.getJListPerfis().getModel();

        if (model.contains(perfil)) {
            PerfilProjeto perfilSelecionado = perfisSelecionados.get(perfil);

            projetoAtual.removerPerfil(perfilSelecionado);

            model.removeElement(perfil);
            exibirMensagem("Perfil removido com sucesso!");
        } else {
            throw new RuntimeException("Erro: Perfil não encontrado!");
        }

        view.getJListPerfis().setModel(model);

    }

    public ProjetoRepositoryService getProjetoService() {
        return projetoService;
    }

    public PerfilRepositoryService getPerfilService() {
        return perfilService;
    }

    public Map<String, PerfilProjeto> getPerfisSelecionados() {
        return perfisSelecionados;
    }

    public Projeto getProjetoAtual() {
        return projetoAtual;
    }

    public EdicaoProjetoView getView() {
        return view;
    }

    public void exibirMensagem(String mensagem) {
        new MostrarMensagemProjetoCommand(mensagem).execute();
    }
}
