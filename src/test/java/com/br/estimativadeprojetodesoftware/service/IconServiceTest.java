package com.br.estimativadeprojetodesoftware.service;

import static org.junit.jupiter.api.Assertions.*;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

class IconServiceTest {

    @Test
    void todasAsChavesCarregamComOTamanhoPedido() {
        for (String chave : IconService.CHAVES) {
            FlatSVGIcon icone = IconService.getIcon(chave);
            assertTrue(icone.hasFound(), "ícone não encontrado: " + chave);
            assertEquals(IconService.TAMANHO_PADRAO, icone.getIconWidth());
            assertEquals(32, IconService.getIcon(chave, 32).getIconHeight());
        }
    }

    @Test
    void listaCobreTodosOsArquivosSvg() throws Exception {
        try (Stream<Path> arquivos = Files.list(Path.of(System.getProperty("basedir", ".")).resolve("src/main/resources/icones"))) {
            assertEquals(IconService.CHAVES.size(), arquivos.filter(p -> p.toString().endsWith(".svg")).count());
        }
    }

    @Test
    void chaveInexistenteFalhaComMensagem() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> IconService.getIcon("nao-existe"));
        assertTrue(e.getMessage().contains("nao-existe"));
    }
}
