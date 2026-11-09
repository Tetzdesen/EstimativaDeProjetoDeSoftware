package com.br.estimativadeprojetodesoftware.presenter.tabela;

import com.br.estimativadeprojetodesoftware.model.TipoSelecao;
import com.br.estimativadeprojetodesoftware.view.componentes.ItemComContador;
import com.br.estimativadeprojetodesoftware.view.projeto.SecaoProjeto;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.swing.AbstractListModel;

/**
 * Barra lateral da tela de projeto: as seções na ordem de {@link SecaoProjeto}
 * e, logo abaixo de Funcionalidades, uma entrada por categoria que não é de
 * configuração, na ordem da planilha. Funcionalidades e as categorias mostram a
 * quantidade de selecionadas. As entradas são fixas para o projeto;
 * {@link #recontar()} só atualiza as quantidades.
 */
public class NavegacaoProjetoModel extends AbstractListModel<ItemComContador> {

    /** @param categoria {@code null} para as seções */
    private record Entrada(SecaoProjeto secao, String categoria) {
    }

    private final ItensProjetoTableModel itens;
    private final List<String> configuracao;
    private final List<Entrada> entradas = new ArrayList<>();
    private final List<ItemComContador> exibicao = new ArrayList<>();

    public NavegacaoProjetoModel(ItensProjetoTableModel itens) {
        this.itens = itens;
        this.configuracao = categoriasDeConfiguracao(itens);
        for (SecaoProjeto secao : SecaoProjeto.values()) {
            entradas.add(new Entrada(secao, null));
            if (secao == SecaoProjeto.FUNCIONALIDADES) {
                for (String categoria : categorias(itens)) {
                    if (!configuracao.contains(categoria)) {
                        entradas.add(new Entrada(secao, categoria));
                    }
                }
            }
        }
        contar();
    }

    /**
     * Categorias de configuração (escolha única ou opcional), na ordem da
     * planilha; no perfil padrão, Tamanho do App, Nível de UI e Gerente de Projeto.
     */
    public static List<String> categoriasDeConfiguracao(ItensProjetoTableModel itens) {
        Set<String> resultado = new LinkedHashSet<>();
        for (int linha = 0; linha < itens.getRowCount(); linha++) {
            TipoSelecao tipo = itens.getTipoSelecao(linha);
            if (tipo == TipoSelecao.UNICA || tipo == TipoSelecao.OPCIONAL) {
                resultado.add(categoriaDaLinha(itens, linha));
            }
        }
        return List.copyOf(resultado);
    }

    private static Set<String> categorias(ItensProjetoTableModel itens) {
        Set<String> resultado = new LinkedHashSet<>();
        for (int linha = 0; linha < itens.getRowCount(); linha++) {
            resultado.add(categoriaDaLinha(itens, linha));
        }
        return resultado;
    }

    private static String categoriaDaLinha(ItensProjetoTableModel itens, int linha) {
        return (String) itens.getValueAt(linha, ItensProjetoTableModel.COLUNA_CATEGORIA);
    }

    public List<String> getCategoriasDeConfiguracao() {
        return configuracao;
    }

    /** Recalcula as quantidades sem inserir nem remover entradas, preservando a seleção da lista. */
    public void recontar() {
        contar();
        fireContentsChanged(this, 0, entradas.size() - 1);
    }

    private void contar() {
        exibicao.clear();
        for (Entrada e : entradas) {
            if (e.categoria() != null) {
                exibicao.add(new ItemComContador(e.categoria(), selecionadas(e.categoria()), true));
            } else if (e.secao() == SecaoProjeto.FUNCIONALIDADES) {
                exibicao.add(new ItemComContador(e.secao().getTitulo(), selecionadas(null), false));
            } else {
                exibicao.add(new ItemComContador(e.secao().getTitulo(), null, false));
            }
        }
    }

    /** Selecionadas na categoria, ou em todas as que não são de configuração ({@code null}). */
    private int selecionadas(String categoria) {
        int total = 0;
        for (int linha = 0; linha < itens.getRowCount(); linha++) {
            String daLinha = categoriaDaLinha(itens, linha);
            if (itens.isSelecionado(linha) && !configuracao.contains(daLinha)
                    && (categoria == null || Objects.equals(categoria, daLinha))) {
                total++;
            }
        }
        return total;
    }

    /** Seção da entrada; índice inválido conta como Funcionalidades. */
    public SecaoProjeto getSecao(int indice) {
        return valido(indice) ? entradas.get(indice).secao() : SecaoProjeto.FUNCIONALIDADES;
    }

    /** Categoria da entrada, ou {@code null} para as seções e índices inválidos. */
    public String getCategoria(int indice) {
        return valido(indice) ? entradas.get(indice).categoria() : null;
    }

    /** Índice da entrada; se a categoria não existir mais, o da própria seção. */
    public int indiceDe(SecaoProjeto secao, String categoria) {
        int indice = entradas.indexOf(new Entrada(secao, categoria));
        return indice >= 0 ? indice : entradas.indexOf(new Entrada(secao, null));
    }

    /** Quantidade exibida para a categoria ({@code null} para a entrada Funcionalidades). */
    public int getSelecionadas(String categoria) {
        int indice = entradas.indexOf(new Entrada(SecaoProjeto.FUNCIONALIDADES, categoria));
        if (indice < 0) {
            throw new IllegalArgumentException("Categoria não está na barra lateral: " + categoria);
        }
        return exibicao.get(indice).quantidade();
    }

    private boolean valido(int indice) {
        return indice >= 0 && indice < entradas.size();
    }

    @Override
    public int getSize() {
        return exibicao.size();
    }

    @Override
    public ItemComContador getElementAt(int index) {
        return exibicao.get(index);
    }
}
