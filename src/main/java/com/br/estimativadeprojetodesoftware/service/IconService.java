package com.br.estimativadeprojetodesoftware.service;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.swing.UIManager;

/**
 * Ícones vetoriais (SVG) do sistema, de src/main/resources/icones/. Cada ícone
 * é desenhado com uma cor marcadora que um filtro global troca pela cor do tema
 * atual (chaves Estimativa.* do UIManager), então o ícone acompanha a troca de
 * tema sem ser recriado.
 */
public final class IconService {

    public static final int TAMANHO_PADRAO = 18;
    private static final String PASTA = "icones/";

    /** Cor marcadora no SVG -> chave de cor do tema. */
    private static final Map<String, String> CORES = Map.of(
            "#6e6e6e", "Estimativa.icone",
            "#2563eb", "Estimativa.destaque",
            "#16a34a", "Estimativa.sucesso",
            "#d97706", "Estimativa.aviso",
            "#dc2626", "Estimativa.erro");

    /** Todos os ícones disponíveis. */
    public static final List<String> CHAVES = List.of(
            "abrir", "adicionar", "aviso", "calcular", "cancelar", "compartilhar", "configuracao", "dashboard",
            "editar", "erro", "excluir", "exportar-csv", "exportar-pdf", "fechar", "info", "log", "logo",
            "novo-projeto", "olho", "olho-fechado", "perfil", "projeto", "remover", "sair", "salvar", "sucesso",
            "tema-claro", "tema-escuro", "usuario");

    static {
        FlatSVGIcon.ColorFilter.getInstance().setMapper(IconService::corDoTema);
    }

    private IconService() {
    }

    private static Color corDoTema(Color original) {
        String hex = String.format(Locale.ROOT, "#%02x%02x%02x", original.getRed(), original.getGreen(), original.getBlue());
        String chave = CORES.get(hex);
        Color cor = chave == null ? null : UIManager.getColor(chave);
        return cor == null ? original : cor;
    }

    public static FlatSVGIcon getIcon(String chave) {
        return getIcon(chave, TAMANHO_PADRAO);
    }

    public static FlatSVGIcon getIcon(String chave, int tamanho) {
        if (!existe(chave)) {
            throw new IllegalArgumentException("Ícone não encontrado: " + chave);
        }
        return new FlatSVGIcon(PASTA + chave + ".svg", tamanho, tamanho, IconService.class.getClassLoader());
    }

    /** Ícone na cor do texto do botão principal (branco), para usar sobre a cor de destaque. */
    public static FlatSVGIcon getIconContraste(String chave, int tamanho) {
        FlatSVGIcon icone = getIcon(chave, tamanho);
        icone.setColorFilter(new FlatSVGIcon.ColorFilter(c -> {
            Color cor = UIManager.getColor("Estimativa.botaoPrincipalTexto");
            return cor == null ? c : cor;
        }));
        return icone;
    }

    public static boolean existe(String chave) {
        URL url = IconService.class.getClassLoader().getResource(PASTA + chave + ".svg");
        if (url == null) {
            return false;
        }
        try (InputStream in = url.openStream()) {
            return in.read() >= 0;
        } catch (IOException e) {
            return false;
        }
    }
}
