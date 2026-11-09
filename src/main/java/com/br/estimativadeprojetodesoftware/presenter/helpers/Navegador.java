package com.br.estimativadeprojetodesoftware.presenter.helpers;

import com.br.estimativadeprojetodesoftware.model.Perfil;
import com.br.estimativadeprojetodesoftware.model.Projeto;
import com.br.estimativadeprojetodesoftware.presenter.configuracao.ConfiguracaoPresenter;
import com.br.estimativadeprojetodesoftware.presenter.dashboard.DashboardPresenter;
import com.br.estimativadeprojetodesoftware.presenter.perfil.ListaPerfisPresenter;
import com.br.estimativadeprojetodesoftware.presenter.perfil.ManterPerfilPresenter;
import com.br.estimativadeprojetodesoftware.presenter.projeto.CompartilharProjetoPresenter;
import com.br.estimativadeprojetodesoftware.presenter.projeto.ListaProjetosPresenter;
import com.br.estimativadeprojetodesoftware.presenter.projeto.ManterProjetoPresenter;
import java.beans.PropertyVetoException;
import java.util.UUID;
import java.util.function.Supplier;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;

/**
 * Abre as janelas internas do MDI. Cada janela tem uma chave; se já estiver
 * aberta, ela é trazida para a frente em vez de abrir outra. Toda janela
 * aberta ou trazida para a frente fica maximizada.
 */
public final class Navegador {

    private static final String CHAVE = "chave-janela";
    private static JDesktopPane desktop;

    private Navegador() {
    }

    public static void inicializar(JDesktopPane principal) {
        desktop = principal;
    }

    public static JDesktopPane getDesktop() {
        return desktop;
    }

    public static void abrir(String chave, Supplier<JInternalFrame> criador) {
        if (desktop == null) {
            throw new IllegalStateException("A janela principal ainda não foi aberta.");
        }
        for (JInternalFrame f : desktop.getAllFrames()) {
            if (chave.equals(f.getClientProperty(CHAVE))) {
                focar(f);
                return;
            }
        }
        JInternalFrame frame = criador.get();
        frame.putClientProperty(CHAVE, chave);
        desktop.add(frame);
        frame.setVisible(true);
        focar(frame);
    }

    /** Traz a janela para a frente, restaurando-a se minimizada, e a maximiza na área MDI. */
    private static void focar(JInternalFrame f) {
        try {
            if (f.isIcon()) {
                f.setIcon(false);
            }
            if (f.isMaximizable() && !f.isMaximum()) {
                f.setMaximum(true);
            }
            f.moveToFront();
            f.setSelected(true);
        } catch (PropertyVetoException ignored) {
            // a janela recusou o foco; continua aberta
        }
    }

    public static void fecharTodas() {
        if (desktop != null) {
            for (JInternalFrame f : desktop.getAllFrames()) {
                f.dispose();
            }
        }
    }

    public static void abrirDashboard() {
        abrir("dashboard", () -> new DashboardPresenter().getView());
    }

    public static void abrirListaPerfis() {
        abrir("perfis", () -> new ListaPerfisPresenter().getView());
    }

    public static void abrirPerfil(UUID id) {
        abrir("perfil-" + id, () -> ManterPerfilPresenter.paraPerfil(id).getView());
    }

    public static void abrirNovoPerfil(Perfil base) {
        abrir("perfil-novo-" + UUID.randomUUID(), () -> ManterPerfilPresenter.novo(base).getView());
    }

    public static void abrirListaProjetos() {
        abrir("projetos", () -> new ListaProjetosPresenter().getView());
    }

    public static void abrirNovoProjeto() {
        abrir("projeto-novo-" + UUID.randomUUID(), () -> ManterProjetoPresenter.novo().getView());
    }

    public static void abrirProjeto(UUID id) {
        abrir("projeto-" + id, () -> ManterProjetoPresenter.paraProjeto(id).getView());
    }

    public static void abrirCompartilhamento(Projeto projeto) {
        abrir("compartilhar-" + projeto.getId(), () -> new CompartilharProjetoPresenter(projeto).getView());
    }

    public static void abrirConfiguracao() {
        abrir("configuracao", () -> new ConfiguracaoPresenter().getView());
    }
}
