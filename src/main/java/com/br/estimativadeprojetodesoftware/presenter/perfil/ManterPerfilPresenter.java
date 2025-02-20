package com.br.estimativadeprojetodesoftware.presenter.perfil;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import com.br.estimativadeprojetodesoftware.model.Perfil;
import com.br.estimativadeprojetodesoftware.model.Projeto;
import com.br.estimativadeprojetodesoftware.presenter.Observer;
import com.br.estimativadeprojetodesoftware.repository.PerfilRepositoryMock;
import com.br.estimativadeprojetodesoftware.view.perfil.ManterPerfilView;

public class ManterPerfilPresenter implements Observer {
    private ManterPerfilView view;
    private List<String> funcionalidades;
    private Map<JLabel, JSpinner> campos;
    private PerfilRepositoryMock repository;

    public ManterPerfilPresenter(ManterPerfilView view, PerfilRepositoryMock repository) {
        this.view = view;
        this.funcionalidades = new ArrayList<>();
        this.repository = repository;
        setFuncionalidades();
        this.campos = new LinkedHashMap<>();
        configuraView();

        carregarCampos();
    }

    private void configuraView() {
        //view.setClosable(true);
        //view.setIconifiable(true);
        //view.setResizable(false);
        //view.setMaximizable(false);
    }

    private void carregarCampos() {
        /*
        for (String funcionalidade : funcionalidades) {
            JSpinner spinner = new JSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));
            JLabel label = new JLabel(funcionalidade);
            campos.put(label, spinner);
            view.adicionarCampoView(label, spinner);
        }
             */

        /*
        for (String campo : repository.getPerfis().get(0).getFuncionalidades()) {

        }
         */
        Object[][] dadosTabela = repository.getPerfis().get(0).getFuncionalidades()
                .entrySet()
                .stream()
                .map(entry -> new Object[]{ entry.getKey() })
                .toArray(Object[][]::new);


        view.atualizarTabela(dadosTabela);
    }
/*
    private void carregarDetalhesPerfil() {
        List<Perfil> perfis = repository.getPerfis();
        for(Perfil perfil : perfis) {
            Object[] dadosTabela = new Object[3];
            dadosTabela[0] = perfil.getNome();

            view.atualizarTabela(dadosTabela);
        }
    }

    private void carregarDetalhes(Perfil perfil) {
        Object[] dadosTabela = new Object[3];
        dadosTabela[0] = perfil.getNome();

        view.atualizarTabela(dadosTabela);
    }

 */
    private void setFuncionalidades() {
        funcionalidades = List.of(
            "Pequeno", 
            "Médio", 
            "Grande", 
            "MVP", 
            "Básico", 
            "Profissional", 
            "Cadastro por Email e Senha", 
            "Cadastro Pelo Facebook",
            "Cadastro por Email e Senha", 
            "Pequeno", 
            "Médio", 
            "Grande", 
            "MVP", 
            "Básico", 
            "Profissional", 
            "Cadastro por Email e Senha", 
            "Cadastro Pelo Facebook",
            "Cadastro por Email e Senha",
            "Pequeno", 
            "Médio", 
            "Grande", 
            "MVP", 
            "Básico", 
            "Profissional", 
            "Cadastro por Email e Senha", 
            "Cadastro Pelo Facebook",
            "Cadastro por Email e Senha", 
            "Pequeno", 
            "Médio", 
            "Grande", 
            "MVP", 
            "Básico", 
            "Profissional", 
            "Cadastro por Email e Senha", 
            "Cadastro Pelo Facebook",
            "Cadastro por Email e Senha"
        );
    }

    public List<String> getFuncionalidades() {
        return funcionalidades;
    }

    @Override
    public void update() {
        // Método necessário para Observer, pode ser implementado no futuro
    }
}
