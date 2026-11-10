package com.br.estimativadeprojetodesoftware.view.projeto;

import com.br.estimativadeprojetodesoftware.view.componentes.JanelaInterna;
import com.br.estimativadeprojetodesoftware.view.componentes.Selo;
import com.br.estimativadeprojetodesoftware.view.componentes.TabelaPadrao;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/** Lista de projetos próprios e compartilhados. Passive View. */
public class ListaProjetosView extends JanelaInterna {

    private final TabelaPadrao tabela = new TabelaPadrao();
    private final JButton btnNovo;
    private final JButton btnAbrir;
    private final JButton btnExcluir;
    private final JButton btnFechar;

    public ListaProjetosView() {
        super("Projetos", "Seus projetos e os compartilhados com você, com prazo e preço. Clique duas vezes (ou Enter) para abrir.", "projeto");
        btnNovo = getBarra().adicionarPrincipal("Novo projeto", "novo-projeto");
        btnAbrir = getBarra().adicionar("Abrir", "abrir");
        btnExcluir = getBarra().adicionarPerigo("Excluir", "excluir");
        getBarra().empurrarParaDireita();
        btnFechar = getBarra().adicionar("Fechar", "fechar");
        tabela.permitirOrdenacao();
        tabela.setColunaSelo(1, v -> v.toString().startsWith("Compartilhado") ? Selo.Tipo.AVISO : Selo.Tipo.DESTAQUE);
        getConteudo().add(new JScrollPane(tabela), BorderLayout.CENTER);
        setSize(820, 460);
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
