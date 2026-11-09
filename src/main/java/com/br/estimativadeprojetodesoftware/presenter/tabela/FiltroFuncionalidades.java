package com.br.estimativadeprojetodesoftware.presenter.tabela;

import java.util.Collection;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;
import javax.swing.RowFilter;
import javax.swing.table.TableModel;

/**
 * Filtra as funcionalidades por categoria, por texto (nome ou categoria) e por
 * seleção, deixando de fora as categorias excluídas.
 */
public class FiltroFuncionalidades extends RowFilter<TableModel, Integer> {

    private final String texto;
    private final boolean somenteSelecionadas;
    private final String categoria;
    private final Set<String> excluidas;

    public FiltroFuncionalidades(String texto, boolean somenteSelecionadas) {
        this(texto, somenteSelecionadas, null);
    }

    /** @param categoria categoria exibida, ou {@code null} para todas */
    public FiltroFuncionalidades(String texto, boolean somenteSelecionadas, String categoria) {
        this(texto, somenteSelecionadas, categoria, Set.of());
    }

    /** @param excluidas categorias que nunca aparecem, qualquer que seja o restante do filtro */
    public FiltroFuncionalidades(String texto, boolean somenteSelecionadas, String categoria, Collection<String> excluidas) {
        this.texto = normalizar(texto);
        this.somenteSelecionadas = somenteSelecionadas;
        this.categoria = categoria == null ? null : normalizar(categoria);
        this.excluidas = excluidas.stream().map(FiltroFuncionalidades::normalizar).collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public boolean include(Entry<? extends TableModel, ? extends Integer> linha) {
        String categoriaLinha = normalizar(linha.getStringValue(ItensProjetoTableModel.COLUNA_CATEGORIA));
        if (excluidas.contains(categoriaLinha) || categoria != null && !categoria.equals(categoriaLinha)) {
            return false;
        }
        if (somenteSelecionadas && !Boolean.TRUE.equals(linha.getValue(ItensProjetoTableModel.COLUNA_SELECIONAR))) {
            return false;
        }
        return texto.isEmpty()
                || normalizar(linha.getStringValue(ItensProjetoTableModel.COLUNA_NOME)).contains(texto)
                || categoriaLinha.contains(texto);
    }

    private static String normalizar(String valor) {
        return valor == null ? "" : valor.trim().toLowerCase(Locale.ROOT);
    }
}
