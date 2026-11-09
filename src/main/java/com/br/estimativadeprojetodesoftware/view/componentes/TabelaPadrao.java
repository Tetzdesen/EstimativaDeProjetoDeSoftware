package com.br.estimativadeprojetodesoftware.view.componentes;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.IntPredicate;
import java.util.regex.Pattern;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;

/**
 * Tabela padrão do sistema: linhas alternadas (tema), cabeçalho fixo, números
 * à direita, reais no formato "R$ 1.234,56", percentuais com "%" e "NA"
 * (não aplicável) em itálico com cor própria, distinto de zero.
 */
public class TabelaPadrao extends JTable {

    public enum Formato {
        TEXTO, NUMERO, REAIS, PERCENTUAL
    }

    public static final String NA = "NA";
    private static final Locale PT_BR = Locale.forLanguageTag("pt-BR");
    private static final Pattern NUMERICO = Pattern.compile("^(R\\$\\s?)?-?[\\d.]*\\d(,\\d+)?%?$");

    private final Map<Integer, Formato> formatos = new HashMap<>();
    private final Map<Integer, java.util.function.Function<Object, Selo.Tipo>> selos = new HashMap<>();
    private IntPredicate linhaDestacada = linha -> false;

    public TabelaPadrao() {
        super();
        configurar();
    }

    public TabelaPadrao(TableModel modelo) {
        super(modelo);
        configurar();
    }

    private void configurar() {
        setFillsViewportHeight(true);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        getTableHeader().setReorderingAllowed(false);
        setDefaultRenderer(Object.class, new Renderizador());
        setDefaultRenderer(String.class, new Renderizador());
        setDefaultRenderer(Number.class, new Renderizador());
        setDefaultRenderer(BigDecimal.class, new Renderizador());
    }

    /** Formato fixo de uma coluna do modelo (sem ele, o formato é deduzido do texto). */
    public void setFormatoColuna(int colunaModelo, Formato formato) {
        formatos.put(colunaModelo, formato);
    }

    /** Coluna exibida como selo: texto em negrito na cor do tipo devolvido para cada valor. */
    public void setColunaSelo(int colunaModelo, java.util.function.Function<Object, Selo.Tipo> tipoPorValor) {
        selos.put(colunaModelo, tipoPorValor);
    }

    /** Linhas em negrito, conforme o índice da linha no modelo (independe de filtro). */
    public void setDestaqueLinha(IntPredicate linhaDestacada) {
        this.linhaDestacada = linhaDestacada;
    }

    public Formato getFormatoColuna(int colunaModelo) {
        return formatos.getOrDefault(colunaModelo, Formato.TEXTO);
    }

    public void permitirOrdenacao() {
        setAutoCreateRowSorter(true);
    }

    /** Larguras preferidas das colunas, na ordem. */
    public void setLarguras(int... larguras) {
        for (int i = 0; i < larguras.length && i < getColumnModel().getColumnCount(); i++) {
            getColumnModel().getColumn(i).setPreferredWidth(larguras[i]);
        }
    }

    // ---------- formatação ----------

    public static String formatar(Object valor, Formato formato) {
        if (valor == null) {
            return "";
        }
        if (valor instanceof Number n && !(valor instanceof Integer || valor instanceof Long)) {
            BigDecimal v = n instanceof BigDecimal b ? b : new BigDecimal(n.toString());
            return switch (formato) {
                case REAIS -> "R$ " + new DecimalFormat("#,##0.00", DecimalFormatSymbols.getInstance(PT_BR))
                        .format(v.setScale(2, RoundingMode.HALF_UP));
                case PERCENTUAL -> new DecimalFormat("#,##0.##", DecimalFormatSymbols.getInstance(PT_BR))
                        .format(v.multiply(BigDecimal.valueOf(100))) + "%";
                default -> new DecimalFormat("#,##0.##", DecimalFormatSymbols.getInstance(PT_BR))
                        .format(v.setScale(2, RoundingMode.HALF_UP));
            };
        }
        return valor.toString();
    }

    public static boolean ehNumerico(String texto) {
        return texto != null && NUMERICO.matcher(texto.trim()).matches();
    }

    /** Renderizador padrão; cores sempre lidas do tema no momento da pintura. */
    public class Renderizador extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable tabela, Object valor, boolean selecionado,
                boolean foco, int linha, int coluna) {
            Formato formato = getFormatoColuna(tabela.convertColumnIndexToModel(coluna));
            String texto = formatar(valor, formato);
            // o DefaultTableCellRenderer guarda a última cor usada; zera para não vazar entre células
            setForeground(null);
            JLabel c = (JLabel) super.getTableCellRendererComponent(tabela, texto, selecionado, foco, linha, coluna);
            c.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 8, 0, 8));
            Font fonte = linhaDestacada.test(tabela.convertRowIndexToModel(linha))
                    ? tabela.getFont().deriveFont(Font.BOLD) : tabela.getFont();
            var selo = selos.get(tabela.convertColumnIndexToModel(coluna));
            if (selo != null && valor != null) {
                c.setFont(fonte.deriveFont(Font.BOLD));
                c.setHorizontalAlignment(SwingConstants.LEADING);
                Color cor = UIManager.getColor(selo.apply(valor).chaveCor());
                if (!selecionado && cor != null) {
                    super.setForeground(cor);
                }
                return c;
            }
            if (NA.equals(texto)) {
                c.setFont(fonte.deriveFont(fonte.getStyle() | Font.ITALIC));
                c.setHorizontalAlignment(SwingConstants.RIGHT);
                Color na = UIManager.getColor("Estimativa.na");
                if (!selecionado && na != null) {
                    super.setForeground(na);
                }
            } else {
                c.setFont(fonte);
                boolean direita = formato != Formato.TEXTO || ehNumerico(texto) || valor instanceof Number;
                c.setHorizontalAlignment(direita ? SwingConstants.RIGHT : SwingConstants.LEADING);
            }
            return c;
        }
    }
}
