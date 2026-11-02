package com.br.estimativadeprojetodesoftware.command.usuario;

import com.br.estimativadeprojetodesoftware.command.AplicarTemaCommand;
import com.br.estimativadeprojetodesoftware.command.Command;
import com.br.estimativadeprojetodesoftware.model.Tema;
import com.br.estimativadeprojetodesoftware.presenter.usuario.PrincipalUsuarioPresenter;
import com.br.estimativadeprojetodesoftware.service.PreferenciasService;
import javax.swing.SwingUtilities;

/**
 * Inicia a interface: lê o tema salvo (abrindo o banco) e o aplica antes de
 * mostrar a primeira tela. Se o banco falhar, usa o tema claro; o erro de
 * conexão continua sendo mostrado no login.
 */
public class AbrirTelaPrincipalCommand implements Command {

    @Override
    public void execute() {
        Tema tema;
        try {
            tema = new PreferenciasService().getTema();
        } catch (RuntimeException e) {
            System.err.println("Não foi possível ler o tema salvo: " + e.getMessage());
            tema = Tema.CLARO;
        }
        final Tema escolhido = tema;
        SwingUtilities.invokeLater(() -> {
            new AplicarTemaCommand(escolhido).execute();
            new PrincipalUsuarioPresenter();
        });
    }
}
