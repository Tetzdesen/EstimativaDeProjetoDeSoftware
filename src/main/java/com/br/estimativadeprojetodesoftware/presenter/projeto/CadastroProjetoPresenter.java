package com.br.estimativadeprojetodesoftware.presenter.projeto;

import com.br.estimativadeprojetodesoftware.command.projeto.MostrarMensagemProjetoCommand;
import com.br.estimativadeprojetodesoftware.command.projeto.RealizarCadastroProjetoCommand;
import com.br.estimativadeprojetodesoftware.model.PerfilProjeto;
import com.br.estimativadeprojetodesoftware.model.Usuario;
import com.br.estimativadeprojetodesoftware.service.PerfilRepositoryService;
import com.br.estimativadeprojetodesoftware.service.ProjetoRepositoryService;
import com.br.estimativadeprojetodesoftware.singleton.UsuarioLogadoSingleton;
import com.br.estimativadeprojetodesoftware.view.projeto.CadastroProjetoView;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;

/**
 *
 * @author Hiago Lopes
 */
public class CadastroProjetoPresenter {

    private CadastroProjetoView view;
    private ProjetoRepositoryService projetoService;
    private PerfilRepositoryService perfilService;
    private Usuario usuario;
    private Map<String, PerfilProjeto> perfisSelecionados;

    public CadastroProjetoPresenter(CadastroProjetoView view, ProjetoRepositoryService projetoService) {
        this.view = view;
        this.projetoService = projetoService;
        this.perfilService = new PerfilRepositoryService();
        this.usuario = UsuarioLogadoSingleton.getInstancia().getUsuario();
        this.perfisSelecionados = new HashMap<>();
        configuraView();
    }

    private void configuraView() {
        view.setModal(true);
        view.setResizable(false);
        configuraActionsListerns();
        carregarListaPerfis();
    }

    private void configuraActionsListerns() {
        view.getBtnCadastrarProjeto().addActionListener(e -> {
            try {
                cadastrarProjeto();
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

    private void carregarListaPerfis() {
        List<PerfilProjeto> perfis = perfilService.buscarTodosPerfisPorIdUsuario(usuario.getId());
        DefaultComboBoxModel<String> cmbPerfis = new DefaultComboBoxModel<>();

        perfis.stream()
                .filter(perfil -> perfil.getUsuario().getId().equals(usuario.getId()))
                .forEach(perfil -> {
                    cmbPerfis.addElement(perfil.getNome());
                    perfisSelecionados.put(perfil.getNome(), perfil);
                });

        view.getCbmPerfis().setModel(cmbPerfis);
    }

    private void cadastrarProjeto() {
        new RealizarCadastroProjetoCommand(this).execute();
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
            model.removeElement(perfil);
            exibirMensagem("Perfil removido com sucesso!");
        } else {
            throw new RuntimeException("Erro: Perfil não removido!");
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

    public CadastroProjetoView getView() {
        return view;
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

    public void exibirMensagem(String mensagem) {
        new MostrarMensagemProjetoCommand(mensagem).execute();
    }
}
