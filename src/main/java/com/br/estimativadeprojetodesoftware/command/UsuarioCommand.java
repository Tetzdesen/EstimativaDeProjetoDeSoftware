package com.br.estimativadeprojetodesoftware.command;

import com.br.estimativadeprojetodesoftware.model.Usuario;
import com.br.estimativadeprojetodesoftware.presenter.UsuarioPresenter;
import com.br.estimativadeprojetodesoftware.presenter.helpers.WindowManager;
import com.br.estimativadeprojetodesoftware.singleton.UsuarioLogadoSingleton;
import com.br.estimativadeprojetodesoftware.view.UsuarioView;
import com.br.estimativadeprojetodesoftware.service.CriarBarraService;
import java.awt.BorderLayout;
import java.util.Map;
import javax.swing.JDesktopPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;

/**
 *
 * @author tetzner
 */
public class UsuarioCommand implements ProjetoCommand {

    private final JDesktopPane desktop;
    private Usuario usuario = UsuarioLogadoSingleton.getInstancia().getUsuario();
    private final Map<String, ProjetoCommand> comandos;

    public UsuarioCommand(JDesktopPane desktop, Map<String, ProjetoCommand> comandos) {
        this.desktop = desktop;
        this.comandos = comandos;
    }

    @Override
    public void execute() {
        String tituloJanela = "Usuário";
        WindowManager windowManager = WindowManager.getInstance();

        if (windowManager.isFrameAberto(tituloJanela)) {
            windowManager.bringToFront(tituloJanela);
        } else {

            UsuarioPresenter usuarioPresenter = new UsuarioPresenter(usuario);
            UsuarioView visualizacaoUsuarioView = usuarioPresenter.getView();
            visualizacaoUsuarioView.setTitle(tituloJanela);

            JPanel painelPrincipal = new JPanel(new BorderLayout());

            JToolBar toolbar = new CriarBarraService(usuarioPresenter.getComandos()).criarBarraVisualizacaoUsuario();

            painelPrincipal.add(toolbar, BorderLayout.NORTH);

            painelPrincipal.add(visualizacaoUsuarioView.getContentPane(), BorderLayout.CENTER);

            visualizacaoUsuarioView.setContentPane(painelPrincipal);

            visualizacaoUsuarioView.setClosable(true);
            visualizacaoUsuarioView.setIconifiable(true);
            visualizacaoUsuarioView.setResizable(false);
            visualizacaoUsuarioView.setSize(583, 310);

            int x = (desktop.getWidth() - visualizacaoUsuarioView.getWidth()) / 2;
            int y = (desktop.getHeight() - visualizacaoUsuarioView.getHeight()) / 2;
            
            visualizacaoUsuarioView.setLocation(x, y);
            desktop.add(visualizacaoUsuarioView);
            visualizacaoUsuarioView.setVisible(true);
            try {
                //visualizacaoUsuarioView.setMaximum(true);
            } catch (Exception ignored) {
            }
        }
    }

}
