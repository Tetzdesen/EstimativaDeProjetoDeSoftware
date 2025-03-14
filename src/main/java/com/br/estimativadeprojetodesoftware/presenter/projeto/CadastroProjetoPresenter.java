package com.br.estimativadeprojetodesoftware.presenter.projeto;

import com.br.estimativadeprojetodesoftware.command.projeto.MostrarMensagemProjetoCommand;
import com.br.estimativadeprojetodesoftware.command.projeto.SalvarProjetoCommand;
import com.br.estimativadeprojetodesoftware.model.Campo;
import com.br.estimativadeprojetodesoftware.model.Perfil;
import com.br.estimativadeprojetodesoftware.model.Projeto;
import com.br.estimativadeprojetodesoftware.model.Usuario;
import com.br.estimativadeprojetodesoftware.repository.ProjetoRepositoryMock;
import com.br.estimativadeprojetodesoftware.service.CampoRepositoryService;
import com.br.estimativadeprojetodesoftware.service.PerfilRepositoryService;
import com.br.estimativadeprojetodesoftware.service.ProjetoRepositoryService;
import com.br.estimativadeprojetodesoftware.singleton.UsuarioLogadoSingleton;
import com.br.estimativadeprojetodesoftware.view.projeto.CadastroProjetoView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.ListModel;

/**
 *
 * @author Hiago Lopes
 */
public class CadastroProjetoPresenter {

    private CadastroProjetoView view;
    private ProjetoRepositoryService projetoService;
    private PerfilRepositoryService perfilService;
    private Usuario usuario;
    private Map<String, Perfil> perfisSelecionados;

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

        List<Perfil> perfis = new PerfilRepositoryService().buscarTodosPerfisPorIdUsuario(UsuarioLogadoSingleton.getInstancia().getUsuario().getId());

        DefaultComboBoxModel<String> cmbPerfis = new DefaultComboBoxModel<>();

        for (Perfil perfil : perfis) {
            cmbPerfis.addElement(perfil.getNome());

            // aqui carrega tudo e não somente o dos perfis 
            perfisSelecionados.put(perfil.getNome(), perfil);
        }

        view.getCbmPerfis().setModel(cmbPerfis);
    }

    private void cadastrarProjeto() {

        String nomeProjeto = view.getTxtNome().getText().trim();

        String tamanho = (String) view.getCbmTamanhoApp().getSelectedItem();

        String nivel = (String) view.getCbmNivelUI().getSelectedItem();

        if (nomeProjeto.isEmpty()) {
            throw new IllegalArgumentException("Nome é um campo obrigatório!");
        }

        if (view.getJListPerfis().getModel().getSize() == 0) {
            throw new IllegalArgumentException("Adicione no mínimo um perfil obrigatório!");
        }

        // pegar do DTO
        Optional<Projeto> optionalProjeto = criarProjeto(nomeProjeto, tamanho, nivel);

        if (optionalProjeto.isPresent()) {
            new SalvarProjetoCommand(projetoService, optionalProjeto.get()).execute();
            exibirMensagem("Projeto criado com sucesso!");
            view.dispose();
        } else {
            throw new RuntimeException("Erro ao criar o projeto!");
        }
    }

    private Optional<Projeto> criarProjeto(String nomeProjeto, String tamanho, String nivel) {
        String nome = nomeProjeto;
        String criador = UsuarioLogadoSingleton.getInstancia().getUsuario().getNome();

        Projeto projeto = new Projeto(nome, criador);
        projeto.adicionarUsuario(UsuarioLogadoSingleton.getInstancia().getUsuario());

        ListModel<String> listModel = view.getJListPerfis().getModel();
        for (int i = 0; i < listModel.getSize(); i++) {
            String item = listModel.getElementAt(i);

            projeto.adicionarPerfil(perfilService.buscarPorId(perfisSelecionados.get(item).getId()).get());
        }

        String tiposConcatenados = projeto.getPerfis().stream()
                .map(Perfil::getNome)
                .collect(Collectors.joining(", "));

        projeto.setTipo(tiposConcatenados);
        List<Campo> campos = new ArrayList<>();

        System.out.println(projeto.getPerfis());
        for (Perfil perfil : projeto.getPerfis()) {
            // Criando campos para estimativa
           
            List<Campo> tamanhos = new CampoRepositoryService().buscarPorIdPerfilTipo(perfil.getId(), "tamanho");
            List<Campo> nivelUI = new CampoRepositoryService().buscarPorIdPerfilTipo(perfil.getId(), "nivel");
            List<Campo> funcionalidades = new CampoRepositoryService().buscarPorIdPerfilTipo(perfil.getId(), "funcionalidade");
            List<Campo> taxasDiarias = new CampoRepositoryService().buscarPorIdPerfilTipo(perfil.getId(), "taxa diária");
            
            for (Campo campo : tamanhos) {
                if (campo.getNome().equalsIgnoreCase(tamanho)) {
                    campos.add(campo);
                }
            }

            for (Campo campo : nivelUI) {
                if (campo.getNome().equalsIgnoreCase(nivel)) {
                    campos.add(campo);
                }
            }

            for (Campo campo : funcionalidades) {
                campos.add(campo);
            }

            for (Campo campo : taxasDiarias) {
                campos.add(campo);
            }
        }

        return Optional.of(projeto);
    }

    private Map<String, Integer> combinarFuncionalidades(List<Perfil> perfis) {
        Map<String, Integer> funcionalidadesCombinadas = new HashMap<>();
        for (Perfil perfil : perfis) {
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
            model.removeElement(perfil);
            exibirMensagem("Perfil removido com sucesso!");
        } else {
            throw new RuntimeException("Erro: Perfil não removido!");
        }

        view.getJListPerfis().setModel(model);

    }

    public CadastroProjetoView getView() {
        return view;
    }

    public void exibirMensagem(String mensagem) {
        new MostrarMensagemProjetoCommand(mensagem).execute();
    }
}
