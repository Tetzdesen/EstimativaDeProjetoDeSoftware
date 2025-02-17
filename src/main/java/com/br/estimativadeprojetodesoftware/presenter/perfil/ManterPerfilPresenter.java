package com.br.estimativadeprojetodesoftware.presenter.perfil;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import com.br.estimativadeprojetodesoftware.presenter.Observer;
import com.br.estimativadeprojetodesoftware.repository.PerfilRepositoryMock;
import com.br.estimativadeprojetodesoftware.view.perfil.ManterPerfilView;

public class ManterPerfilPresenter implements Observer {
    private ManterPerfilView view;
    private List<String> funcionalidades;
    private Map<JLabel, JSpinner> campos;

    public ManterPerfilPresenter(ManterPerfilView view, PerfilRepositoryMock repository) {
        this.view = view;
        this.view.setVisible(true);
        this.funcionalidades = new ArrayList<>();
        setFuncionalidades();
        this.campos = new LinkedHashMap<>();
        configuraView();

        carregarCampos();
    }

    private void configuraView() {
        view.setClosable(true);
        view.setIconifiable(true);
        view.setResizable(false);
        view.setMaximizable(false);
    }

    private void carregarCampos() {
        for (String funcionalidade : funcionalidades) {
            JSpinner spinner = new JSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));
            JLabel label = new JLabel(funcionalidade);
            campos.put(label, spinner);
            view.adicionarCampoView(label, spinner);
        }
    }

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
            "Cadastro por Email e Senha", 
            "Cadastro por Email e Senha", 
            "Cadastro por Email e Senha", 
            "Cadastro por Email e Senha", 
            "Cadastro por Email e Senha", 
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
