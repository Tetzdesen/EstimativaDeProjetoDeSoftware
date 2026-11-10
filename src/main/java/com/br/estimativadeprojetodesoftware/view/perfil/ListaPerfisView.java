package com.br.estimativadeprojetodesoftware.view.perfil;

import com.br.estimativadeprojetodesoftware.view.componentes.JanelaInterna;
import com.br.estimativadeprojetodesoftware.view.componentes.Selo;
import com.br.estimativadeprojetodesoftware.view.componentes.TabelaPadrao;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/** Lista de perfis de estimativa. Passive View. */
public class ListaPerfisView extends JanelaInterna {

    private final TabelaPadrao tabela = new TabelaPadrao();
    private final JButton btnNovo;
    private final JButton btnAbrir;
    private final JButton btnExcluir;
    private final JButton btnFechar;

    public ListaPerfisView() {
        super("Perfis de estimativa", "Modelos de funcionalidades, dias e taxas usados para criar projetos.", "perfil");
        btnNovo = getBarra().adicionarPrincipal("Novo perfil (cópia do selecionado)", "adicionar");
        btnAbrir = getBarra().adicionar("Abrir", "abrir");
        btnExcluir = getBarra().adicionarPerigo("Excluir", "excluir");
        getBarra().empurrarParaDireita();
        btnFechar = getBarra().adicionar("Fechar", "fechar");
        tabela.permitirOrdenacao();
        tabela.setColunaSelo(1, v -> v.toString().startsWith("Padrão") ? Selo.Tipo.DESTAQUE : Selo.Tipo.NEUTRO);
        tabela.setFormatoColuna(2, TabelaPadrao.Formato.NUMERO);
        getConteudo().add(new JScrollPane(tabela), BorderLayout.CENTER);
        setSize(780, 440);
    }

    public TabelaPadrao getTabela() {
        return tabela;
    }

    public JButton getBtnNovo() {
        return btnNovo;
    }

    public JButton getBtnAbrir() {
        return btnAbrir;
    }

    public JButton getBtnExcluir() {
        return btnExcluir;
    }

    public JButton getBtnFechar() {
        return btnFechar;
    }
}
