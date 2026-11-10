package com.br.estimativadeprojetodesoftware.view;

import static org.junit.jupiter.api.Assertions.*;

import com.br.estimativadeprojetodesoftware.presenter.helpers.Navegador;
import com.br.estimativadeprojetodesoftware.presenter.DesktopMemento;
import com.br.estimativadeprojetodesoftware.presenter.Zelador;
import com.br.estimativadeprojetodesoftware.presenter.window_command.OrganizarLadoALadoCommand;
import java.awt.Rectangle;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Cenários da spec "interface": janelas do MDI abrem maximizadas e convivem com Lado a Lado / Restaurar. */
class JanelasMaximizadasTest {

    private JDesktopPane desktop;

    @BeforeEach
    void preparar() {
        desktop = new JDesktopPane();
        desktop.setSize(1000, 600);
        Navegador.inicializar(desktop);
    }

    private static JInternalFrame janela(String titulo) {
        JInternalFrame f;
        try {
            f = new JInternalFrame(titulo, true, true, true, true);
        } catch (Throwable t) {
            Assumptions.abort("Ambiente sem suporte a janelas internas: " + t.getMessage());
            return null;
        }
        f.setSize(400, 300);
        return f;
    }

    @Test
    void abreMaximizadaOcupandoAreaMdi() {
        Navegador.abrir("lista", () -> janela("Projetos"));

        JInternalFrame f = desktop.getAllFrames()[0];
        assertTrue(f.isMaximum());
        assertEquals(new Rectangle(0, 0, 1000, 600), f.getBounds());
    }

    @Test
    void janelaJaAbertaVoltaMaximizadaSemDuplicar() throws Exception {
        Navegador.abrir("lista", () -> janela("Projetos"));
        JInternalFrame f = desktop.getAllFrames()[0];
        f.setIcon(true);

        Navegador.abrir("lista", () -> janela("Projetos"));

        assertEquals(1, desktop.getAllFrames().length);
        assertSame(f, desktop.getAllFrames()[0]);
        assertFalse(f.isIcon());
        assertTrue(f.isMaximum());
    }

    @Test
    void ladoALadoComJanelasMaximizadas() {
        Navegador.abrir("dashboard", () -> janela("Dashboard"));
        Navegador.abrir("lista", () -> janela("Projetos"));

        new OrganizarLadoALadoCommand(desktop).execute();

        JInternalFrame[] frames = desktop.getAllFrames();
        assertEquals(2, frames.length);
        for (JInternalFrame f : frames) {
            assertFalse(f.isMaximum());
            assertEquals(500, f.getWidth());
            assertEquals(600, f.getHeight());
        }
        assertNotEquals(frames[0].getX(), frames[1].getX());
    }

    @Test
    void restaurarDepoisDoLadoALadoVoltaMaximizada() {
        Navegador.abrir("dashboard", () -> janela("Dashboard"));
        Navegador.abrir("lista", () -> janela("Projetos"));
        new OrganizarLadoALadoCommand(desktop).execute();

        DesktopMemento memento = Zelador.getInstance().restaurarEstado();
        assertNotNull(memento);
        memento.restore(desktop);

        for (JInternalFrame f : desktop.getAllFrames()) {
            assertTrue(f.isMaximum());
            assertEquals(new Rectangle(0, 0, 1000, 600), f.getBounds());
        }
    }

    @Test
    void janelaDeProjetoNovoSalvoNaoDuplicaAoAbrirPelaArvore() {
        // o projeto novo abre com uma chave provisória; ao ser criado, a janela passa a ser a do projeto
        Navegador.abrir("projeto-novo-1", () -> janela("Novo projeto"));
        JInternalFrame f = desktop.getAllFrames()[0];
        java.util.UUID id = java.util.UUID.randomUUID();
        Navegador.renomear(f, Navegador.chaveProjeto(id));

        Navegador.abrir(Navegador.chaveProjeto(id), () -> janela("Projeto"));

        assertEquals(1, desktop.getAllFrames().length);
        assertSame(f, desktop.getAllFrames()[0]);
    }

    @Test
    void chaveDadaPeloPresenterNaoEhSobrescrita() {
        java.util.UUID id = java.util.UUID.randomUUID();
        Navegador.abrir("perfil-novo-1", () -> {
            JInternalFrame f = janela("Cópia");
            Navegador.renomear(f, Navegador.chavePerfil(id));
            return f;
        });

        Navegador.abrir(Navegador.chavePerfil(id), () -> janela("Perfil"));

        assertEquals(1, desktop.getAllFrames().length);
    }
}
