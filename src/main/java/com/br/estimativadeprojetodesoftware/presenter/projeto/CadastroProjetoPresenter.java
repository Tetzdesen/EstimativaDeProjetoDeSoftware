package com.br.estimativadeprojetodesoftware.presenter.projeto;

import com.br.estimativadeprojetodesoftware.command.MostrarMensagemProjetoCommand;
import com.br.estimativadeprojetodesoftware.model.Campo;
import com.br.estimativadeprojetodesoftware.model.Perfil;
import com.br.estimativadeprojetodesoftware.model.Projeto;
import com.br.estimativadeprojetodesoftware.model.Usuario;
import com.br.estimativadeprojetodesoftware.repository.ProjetoRepositoryMock;
import com.br.estimativadeprojetodesoftware.singleton.UsuarioLogadoSingleton;
import com.br.estimativadeprojetodesoftware.view.projeto.CadastroProjetoView;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.ListModel;

/**
 *
 * @author Hiago Lopes
 */
public class CadastroProjetoPresenter {

    private CadastroProjetoView view;
    private ProjetoRepositoryMock repositoryProjeto;
    private Usuario usuario;
    private Map<String, Perfil> perfisSelecionados;

    public CadastroProjetoPresenter(CadastroProjetoView view, ProjetoRepositoryMock repositoryProjeto) {
        this.view = view;
        this.repositoryProjeto = repositoryProjeto;
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

        List<Perfil> perfis = usuario.getPerfis();

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

        if (nomeProjeto.isEmpty()) {
            exibirMensagem("Nome é um campo obrigatório!");
            return;
        }

        Optional<Projeto> optionalProjeto = criarProjeto(nomeProjeto);
        optionalProjeto.ifPresentOrElse(
                projeto -> repositoryProjeto.adicionarProjeto(projeto),
                () -> exibirMensagem("Erro ao criar o projeto!")
        );
        exibirMensagem("Projeto criado com sucesso!");
    }

    private Optional<Projeto> criarProjeto(String nomeProjeto) {
        String nome = nomeProjeto;
        String criador = UsuarioLogadoSingleton.getInstancia().getUsuario().getNome();

        Projeto projeto = new Projeto(nome, criador);
        projeto.adicionarUsuario(UsuarioLogadoSingleton.getInstancia().getUsuario());

        ListModel<String> listModel = view.getJListPerfis().getModel();
        for (int i = 0; i < listModel.getSize(); i++) {
            String item = listModel.getElementAt(i);
            projeto.adicionarPerfil(perfisSelecionados.get(item));
        }

        String tiposConcatenados = projeto.getPerfis().stream()
                .map(Perfil::getNome)
                .collect(Collectors.joining(", "));

        projeto.setTipo(tiposConcatenados);
        List<Campo> campos = new ArrayList<>();

        for (Perfil perfil : projeto.getPerfis()) {
            // Criando campos para estimativa
            Map<String, Integer> tamanhosApp = perfil.getTamanhosApp();

            for (Map.Entry<String, Integer> entry : tamanhosApp.entrySet()) {
                campos.add(new Campo(UUID.randomUUID(), "", entry.getKey(), entry.getValue()));
            }

            // Criando campos para niveisUD
            Map<String, Double> niveisUI = perfil.getNiveisUI();
            for (Map.Entry<String, Double> entry : niveisUI.entrySet()) {
                campos.add(new Campo(UUID.randomUUID(), "", entry.getKey(), entry.getValue()));
            }

            // Criando campos para funcionalidades
            Map<String, Integer> funcionalidades = perfil.getFuncionalidades();
            for (Map.Entry<String, Integer> entry : funcionalidades.entrySet()) {
                campos.add(new Campo(UUID.randomUUID(), "", entry.getKey(), entry.getValue()));
            }

            // Criando campos para taxas diarias
            Map<String, Double> taxasDiarias = perfil.getTaxasDiarias();

            for (Map.Entry<String, Double> entry : taxasDiarias.entrySet()) {
                campos.add(new Campo(UUID.randomUUID(), "", entry.getKey(), entry.getValue()));
            }

            //  Map<String, Integer> funcionalidades = combinarFuncionalidades(projeto.getPerfis());
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
            JOptionPane.showMessageDialog(view, "Selecione um perfil válido!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!(view.getJListPerfis().getModel() instanceof DefaultListModel)) {
            view.getJListPerfis().setModel(new DefaultListModel<>());
        }

        DefaultListModel<String> model = (DefaultListModel<String>) view.getJListPerfis().getModel();

        if (!model.contains(perfil)) {
            model.addElement(perfil);
        } else {
            exibirMensagem("Este perfil já foi adicionado!");
        }

        exibirMensagem("Perfil adicionado com sucesso!");

    }

    private void removerPerfil() {

        String perfil = (String) view.getJListPerfis().getSelectedValue();

        if (perfil == null || perfil.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Selecione um perfil válido!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        DefaultListModel<String> model = (DefaultListModel<String>) view.getJListPerfis().getModel();

        if (model.contains(perfil)) {
            model.removeElement(perfil);
            exibirMensagem("Perfil removido com sucesso!");
        } else {
            exibirMensagem("Erro: Perfil não removido!");
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
