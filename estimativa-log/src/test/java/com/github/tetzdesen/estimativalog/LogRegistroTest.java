package com.github.tetzdesen.estimativalog;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class LogRegistroTest {

    private static final LocalDateTime QUANDO = LocalDateTime.of(2026, 3, 5, 9, 7, 3);

    @Test
    void textoDeSucessoSegueASpec() {
        LogRegistro r = new LogRegistro("Criação", "App Loja", "Maria", null, QUANDO);
        assertEquals("Criação: App Loja, (05/03/2026, 09:07:03, e Maria)", r.getMensagem());
        assertFalse(r.isFalha());
    }

    @Test
    void textoDeFalhaSegueASpec() {
        LogRegistro r = new LogRegistro("exclusão", "App Loja", "Maria", "banco indisponível", QUANDO);
        assertEquals("Ocorreu a falha banco indisponível ao realizar a exclusão do projeto App Loja, "
                + "(05/03/2026, 09:07:03, e Maria).", r.getMensagem());
        assertTrue(r.isFalha());
    }

    @Test
    void camposObrigatorios() {
        assertThrows(IllegalArgumentException.class, () -> new LogRegistro("", "P", "U", null, QUANDO));
        assertThrows(IllegalArgumentException.class, () -> new LogRegistro("Op", " ", "U", null, QUANDO));
        assertThrows(IllegalArgumentException.class, () -> new LogRegistro("Op", "P", null, null, QUANDO));
    }

    @Test
    void falhaSemMensagemUsaTextoPadrao() {
        assertTrue(LogRegistro.falha("Op", "P", "U", null).getMensagem().startsWith("Ocorreu a falha desconhecida"));
    }
}
