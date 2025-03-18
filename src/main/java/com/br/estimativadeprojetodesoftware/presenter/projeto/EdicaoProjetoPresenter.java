package com.br.estimativadeprojetodesoftware.presenter.projeto;

import com.br.estimativadeprojetodesoftware.command.projeto.MostrarMensagemProjetoCommand;
import com.br.estimativadeprojetodesoftware.command.projeto.RealizarEdicaoProjetoCommand;
import com.br.estimativadeprojetodesoftware.model.Campo;
import com.br.estimativadeprojetodesoftware.model.PerfilProjeto;
import com.br.estimativadeprojetodesoftware.model.Projeto;
import com.br.estimativadeprojetodesoftware.model.Usuario;
import com.br.estimativadeprojetodesoftware.service.CampoService;
import com.br.estimativadeprojetodesoftware.service.PerfilProjetoService;
import com.br.estimativadeprojetodesoftware.service.ProjetoService;
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

    private final ProjetoService projetoService;
    private PerfilProjetoService perfilService;
    private CampoService campoService;
    private EdicaoProjetoView view;
    private String nomeProjeto;
    private Usuario usuario;
    private Map<String, PerfilProjeto> perfisSelecionados;
    private Projeto projetoAtual;

    public EdicaoProjetoPresenter(ProjetoService projetoService, EdicaoProjetoView view, String nomeProjeto) {
        this.projetoService = projetoService;
        this.perfilService = new PerfilProjetoService();
        this.campoService = new CampoService();
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
        view.getBtnRemoverPerfil().setEnabled(false);
        configuraActionsListerns();
        carregarListaPerfis();
        carregarDadosProjeto();
    }

    private void carregarDadosProjeto() {
        if (projetoAtual == null) {
            throw new RuntimeException("Erro: Projeto nulo.");
        }

        view.getTxtNome().setText(projetoAtual.getNome());

        List<Campo> campos = campoService.buscarPorIdProjetoTipo(projetoAtual.getId(), "tamanho");

        if (!campos.isEmpty()) {
            Campo campoTamanho = campos.get(0);
            ListModel<String> modelTamanho = view.getCbmTamanhoApp().getModel();

            for (int i = 0; i < modelTamanho.getSize(); i++) {
                String item = modelTamanho.getElementAt(i);
                if (item.equalsIgnoreCase(campoTamanho.getNome())) {
                    view.getCbmTamanhoApp().setSelectedItem(item);
                    break;
                }
            }
        }

        campos = campoService.buscarPorIdProjetoTipo(projetoAtual.getId(), "nivel");

        if (!campos.isEmpty()) {
            Campo campoNivel = campos.get(0);
            ListModel<String> modelNivel = view.getCbmNivelUI().getModel();

            for (int i = 0; i < modelNivel.getSize(); i++) {
                String item = modelNivel.getElementAt(i);
                if (item.equalsIgnoreCase(campoNivel.getNome())) {
                    view.getCbmNivelUI().setSelectedItem(item);
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

        view.getJListPerfis().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                boolean itemSelecionado = view.getJListPerfis().getSelectedIndex() != -1;
                view.getBtnRemoverPerfil().setEnabled(itemSelecionado);
            }
        });

    }

    private void salvarAlteracoes() {
        new RealizarEdicaoProjetoCommand(this).execute();
        carregarListaPerfis();
    }

    private void carregarListaPerfis() {

        List<PerfilProjeto> perfis = new PerfilProjetoService().buscarTodosPerfisPorIdUsuario(UsuarioLogadoSingleton.getInstancia().getUsuario().getId());

        DefaultComboBoxModel<String> cmbPerfis = new DefaultComboBoxModel<>();

        for (PerfilProjeto perfil : perfis) {
            cmbPerfis.addElement(perfil.getNome());
            perfisSelecionados.put(perfil.getNome(), perfil);
        }

        view.getCbmPerfis().setModel(cmbPerfis);
    }

    private void adicionarPerfil() {

        String perfil = (String) view.getCbmPerfis().getSelectedItem();

        if (perfil == null || perfil.isEmpty()) {
            throw new IllegalArgumentException("Selecione um perfil válido!");
        }

        PerfilProjeto perfilSelecionado = perfisSelecionados.get(perfil);

        if (perfilSelecionado == null) {
            throw new RuntimeException("Perfil não encontrado!");
        }

        if (perfilSelecionado.isPerfilBackEnd() && isMaisDeUmPerfilBackend()) {
            throw new RuntimeException("Não é possível adicionar mais de um perfil backend!");
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

    private boolean isMaisDeUmPerfilBackend() {
        DefaultListModel<String> model = (DefaultListModel<String>) view.getJListPerfis().getModel();

        long countBackend = 0;

        for (int i = 0; i < model.getSize(); i++) {
            String perfilNome = model.getElementAt(i);
            if (perfisSelecionados.get(perfilNome).isPerfilBackEnd()) {
                countBackend++;
            }
        }

        return countBackend > 0;
    }

    public ProjetoService getProjetoService() {
        return projetoService;
    }

    public PerfilProjetoService getPerfilService() {
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
