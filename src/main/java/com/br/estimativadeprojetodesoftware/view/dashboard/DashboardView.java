package com.br.estimativadeprojetodesoftware.view.dashboard;

import com.br.estimativadeprojetodesoftware.view.componentes.CartaoIndicador;
import com.br.estimativadeprojetodesoftware.view.componentes.Estilos;
import com.br.estimativadeprojetodesoftware.view.componentes.JanelaInterna;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * Dashboard do usuário. Passive View: só componentes e métodos de exibição;
 * recebe textos e gráficos prontos do presenter.
 */
public class DashboardView extends JanelaInterna {

    private final CartaoIndicador cartaoProprios = new CartaoIndicador("Meus projetos");
    private final CartaoIndicador cartaoCompartilhados = new CartaoIndicador("Compartilhados comigo");
    private final CartaoIndicador cartaoCalculados = new CartaoIndicador("Estimativas calculadas");
    private final CartaoIndicador cartaoDias = new CartaoIndicador("Prazo total (dias)");
    private final CartaoIndicador cartaoPreco = new CartaoIndicador("Preço final total");
    private final JLabel lblAviso = Estilos.aplicar(new JLabel(" "), "foreground: $Estimativa.aviso");
    private final JPanel painelGraficos = new JPanel(new BorderLayout());
    private final JButton btnFechar;

    public DashboardView() {
        super("Dashboard", "Visão geral dos seus projetos de estimativa", "dashboard");
        btnFechar = getBarra().adicionar("Fechar", "fechar");

        JPanel indicadores = new JPanel(new GridLayout(1, 5, 12, 0));
        indicadores.setOpaque(false);
        for (CartaoIndicador c : new CartaoIndicador[]{cartaoProprios, cartaoCompartilhados, cartaoCalculados, cartaoDias, cartaoPreco}) {
            indicadores.add(c);
        }
        JPanel topo = new JPanel(new BorderLayout(0, 6));
        topo.setOpaque(false);
        topo.add(indicadores, BorderLayout.CENTER);
        topo.add(lblAviso, BorderLayout.SOUTH);
        painelGraficos.setOpaque(false);
        painelGraficos.setBorder(BorderFactory.createEmptyBorder(6, 0, 0, 0));

        getConteudo().add(topo, BorderLayout.NORTH);
        getConteudo().add(painelGraficos, BorderLayout.CENTER);
        setSize(1080, 620);
    }

    public void exibirIndicadores(String proprios, String compartilhados, String calculados, String dias, String preco) {
        cartaoProprios.setValor(proprios);
        cartaoCompartilhados.setValor(compartilhados);
        cartaoCalculados.setValor(calculados);
        cartaoDias.setValor(dias);
        cartaoPreco.setValor(preco);
    }

    public void exibirDetalhes(String dias, String preco) {
        cartaoDias.setDetalhe(dias);
        cartaoPreco.setDetalhe(preco);
    }

    public void exibirAviso(String texto) {
        lblAviso.setText(texto == null || texto.isBlank() ? " " : texto);
    }

    public void exibirGraficos(JComponent graficoPreco, JComponent graficoPlataformas) {
        painelGraficos.removeAll();
        JPanel grade = new JPanel(new GridLayout(1, 2, 12, 0));
        grade.setOpaque(false);
        grade.add(Estilos.aplicar(envolver(graficoPreco), Estilos.CARTAO));
        grade.add(Estilos.aplicar(envolver(graficoPlataformas), Estilos.CARTAO));
        painelGraficos.add(grade, BorderLayout.CENTER);
        painelGraficos.revalidate();
        painelGraficos.repaint();
    }

    private static JPanel envolver(JComponent c) {
        JPanel p = new JPanel(new BorderLayout());
        p.add(c, BorderLayout.CENTER);
        return p;
    }

    public void exibirMensagemVazia(String mensagem) {
        painelGraficos.removeAll();
        JLabel l = Estilos.aplicar(new JLabel("<html><div style='text-align:center'>" + mensagem + "</div></html>",
                com.br.estimativadeprojetodesoftware.service.IconService.getIcon("info", 32), SwingConstants.CENTER), "font: +2; foreground: $Estimativa.subtitulo");
        l.setVerticalTextPosition(SwingConstants.BOTTOM);
        l.setHorizontalTextPosition(SwingConstants.CENTER);
        painelGraficos.add(Estilos.aplicar(envolver(l), Estilos.CARTAO), BorderLayout.CENTER);
        painelGraficos.revalidate();
        painelGraficos.repaint();
    }

    public JLabel getLblProprios() {
        return cartaoProprios.getLblValor();
    }

    public JLabel getLblCompartilhados() {
        return cartaoCompartilhados.getLblValor();
    }

    public JLabel getLblCalculados() {
        return cartaoCalculados.getLblValor();
    }

    public JLabel getLblDias() {
        return cartaoDias.getLblValor();
    }

    public JLabel getLblPreco() {
        return cartaoPreco.getLblValor();
    }

    public JLabel getLblAviso() {
        return lblAviso;
    }

    public JPanel getPainelGraficos() {
        return painelGraficos;
    }

    public JButton getBtnFechar() {
        return btnFechar;
    }
}
