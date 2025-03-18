package com.br.estimativadeprojetodesoftware.command.projeto;

import com.br.estimativadeprojetodesoftware.model.Campo;
import com.br.estimativadeprojetodesoftware.model.PerfilProjeto;
import com.br.estimativadeprojetodesoftware.model.Projeto;
import com.br.estimativadeprojetodesoftware.presenter.projeto.DetalheProjetoPresenter;
import com.br.estimativadeprojetodesoftware.service.ProjetoService;
import com.br.estimativadeprojetodesoftware.view.projeto.DetalheProjetoView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.Map;
import com.br.estimativadeprojetodesoftware.command.Command;

public class RealizarEstimativaProjetoCommand implements Command {

    private final DetalheProjetoPresenter detalheProjetoPresenter;
    private final ProjetoService projetoService;
    private final Projeto projeto;
    private final DetalheProjetoView view;

    public RealizarEstimativaProjetoCommand(DetalheProjetoPresenter detalheProjetoPresenter) {
        this.detalheProjetoPresenter = detalheProjetoPresenter;
        this.projetoService = detalheProjetoPresenter.getProjetoService();
        this.projeto = detalheProjetoPresenter.getProjeto();
        this.view = detalheProjetoPresenter.getView();
    }

    @Override
    public void execute() {
        atualizarCamposDoProjeto();
    }

    private void atualizarCamposDoProjeto() {
        JTable tabela = view.getTabelaDetalhes();
        DefaultTableModel model = (DefaultTableModel) tabela.getModel();
        List<PerfilProjeto> perfis = projeto.getPerfis();
        boolean houveAlteracao = false;

        double soma;
        for (int i = 0; i < model.getRowCount(); i++) {
            String nomeCampo = (String) model.getValueAt(i, 0);
            soma = 0;

            for (int j = 1; j <= perfis.size(); j++) {
                Object valorCelula = model.getValueAt(i, j);
                double diasNovos = extrairValorComoInteiro(valorCelula);

                if (valorCelula == null || valorCelula.toString().isEmpty()) {
                    continue;
                }

                // if (j != perfis.size()) {
                //     soma += diasNovos;
                //     continue;
                // }

                PerfilProjeto perfil = perfis.get(j - 1);

                if (perfil.getTamanhosApp().containsKey(nomeCampo)) {
                    int diasOriginais = perfil.getTamanhosApp().get(nomeCampo);

                    if (diasNovos != diasOriginais) {
                        //projeto.adicionarCampo(new Campo("tamanho", nomeCampo, soma));
                        // Map<String, Integer> funcionalidadesAtualizadas = perfil.getTamanhosApp();
                        // funcionalidadesAtualizadas.put(nomeCampo, (int) diasNovos);
                        perfil.adicionarTamanhoApp(nomeCampo, (int) diasNovos);
                        houveAlteracao = true;
                    }
                }

                if (perfil.getNiveisUI().containsKey(nomeCampo)) {
                    double diasOriginais = perfil.getNiveisUI().get(nomeCampo);

                    if (diasNovos != diasOriginais) {
                        // projeto.adicionarCampo(new Campo("nivel", nomeCampo, soma));
                        // Map<String, Double> funcionalidadesAtualizadas = perfil.getNiveisUI();
                        // funcionalidadesAtualizadas.put(nomeCampo, diasNovos);
                        perfil.adicionarNivelUI(nomeCampo, diasNovos);
                        houveAlteracao = true;
                    }
                }

                if (perfil.getFuncionalidades().containsKey(nomeCampo)) {
                    int diasOriginais = perfil.getFuncionalidades().get(nomeCampo);

                    if (diasNovos != diasOriginais) {
                        //perfil.adicionarFuncionalidade(nomeCampo, (int) diasNovos);
                        //projeto.adicionarCampo(new Campo("funcionalidade", nomeCampo, soma));
                        // Map<String, Integer> funcionalidadesAtualizadas = perfil.getFuncionalidades();
                        // funcionalidadesAtualizadas.put(nomeCampo, (int) diasNovos);
                        perfil.adicionarFuncionalidade(nomeCampo, (int) diasNovos);
                        houveAlteracao = true;
                    }
                }

                if (perfil.getTaxasDiarias().containsKey(nomeCampo)) {
                    double diasOriginais = perfil.getTaxasDiarias().get(nomeCampo);

                    if (diasNovos != diasOriginais) {
                        //projeto.adicionarCampo(new Campo("taxa diária", nomeCampo, soma));
                        // Map<String, Double> funcionalidadesAtualizadas = perfil.getTaxasDiarias();
                        // funcionalidadesAtualizadas.put(nomeCampo, diasNovos);
                        perfil.adicionarTaxaDiaria(nomeCampo, diasNovos);
                        houveAlteracao = true;
                    }
                }
            }
        }
        projeto.setStatus("Estimado");
        detalheProjetoPresenter.setProjeto(projeto);
        projetoService.atualizar(projeto);

        //new CarregarDetalhesProjetoCommand(view, projeto, true).execute(); // Recarrega os dados

    }

    private int extrairValorComoInteiro(Object valor) {
        if (valor == null) {
            return 0;
        }
        if (valor instanceof Integer) {
            return (Integer) valor;
        }
        if (valor instanceof String) {
            try {
                return Integer.parseInt(((String) valor).trim());
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return 0;
    }
}
